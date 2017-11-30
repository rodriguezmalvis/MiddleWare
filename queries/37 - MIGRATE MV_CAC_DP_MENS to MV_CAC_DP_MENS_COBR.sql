/*
    37 - MIGRATE MV_CAC_DP_MENS to MV_CAC_DP_MENS_COBR.sql:

    Migra os dados existentes atualmente em MV_CAC_DE_PARA_MENSALIDADE e 
    MV_CAC_DE_PARA_ITEM_MENSALIDADE para MV_CAC_DE_PARA_MENSALIDADE_COBRANCA
    e MV_CAC_DE_PARA_ITEM_MENSALIDADE_COBRANCA.

    No momento, os dados ficarão inconsistentes, devido ao fato de existirem
    comandos de devolução em MV_CAC_DE_PARA_ITEM_MENSALIDADE. Futuramente
    será ajustado, pois haverá uma limpeza de base por parte da MV e, com isso,
    também da base CAC.

    * Pré-requisitos para execução:
    - Execução dos scripts 33 a 36 (criação das tabelas necessárias);
*/



begin try
    begin tran

    /* Primeiro, insere os dados existentes em MV_CAC_DE_PARA_MENSALIDADE na
       MV_CAC_DE_PARA_MENSALIDADE_COBRANCA (já que o id desta tabela é o mesmo
       da primeira)
    */ 
    
    print 'Inserindo dados de MV_CAC_DE_PARA_MENSALIDADE em MV_CAC_DE_PARA_MENSALIDADE_COBRANCA'

    INSERT INTO MV_CAC_DE_PARA_MENSALIDADE_COBRANCA(id_de_para_mensalidade_cobranca, cd_mens_contrato_interno, cd_mens_contrato_efetivo)
    select id_de_para_mensalidade, cd_mens_contrato_interno, cd_mens_contrato_efetivo
    from MV_CAC_DE_PARA_MENSALIDADE
    order by id_de_para_mensalidade

    /* Depois, insere os dados dos itens de mensalidade existente, no momento
       todos como cobrança.    
    */

    print 'Inserindo dados de MV_CAC_DE_PARA_ITEM_MENSALIDADE em MV_CAC_DE_PARA_ITEM_MENSALIDADE_COBRANCA'
    
    INSERT INTO MV_CAC_DE_PARA_ITEM_MENSALIDADE_COBRANCA(id_de_para_mensalidade_cobranca, id_comando)
    select id_de_para_mensalidade, id_comando
    from MV_CAC_DE_PARA_ITEM_MENSALIDADE
    order by id_de_para_item_mensalidade

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