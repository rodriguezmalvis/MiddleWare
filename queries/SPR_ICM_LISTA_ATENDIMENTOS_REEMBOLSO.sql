IF  EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[SPR_ICM_LISTA_ATENDIMENTOS_REEMBOLSO]') AND type in (N'P', N'PC'))
begin
    DROP PROCEDURE [dbo].[SPR_ICM_LISTA_ATENDIMENTOS_REEMBOLSO]
end
GO

CREATE /*ALTER*/ PROCEDURE dbo.SPR_ICM_LISTA_ATENDIMENTOS_REEMBOLSO(
    @ano_apresentacao int,
    @id_representacao int,
    @id_processo int,
    @d_sub_processo char(1),
    @d_natureza char(1),
    @id_sequencial_natureza tinyint
)
as
BEGIN
    SET NOCOUNT ON;

    /*declare     @ano_apresentacao int = 2017,
                @id_representacao int = 0,
                @id_processo int = 2,
                @d_sub_processo char(1) = '1',
                @d_natureza char(1) = 'O',
                @id_sequencial_natureza tinyint = 1; */
                
    declare @sub_processo varchar(40) = CONVERT(varchar, @ano_apresentacao) + '-' +
                                       CONVERT(varchar, @id_representacao) + '-' +
                                       CONVERT(varchar, @id_processo) + '-' +
                                       @d_sub_processo + '-' +
                                       @d_natureza + '-' +
                                       convert(varchar, @id_sequencial_natureza)
    declare @msg varchar(800)
                                       
                
    declare @resultado table(
        ano_apresentacao numeric(4,0),
        id_representacao int,
        id_processo int,
        d_sub_processo char(1),
        d_natureza char(1),
        id_sequencial_natureza tinyint,
        id_atendimento smallint,
        dthr_homologacao datetime
        )
        
    /* Validações */

    /* 1- O subprocesso não pode ser negativo */
    if @d_natureza = 'N'
    begin
        set @msg= 'O subprocesso não pode ser negativo! ' + @sub_processo
        RAISERROR(@msg, 16, 1)
        RETURN
    end

    /* 2- O subprocesso deve existir */
    if not exists ( select 1 from SUB_PROCESSO sp
                    where sp.ano_apresentacao = @ano_apresentacao
                    and sp.id_representacao = @id_representacao
                    and sp.id_processo = @id_processo
                    and sp.d_sub_processo = @d_sub_processo
                    and sp.d_natureza = @d_natureza
                    and sp.id_sequencial_natureza = @id_sequencial_natureza )
    begin
        set @msg = 'O subprocesso informado não existe! Subprocesso: ' + @sub_processo
        RAISERROR(@msg, 16, 1)
        RETURN                               
    end

    /* 3- Não pode ser processo de reembolso */
    if exists (select 1 from PROCESSO pp
               where pp.ano_apresentacao = @ano_apresentacao
               and pp.id_representacao = @id_representacao
               and pp.id_processo = @id_processo
               and pp.numero_carteira is null)
    begin
        set @msg = 'O processo informado não é de reembolso! Subprocesso: ' + @sub_processo
        RAISERROR(@msg, 16, 1)
        RETURN
    end 
    
    /* 4- O processo deve estar homologado */
    if exists (select 1 from SUB_PROCESSO sp
                where sp.ano_apresentacao = @ano_apresentacao
                and sp.id_representacao = @id_representacao
                and sp.id_processo = @id_processo
                and sp.d_sub_processo = @d_sub_processo
                and sp.d_natureza = @d_natureza
                and sp.id_sequencial_natureza = @id_sequencial_natureza 
                and sp.dthr_homologacao is null)
    begin
        set @msg = 'O subprocesso informado não está homologado! Subprocesso: ' + @sub_processo
        RAISERROR(@msg, 16, 1)
        RETURN            
    end
    
    /* 5- O processo não pode ter sido pago pelo SPAG (não pode existir a chave 
          deste processo em LANCAMENTO_BRUTO_PAGAMENTO com id_tipo_movimento
          igual a 1 e com id_lancamento_liquido preenchido) */
    /* COMENTADO PARA FINS DE DESENVOLVIMENTO */
    if exists (select 1 from SUB_PROCESSO sp inner join LANCAMENTO_BRUTO_PAGAMENTO lbp
                    on sp.ano_apresentacao = lbp.ano_apresentacao
                    and sp.id_representacao = lbp.id_representacao
                    and sp.id_processo = lbp.id_processo
                    and sp.d_sub_processo = lbp.d_sub_processo
                    and sp.d_natureza = lbp.d_natureza
                    and sp.id_sequencial_natureza = lbp.id_sequencial_natureza
                where sp.ano_apresentacao = @ano_apresentacao
                and sp.id_representacao = @id_representacao
                and sp.id_processo = @id_processo
                and sp.d_sub_processo = @d_sub_processo
                and sp.d_natureza = @d_natureza
                and sp.id_sequencial_natureza = @id_sequencial_natureza 
                and lbp.id_tipo_movimento = 1
                and lbp.id_lancamento_liquido is not null)
    begin
        set @msg = 'O subprocesso informado já teve pagamento gerado pelo SPAG! Subprocesso: ' + @sub_processo
        RAISERROR(@msg, 16, 1)
        RETURN    
    end   
    
    /* 6- O processo não pode já ter sido totalmente enviado para o SOUL.
    
    */

    if not exists (select 1 from ATENDIMENTO aa 
                   where aa.ano_apresentacao = @ano_apresentacao
                         and aa.id_representacao = @id_representacao
                         and aa.id_processo = @id_processo
                         and aa.d_sub_processo = @d_sub_processo
                         and aa.d_natureza = @d_natureza
                         and aa.id_sequencial_natureza = @id_sequencial_natureza
                         and not exists (select 1 from MV_CAC_DE_PARA_REEMBOLSO dpr
                                         where aa.ano_apresentacao = dpr.ano_apresentacao
                                         and aa.id_representacao = dpr.id_representacao
                                         and aa.id_processo = dpr.id_processo
                                         and aa.d_sub_processo = dpr.d_sub_processo
                                         and aa.d_natureza = dpr.d_natureza
                                         and aa.id_sequencial_natureza = dpr.id_sequencial_natureza
                                         and aa.id_atendimento = dpr.id_atendimento))
    begin
        set @msg = 'O subprocesso informado já teve todos os seus atendimentos enviados para o SOUL! Subprocesso: ' + @sub_processo
        RAISERROR(@msg, 16, 1)
        RETURN    
    end
                    
        
    insert into @resultado(ano_apresentacao, id_representacao, id_processo,
        d_sub_processo, d_natureza, id_sequencial_natureza, id_atendimento)
            
     select  aa.ano_apresentacao, 
             aa.id_representacao, 
             aa.id_processo,
             aa.d_sub_processo, 
             aa.d_natureza, 
             aa.id_sequencial_natureza,
             aa.id_atendimento            
     
     from ATENDIMENTO aa
     where aa.ano_apresentacao = @ano_apresentacao
     and aa.id_representacao = @id_representacao
     and aa.id_processo = @id_processo
     and aa.d_sub_processo = @d_sub_processo
     and aa.d_natureza = @d_natureza
     and aa.id_sequencial_natureza = @id_sequencial_natureza
     and not exists (select 1 from MV_CAC_DE_PARA_REEMBOLSO dpr
                     where dpr.ano_apresentacao = aa.ano_apresentacao
                     and dpr.id_representacao = aa.id_representacao
                     and dpr.id_processo = aa.id_processo
                     and dpr.d_sub_processo = aa.d_sub_processo
                     and dpr.d_natureza = aa.d_natureza
                     and dpr.id_sequencial_natureza = aa.id_sequencial_natureza
                     and dpr.id_atendimento = aa.id_atendimento)
     -- TODO: tratar casos de negativado                
     
        
    select * from @resultado rr
    order by rr.ano_apresentacao, rr.id_representacao, rr.id_processo, rr.d_sub_processo,
        rr.d_natureza, rr.id_sequencial_natureza, rr.id_atendimento

END
GO

GRANT EXECUTE on SPR_ICM_LISTA_ATENDIMENTOS_REEMBOLSO to LogCacMv
GO