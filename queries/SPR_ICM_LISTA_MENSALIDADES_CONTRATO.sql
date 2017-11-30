IF  EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[SPR_ICM_LISTA_MENSALIDADES_CONTRATO]') AND type in (N'P', N'PC'))
begin
    DROP PROCEDURE [dbo].[SPR_ICM_LISTA_MENSALIDADES_CONTRATO]
end
GO

/* 
    dbo.SPR_ICM_LISTA_MENSALIDADES_CONTRATO:
    
    v1.1: Script atualizado devido à migração para a terceira versão da estrutura de Títulos a Receber.
    Agora, os comandos já são previamente decididos, organizados em grupos, e o processamento será feito
    apenas sobre esses grupos. A origem da maioria dos dados continua a mesma, apenas o sequencial de 
    mensalidade que não faz mais sentido (e, portanto, foi retirado).

    Autor: JCJ
    Data: 03/07/2017


    v1.0: Script que gera os dados efetivos relacionados à estrutura MensalidadeContrato. Alguns dos dados
    não serão mais extraídos dessa query (devido ao pré-processamento por SPR_ICM_LISTA_MENSALIDADES_TRABALHO,
    porém foram mantidos no momento por compatibilidade (podendo ser excluídos futuramente).
    
    Autor: JCJ
    Data: 27/03/2017
*/

