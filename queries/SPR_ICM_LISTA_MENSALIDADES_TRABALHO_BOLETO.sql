IF  EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[SPR_ICM_LISTA_MENSALIDADES_TRABALHO_BOLETO]') AND type in (N'P', N'PC'))
begin
    DROP PROCEDURE [dbo].[SPR_ICM_LISTA_MENSALIDADES_TRABALHO_BOLETO]
end
GO

/* 
    dbo.SPR_ICM_LISTA_MENSALIDADES_TRABALHO_BOLETO:
    
    Script baseado no script original de SPR_ICM_LISTA_MENSALIDADES_TRABALHO, porém com regras novas
    e restrito a comandos de boleto (carnês individuais e unificados).
    
    Autor: JCJ
    Data: 29/09/2017
   
    dia 04/10/2017 - LAT  - Modificação da query de boleto por comando interface retirando a captura por dt_vencimento para
    capturar por dt_geracao_comando para determinar a data limite. Retirada do sinal de igual para as duas condições de dt_vencimento
    do tratamento sem comando interface. Mantive so na data de vencimento base. 
    --para a data de vencimento limite informar sempre o primeiro dia posterior ao limite 
   
*/

CREATE /*ALTER*/ PROCEDURE dbo.SPR_ICM_LISTA_MENSALIDADES_TRABALHO_BOLETO(
    @dt_referencia datetime,
    @dt_vencimento_base datetime,
    @dt_vencimento_limite datetime
)
AS
BEGIN
    SET NOCOUNT ON;

    if OBJECT_ID('TempDb..#dados') is not null
    begin
        drop table #dados
    end

    /*declare @dt_referencia date = '01/06/2016'
    declare @dt_vencimento_base date = '01/06/2016'
    declare @dt_vencimento_limite date = '01/07/2016'*/


    set @dt_referencia = dbo.F_inicio_do_mes(@dt_referencia)
    set @dt_vencimento_base = CONVERT(date, @dt_vencimento_base)
    set @dt_vencimento_limite = CONVERT(date, @dt_vencimento_limite)


    /* 
        Critérios para comandos de boleto sem comando_interface:
        - Critérios de busca: Comandos com data de referência igual à informada como parâmetro, ou com data de vencimento a partir de uma data base, até uma data de vencimento limite
        - Critérios de agrupamento: 
            > Para carnês unificados: titular + data de vencimento; 
            > Para carnês individuais: titular + data de vencimento + pessoa
        - Dados:
            > Emissão: menor data de geração de comando do grupo, desde que maior ou igual à data de referência parâmetro;
            > Referência: Igual à informada como parâmetro;
            > Vencimento: Igual a qualquer uma do grupo (já que a data de vencimento é um dos critérios de agrupamento
    */

    select
            case 
                 when cc.d_forma_pagamento = 'C' then 
                    RANK() over (order by dt.id_titular, convert(date, cc.dt_vencimento))                     
                 when cc.d_forma_pagamento = 'I' then 
                    RANK() over (order by dt.id_titular, convert(date, cc.dt_vencimento), cc.id_pessoa)                     
            end as seq_mensalidade,
            dt.id_titular,
            cc.d_forma_pagamento,
            dt_emissao                           = case when convert(date, cc.dt_geracao_comando) < @dt_referencia then @dt_referencia else CONVERT(date, cc.dt_geracao_comando) end,           
            dt_referencia_prevista               = @dt_referencia,                        
            dt_vencimento                        = convert(date, cc.dt_vencimento),
            cc.id_comando,
            cc.id_pessoa,
            cc.id_comando_interface
            
    into #dados
    from COMANDO cc left join VW_DEPENDENTE_TITULAR dt
            on cc.id_pessoa = dt.id_dependente
    where 
    /* Apenas comandos de boleto */
        cc.d_forma_pagamento in ('C', 'I')

    /* Apenas comandos sem comando interface */
        and cc.id_comando_interface is null
    
    /* Exclui rubricas CAC que não representam títulos a receber */                
        and cc.id_rubrica_cac not in (6042, 7042) 
    
    /* Exclui rubricas CAC que representam juros de pagamento por atraso, a serem geradas pelo SOUL */
        and cc.id_rubrica_cac not in (618, 718)
   
    /* Apenas referência atual (regra de geração normal de boletos) ou boletos com vencimento a partir de uma data base (regra de geração de boleto diária) */
        and ( ( CONVERT(date, dbo.f_inicio_do_mes(cc.dt_referencia)) = @dt_referencia ) 
              or ( CONVERT(date, cc.dt_vencimento) >= @dt_vencimento_base )
              )
    /* Para ambos os casos, limita até a data de vencimento informada. */
        and CONVERT(date, cc.dt_vencimento) < @dt_vencimento_limite

    /* Exclui comandos que são de tesouraria. */
        and cc.d_forma_pagamento <> 'T'
    
    /* Apenas comandos não enviados à MV. */        
        and not exists (select 1 from MV_CAC_DE_PARA_ITEM_MENSALIDADE dpim inner join MV_CAC_DE_PARA_GRUPO_ITEM_MENSALIDADE dpgim
                                on dpim.id_de_para_grupo_item_mensalidade = dpgim.id_de_para_grupo_item_mensalidade
                            inner join MV_CAC_STATUS_ENVIO_GRUPO_ITEM_MENS segim
                                on dpgim.id_status_envio_grupo_item_mens = segim.id_status_envio_grupo_item_mens
                        where cc.id_comando = dpim.id_comando
                        and segim.cod_status <> 'CA')

    -- TODO: Query para boletos com comando interface
    --       Ajustar datas de emissão e vencimento
    --       Incluir comandos de boleto que porventura tenham comando interface igual aos presentes mas que não estejam aqui

    /* 
        Critérios para comandos de boleto com comando interface:
        - Critérios de busca: Comandos com comando interface cuja data de referência é igual a do parâmetro, e com data de vencimento até uma data de vencimento limite ;
        - Critérios de agrupamento: O próprio comando interface;
        - Dados:
            > Emissão: menor data de geração de comando do grupo, desde que maior ou igual à data de referência parâmetro;
            > Referência: Igual à informada como parâmetro (que é a mesma do comando interface);
            > Vencimento: menor data de vencimento dos comandos do grupo.    
    */

    insert into #dados(seq_mensalidade, id_titular, d_forma_pagamento, dt_emissao, dt_referencia_prevista, dt_vencimento, id_comando, id_pessoa, id_comando_interface)
    select
            -cc.id_comando_interface as seq_mensalidade,
            dt.id_titular,
            cc.d_forma_pagamento,
            dt_emissao                           = case when convert(date, cc.dt_geracao_comando) < @dt_referencia then @dt_referencia else CONVERT(date, cc.dt_geracao_comando) end,           
            dt_referencia_prevista               = @dt_referencia,                        
            dt_vencimento                        = convert(date, cc.dt_vencimento),
            cc.id_comando,
            cc.id_pessoa,
            cc.id_comando_interface
    from COMANDO cc inner join COMANDO_INTERFACE ci
            on cc.id_comando_interface = ci.id_comando_interface
        left join VW_DEPENDENTE_TITULAR dt
            on cc.id_pessoa = dt.id_dependente
    where
    /* Apenas comandos de boleto */
        cc.d_forma_pagamento in ('C', 'I')

    /* Exclui rubricas CAC que não representam títulos a receber */                
        and cc.id_rubrica_cac not in (6042, 7042) 
    
    /* Exclui rubricas CAC que representam juros de pagamento por atraso, a serem geradas pelo SOUL */
        and cc.id_rubrica_cac not in (618, 718)

    /* Apenas comandos cujos comandos interface têm referência igual à passada por parâmetro */
        and ci.dt_referencia = @dt_referencia

    /* Limita até a data de vencimento informada */
        and CONVERT(date, cc.dt_geracao_comando) < @dt_vencimento_limite

    /* Exclui comandos que são de tesouraria */
        and cc.d_forma_pagamento <> 'T'

    /* Apenas comandos não enviados à MV. */        
        and not exists (select 1 from MV_CAC_DE_PARA_ITEM_MENSALIDADE dpim inner join MV_CAC_DE_PARA_GRUPO_ITEM_MENSALIDADE dpgim
                                on dpim.id_de_para_grupo_item_mensalidade = dpgim.id_de_para_grupo_item_mensalidade
                            inner join MV_CAC_STATUS_ENVIO_GRUPO_ITEM_MENS segim
                                on dpgim.id_status_envio_grupo_item_mens = segim.id_status_envio_grupo_item_mens
                        where cc.id_comando = dpim.id_comando
                        and segim.cod_status <> 'CA')

   
    /* Ajuste das datas de vencimento menores que as de emissão */
    update #dados
    set dt_vencimento = dt_emissao
    where dt_emissao > dt_vencimento

    /* Ajustando as datas a serem enviadas */
    ;WITH DATAS_CERTAS as (
        select seq_mensalidade, MIN(dt_emissao) as dt_emissao, MIN(dt_vencimento) as dt_vencimento
        from #dados
        group by seq_mensalidade
    )
    update dd
    set dt_emissao = dc.dt_emissao,
        dt_vencimento = dc.dt_vencimento
    from #dados dd inner join DATAS_CERTAS dc
        on dd.seq_mensalidade = dc.seq_mensalidade 

    /* Inserindo comandos que já possuem comando_interface em #dados mas ainda não estão presentes nela. */
    insert into #dados(seq_mensalidade, id_titular, d_forma_pagamento, dt_emissao, dt_referencia_prevista, dt_vencimento, id_comando, id_pessoa, id_comando_interface)
    select distinct 
        dd.seq_mensalidade,
        dd.id_titular,
        dd.d_forma_pagamento,
        dd.dt_emissao,
        dd.dt_referencia_prevista,
        dd.dt_vencimento,
        cc.id_comando,
        cc.id_pessoa,
        dd.id_comando_interface
    from #dados dd inner join COMANDO cc
        on dd.id_comando_interface = cc.id_comando_interface
    where not exists (select 1 from #dados ddd where ddd.id_comando_interface = dd.id_comando_interface
                        and ddd.id_comando = cc.id_comando)
    /* Apenas comandos de boleto */
        and cc.d_forma_pagamento in ('C', 'I')

    /* Exclui rubricas CAC que não representam títulos a receber */                
        and cc.id_rubrica_cac not in (6042, 7042) 
    
    /* Exclui rubricas CAC que representam juros de pagamento por atraso, a serem geradas pelo SOUL */
        and cc.id_rubrica_cac not in (618, 718)

    /* Exclui comandos que são de tesouraria */
        and cc.d_forma_pagamento <> 'T'

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

GRANT EXECUTE ON dbo.SPR_ICM_LISTA_MENSALIDADES_TRABALHO_BOLETO to LogCacMv
GO

GRANT EXECUTE on dbo.F_Inicio_do_mes to LogCacMv
GO