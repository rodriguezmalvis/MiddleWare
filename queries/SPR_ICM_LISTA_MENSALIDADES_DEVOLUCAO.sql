IF  EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[SPR_ICM_LISTA_MENSALIDADES_DEVOLUCAO]') AND type in (N'P', N'PC'))
begin
    DROP PROCEDURE [dbo].[SPR_ICM_LISTA_MENSALIDADES_DEVOLUCAO]
end
GO

/* 
    dbo.SPR_ICM_LISTA_MENSALIDADES_DEVOLUCAO:
    
    Script que gera os dados efetivos relacionados às devoluções presentes em uma 
    MensalidadeTrabalho. 
    
    Autor: JCJ
    Data: 20/06/2017
*/

CREATE /*ALTER*/ PROCEDURE dbo.SPR_ICM_LISTA_MENSALIDADES_DEVOLUCAO(
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

    if (@f_devolucao <> 1)
    begin
        set @msg = 'O grupo de itens de mensalidade informado não é de devolução! Id: ' + CONVERT(varchar,@id_grupo_item_mensalidade_trabalho)
        RAISERROR(@msg, 16, 1)
        RETURN
    end


    select
            -- Esta regra de agrupamento é baseada na anterior, que foi movida para SPR_ICM_LISTA_GRUPOS_ITENS_MENSALIDADE. Em caso de mudança aqui, a regra de lá também deverá ser alterada
            case when cc.d_forma_pagamento = 'F' then RANK() over (partition by dt.id_titular, cc.id_pessoa, cc.d_forma_pagamento, cc.dt_referencia order by cc.id_pessoa, cc.id_rubrica_cac) 
                 else RANK() over (partition by dt.id_titular, cc.d_forma_pagamento, cc.id_pessoa order by cc.id_pessoa, cc.id_rubrica_cac) 
            end as seq_item_devolucao,
            cc.id_comando,
            cc.d_forma_pagamento,
            cd_matricula_titular                = ( select 
                                                    case when cc.id_plano <> 7 then
                                                        (select top 1 dpba.cod_pessoa
                                                         from MV_CAC_DE_PARA_BENEFICIARIO_ASSISTENCIAL dpba
                                                         where dpba.id_pessoa = dt.id_titular
                                                         )
                                                    else null
                                                    end ),
            cd_matricula_fornecedor             = CONVERT(int, null),

            tp_reembolso                        = 'D',
            cd_reembolso                        = null, -- !!! A ser preenchido via código
            cd_controle_interno                 = null,
            cd_matricula                        = ( select 
                                                    case when cc.id_plano <> 7 then
                                                        (select top 1 dpba.cod_pessoa
                                                         from MV_CAC_DE_PARA_BENEFICIARIO_ASSISTENCIAL dpba
                                                         where dpba.id_pessoa = cc.id_pessoa
                                                         )
                                                    else null
                                                    end ),
            cd_mens_contrato                    = null, -- !!! A ser preenchido via código
            vl_total_original                   = CONVERT(float, null),
            cd_fornecedor                       = null, -- !!! A ser preenchido via código
            dt_vencimento                       = @dt_vencimento,
            dt_inclusao                         = @dt_emissao,
            cd_usuario_inclusao                 = 'DBAMV',
            cd_tip_doc                          = null,
            cd_multi_empresa                    = 1,
            sn_notificado                       = null,
            sn_recusado                         = 'N',
            nr_ano                              = DATEPART(year, @dt_referencia_prevista), /* Boletos serão considerados como data de referência atual, independente da original */
            nr_mes                              = DATEPART(month, @dt_referencia_prevista),   /* Boletos serão considerados como data de referência atual, independente da original */
            cd_lcto_mensalidade                 = cc.id_rubrica_cac,
            cd_setor                            = null, -- !!! erro se não preenchido
            cd_item_res                         = null,
            qt_cobrado                          = 1,
            vl_cobrado                          = cc.valor                                    
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
    and rc.f_provento = 1 -- Por construção, este grupo só deveria ter devoluções, mas por via das dúvidas será mantida esta condição
        

    /* Acumulando os valores das mensalidades (conjuntos de comandos) */
    ;with TOTAL as(
        select SUM(isnull(vl_cobrado, 0)) as vl_total
        from #dados
    )
    update dd
    set vl_total_original = tt.vl_total
    from #dados dd, TOTAL tt

    /* Definindo o cd_matricula_fornecedor, campo que será base para a busca do cdFornecedor, e que 
       corresponde ao beneficiário que irá efetivamente receber o reembolso.
       No caso, para as formas de pagamento folha e carnê unificado, é o titular quem efetivamente recebe.
       Nos demais casos, é o próprio beneficiário quem recebe.
       */
    update dd
    set cd_matricula_fornecedor = case when d_forma_pagamento in ('F', 'C') then cd_matricula_titular
                                       else cd_matricula
                                       end
    from #dados dd

    /* Apesar de ser necessário acumular também os valores por mensalidade/pessoa/rubrica, são necessários os comandos individuais,
       para poder ser gravada a associação. Assim, não é feito mais nenhum agrupamento e os dados são enviados.
    */


    select * from #dados
    order by seq_item_devolucao

    if OBJECT_ID('TempDb..#dados') is not null
    begin
        drop table #dados
    end

END
GO

GRANT EXECUTE ON dbo.SPR_ICM_LISTA_MENSALIDADES_DEVOLUCAO to LogCacMv
GO

GRANT EXECUTE ON dbo.f_inicio_do_mes to LogCacMv
GO
