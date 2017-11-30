SET ANSI_PADDING on
GO

/**
    48 - MIGRATE MV_CAC_DE_PARA_GRUPO_ITEM_MENSALIDADE.sql
    
    Tem como objetivo adicionar a coluna de status de envio de um grupo
    de itens de mensalidade, para a funcionalidade de consistência no
    envio de títulos a receber.

    - Pré-requisitos:
    Execução do script 47 - 47 - CREATE TABLE MV_CAC_STATUS_ENVIO_GRUPO_ITEM_MENS.sql
    
*/
SET NOCOUNT ON

begin try
    begin tran

        ALTER TABLE MV_CAC_DE_PARA_GRUPO_ITEM_MENSALIDADE
        add id_status_envio_grupo_item_mens int not null CONSTRAINT DEF_TMP_DPGIM_ISEGIM DEFAULT 1       

        ALTER TABLE MV_CAC_DE_PARA_GRUPO_ITEM_MENSALIDADE
        add constraint FK_MV_CAC_DP_GRP_IT_MENS_STAT_ENV_GRP_IT_MENS FOREIGN KEY (id_status_envio_grupo_item_mens)
        references MV_CAC_STATUS_ENVIO_GRUPO_ITEM_MENS(id_status_envio_grupo_item_mens)        

        ALTER TABLE MV_CAC_DE_PARA_GRUPO_ITEM_MENSALIDADE
        drop constraint DEF_TMP_DPGIM_ISEGIM        

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