IF  EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[SPR_ICM_LISTA_MENSALIDADES_TRABALHO_FOLHA]') AND type in (N'P', N'PC'))
begin
    DROP PROCEDURE [dbo].[SPR_ICM_LISTA_MENSALIDADES_TRABALHO_FOLHA]
end
GO

/* 
    dbo.SPR_ICM_LISTA_MENSALIDADES_TRABALHO_FOLHA:
    
    Script baseado no script original de SPR_ICM_LISTA_MENSALIDADES_TRABALHO, porém com regras novas
    e restrito a comandos de folha.
    
    Autor: JCJ
    Data: 29/09/2017
    
    Dia 05/10/2017 LAT  -Alteração da regra 1 para acréscimo diferenciado de regra para comandos com baixa e sem baixa
*/

CREATE /*ALTER*/ PROCEDURE dbo.SPR_ICM_LISTA_MENSALIDADES_TRABALHO_FOLHA(
    @dt_referencia datetime
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
    declare @dt_vencimento_limite date = '30/06/2016'*/


    set @dt_referencia = dbo.F_inicio_do_mes(@dt_referencia)

    declare @dt_vencimento_folha date = dateadd(day, 14, dateadd(month, 1, @dt_referencia))

    /* 
        Regra de Folha 1: Sem Comando Interface, geração normal (não retroativa):
        - Critérios de busca: todos os comandos com data de referência igual à passada por parâmetro e com o mês da data de geração igual ao menor que o mês da data de referência parâmetro;
        - Critérios de agrupamento: Pelo titular do beneficiário do comando + data de referência (essa última, por construção, é constante num determinado processamento);
        - Dados:
        > Emissão: Menor data de geração de comando do grupo que seja maior que a data de referência passada como parâmetro. Caso não exista, considerar a própria data de referência;
        > Referência: Igual à passada como parâmetro;
        > Vencimento: Fixo, igual ao dia 15 do mês seguinte ao mês da data de referência passada como parâmetro
    */

    /* TODO: Adicionar condição para listar apenas comandos com data de geração até o fim do mês da data de referência passada por parâmetro. */
    select
            -dt.id_titular as seq_mensalidade, /* Como o agrupamento das regras 1 e 3 será único, será utilizado o mesmo agrupador para ambos.*/
            dt.id_titular,
            cc.d_forma_pagamento,
            dt_emissao                           = case when convert(date, cc.dt_geracao_comando) < @dt_referencia then @dt_referencia else CONVERT(date, cc.dt_geracao_comando) end,           
            dt_referencia_prevista               = @dt_referencia,                        
            dt_vencimento                        = @dt_vencimento_folha,
            cc.id_comando,
            cc.id_pessoa,
            cc.id_comando_interface,
            '1' as regra
            
    into #dados
    from COMANDO cc left join VW_DEPENDENTE_TITULAR dt
            on cc.id_pessoa = dt.id_dependente
    where 
    /* Apenas comandos de folha */
        cc.d_forma_pagamento in ('F')

    /* Apenas comandos sem comando interface */
        and cc.id_comando_interface is null
    
    /* Exclui rubricas CAC que não representam títulos a receber */                
        and cc.id_rubrica_cac not in (6042, 7042) 
    
    /* Exclui rubricas CAC que representam juros de pagamento por atraso, a serem geradas pelo SOUL */
        and cc.id_rubrica_cac not in (618, 718)
   
 
    /* REGRA PARA NÃO BAIXADOS */
    /* Apenas referência igual ao parâmetro */
  AND  ( (CONVERT(date, dbo.f_inicio_do_mes(cc.dt_referencia)) = @dt_referencia 

    /* Apenas comandos com data de geração igual ou menor que o mês da referência parâmetro */
        and CONVERT(date, dbo.F_inicio_do_mes(cc.dt_geracao_comando)) <= @dt_referencia 
        and cc.f_baixado = 0
        )
    
    OR
     
    /* REGRA PARA BAIXADOS  */
   /* Apenas comandos com data de referncia maior que a data referencia parametro e
      data de geração igual ao mês da referência parâmetro */
       (CONVERT(date, dbo.f_inicio_do_mes(cc.dt_referencia)) > @dt_referencia 
     AND ( CONVERT(date, dbo.F_inicio_do_mes(cc.dt_geracao_comando)) = @dt_referencia 
            and cc.f_baixado = 1))
    
    )
    
    /* Apenas comandos não enviados à MV. */        
        and not exists (select 1 from MV_CAC_DE_PARA_ITEM_MENSALIDADE dpim inner join MV_CAC_DE_PARA_GRUPO_ITEM_MENSALIDADE dpgim
                                on dpim.id_de_para_grupo_item_mensalidade = dpgim.id_de_para_grupo_item_mensalidade
                            inner join MV_CAC_STATUS_ENVIO_GRUPO_ITEM_MENS segim
                                on dpgim.id_status_envio_grupo_item_mens = segim.id_status_envio_grupo_item_mens
                        where cc.id_comando = dpim.id_comando
                        and segim.cod_status <> 'CA')

    /* 
        Regra de Folha 2: Com comando interface, geração normal (não retroativa):
        - Critérios de Busca: Todos os comandos com comandos interface de folha (com id_empresa), com data de referência do comando interface igual à data de referência 
            passada como parâmetro e com o mês da data de geração do comando igual ou menor que o mês da data de referência parâmetro;
        - Critérios de agrupamento: Pelo titular dos beneficiários do comando (que é o mesmo presente no comando interface de folha) + data de referência (essa última, por construção, 
            é constante num determinado processamento);
        - Dados: 
        > Emissão: Menor data de geração de comando do grupo que seja maior que a data de referência passada como parâmetro. Caso não exista, considerar a própria data de referência;
        > Referência: Igual à passada como parâmetro;
        > Vencimento: Fixo, igual ao dia 15 do mês seguinte ao mês da data de referência passada como parâmetro.
    */

    insert into #dados(seq_mensalidade, id_titular, d_forma_pagamento, dt_emissao, dt_referencia_prevista, dt_vencimento, id_comando, id_pessoa, id_comando_interface, regra)
    select
            dt.id_titular as seq_mensalidade,
            dt.id_titular,
            cc.d_forma_pagamento,
            dt_emissao                           = case when convert(date, cc.dt_geracao_comando) < @dt_referencia then @dt_referencia else CONVERT(date, cc.dt_geracao_comando) end,           
            dt_referencia_prevista               = @dt_referencia,                        
            dt_vencimento                        = @dt_vencimento_folha,
            cc.id_comando,
            cc.id_pessoa,
            cc.id_comando_interface,
            '2' as regra
    from COMANDO cc inner join COMANDO_INTERFACE ci
            on cc.id_comando_interface = ci.id_comando_interface
        left join VW_DEPENDENTE_TITULAR dt
            on cc.id_pessoa = dt.id_dependente
    where
    /* Apenas comandos de boleto */
        cc.d_forma_pagamento in ('F')

    /* Exclui rubricas CAC que não representam títulos a receber */                
        and cc.id_rubrica_cac not in (6042, 7042) 
    
    /* Exclui rubricas CAC que representam juros de pagamento por atraso, a serem geradas pelo SOUL */
        and cc.id_rubrica_cac not in (618, 718)

    /* Apenas comandos cujos comandos interface têm referência igual à passada por parâmetro */
        and CONVERT(date, dbo.f_inicio_do_mes(ci.dt_referencia)) = @dt_referencia

    /* Apenas comandos com data de geração igual ou menor que o mês da referência parâmetro */
        and CONVERT(date, dbo.F_inicio_do_mes(cc.dt_geracao_comando)) <= @dt_referencia

    /* Apenas comandos interface de folha (comandos_interface com id_empresa diferente de null) */
        and ci.id_empresa is not null

    /* Apenas comandos não enviados à MV. */        
        and not exists (select 1 from MV_CAC_DE_PARA_ITEM_MENSALIDADE dpim inner join MV_CAC_DE_PARA_GRUPO_ITEM_MENSALIDADE dpgim
                                on dpim.id_de_para_grupo_item_mensalidade = dpgim.id_de_para_grupo_item_mensalidade
                            inner join MV_CAC_STATUS_ENVIO_GRUPO_ITEM_MENS segim
                                on dpgim.id_status_envio_grupo_item_mens = segim.id_status_envio_grupo_item_mens
                        where cc.id_comando = dpim.id_comando
                        and segim.cod_status <> 'CA')

    /*
        Regra de Folha 3: Sem comando interface, geração retroativa (data de geração do comando tem mês posterior à data de referência do comando):
        - Critérios de Busca: todos os comandos com data de geração de comando com mês igual à data de referência parâmetro e com data de referência de comando menor que à data de referência parâmetro;
        - Critérios de agrupamento: Pelo titular do beneficiário do comando;
        - Dados:
        > Emissão: Menor data de geração do comando do grupo que seja maior que a data de referência passada como parâmetro. Caso não exista, considerar a própria data de referência parâmetro. Caso seja 
            maior que a data de vencimento definida (através da regra abaixo), considerar a data de vencimento definida;
        > Referência: Igual à passada como parâmetro
        > Vencimento: Menor data de vencimento do grupo que seja maior que a data de referência passada como parâmetro. Caso não exista, considerar a própria data de referência parâmetro.
    */

    insert into #dados(seq_mensalidade, id_titular, d_forma_pagamento, dt_emissao, dt_referencia_prevista, dt_vencimento, id_comando, id_pessoa, id_comando_interface, regra)
    select
            dt.id_titular as seq_mensalidade,
            dt.id_titular,
            cc.d_forma_pagamento,
            dt_emissao                           = case when convert(date, cc.dt_geracao_comando) < @dt_referencia then @dt_referencia else CONVERT(date, cc.dt_geracao_comando) end,           
            dt_referencia_prevista               = @dt_referencia,                        
            dt_vencimento                        = case when convert(date, cc.dt_vencimento) < @dt_referencia then @dt_referencia else CONVERT(date, cc.dt_vencimento) end,
            cc.id_comando,
            cc.id_pessoa,
            cc.id_comando_interface,
            '3' as regra
    from COMANDO cc left join VW_DEPENDENTE_TITULAR dt
            on cc.id_pessoa = dt.id_dependente
    where
    /* Apenas comandos de boleto */
        cc.d_forma_pagamento in ('F')

    /* Exclui rubricas CAC que não representam títulos a receber */                
        and cc.id_rubrica_cac not in (6042, 7042) 

    /* Exclui rubricas CAC que representam juros de pagamento por atraso, a serem geradas pelo SOUL */
        and cc.id_rubrica_cac not in (618, 718)

    /* Apenas comandos sem comando interface */
        and cc.id_comando_interface is null

    /* Apenas comandos com data de geração no mesmo mês que a data de referência parâmetro */
        and CONVERT(date, dbo.f_inicio_do_mes(cc.dt_geracao_comando)) = @dt_referencia

    /* Apenas comandos com data de referência menor que a data de referência parâmetro */
        and CONVERT(date, dbo.f_inicio_do_mes(cc.dt_referencia)) < @dt_referencia

    /* Apenas comandos não enviados à MV. */        
        and not exists (select 1 from MV_CAC_DE_PARA_ITEM_MENSALIDADE dpim inner join MV_CAC_DE_PARA_GRUPO_ITEM_MENSALIDADE dpgim
                                on dpim.id_de_para_grupo_item_mensalidade = dpgim.id_de_para_grupo_item_mensalidade
                            inner join MV_CAC_STATUS_ENVIO_GRUPO_ITEM_MENS segim
                                on dpgim.id_status_envio_grupo_item_mens = segim.id_status_envio_grupo_item_mens
                        where cc.id_comando = dpim.id_comando
                        and segim.cod_status <> 'CA')


/*
    Regra de Folha 4: Com comando interface, geração retroativa (data de geração do comando tem mês posterior à data de referência do comando):
    - Critérios de busca: todos os comandos com data de geração de comando com mês igual à data de referência parâmetro e com data de referência de comando menor que à data de referência parâmetro;
    - Critérios de agrupamento: Pelo titular do beneficiário do comando;
    - Dados:
    > Emissão: Menor data de geração do comando do grupo que seja maior que a data de referência passada como parâmetro. Caso não exista, considerar a própria data de referência parâmetro. Caso seja 
        maior que a data de vencimento definida (através da regra abaixo), considerar a data de vencimento definida;
    > Referência: Igual à passada como parâmetro
    > Vencimento: Menor data de vencimento do grupo que seja maior que a data de referência passada como parâmetro. Caso não exista, considerar a própria data de referência parâmetro.
*/

    /* Para garantir que os registros da regra 2 e 4 não sejam agrupados entre si, será adicionado um offset no sequencial de mensalidade da regra 4 */
    declare @OFFSET int
    select @OFFSET = MAX(id_pessoa) + 1000 from PESSOA

    insert into #dados(seq_mensalidade, id_titular, d_forma_pagamento, dt_emissao, dt_referencia_prevista, dt_vencimento, id_comando, id_pessoa, id_comando_interface, regra)
    select
            dt.id_titular + @OFFSET as seq_mensalidade,
            dt.id_titular,
            cc.d_forma_pagamento,
            dt_emissao                           = case when convert(date, cc.dt_geracao_comando) < @dt_referencia then @dt_referencia else CONVERT(date, cc.dt_geracao_comando) end,           
            dt_referencia_prevista               = @dt_referencia,                        
            dt_vencimento                        = case when convert(date, cc.dt_vencimento) < @dt_referencia then @dt_referencia else CONVERT(date, cc.dt_vencimento) end,
            cc.id_comando,
            cc.id_pessoa,
            cc.id_comando_interface,
            '4' as regra
    from COMANDO cc left join VW_DEPENDENTE_TITULAR dt
            on cc.id_pessoa = dt.id_dependente
    where
    /* Apenas comandos de boleto */
        cc.d_forma_pagamento in ('F')

    /* Exclui rubricas CAC que não representam títulos a receber */                
        and cc.id_rubrica_cac not in (6042, 7042) 

    /* Exclui rubricas CAC que representam juros de pagamento por atraso, a serem geradas pelo SOUL */
        and cc.id_rubrica_cac not in (618, 718)

    /* Apenas comandos com comando interface */
        and cc.id_comando_interface is not null

    /* Apenas comandos com data de geração no mesmo mês que a data de referência parâmetro */
        and CONVERT(date, dbo.f_inicio_do_mes(cc.dt_geracao_comando)) = @dt_referencia

    /* Apenas comandos com data de referência menor que a data de referência parâmetro */
        and CONVERT(date, dbo.f_inicio_do_mes(cc.dt_referencia)) < @dt_referencia

    /* Apenas comandos não enviados à MV. */        
        and not exists (select 1 from MV_CAC_DE_PARA_ITEM_MENSALIDADE dpim inner join MV_CAC_DE_PARA_GRUPO_ITEM_MENSALIDADE dpgim
                                on dpim.id_de_para_grupo_item_mensalidade = dpgim.id_de_para_grupo_item_mensalidade
                            inner join MV_CAC_STATUS_ENVIO_GRUPO_ITEM_MENS segim
                                on dpgim.id_status_envio_grupo_item_mens = segim.id_status_envio_grupo_item_mens
                        where cc.id_comando = dpim.id_comando
                        and segim.cod_status <> 'CA')

   
    /* Ajuste das datas de emissão maiores que as de vencimento */
    update #dados
    set dt_emissao = dt_vencimento
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




    select * from #dados
    order by seq_mensalidade

    if OBJECT_ID('TempDb..#dados') is not null
    begin
        drop table #dados
    end

END
GO

GRANT EXECUTE ON dbo.SPR_ICM_LISTA_MENSALIDADES_TRABALHO_FOLHA to LogCacMv
GO

GRANT EXECUTE on dbo.F_Inicio_do_mes to LogCacMv
GO