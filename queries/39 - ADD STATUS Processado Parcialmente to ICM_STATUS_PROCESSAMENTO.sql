/*
    39 - ADD STATUS to ICM_STATUS_PROCESSAMENTO.sql:

    Script incremental: adiciona um novo status na tabela 
    ICM_STATUS_PROCESSAMENTO: Processado Parciaplemnte.

    Pré-requisitos:
    A tabela ICM_STATUS_PROCESSAMENTO deve existir (script
    CREATE TABLE ICM_STATUS_PROCESSAMENTO.sql)

*/



begin try
    begin tran
        
    insert into ICM_STATUS_PROCESSAMENTO(cod_status, descricao, f_final)
    values ('PP', 'Processado Parcialmente: o item teve parte do conteúdo processado e ainda há processamento a fazer', 0)

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