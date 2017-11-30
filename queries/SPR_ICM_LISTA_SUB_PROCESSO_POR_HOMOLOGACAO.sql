IF  EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[SPR_ICM_LISTA_SUB_PROCESSO_POR_HOMOLOGACAO]') AND type in (N'P', N'PC'))
begin
    DROP PROCEDURE [dbo].[SPR_ICM_LISTA_SUB_PROCESSO_POR_HOMOLOGACAO]
end
GO

CREATE /*ALTER*/ PROCEDURE dbo.SPR_ICM_LISTA_SUB_PROCESSO_POR_HOMOLOGACAO(
    @dt_homologacao_inicial date,
    @dt_homologacao_final date
)
as
BEGIN
    SET NOCOUNT ON;

    select  sp.ano_apresentacao,
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
    where CONVERT(date, sp.dthr_homologacao) >= @dt_homologacao_inicial
    and CONVERT(date, sp.dthr_homologacao) <= @dt_homologacao_final
    and sp.d_natureza <> 'N'
    and not exists (select 1 from LANCAMENTO_BRUTO_PAGAMENTO lbp
                    where lbp.ano_apresentacao = sp.ano_apresentacao
                    and lbp.id_representacao = sp.id_representacao
                    and lbp.id_processo = sp.id_processo
                    and lbp.d_sub_processo = sp.d_sub_processo
                    and lbp.d_natureza = sp.d_natureza
                    and lbp.id_sequencial_natureza = sp.id_sequencial_natureza
                    and lbp.id_tipo_movimento = 1
                    and lbp.id_lancamento_liquido is not null)
    -- TODO: Tratar o caso de reprocessamento                    
    and not exists (select 1 from MV_CAC_DE_PARA_SUB_PROCESSO dpsp
                    where sp.ano_apresentacao = dpsp.ano_apresentacao
                    and sp.id_representacao = dpsp.id_representacao
                    and sp.id_processo = dpsp.id_processo
                    and sp.d_sub_processo = dpsp.d_sub_processo
                    and sp.d_natureza = dpsp.d_natureza
                    and sp.id_sequencial_natureza = dpsp.id_sequencial_natureza)                    
    and pp.numero_carteira is null
    order by sp.ano_apresentacao, sp.id_representacao, sp.id_processo,
        sp.d_sub_processo, sp.d_natureza, sp.id_sequencial_natureza,
        sp.dthr_homologacao                   
                    
                    

END
GO

GRANT EXECUTE on SPR_ICM_LISTA_SUB_PROCESSO_POR_HOMOLOGACAO to LogCacMv
GO