CREATE /*ALTER*/ PROCEDURE dbo.SPR_ICM_LISTA_MENSALIDADES_CONTRATO(
    @id_grupo_item_mensalidade_trabalho int
)
AS
BEGIN
    SET NOCOUNT ON;

    declare @f_devolucao bit = null
    declare @msg varchar(500)
    declare @id_titular int
    declare @d_forma_pagamento char(1)
    declare @dt_emissao datetime
    declare @dt_referencia_prevista datetime
    declare @dt_vencimento datetime


    select  @id_titular             = mt.id_titular,
            @d_forma_pagamento      = mt.d_forma_pagamento,
            @dt_emissao             = mt.dt_emissao,
            @dt_referencia_prevista = mt.dt_referencia_prevista,
            @dt_vencimento          = mt.dt_vencimento,
            @f_devolucao            = gimt.f_devolucao
    from ICM_MENSALIDADE_TRABALHO mt inner join ICM_GRUPO_ITEM_MENSALIDADE_TRABALHO gimt
        on mt.id_mensalidade_trabalho = gimt.id_mensalidade_trabalho
    where gimt.id_grupo_item_mensalidade_trabalho = @id_grupo_item_mensalidade_trabalho


    if (@f_devolucao is null)
    begin
        set @msg = 'Não existe grupo de itens de mensalidade com o id informado! Id: ' + CONVERT(varchar,@id_grupo_item_mensalidade_trabalho)
        RAISERROR(@msg, 16, 1)
        RETURN
    end

    if (@f_devolucao <> 0)
    begin
        set @msg = 'O grupo de itens de mensalidade informado não é de cobrança! Id: ' + CONVERT(varchar,@id_grupo_item_mensalidade_trabalho)
        RAISERROR(@msg, 16, 1)
        RETURN
    end



    if OBJECT_ID('TempDb..#dados') is not null
    begin
        drop table #dados
    end        

    select
            -- Esta regra de agrupamento é baseada na anterior, que foi movida para SPR_ICM_LISTA_GRUPOS_ITENS_MENSALIDADE. Em caso de mudança aqui, a regra de lá também deverá ser alterada
            -- Os campos dos partition by são os mesmos do order by em SPR_ICM_LISTA_GRUPOS_ITENS_MENSALIDADE
            seq_item_mensalidade                = RANK() over (order by cc.id_pessoa, cc.id_rubrica_cac), 
            cc.id_comando,
            d_forma_pagamento                   = @d_forma_pagamento,
            tp_receita                          = case when cc.d_forma_pagamento in ('C', 'I') then 'M'
                                                       when cc.d_forma_pagamento in ('F') then 'F'
                                                       else null
                                                  end,
            --cc.id_pessoa,
            id_titular                           = @id_titular,
            --cc.id_financiamento,
            cd_contrato                          = (select dpba.cd_contrato from MV_CAC_DE_PARA_BENEFICIARIO_ASSISTENCIAL dpba
                                                    where cc.id_pessoa = dpba.id_pessoa),
            cd_motivo_cancelamento               = null,
            cd_nosso_numero                      = case when @d_forma_pagamento in ('C', 'I') then cc.id_comando_interface end,
            ds_observacao                        = null,
            dt_cancelamento                      = convert(datetime, null),
            dt_emissao                           = @dt_emissao,
            dt_vencimento                        = @dt_vencimento,
            dt_vencimento_original               = @dt_vencimento,  
            dt_referencia_original               = cc.dt_referencia,      
            dt_referencia_prevista               = @dt_referencia_prevista,
            nr_ano                               = DATEPART(year, @dt_referencia_prevista), /* Boletos serão considerados como data de referência atual, independente da original */
            nr_mes                               = DATEPART(month, @dt_referencia_prevista),   /* Boletos serão considerados como data de referência atual, independente da original */
            nr_agencia                           = null,
            nr_banco                             = null,
            nr_conta                             = null,
            nr_documento                         = null,
            nr_parcela                           = 1,   /* No momento, fixo em 1. Necessária decisão sobre financiamentos da CAC para verificar o que deve ser preenchido. */
            tp_quitacao                          = 'A', /* No momento, fixo em 'A', devido à validação do banco da MV permitir apenas este valor para a CAC */
            vl_juros_mora                        = 0,
            vl_mensalidade                       = CONVERT(float, null),                      
            vl_pago                              = null,
            vl_percentual_multa                  = 0,
            cd_lcto_mensalidade                  = cc.id_rubrica_cac,
            rc.f_provento,
            cc.id_pessoa,
            cd_matricula                         = (select dpba.cod_pessoa from MV_CAC_DE_PARA_BENEFICIARIO_ASSISTENCIAL dpba
                                                    where cc.id_pessoa = dpba.id_pessoa),
            ds_observacao_item_mensalidade       = null,
            vl_lancamento                        = cc.valor,
            tipo_rubrica_cac                     = trc.d_tipo_rubrica
                                                   
    into #dados
    from COMANDO cc inner join RUBRICA_CAC rc
            on cc.id_rubrica_cac = rc.id_rubrica_cac
        inner join RE_TIPO_RUBRICA_CAC trc
            on rc.id_tipo_rubrica_cac = trc.id_tipo_rubrica_cac
        left join VW_DEPENDENTE_TITULAR dt
            on cc.id_pessoa = dt.id_dependente
    where 
    cc.id_comando in (select imt.id_comando
                      from ICM_ITEM_MENSALIDADE_TRABALHO imt
                      where imt.id_grupo_item_mensalidade_trabalho = @id_grupo_item_mensalidade_trabalho)
    and rc.f_provento = 0 -- Por construção, este grupo só deveria ter cobranças, mas por via das dúvidas será mantida esta condição
        

    /* Acumulando os valores das mensalidades (conjuntos de comandos) */
    ;with TOTAL as (
        select SUM(isnull(vl_lancamento, 0)) as vl_total
        from #dados
    )
    update dd
    set vl_mensalidade = tt.vl_total
    from #dados dd, TOTAL tt

    /* Apesar de ser necessário acumular também os valores por mensalidade/pessoa/rubrica, são necessários os comandos individuais,
       para poder ser gravada a associação. Assim, não é feito mais nenhum agrupamento e os dados são enviados.
    */


    select * from #dados
    order by seq_item_mensalidade

    if OBJECT_ID('TempDb..#dados') is not null
    begin
        drop table #dados
    end

END
GO

GRANT EXECUTE ON dbo.SPR_ICM_LISTA_MENSALIDADES_CONTRATO to LogCacMv
GO

GRANT EXECUTE ON dbo.f_inicio_do_mes to LogCacMv
GO
