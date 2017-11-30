/*
    51 - ADD TIPO_PROCESSAMENTO Lote Folha Boleto to ICM_TIPO_PROCESSAMENTO.sql

    Script incremental: adiciona dois novos status na tabela 
    ICM_TIPO_PROCESSAMENTO: Lote de títulos a receber de Folha
    e de Boleto.

    Pré-requisitos:
    A tabela ICM_TIPO_PROCESSAMENTO deve existir (script
    CREATE TABLE ICM_TIPO_PROCESSAMENTO.sql)

*/



begin try
    begin tran
        
    insert into ICM_TIPO_PROCESSAMENTO(cod_processamento, descricao)
    values ('LF', 'Lote de Títulos a Receber de Folha: trata vários comandos de folha de uma data de referência, transformando em mensalidades'),
    ('LB', 'Lote de Títulos a Receber de Boleto: trata vários comandos de boletos de uma data de referência, com faixa de data de vencimento determinada, transformando em mensalidades')

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