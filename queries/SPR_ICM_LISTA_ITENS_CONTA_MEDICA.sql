IF  EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[SPR_ICM_LISTA_ITENS_CONTA_MEDICA]') AND type in (N'P', N'PC'))
begin
    DROP PROCEDURE [dbo].[SPR_ICM_LISTA_ITENS_CONTA_MEDICA]
end
GO

CREATE /*ALTER*/ PROCEDURE dbo.[SPR_ICM_LISTA_ITENS_CONTA_MEDICA](
    @ano_apresentacao numeric(4,0),
    @id_representacao int,
    @id_processo int,
    @d_sub_processo char(1),
    @d_natureza char(1),
    @id_sequencial_natureza tinyint,
    @id_atendimento smallint,
    @cd_conta_medica int
) AS
begin
    SET NOCOUNT ON;

    /* Guias mistas */
    /*declare     @ano_apresentacao int = 2016,
                @id_representacao int = 1,
                @id_processo int = 107,
                @d_sub_processo char(1) = '1',
                @d_natureza char(1) = 'O',
                @id_sequencial_natureza int = 1,
                @id_atendimento int = 1,
                @cd_conta_medica int = 10001;*/
    
    /* Trat ser            
    declare     @ano_apresentacao int = 2016,
                @id_representacao int = 8,
                @id_processo int = 14,
                @d_sub_processo char(1) = '1',
                @d_natureza char(1) = 'O',
                @id_sequencial_natureza int = 1;*/
                   

    if OBJECT_ID('TempDb..#dados') is not null
    begin
        drop table #dados
    end

    declare @VALOR_APRESENTADO_PADRAO_PROCEDIMENTO float = 1.00
    declare @PROCEDIMENTO_PADRAO int = 50010000


    
    declare @dt_atendimento datetime
    declare @mes_ano_vigencia_sub_processo datetime
    
    select  @dt_atendimento = aa.dt_atendimento,
            @mes_ano_vigencia_sub_processo = sp.mes_ano_vigencia
    
    from ATENDIMENTO aa inner join SUB_PROCESSO sp
        on aa.ano_apresentacao = sp.ano_apresentacao
        and aa.id_representacao = sp.id_representacao
        and aa.id_processo = sp.id_processo
        and aa.d_sub_processo = sp.d_sub_processo
        and aa.d_natureza = sp.d_natureza
        and aa.id_sequencial_natureza = sp.id_sequencial_natureza
    where aa.ano_apresentacao = @ano_apresentacao
    and aa.id_representacao = @id_representacao
    and aa.id_processo = @id_processo
    and aa.d_sub_processo = @d_sub_processo
    and aa.d_natureza = @d_natureza
    and aa.id_sequencial_natureza = @id_sequencial_natureza
    and aa.id_atendimento = @id_atendimento
    
   
    /* Deve-se listar os PROCEDIMENTOS na forma de itens de contas médicas, como esperado pela MV. */
     
    
    select ROW_NUMBER() over (order by pp.id_procedimento) linha,
        pp.ano_apresentacao,
        pp.id_representacao,
        pp.id_processo,
        pp.d_sub_processo,
        pp.d_natureza,
        pp.id_sequencial_natureza,
        pp.id_atendimento,
        pp.id_procedimento,
        cdContaMedica               = @cd_conta_medica,
        cdItContaMedica             = CONVERT(int, 0),
        cdLctoMensalidade           = null,
        cdMensContrato              = null,
        cdMotivo                    = case when isnull(pp.valor_pago, 0) = 0 then
                                            ( select top 1 gp.id_glosa
                                            from GLOSA_PROCESSO gp
                                            where gp.ano_apresentacao = pp.ano_apresentacao
                                            and gp.id_representacao = pp.id_representacao
                                            and gp.id_processo = pp.id_processo
                                            and gp.d_sub_processo = pp.d_sub_processo
                                            and gp.d_natureza = pp.d_natureza
                                            and gp.id_sequencial_natureza = pp.id_sequencial_natureza
                                            and gp.id_atendimento = pp.id_atendimento
                                            and gp.id_procedimento = pp.id_procedimento
                                            and gp.id_glosa <> 2
                                            and gp.dthr_liberacao_glosa is null
                                            order by gp.dthr_aplicacao_glosa)
                                      else null
                                      end,
        cdProcDigita                = null,
        cdProcedimento              = coalesce(dpp.id_amb_destino, pp.id_amb, @PROCEDIMENTO_PADRAO),
        cdTabela                    = null,
        cdTecnica                   = null, -- Este campo não existe na guia antiga
        cdTermo                     = null, -- Este campo não existe na guia antiga
        cdViaAcesso                 = null, -- Este campo não existe na guia antiga
        dsObservacao                = null,
        dsObservacaoGlosa           = null,
        dtLancamento                = @dt_atendimento,
        nrAnoCob                    = DATEPART(YEAR, @mes_ano_vigencia_sub_processo),
        nrMesCob                    = DATEPART(MONTH, @mes_ano_vigencia_sub_processo),
        qtCobrado                   = case when isnull(pp.quantidade_amb, 0) <> 0
                                           then abs(pp.quantidade_amb)
                                      else 1
                                      end,
        qtFranquia                  = null,
        qtPaga                      = case when isnull(pp.valor_pago, 0) <> 0
                                           then isnull(abs(pp.quantidade_amb), 1)
                                      else 0
                                      end,
        snFaturar                   = null,
        snHorarioNormal             = null,
        snPago                      = null,
        tpSituacao                  = 'AA',
        vlCalculado                 = abs(pp.valor_calculado),
        vlFranquia                  = abs(pp.valor_participacao_bruto),
        vlFranquiaCalculado         = null,
        vlPago                      = isnull(pp.valor_pago, 0),
        vlPercentual                = 100,
        vlTotalCobrado              = case when pp.valor_apresentado IS NULL then
                                              case when pp.valor_pago is null then @VALOR_APRESENTADO_PADRAO_PROCEDIMENTO
                                                   else abs(pp.valor_pago)
                                              end
                                           else abs(pp.valor_apresentado)
                                      end,
        vlUnitarioCobrado           = convert(float, 0),
        vlUnitPago                  = convert(float, 0)               
    into #dados                                                                                                                                                             
    from PROCEDIMENTO pp left join ICM_DE_PARA_PROCEDIMENTO dpp
        on pp.id_amb = dpp.id_amb_origem
        and pp.id_tipo_tabela = dpp.id_tipo_tabela_origem
    where pp.ano_apresentacao = @ano_apresentacao
    and pp.id_representacao = @id_representacao
    and pp.id_processo = @id_processo
    and pp.d_sub_processo = @d_sub_processo
    and pp.d_natureza = @d_natureza
    and pp.id_sequencial_natureza = @id_sequencial_natureza
    and pp.id_atendimento = @id_atendimento
    and ( (ISNULL(pp.valor_apresentado, 0) > 0) or (ISNULL(pp.valor_pago, 0) > 0) )
    order by pp.id_procedimento
    
    
    /* TODO: Incluir as outras despesas, de alguma maneira (via códigos
             agrupadores + rateio ou diretamente os itens)*/
    
    /* Corrigindo valores que são obrigatórios mas podem ter ficado nulos devido aos dados da nossa base: */
    /* vlUnitarioCobrado e vlUnitPago */
    update #dados
    set vlUnitarioCobrado = abs(vlTotalCobrado / qtCobrado),
        vlUnitPago = abs(isnull(vlPago, 0.0) / qtCobrado)
        
    
    select * from #dados
    
    if OBJECT_ID('TempDb..#dados') is not null
    begin
        drop table #dados
    end
end    
GO

GRANT EXECUTE on dbo.SPR_ICM_LISTA_ITENS_CONTA_MEDICA to LogCacMv
GO