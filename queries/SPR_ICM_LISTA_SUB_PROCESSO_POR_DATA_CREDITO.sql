IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[SPR_ICM_LISTA_SUB_PROCESSO_POR_DATA_CREDITO]') AND type in (N'P', N'PC'))
begin
    EXEC('CREATE PROCEDURE [dbo].[SPR_ICM_LISTA_SUB_PROCESSO_POR_DATA_CREDITO] AS BEGIN SET NOCOUNT ON; END');
end
GO

ALTER PROCEDURE dbo.SPR_ICM_LISTA_SUB_PROCESSO_POR_DATA_CREDITO(
    @dt_credito_inicial date,
    @dt_credito_final date
)
as
BEGIN
    SET NOCOUNT ON;

    select  distinct sp.ano_apresentacao,
            sp.id_representacao,
            sp.id_processo,
            sp.d_sub_processo,
            sp.d_natureza,
            sp.id_sequencial_natureza,
            sp.dthr_homologacao
    from    SUB_PROCESSO sp inner join PROCESSO pp
                on sp.ano_apresentacao = pp.ano_apresentacao
                and sp.id_representacao = pp.id_representacao
                and sp.id_processo = pp.id_processo
            inner join LANCAMENTO_BRUTO_PAGAMENTO lbp
                on sp.ano_apresentacao = lbp.ano_apresentacao
                and sp.id_representacao = lbp.id_representacao
                and sp.id_processo = lbp.id_processo
                and sp.d_sub_processo = lbp.d_sub_processo
                and sp.d_natureza = lbp.d_natureza
                and sp.id_sequencial_natureza = lbp.id_sequencial_natureza
                and lbp.id_tipo_movimento = 1
            inner join LANCAMENTO_LIQUIDO_PAGAMENTO llp
                on lbp.id_lancamento_liquido = llp.id_lancamento_liquido
    where 
        sp.dthr_homologacao is not null
        and CONVERT(date, llp.dt_credito) >= @dt_credito_inicial
        and CONVERT(date, llp.dt_credito) <= @dt_credito_final
        and sp.d_natureza <> 'N'
        and pp.numero_carteira is null
        -- TODO: Tratar o caso de reprocessamento                    
        and not exists (select 1 from MV_CAC_DE_PARA_SUB_PROCESSO dpsp
                        where sp.ano_apresentacao = dpsp.ano_apresentacao
                        and sp.id_representacao = dpsp.id_representacao
                        and sp.id_processo = dpsp.id_processo
                        and sp.d_sub_processo = dpsp.d_sub_processo
                        and sp.d_natureza = dpsp.d_natureza
                        and sp.id_sequencial_natureza = dpsp.id_sequencial_natureza)                    

    order by sp.ano_apresentacao, sp.id_representacao, sp.id_processo,
        sp.d_sub_processo, sp.d_natureza, sp.id_sequencial_natureza,
        sp.dthr_homologacao                   
                    
                    

END
GO

GRANT EXECUTE on SPR_ICM_LISTA_SUB_PROCESSO_POR_DATA_CREDITO to LogCacMv
GO