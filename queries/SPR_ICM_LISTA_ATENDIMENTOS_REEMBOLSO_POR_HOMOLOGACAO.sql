IF  EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[SPR_ICM_LISTA_ATENDIMENTOS_REEMBOLSO_POR_HOMOLOGACAO]') AND type in (N'P', N'PC'))
begin
    DROP PROCEDURE [dbo].[SPR_ICM_LISTA_ATENDIMENTOS_REEMBOLSO_POR_HOMOLOGACAO]
end
GO


CREATE /*ALTER*/ PROCEDURE dbo.SPR_ICM_LISTA_ATENDIMENTOS_REEMBOLSO_POR_HOMOLOGACAO(
    @dt_homologacao_inicial datetime,
    @dt_homologacao_final datetime
)
as
BEGIN
    SET NOCOUNT ON;

    select  aa.ano_apresentacao,
            aa.id_representacao,
            aa.id_processo,
            aa.d_sub_processo,
            aa.d_natureza,
            aa.id_sequencial_natureza,
            aa.id_atendimento,
            sp.dthr_homologacao
    from    SUB_PROCESSO sp inner join PROCESSO pp
                on sp.ano_apresentacao = pp.ano_apresentacao
                and sp.id_representacao = pp.id_representacao
                and sp.id_processo = pp.id_processo
            inner JOIN ATENDIMENTO aa
                on sp.ano_apresentacao = aa.ano_apresentacao
                and sp.id_representacao = aa.id_representacao
                and sp.id_processo = aa.id_processo
                and sp.d_sub_processo = aa.d_sub_processo
                and sp.d_natureza = aa.d_natureza
                and sp.id_sequencial_natureza = aa.id_sequencial_natureza
    where CONVERT(date, sp.dthr_homologacao) >= convert(date, @dt_homologacao_inicial)
    and CONVERT(date, sp.dthr_homologacao) <= convert(date, @dt_homologacao_final)
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
    and not exists (select 1 from MV_CAC_DE_PARA_REEMBOLSO dpr
                    where aa.ano_apresentacao = dpr.ano_apresentacao
                    and aa.id_representacao = dpr.id_representacao
                    and aa.id_processo = dpr.id_processo
                    and aa.d_sub_processo = dpr.d_sub_processo
                    and aa.d_natureza = dpr.d_natureza
                    and aa.id_sequencial_natureza = dpr.id_sequencial_natureza
                    and aa.id_atendimento = dpr.id_atendimento)                    
    and pp.numero_carteira is not null
    order by sp.dthr_homologacao, aa.ano_apresentacao, aa.id_representacao, aa.id_processo,
        aa.d_sub_processo, aa.d_natureza, aa.id_sequencial_natureza, aa.id_atendimento
                          
                    
                    

END
GO

GRANT EXECUTE on SPR_ICM_LISTA_ATENDIMENTOS_REEMBOLSO_POR_HOMOLOGACAO to LogCacMv
GO