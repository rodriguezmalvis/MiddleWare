IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[SPR_ICM_LISTA_IMP_COPARTICIPACAO]') AND type in (N'P', N'PC'))
begin
    EXEC('CREATE PROCEDURE [dbo].[SPR_ICM_LISTA_IMP_COPARTICIPACAO] AS BEGIN SET NOCOUNT ON; END');
    GRANT EXECUTE ON dbo.SPR_ICM_LISTA_IMP_COPARTICIPACAO to LogCacMv
end
GO

/* 
    dbo.SPR_ICM_LISTA_IMP_COPARTICIPACAO:
    
    Script que gera os dados efetivos relacionados à estrutura impCoparticipacoes, para o envio de dados de
    procedimento quando do envio de itens de mensalidade de coparticipação.
    
    Autor: JCJ
    Data: 07/06/2017
*/

ALTER PROCEDURE dbo.SPR_ICM_LISTA_IMP_COPARTICIPACAO(
    @id_comando int
)
AS
BEGIN
    SET NOCOUNT ON;
   
    select  qq.*,
            cd_prestador = dpp.cod_prestador
    from (
        select
            id_comando = @id_comando,
            pp.ano_apresentacao,
            pp.id_representacao,
            pp.id_processo,
            pp.d_sub_processo,
            pp.d_natureza,
            pp.id_sequencial_natureza,
            pp.id_atendimento,
            pp.id_procedimento,
            cd_procedimento = isnull(dpp.id_amb_destino, pp.id_amb),
            tp_guia = case when dd.tipo_guia_mv = 'I' then 'I'
                           when dd.tipo_guia_mv is not null then 'A'
                           else null
                      end,
            dt_realizacao = aa.dt_atendimento,
            vl_coparticipacao = cc.valor,
            id_prestador = case when pc.id_prestador is not null then sp.id_prestador
                                else aa.id_prestador_reembolso
                           end
        from dbo.F_procedimento_comando_tab(@id_comando) as pct inner join PROCEDIMENTO pp
                on pct.ano_apresentacao = pp.ano_apresentacao
                and pp.id_representacao = pct.id_representacao
                and pp.id_processo = pct.id_processo
                and pp.d_sub_processo = pct.d_sub_processo
                and pp.d_natureza = pct.d_natureza
                and pp.id_sequencial_natureza = pct.id_sequencial_natureza
                and pp.id_atendimento = pct.id_atendimento
                and pp.id_procedimento = pct.id_procedimento
            inner join ATENDIMENTO aa
                on pp.ano_apresentacao = aa.ano_apresentacao
                and pp.id_representacao = aa.id_representacao
                and pp.id_processo = aa.id_processo
                and pp.d_sub_processo = aa.d_sub_processo
                and pp.d_natureza = aa.d_natureza
                and pp.id_sequencial_natureza = aa.id_sequencial_natureza
                and pp.id_atendimento = aa.id_atendimento
            inner join SUB_PROCESSO sp
                on pp.ano_apresentacao = sp.ano_apresentacao
                and pp.id_representacao = sp.id_representacao
                and pp.id_processo = sp.id_processo
                and pp.d_sub_processo = sp.d_sub_processo
                and pp.d_natureza = sp.d_natureza
                and pp.id_sequencial_natureza = sp.id_sequencial_natureza
            inner join PROCESSO pc
                on pp.ano_apresentacao = pc.ano_apresentacao
                and pp.id_representacao = pc.id_representacao
                and pp.id_processo = pc.id_processo
            inner join DOCUMENTO dd
                on aa.id_documento = dd.id_documento
            left join ICM_DE_PARA_PROCEDIMENTO dpp
                on pp.id_amb = dpp.id_amb_origem
                and pp.id_tipo_tabela = dpp.id_tipo_tabela_origem,
            COMANDO cc
        where cc.id_comando = @id_comando
    ) qq left join MV_CAC_DE_PARA_PRESTADOR dpp
        on qq.id_prestador = dpp.id_prestador

END
GO
