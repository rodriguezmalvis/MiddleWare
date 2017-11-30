/*
    45 - MIGRATE MV_CAC_DE_PARA_MENSALIDADE_COBRANCA_DEVOLUCAO.sql
    
    * Objetivo:
    Excluir as tabelas MV_CAC_DE_PARA_MENSALIDADE_COBRANCA, MV_CAC_DE_PARA_MENSALIDADE_DEVOLUCAO
    e suas dependentes, devido à migração para novas tabelas.

    * Pré-requisitos:
    Execução do script 44.

    /**************************** ATENÇÃO ***************************/
    ESSE SCRIPT É DESTRUTIVO! UMA VEZ EXECUTADO, NÃO É POSSÍVEL DESFAZER AS ALTERAÇÕES, NEM
    EXECUTAR O SCRIPT 44 NOVAMENTE.
*/

SET NOCOUNT ON

begin try
    if not exists (select 1 from MV_CAC_DE_PARA_GRUPO_ITEM_MENSALIDADE)
    begin
        raiserror('A tabela MV_CAC_DE_PARA_GRUPO_ITEM_MENSALIDADE está vazia. Verifique se o script 44 foi executado.', 16, 1)
        RETURN
    end

    -- Removendo as tabelas que não serão mais usadas
    begin tran

    print 'Removendo tabela MV_CAC_DE_PARA_ITEM_MENSALIDADE_COBRANCA'
    drop table MV_CAC_DE_PARA_ITEM_MENSALIDADE_COBRANCA

    print 'Removendo tabela MV_CAC_DE_PARA_MENSALIDADE_COBRANCA'
    drop table MV_CAC_DE_PARA_MENSALIDADE_COBRANCA
    
    print 'Removendo tabela MV_CAC_DE_PARA_ITEM_MENSALIDADE_DEVOLUCAO'
    drop table MV_CAC_DE_PARA_ITEM_MENSALIDADE_DEVOLUCAO
    
    print 'Removendo tabela MV_CAC_DE_PARA_MENSALIDADE_DEVOLUCAO'
    drop table MV_CAC_DE_PARA_MENSALIDADE_DEVOLUCAO


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

    RAISERROR (@ErrorMessage,
		       @ErrorSeverity,
		       @ErrorState
		       );
	
	print ''	           
    RETURN; 
end catch
GO
