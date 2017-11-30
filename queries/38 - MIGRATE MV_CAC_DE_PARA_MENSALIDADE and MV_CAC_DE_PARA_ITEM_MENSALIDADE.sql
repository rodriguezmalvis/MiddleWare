/*
    38 - MIGRATE MV_CAC_DE_PARA_MENSALIDADE and MV_CAC_DE_PARA_ITEM_MENSALIDADE.sql:

    Atualiza a estrutura das tabelas MV_CAC_DE_PARA_MENSALIDADE e 
    MV_CAC_DE_PARA_ITEM_MENSALIDADE, de acordo com a nova estrutura de itens
    de mensalidade:

    - Dropa a tabela MV_CAC_DE_PARA_ITEM_MENSALIDADE;

    - Dropa as colunas cd_mens_contrato_interno e cd_mens_contrato_efetivo
    da tabela MV_CAC_DE_PARA_MENSALIDADE.


    * Pré-requisitos para execução:
    - Execução do script 37 (migra os dados que serão excluídos neste script
    para as novas tabelas de destino);

    ===========================ATENÇÃO!==================================
    AS ALTERAÇÕES EFETUADAS POR ESTE SCRIPT NÃO PODERÃO SER DESFEITAS!
    CERTIFIQUE-SE QUE O SCRIPT 37 FOI EXECUTADO CORRETAMENTE ANTES DE EXECUTAR
    ESTE SCRIPT!

*/



begin try
    begin tran

    /* Dropar a tabela MV_CAC_DE_PARA_ITEM_MENSALIDADE */ 
    
    
    print 'Dropando MV_CAC_DE_PARA_ITEM_MENSALIDADE'

    drop table MV_CAC_DE_PARA_ITEM_MENSALIDADE

    /* Dropar as colunas de MV_CAC_DE_PARA_MENSALIDADE */

    print 'Dropando coluna cd_mens_contrato_interno de MV_CAC_DE_PARA_MENSALIDADE'

    alter table MV_CAC_DE_PARA_MENSALIDADE
    drop column cd_mens_contrato_interno

    print 'Dropando coluna cd_mens_contrato_efetivo de MV_CAC_DE_PARA_MENSALIDADE'

    alter table MV_CAC_DE_PARA_MENSALIDADE
    drop column cd_mens_contrato_efetivo

    print 'Finalizando transação'

    commit tran

    print 'Tudo certo!'

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