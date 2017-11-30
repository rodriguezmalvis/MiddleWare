/*
    44 - MIGRATE to MV_CAC_DE_PARA_GRUPO_ITEM_MENSALIDADE.sql:
    
    * Objetivo:
    Migrar os dados presentes em MV_CAC_DE_PARA_MENSALIDADE_COBRANCA, 
    MV_CAC_DE_PARA_ITEM_MENSALIDADE_COBRANCA, MV_CAC_DE_PARA_MENSALIDADE_DEVOLUCAO
    e MV_CAC_DE_PARA_ITEM_MENSALIDADE_DEVOLUCAO para as novas tabelas
    (MV_CAC_DE_PARA_GRUPO_ITEM_MENSALIDADE e MV_CAC_DE_PARA_ITEM_MENSALIDADE).

    * Pré-requisitos:
    Execução dos scripts 40 a 42.

    /* ESTE SCRIPT ESTÁ INCORRETO: não grava corretamente as devoluções, apenas as cobranças */
*/

SET NOCOUNT ON

begin try
    -- Primeiramente, serão migradas as cobranças, e depois as devoluções
    begin tran

    declare cur_cobrancas cursor for
        select dpmc.id_de_para_mensalidade_cobranca, dpmc.cd_mens_contrato_interno, dpmc.cd_mens_contrato_efetivo
        from MV_CAC_DE_PARA_MENSALIDADE_COBRANCA dpmc inner join ICM_MENSALIDADE_TRABALHO mt
            on dpmc.cd_mens_contrato_interno = mt.id_mensalidade_trabalho
        order by dpmc.id_de_para_mensalidade_cobranca

    declare @id_de_para_tabela int,
            @id_de_para_mensalidade int,
            @cd_inclusao_interno int,
            @cd_inclusao_efetivo int,
            @id_grupo int,
            @id_grupo_trabalho int

    open cur_cobrancas
    fetch next from cur_cobrancas into @id_de_para_tabela, @cd_inclusao_interno, @cd_inclusao_efetivo

    while @@FETCH_STATUS = 0
    begin
        print 'Processando id de cobrança ' + convert(varchar, @id_de_para_tabela)

        insert into MV_CAC_DE_PARA_GRUPO_ITEM_MENSALIDADE (id_de_para_mensalidade, f_devolucao, cd_inclusao_interno, cd_inclusao_efetivo)
        values (@id_de_para_tabela, 0, @cd_inclusao_interno, @cd_inclusao_efetivo)

        set @id_grupo = @@IDENTITY

        print '  Inserindo os itens de cobrança'
        insert into MV_CAC_DE_PARA_ITEM_MENSALIDADE (id_de_para_grupo_item_mensalidade, id_comando)
        select @id_grupo, id_comando
        from MV_CAC_DE_PARA_ITEM_MENSALIDADE_COBRANCA
        where id_de_para_mensalidade_cobranca = @id_de_para_tabela

        -- Criando os grupos de trabalho
        print '  Criando os grupos de trabalho: id_mensalidade_trabalho = ' + convert(varchar, @cd_inclusao_interno)
        insert into ICM_GRUPO_ITEM_MENSALIDADE_TRABALHO (id_mensalidade_trabalho, id_de_para_grupo_item_mensalidade,
            f_devolucao)
        values (@cd_inclusao_interno, @id_grupo, 0)

        set @id_grupo = @@IDENTITY

        print '  Atualizando itens de mensalidade com os grupos de trabalho'
        update ICM_ITEM_MENSALIDADE_TRABALHO
        set id_grupo_item_mensalidade_trabalho = @id_grupo
        where id_mensalidade_trabalho = @cd_inclusao_interno

        print '  Ajustando devoluções'

        -- Tratando as devoluções
        declare cur_devolucoes cursor for
            select  dpmd.id_de_para_mensalidade_devolucao, dpmd.id_de_para_mensalidade,
                    dpmd.cd_devolucao_interno, dpmd.cd_devolucao_efetivo
            from MV_CAC_DE_PARA_MENSALIDADE_DEVOLUCAO dpmd
            where dpmd.id_de_para_mensalidade = @id_de_para_tabela 
            order by dpmd.id_de_para_mensalidade_devolucao

        declare @did_de_para_tabela int, 
                @did_de_para_mensalidade int,
                @dcd_inclusao_interno int, 
                @dcd_inclusao_efetivo int, 
                @did_grupo int

        open cur_devolucoes
        fetch next from cur_devolucoes into @did_de_para_tabela, @did_de_para_mensalidade,
            @dcd_inclusao_interno, @dcd_inclusao_efetivo

        while @@FETCH_STATUS = 0
        begin
            print 'Processando id de devolução ' + convert(varchar, @did_de_para_tabela)
            insert into MV_CAC_DE_PARA_GRUPO_ITEM_MENSALIDADE (id_de_para_mensalidade, f_devolucao,
                cd_inclusao_interno, cd_inclusao_efetivo)
            values (@did_de_para_mensalidade, 1, @dcd_inclusao_interno, @dcd_inclusao_efetivo)
            -- terminar de inserir no grupo, de para e os outros

            set @did_grupo = @@IDENTITY

            print '  Inserindo os itens de devolução'
            insert into MV_CAC_DE_PARA_ITEM_MENSALIDADE (id_de_para_grupo_item_mensalidade, id_comando)
            select @did_grupo, id_comando
            from MV_CAC_DE_PARA_ITEM_MENSALIDADE_DEVOLUCAO
            where id_de_para_mensalidade_devolucao = @did_de_para_tabela

            -- Criando os grupos de trabalho
            /* Por construção, o id_mensalidade_trabalho é utilizado como código de inclusão interno de COBRANÇA. */
            print '  Criando os grupos de trabalho'
            insert into ICM_GRUPO_ITEM_MENSALIDADE_TRABALHO (id_mensalidade_trabalho, id_de_para_grupo_item_mensalidade,
                f_devolucao)
            values (@cd_inclusao_interno, @did_grupo, 1) -- usa o cd_inclusao_interno de cobrança

            set @did_grupo = @@IDENTITY

            print '  Atualizando itens de mensalidade com os grupos de trabalho'
            update ICM_ITEM_MENSALIDADE_TRABALHO
            set id_grupo_item_mensalidade_trabalho = @did_grupo
            where id_mensalidade_trabalho = @dcd_inclusao_interno

            fetch next from cur_devolucoes into @did_de_para_tabela, @did_de_para_mensalidade,
                @dcd_inclusao_interno, @dcd_inclusao_efetivo
        end

        close cur_devolucoes
        deallocate cur_devolucoes


        fetch next from cur_cobrancas into @id_de_para_tabela, @cd_inclusao_interno, @cd_inclusao_efetivo
    end

    close cur_cobrancas
    deallocate cur_cobrancas


    commit tran

    select 'Tudo certo!'
end try
begin catch
if @@TRANCOUNT > 0
    begin
	    rollback tran
    end
      
    /* Levantando o erro ocorrido */
    DECLARE @ErrorMessage NVARCHAR(4000);
    DECLARE @ErrorSeverity INT;
    DECLARE @ErrorState INT;
    DECLARE @ErrorLine INT;

    SELECT 
	    @ErrorMessage = 'Linha ' + convert(varchar(5), ERROR_LINE()) + ': ' + ERROR_MESSAGE(),
	    @ErrorSeverity = ERROR_SEVERITY(),
	    @ErrorState = ERROR_STATE();
        
    if CURSOR_STATUS('global', 'cur_devolucoes') >= -1
    begin
	    print 'Fechando o cursor cur_devolucoes...'
	    close cur_devolucoes
	    deallocate cur_devolucoes
    end    
    
    if CURSOR_STATUS('global', 'cur_cobrancas') >= -1
    begin
	    print 'Fechando o cursor cur_cobrancas...'
	    close cur_cobrancas
	    deallocate cur_cobrancas
    end            	    

    RAISERROR (@ErrorMessage,
		       @ErrorSeverity,
		       @ErrorState
		       );
	
	print ''	           
    RETURN; 
end catch
GO
