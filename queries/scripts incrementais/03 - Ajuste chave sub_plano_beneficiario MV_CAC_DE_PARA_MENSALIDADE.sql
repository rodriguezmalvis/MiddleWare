ALTER TABLE MV_CAC_DE_PARA_MENSALIDADE
add id_sub_plano_titular int not null CONSTRAINT DF_TMP_DPM_ID_SUB_PLANO default 0
GO

ALTER TABLE MV_CAC_DE_PARA_MENSALIDADE
add id_plano_titular int not null CONSTRAINT DF_TMP_DPM_ID_PLANO default 0
GO

ALTER TABLE MV_CAC_DE_PARA_MENSALIDADE
add dt_inscricao_titular datetime not null CONSTRAINT DF_TMP_DPM_DT_INSCRICAO default '01/01/2000'
GO

begin try
    begin tran

    update dpm
    set id_sub_plano_titular = imt.id_sub_plano_titular,
        id_plano_titular = imt.id_plano_titular,
        dt_inscricao_titular = imt.dt_inscricao_titular
    from MV_CAC_DE_PARA_MENSALIDADE dpm inner join ICM_MENSALIDADE_TRABALHO imt
        on dpm.cd_mens_contrato_interno = imt.id_mensalidade_trabalho
        
    if exists (select 1 from MV_CAC_DE_PARA_MENSALIDADE
               where id_sub_plano_titular = 0 or id_plano_titular = 0)
    begin
        RAISERROR('Existem mensalidades no de/para sem informação de titular!', 16, 1)
    end

    commit tran

    select 'tudo certo!'
end try
begin catch
    if @@trancount > 0
    begin
        rollback tran
    end
    
    DECLARE @ErrorMessage NVARCHAR(4000);
    DECLARE @ErrorSeverity INT;
    DECLARE @ErrorState INT;
    DECLARE @ErrorLine INT;

    SELECT 
	    @ErrorMessage = 'Linha ' + convert(varchar(5), ERROR_LINE()) + ': ' + ERROR_MESSAGE(),
	    @ErrorSeverity = ERROR_SEVERITY(),
	    @ErrorState = ERROR_STATE();
    
    select 'deu erro...'
    return;
    
end catch
GO


ALTER TABLE MV_CAC_DE_PARA_MENSALIDADE
add constraint FK_MV_CAC_DP_MENS_SUB_PLANO_BENEF FOREIGN KEY (id_titular, id_sub_plano_titular, id_plano_titular, dt_inscricao_titular)
    REFERENCES SUB_PLANO_BENEFICIARIO (id_pessoa, id_sub_plano, id_plano, dt_inscricao)
GO

ALTER TABLE MV_CAC_DE_PARA_MENSALIDADE
DROP CONSTRAINT DF_TMP_DPM_ID_SUB_PLANO
GO

ALTER TABLE MV_CAC_DE_PARA_MENSALIDADE
DROP CONSTRAINT DF_TMP_DPM_ID_PLANO
GO

ALTER TABLE MV_CAC_DE_PARA_MENSALIDADE
DROP CONSTRAINT DF_TMP_DPM_DT_INSCRICAO
GO
