ALTER TABLE ICM_MENSALIDADE_TRABALHO
add id_sub_plano_titular int not null CONSTRAINT DF_TMP_IMT_ID_SUB_PLANO default 0
GO

ALTER TABLE ICM_MENSALIDADE_TRABALHO
add id_plano_titular int not null CONSTRAINT DF_TMP_IMT_ID_PLANO default 0
GO

ALTER TABLE ICM_MENSALIDADE_TRABALHO
add dt_inscricao_titular datetime not null CONSTRAINT DF_TMP_IMT_DT_INSCRICAO default '01/01/2000'
GO

begin try
    begin tran

    ; with SUB_PLANO_ATUAL as(
        select spb.id_pessoa, spb.id_plano, spb.id_sub_plano, spb.dt_inscricao, spb.numero_carteira
        from SUB_PLANO_BENEFICIARIO spb inner join (
            select hsb.id_pessoa, hsb.id_plano, hsb.id_sub_plano, hsb.dt_inscricao                           
            from HISTORICO_STATUS_BENEFICIARIO hsb
            where hsb.id_plano <> 7            
            and hsb.dthr_registro = (select MAX (hsbb.dthr_registro)
                                     from HISTORICO_STATUS_BENEFICIARIO hsbb
                                     where hsbb.id_pessoa = hsb.id_pessoa
                                     and hsbb.id_plano <> 7
                                     and convert(date, hsbb.dthr_registro) <= convert(date, GETDATE()) )
            
        ) sb
        on sb.id_pessoa = spb.id_pessoa
        and sb.id_plano = spb.id_plano
        and sb.id_sub_plano = spb.id_sub_plano
        and sb.dt_inscricao = spb.dt_inscricao    
    )
    update imt
    set id_sub_plano_titular = spa.id_sub_plano,
        id_plano_titular = spa.id_plano,
        dt_inscricao_titular = spa.dt_inscricao
    from ICM_MENSALIDADE_TRABALHO imt inner join SUB_PLANO_ATUAL spa
        on imt.id_titular = spa.id_pessoa
        
    if exists (select 1 from ICM_MENSALIDADE_TRABALHO
               where id_sub_plano_titular = 0 or id_plano_titular = 0)
    begin
        RAISERROR('Existem mensalidades de trabalho sem informação de titular!', 16, 1)
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


ALTER TABLE ICM_MENSALIDADE_TRABALHO
add constraint FK_MENS_TRAB_SUB_PLANO_BENEF FOREIGN KEY (id_titular, id_sub_plano_titular, id_plano_titular, dt_inscricao_titular)
    REFERENCES SUB_PLANO_BENEFICIARIO (id_pessoa, id_sub_plano, id_plano, dt_inscricao)
GO

ALTER TABLE ICM_MENSALIDADE_TRABALHO
DROP CONSTRAINT DF_TMP_IMT_ID_SUB_PLANO
GO

ALTER TABLE ICM_MENSALIDADE_TRABALHO
DROP CONSTRAINT DF_TMP_IMT_ID_PLANO
GO

ALTER TABLE ICM_MENSALIDADE_TRABALHO
DROP CONSTRAINT DF_TMP_IMT_DT_INSCRICAO
GO
