IF  EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[SPR_ICM_LISTA_MENSALIDADES_TRABALHO]') AND type in (N'P', N'PC'))
begin
    DROP PROCEDURE [dbo].[SPR_ICM_LISTA_MENSALIDADES_TRABALHO]
end
GO

/* 
    dbo.SPR_ICM_LISTA_MENSALIDADES_TRABALHO:
    
    Script baseado no script original de SPR_ICM_LISTA_MENSALIDADES_CONTRATO, para listar apenas os dados
    iniciais a serem gravados em ICM_MENSALIDADE_TRABALHO.
    
    Autor: JCJ
    Data: 27/03/2017
*/

CREATE /*ALTER*/ PROCEDURE dbo.SPR_ICM_LISTA_MENSALIDADES_TRABALHO(
    @dt_referencia_atual datetime,
    @dt_geracao_inicial datetime,
    @dt_geracao_final datetime
)
AS
BEGIN
    SET NOCOUNT ON;

    if OBJECT_ID('TempDb..#dados') is not null
    begin
        drop table #dados
    end

    /*declare @dt_referencia_atual date = '01/06/2016'
    declare @dt_geracao_inicial date = '01/06/2016'
    declare @dt_geracao_final date = '30/06/2016'*/


    set @dt_referencia_atual = dbo.F_inicio_do_mes(@dt_referencia_atual)

    select
            case 
                 when cc.d_forma_pagamento = 'C' then 
                    case when cc.id_comando_interface is not null then -cc.id_comando_interface
                         else RANK() over (order by dt.id_titular, cc.d_forma_pagamento, cc.dt_vencimento) 
                    end
                 when cc.d_forma_pagamento = 'I' then 
                    case when cc.id_comando_interface is not null then -cc.id_comando_interface 
                         else RANK() over (order by dt.id_titular, cc.d_forma_pagamento, cc.dt_vencimento, cc.id_pessoa) 
                    end
                 when cc.d_forma_pagamento = 'F' then RANK() over (order by dt.id_titular, cc.d_forma_pagamento, DATEADD(day, 14, dateadd(month, 1, dbo.f_inicio_do_mes(cc.dt_referencia))), cc.dt_referencia) 
                 else RANK() over (order by dt.id_titular, cc.d_forma_pagamento, cc.dt_vencimento, cc.id_pessoa) 
            end as seq_mensalidade,
            dt.id_titular,

            cc.d_forma_pagamento,
            dt_emissao                           = case when convert(date, GETDATE()) > convert(date, cc.dt_vencimento) then dbo.f_inicio_do_mes(cc.dt_vencimento)
                                                        else GETDATE()
                                                   end,           
            dt_referencia_prevista               = case when cc.d_forma_pagamento = 'F' then cc.dt_referencia
                                                        else  @dt_referencia_atual /* Boletos serão considerados como data de referência atual, independente da original */
                                                   end,
                        
            dt_vencimento                        = case when cc.d_forma_pagamento = 'F' then DATEADD(day, 14, dateadd(month, 1, dbo.f_inicio_do_mes(cc.dt_referencia)))
                                                        else cc.dt_vencimento      /* Folha tem como data de referência fixa o dia 15 do mês seguinte ao da data de referência. */
                                                   end,
            cc.id_comando
            
    into #dados
    from COMANDO cc inner join RUBRICA_CAC rc
            on cc.id_rubrica_cac = rc.id_rubrica_cac
        left join VW_DEPENDENTE_TITULAR dt
            on cc.id_pessoa = dt.id_dependente
    where 
    /* não pode ter sido enviado */
        cc.id_comando_interface is null 
    /* não pode ter sido baixado */
        and cc.id_motivo_regularizacao is null 
        and cc.f_baixado = 0
        and not exists (select 1 from BAIXA bb
                        where cc.id_comando = bb.id_comando)
    /* rubricas CAC que não representam títulos a receber */                
        and cc.id_rubrica_cac not in (6042, 7042) 
    /* rubricas CAC que representam juros de pagamento por atraso, a serem geradas pelo SOUL */
        and cc.id_rubrica_cac not in (618, 718)
    /* Intervalo de busca */
        and convert(date, cc.dt_geracao_comando) >= convert(date, @dt_geracao_inicial) 
        and convert(date, cc.dt_geracao_comando) <= convert(date, @dt_geracao_final)
    /* Apenas referência atual ou anterior à atual */
        and CONVERT(date, dbo.f_inicio_do_mes(cc.dt_referencia)) <= @dt_referencia_atual
        and cc.d_forma_pagamento <> 'T'
    /* Apenas comandos com data de vencimento iguais ou maiores que hoje */
        and   ( 
                ( (cc.d_forma_pagamento = 'F') and (CONVERT(date, DATEADD(day, 14, dateadd(month, 1, dbo.f_inicio_do_mes(cc.dt_referencia)))) > CONVERT(date, getdate()) ) )
                or 
                ( CONVERT(date, cc.dt_vencimento) >= CONVERT(date, getdate()) ) 
              )
    /* Apenas comandos não enviados à MV. */        
        and not exists (select 1 from MV_CAC_DE_PARA_ITEM_MENSALIDADE dpim inner join MV_CAC_DE_PARA_GRUPO_ITEM_MENSALIDADE dpgim
                                on dpim.id_de_para_grupo_item_mensalidade = dpgim.id_de_para_grupo_item_mensalidade
                            inner join MV_CAC_STATUS_ENVIO_GRUPO_ITEM_MENS segim
                                on dpgim.id_status_envio_grupo_item_mens = segim.id_status_envio_grupo_item_mens
                        where cc.id_comando = dpim.id_comando
                        and segim.cod_status <> 'CA')
        
    select * from #dados
    order by seq_mensalidade

    if OBJECT_ID('TempDb..#dados') is not null
    begin
        drop table #dados
    end

END
GO

GRANT EXECUTE ON dbo.SPR_ICM_LISTA_MENSALIDADES_TRABALHO to LogCacMv
GO

GRANT EXECUTE on dbo.F_Inicio_do_mes to LogCacMv
GO