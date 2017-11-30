IF  EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[SPR_ICM_LISTA_MENSALIDADES_TRABALHO_POR_DIOPS]') AND type in (N'P', N'PC'))
begin
    DROP PROCEDURE [dbo].[SPR_ICM_LISTA_MENSALIDADES_TRABALHO_POR_DIOPS]
end
GO

/* 
    dbo.SPR_ICM_LISTA_MENSALIDADES_TRABALHO_POR_DIOPS:
    
    Script baseado no script original de SPR_ICM_LISTA_MENSALIDADES_TRABALHO, porém
    aproveitando a estrutura de dados enviada ao PROTHEUS (EXP_PROVISAO_PRR e EXP_BAIXA_BXR)
    para decidir os comandos que serão enviados.
    
    Autor: JCJ
    Data: 11/07/2017
*/

CREATE /*ALTER*/ PROCEDURE dbo.SPR_ICM_LISTA_MENSALIDADES_TRABALHO_POR_DIOPS(
    @dt_referencia_diops datetime,
    @dt_vencimento_limite datetime
)
AS
BEGIN
    SET NOCOUNT ON;

    if OBJECT_ID('TempDb..#dados') is not null
    begin
        drop table #dados
    end

    --declare @dt_referencia_diops datetime = '01/04/2017'
    set @dt_referencia_diops = dbo.F_inicio_do_mes(@dt_referencia_diops)

    ;WITH COMANDOS_DIOPS as (
        select  prr.id_comando,
                @dt_referencia_diops as dt_referencia,
                CONVERT(datetime, prr.datatitulo) as dt_emissao,
                CONVERT(datetime, prr.datavenc) as dt_vencimento                 
        from EXP_PROVISAO_PRR prr inner join EXP_DIOPS ed
            on prr.id_exp_diops = ed.id_exp_diops
        where ed.codigo = 'PRR'
            and ed.dt_inicio = @dt_referencia_diops
        union
        select  ebb.id_comando,
                @dt_referencia_diops as dt_referencia,
                CONVERT(datetime, epp.datatitulo) as dt_emissao,
                CONVERT(datetime, epp.datavenc) as dt_vencimento   
        from EXP_BAIXA_BXR ebb inner join EXP_PROVISAO_PRR epp
                on ebb.sequencial = epp.sequencial
            inner join EXP_DIOPS ed
                on ebb.id_exp_diops = ed.id_exp_diops
        where ed.codigo = 'BXR'
            and ed.dt_inicio = @dt_referencia_diops
    )
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
                 when cc.d_forma_pagamento = 'F' then RANK() over (order by dt.id_titular, cc.d_forma_pagamento, cc.dt_vencimento, cc.dt_referencia) 
                 else RANK() over (order by dt.id_titular, cc.d_forma_pagamento, cc.dt_vencimento, cc.id_pessoa) 
            end as seq_mensalidade,
            dt.id_titular,

            cc.d_forma_pagamento,
            dt_emissao                           = cd.dt_emissao,           
            dt_referencia_prevista               = @dt_referencia_diops, -- para a MV, a data de referência é a do título em si, e não a referência no conceito como utilizado pela CAC na COMANDO (no sentido de cobertura)
            dt_vencimento                        = cd.dt_vencimento,
            cc.id_comando
            
    into #dados
    from COMANDO cc inner join RUBRICA_CAC rc
            on cc.id_rubrica_cac = rc.id_rubrica_cac         
        inner join COMANDOS_DIOPS cd
            on cc.id_comando = cd.id_comando
        left join VW_DEPENDENTE_TITULAR dt
            on cc.id_pessoa = dt.id_dependente
    where 
        not exists (select 1 from MV_CAC_DE_PARA_ITEM_MENSALIDADE dpim inner join MV_CAC_DE_PARA_GRUPO_ITEM_MENSALIDADE dpgim
                            on dpim.id_de_para_grupo_item_mensalidade = dpgim.id_de_para_grupo_item_mensalidade
                        inner join MV_CAC_STATUS_ENVIO_GRUPO_ITEM_MENS segim
                            on dpgim.id_status_envio_grupo_item_mens = segim.id_status_envio_grupo_item_mens
                    where cc.id_comando = dpim.id_comando
                    and segim.cod_status <> 'CA')
        /*and not exists (select 1 from COMANDO cr
                        where cr.id_comando_novo = cc.id_comando
                        and cr.id_motivo_regularizacao = 18
                        and cr.d_forma_pagamento = 'F')  */      
        and convert(date, cd.dt_vencimento) <= CONVERT(date, @dt_vencimento_limite)
    /* rubricas CAC que representam juros de pagamento por atraso, a serem geradas pelo SOUL */
        and cc.id_rubrica_cac not in (618, 718)
        and cc.d_forma_pagamento <> 'T'
        
        
    select * from #dados
    order by seq_mensalidade

    if OBJECT_ID('TempDb..#dados') is not null
    begin
        drop table #dados
    end

END
GO

GRANT EXECUTE ON dbo.SPR_ICM_LISTA_MENSALIDADES_TRABALHO_POR_DIOPS to LogCacMv
GO

GRANT EXECUTE on dbo.F_Inicio_do_mes to LogCacMv
GO