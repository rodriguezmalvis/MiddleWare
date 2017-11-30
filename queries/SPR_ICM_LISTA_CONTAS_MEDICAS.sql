IF  EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[SPR_ICM_LISTA_CONTAS_MEDICAS]') AND type in (N'P', N'PC'))
begin
    DROP PROCEDURE [dbo].[SPR_ICM_LISTA_CONTAS_MEDICAS]
end
GO

CREATE /*ALTER*/ PROCEDURE dbo.[SPR_ICM_LISTA_CONTAS_MEDICAS](
    @ano_apresentacao numeric(4,0),
    @id_representacao int,
    @id_processo int,
    @d_sub_processo char(1),
    @d_natureza char(1),
    @id_sequencial_natureza tinyint,
    @cd_lote int
) AS
begin
    SET NOCOUNT ON;

    /* TODO: verificar a possibilidade de associar GUIA corretamente à carteira de
             ATENDIMENTO (apenas quando houver garantia de que as informações
             estão corretas) */

    /* Guias mistas */
    /*declare     @ano_apresentacao int = 2016,
                @id_representacao int = 1,
                @id_processo int = 107,
                @d_sub_processo char(1) = '1',
                @d_natureza char(1) = 'O',
                @id_sequencial_natureza int = 1,
                @cd_lote int = 1;*/
    
    /* Trat ser            
    declare     @ano_apresentacao int = 2016,
                @id_representacao int = 8,
                @id_processo int = 14,
                @d_sub_processo char(1) = '1',
                @d_natureza char(1) = 'O',
                @id_sequencial_natureza int = 1;*/
                
                
    /* TODO: Ver se serão adicionadas validações, ou se será feito via código:
             - Subprocesso homologado
             - Prestadores e beneficiários existentes nos DE-Para */
                   

    if OBJECT_ID('TempDb..#dados') is not null
    begin
        drop table #dados
    end
    
    declare @valor_padrao_regime_internacao varchar(2) = '-1';
    declare @valor_padrao_atendimento_tiss varchar(2) = '05';
    declare @valor_padrao_tipo_consulta varchar(1) = '1'
    declare @valor_padrao_tipo_saida_guia_consulta int = 5;
    declare @valor_padrao_tipo_saida_guia_sadt int = 11;
    declare @VALOR_PADRAO_MOTIVO_ALTA_FATURAMENTO_PARCIAL int = 28;
    
    declare @id_prestador_sub_processo int;
    declare @dt_apresentacao_sub_processo datetime;
    declare @dt_homologacao_sub_processo datetime;
    declare @nome_prestador_sub_processo varchar(50);
    declare @inicio_mes_ano_vigencia_efetivo datetime;
    
    select @id_prestador_sub_processo  = sp.id_prestador,
           @dt_apresentacao_sub_processo = sp.dthr_apresentacao,
           @dt_homologacao_sub_processo = sp.dthr_homologacao,
           @nome_prestador_sub_processo = pp.nome,
           @inicio_mes_ano_vigencia_efetivo = case when convert(date, dbo.f_inicio_do_mes(sp.dthr_apresentacao)) = convert(date, dbo.f_inicio_do_mes(sp.mes_ano_vigencia)) then
                                                case when (sp.id_representacao in (41, 42) OR pr.f_anestesista = 1) then dbo.F_inicio_do_mes(sp.mes_ano_vigencia)
                                                     else DATEADD(month, -1, dbo.f_inicio_do_mes(sp.mes_ano_vigencia))
                                                end
                                              else 
                                                case when CONVERT(date, dbo.f_inicio_do_mes(sp.dthr_apresentacao)) < CONVERT(date, dbo.f_inicio_do_mes(sp.mes_ano_vigencia)) 
                                                     then dbo.F_inicio_do_mes(sp.dthr_apresentacao)
                                                     else dbo.F_inicio_do_mes(sp.mes_ano_vigencia)
                                                end
                                              end
    from SUB_PROCESSO sp inner join PRESTADOR pp
            on sp.id_prestador = pp.id_prestador
        inner join PROCESSO pr
            on sp.ano_apresentacao = pr.ano_apresentacao
            and sp.id_representacao = pr.id_representacao
            and sp.id_processo = pr.id_processo
    where sp.ano_apresentacao = @ano_apresentacao
    and sp.id_representacao = @id_representacao
    and sp.id_processo = @id_processo
    and sp.d_sub_processo = @d_sub_processo
    and sp.d_natureza = @d_natureza
    and sp.id_sequencial_natureza = @id_sequencial_natureza

    declare @fim_mes_ano_vigencia_efetivo datetime = convert(datetime, convert(date, dbo.f_fim_do_mes(@inicio_mes_ano_vigencia_efetivo)))
    
    /* Deve-se listar os ATENDIMENTOS na forma de contas médicas, como esperado pela MV. */
    
    /* O primeiro passo é separar os atendimentos do processo por tipo de guia da MV, guia e beneficiário */
    
    
    select ROW_NUMBER() over (order by dd.tipo_guia_mv, aa.id_guia, aa.numero_carteira, aa.num_guia_prestador) linha,
        RANK() over (order by dd.tipo_guia_mv, aa.id_guia, aa.numero_carteira, aa.num_guia_prestador) seq_conta_medica,
        aa.ano_apresentacao,
        aa.id_representacao,
        aa.id_processo,
        aa.d_sub_processo,
        aa.d_natureza,
        aa.id_sequencial_natureza,
        aa.id_atendimento,
        dt_atendimento_atendimento          = aa.dt_atendimento,
        f_cobranca_parcial_atendimento      = aa.f_cobranca_parcial,
        dthr_inicio_faturamento_atendimento = aa.dthr_inicio_faturamento,
        dthr_fim_faturamento_atendimento    = aa.dthr_fim_faturamento,
        cdCid                       =   case when aa.id_documento <> 1 then
	                                        (select replace(ltrim(rtrim(id_cid)), '.', '') as id_cid
	                                         from 
                                                (select distinct top 4 HC.id_cid ,RANK() OVER (order by HC.id_guia, HC.id_tipo_guia, HC.id_cid) as linha 
                                                 from  HISTORICO_CID HC inner join GUIA gg 
                                                    on hc.id_guia = gg.id_guia
                                                    and HC.id_tipo_guia = gg.id_tipo_guia
                                                 where gg.id_guia = aa.id_guia--HC.id_guia = AA.id_guia and HC.id_tipo_guia = AA.id_tipo_guia 
                                                 and ltrim(rtrim(HC.id_cid)) <> '0') aa 
	                                         where linha = 1)
                                        else
                                            null
                                        end,
        cdCid2                      =   case when aa.id_documento <> 1 then
	                                        (select replace(ltrim(rtrim(id_cid)), '.', '') as id_cid
	                                         from 
                                                (select distinct top 4 HC.id_cid ,RANK() OVER (order by HC.id_guia, HC.id_tipo_guia, HC.id_cid) as linha 
                                                 from  HISTORICO_CID HC inner join GUIA gg 
                                                    on hc.id_guia = gg.id_guia
                                                    and HC.id_tipo_guia = gg.id_tipo_guia
                                                 where gg.id_guia = aa.id_guia--HC.id_guia = AA.id_guia and HC.id_tipo_guia = AA.id_tipo_guia 
                                                 and ltrim(rtrim(HC.id_cid)) <> '0') aa 
	                                         where linha = 2)
                                        else
                                            null
                                        end,
        cdCid3                      =   case when aa.id_documento <> 1 then
	                                        (select replace(ltrim(rtrim(id_cid)), '.', '') as id_cid
	                                         from 
                                                (select distinct top 4 HC.id_cid ,RANK() OVER (order by HC.id_guia, HC.id_tipo_guia, HC.id_cid) as linha 
                                                 from  HISTORICO_CID HC inner join GUIA gg 
                                                    on hc.id_guia = gg.id_guia
                                                    and HC.id_tipo_guia = gg.id_tipo_guia
                                                 where gg.id_guia = aa.id_guia--HC.id_guia = AA.id_guia and HC.id_tipo_guia = AA.id_tipo_guia 
                                                 and ltrim(rtrim(HC.id_cid)) <> '0') aa 
	                                         where linha = 3)
                                        else
                                            null
                                        end,
        cdCid4                      =   case when aa.id_documento <> 1 then
	                                        (select replace(ltrim(rtrim(id_cid)), '.', '') as id_cid
	                                         from 
                                                (select distinct top 4 HC.id_cid ,RANK() OVER (order by HC.id_guia, HC.id_tipo_guia, HC.id_cid) as linha 
                                                 from  HISTORICO_CID HC inner join GUIA gg 
                                                    on hc.id_guia = gg.id_guia
                                                    and HC.id_tipo_guia = gg.id_tipo_guia
                                                 where gg.id_guia = aa.id_guia--HC.id_guia = AA.id_guia and HC.id_tipo_guia = AA.id_tipo_guia 
                                                 and ltrim(rtrim(HC.id_cid)) <> '0') aa 
	                                         where linha = 4)
                                        else
                                            null
                                        end,
        cdCidObito                  = null, -- Campo apenas para guia nova
        cdCnes                      = (select coalesce(ep.cnes, pp.cnes, '9999999')
                                        from PRESTADOR pp, (select top 1 ep.cnes
                                                            from ENDERECO_PRESTADOR ep
                                                            where ep.id_prestador = @id_prestador_sub_processo
                                                            and ep.id_endereco = aa.id_endereco
                                                            and ep.f_ativo = 1
                                                            order by ep.f_atendimento desc, ep.f_correspondencia desc,
                                                            ep.id_endereco_prestador) ep 
                                        where pp.id_prestador = @id_prestador_sub_processo ),
                                                            
                                        
        cdCnesLocalAtendimento      = null, -- Campo apenas para guia nova
        cdContaMedica               = convert(int, 0),--@cd_lote * POWER(10, 4) + aa.id_atendimento,
        cdContaMedicaTem            = null,
        cdEspecialidade             = ( select top 1 dpe.CD_ESPECIALIDADE
                                        from MV_CAC_DE_PARA_ESPECIALIDADES dpe inner join ESPECIALIDADE_PRESTADOR ep
                                            on dpe.ID_ESPECIALIDADE = ep.id_especialidade
                                        where ep.id_prestador = @id_prestador_sub_processo
                                        and ep.dt_inicio_espc_prestador <= aa.dt_atendimento
                                        and (ep.dt_cancelamento_espc_prestador is null or 
                                             ep.dt_cancelamento_espc_prestador > aa.dt_atendimento) 
                                        order by ep.dt_inicio_espc_prestador     ),
        cdIndicadorAcidente         = '9',
        cdLote                      = @cd_lote,
        cdMatricula                 = ( select 
                                        case when spb.id_plano <> 7 then
                                            (select dpba.cod_pessoa
                                             from MV_CAC_DE_PARA_BENEFICIARIO_ASSISTENCIAL dpba
                                             where dpba.id_pessoa = spb.id_pessoa
                                             )
                                        else (select dpbo.cod_pessoa
                                              from MV_CAC_DE_PARA_BENEFICIARIO_OCUPACIONAL dpbo
                                              where dpbo.id_pessoa = spb.id_pessoa)
                                        end ),
        cdMotivo                    = ( select top 1 gp.id_glosa
                                        from GLOSA_PROCESSO gp
                                        where gp.ano_apresentacao = aa.ano_apresentacao
                                        and gp.id_representacao = aa.id_representacao
                                        and gp.id_processo = aa.id_processo
                                        and gp.d_sub_processo = aa.d_sub_processo
                                        and gp.d_natureza = aa.d_natureza
                                        and gp.id_sequencial_natureza = aa.id_sequencial_natureza
                                        and gp.id_atendimento = aa.id_atendimento
                                        and gp.id_procedimento is null
                                        and gp.dthr_liberacao_glosa is null
                                        order by gp.dthr_aplicacao_glosa),
        cdMotivoAlta                = ( case when dd.tipo_guia_mv = 'I' then
                                            case when (select count(1) from GUIA gg where gg.id_guia = aa.id_guia) > 1 then
                                                ( select gg.id_motivo_encerramento
                                                    from GUIA gg
                                                    where gg.id_guia = aa.id_guia
                                                    and gg.id_pessoa = spb.id_pessoa
                                                    and gg.id_plano = spb.id_plano
                                                    and gg.id_sub_plano = spb.id_sub_plano
                                                    and gg.dt_inscricao = spb.dt_inscricao
                                                )
                                            else
                                                ( select gg.id_motivo_encerramento
                                                    from GUIA gg
                                                    where gg.id_guia = aa.id_guia
                                                )
                                            end
                                        else null
                                        end),
        cdMotivoObitoMulher         = null, -- Este campo não existe para a ANS
        cdPlano                     = case spb.id_plano
                                        when 1 then '2'
                                        when 2 then '1'
                                        when 7 then '3'
                                      else null
                                      end,
        cdPrestador                 = (select dpp.cod_prestador
                                       from MV_CAC_DE_PARA_PRESTADOR dpp
                                       where dpp.id_prestador = @id_prestador_sub_processo),                                    
        cdPrestadorEndereco         = null,
        cdPrestadorLocalAtendimento = null, -- Campo só existe nas guias novas
        cdPrestadorSolicitante      = null, -- Campo mais provável para as guias novas; pode existir em guias antigas, mas não será enviado por não ser obrigatório
        cdRegimeInternacao          = CONVERT(varchar(2), null),
        cdTipAcomodacao             = CONVERT(int, null),
        cdTipoAtendimentoTiss       = CONVERT(varchar(2), null),
        /*  Quando a nova versão do SPRO entrar no ar, o select acima terá que ser atualizado, para substituir 
            GUIA_ANS.d_tipo_atendimento por GUIA_ANS.id_tipo_atendimento e pegar o valor de 
            TIPO_ATENDIMENTO.cod_ans */ 
        cdTipoConsulta              = case when dd.tipo_guia_mv = 'C' then
                                        isnull(
                                                (select top 1 gc.d_tipo_consulta
                                                 from GUIA_CONSULTA gc
                                                 where gc.id_guia_consulta = aa.id_guia
                                                 and gc.d_tipo_consulta <> '0'
                                                 order by gc.id_guia_consulta desc),
                                                @valor_padrao_tipo_consulta
                                              )  
                                      else null
                                      end,
        cdTipoFaturamento           = CONVERT(char(1), null),
        cdTipoSaidaGuiaConsulta     = case when dd.tipo_guia_mv = 'C' then
                                        ISNULL(
                                                (select top 1 gc.id_tipo_saida
                                                 from GUIA_CONSULTA gc
                                                 where gc.id_guia_consulta = aa.id_guia
                                                 order by gc.id_guia_consulta desc),
                                                @valor_padrao_tipo_saida_guia_consulta 
                                              )
                                      else null
                                      end,
        cdTipoSaidaGuiaSadt         = CONVERT(int, null),
        dsObservacao                = null,
        dtAlta                      = convert(datetime, null),
                                      
        dtApresentacao              = @dt_apresentacao_sub_processo,
        dtAtendimento               = MIN(aa.dt_atendimento) over(partition by dd.tipo_guia_mv, aa.id_guia, aa.numero_carteira),
        dtAuditoria                 = @dt_homologacao_sub_processo,                     
        dtInternacao                = CONVERT(datetime, null),
        nmPrestador                 = @nome_prestador_sub_processo,
        nrCarteiraBeneficiario      = aa.numero_carteira,
        nrDeclaracaoNascidoVivo     = null, -- Campo não existe na guia antiga
        nrDeclaracaoObito           = null, -- Campo não existe na guia antiga
        nrGuia                      = aa.id_guia,
        nrGuiaPrestadorPrincipal    = aa.num_guia_prestador,
        qtNascidoMorto              = null, -- Estes campos não existem diretamente na ANS
        qtNascidoVivoPrematuro      = null,
        qtNascidoVivoTermo          = null,
        qtObitoNeonatalPrecoce      = null,
        qtObitoNeonatalTardio       = null,
        snAborto                    = null,
        snAtendimentoRnSalaParto    = null,
        snBaixoPeso                 = null,
        snComplicacaoNeonatal       = null,
        snComplicacaoPuerperio      = null,
        snGestacao                  = null, -- Estes campos não existem diretamente na ANS
        snIndicadorAtendimentoRn    = null, -- Campo não existe na guia antiga
        snObitoCausaMaterna         = null, -- Estes campos não existem diretamente na ANS
        snPartoCesario              = null,
        snPartoNormal               = null, -- Estes campos não existem diretamente na ANS
        snReapresentacao            = case when aa.d_natureza in ('C', 'R') then 'S'
                                           else 'N'
                                      end,
        snTranstornoMaternoGravidez = null, -- Este campo não existe diretamente na ANS
        tpCaraterInternacao         = CONVERT(char(1), null),                      
        tpCaraterSolicitacao        = null,
        tpCausaInternacao           = null,
        tpContaHospitalar           = CONVERT(char(1), null),
        tpGuia                      = CONVERT(char(1), null),
        tpSituacao                  = 'AA',
        vlFranquia                  = null,
        id_guia_atendimento         = aa.id_guia,
        id_tipo_guia_atendimento    = (select top 1 gg.id_tipo_guia
                                       from GUIA gg where gg.id_guia = aa.id_guia
                                       order by gg.id_guia, gg.id_tipo_guia),
        id_guia_associada           = (select top 1 gg.id_guia_associada 
                                       from GUIA gg where gg.id_guia = aa.id_guia
                                       and gg.id_tipo_guia in (1,4,5,9,10,11)
                                       order by gg.id_guia, gg.id_tipo_guia),
        id_tipo_guia_associada      = (select top 1 gg.id_tipo_guia_associada
                                       from GUIA gg where gg.id_guia = aa.id_guia
                                       and gg.id_tipo_guia in (1,4,5,9,10,11)
                                       order by gg.id_guia, gg.id_tipo_guia),
        dd.tipo_guia_mv,
        forca_guia_servico          = case  when exists (select 1 from GUIA gg
                                                        where gg.id_guia = aa.id_guia
                                                        and gg.id_tipo_guia in (1, 4, 5, 9, 10, 11, 26)
                                                        and gg.id_guia_associada is null)
                                            then 1
											when  @id_representacao =41 
                                            then 1  
                                            else 0
                                      end,
        motivo_ajuste                 = CONVERT(char(2), null),
        campo_ajuste                  = convert(varchar(40), null),
        valor_antigo                  = CONVERT(varchar(40), null),
        valor_novo                    = CONVERT(varchar(40), null)
                                                
    into #dados                                                                                                                                                             
    from ATENDIMENTO aa inner join DOCUMENTO dd
            on aa.id_documento = dd.id_documento 
        left join SUB_PLANO_BENEFICIARIO spb
            on aa.numero_carteira = spb.numero_carteira
    where aa.ano_apresentacao = @ano_apresentacao
    and aa.id_representacao = @id_representacao
    and aa.id_processo = @id_processo
    and aa.d_sub_processo = @d_sub_processo
    and aa.d_natureza = @d_natureza
    and aa.id_sequencial_natureza = @id_sequencial_natureza
    order by dd.tipo_guia_mv, aa.id_guia, aa.dt_atendimento

    /***********************************
        CORRIGIR OS CAMPOS QUE CONSIDERAM dd.tipo_guia_mv PARA FAZER ISSO NA SEGUNDA PASSADA (PARTICULARMENTE OS QUE ENVOLVEM OS TIPOS
        DE GUIA 'I' E 'S'    
    */

    /* Classificação do tipo: */
    update dd
    set tpGuia = case forca_guia_servico when 1 then 'S'
                      else dd.tipo_guia_mv
                 end
    from #dados dd

    /* Correção para Internação domiciliar sem data de alta (considerar como faturamento parcial) */ 
    update dd
    set f_cobranca_parcial_atendimento = 1,
        motivo_ajuste = 'ID',
        campo_ajuste = 'f_cobranca_parcial_atendimento',
        valor_antigo = CONVERT(varchar, dd.f_cobranca_parcial_atendimento),
        valor_novo = CONVERT(varchar, 1)
    from #dados dd
    where dd.id_tipo_guia_atendimento = 14
    and      (select top 1 gg.dt_internacao
                    from GUIA gg
                    where gg.id_guia = isnull(dd.id_tipo_guia_associada, dd.id_guia_atendimento)
                    order by gg.id_guia) is null


    /* Com o tipo definido, são atualizados os campos que dependem dessa classificação (principalmente os que envolvem 'S' e 'I'. */
            
    
    /* cdRegimeInternacao */
    update dd
    set cdRegimeInternacao = isnull(
                                    ( select top 1 isnull(convert(varchar(2), ga.d_regime_internacao), @valor_padrao_regime_internacao)
                                    from GUIA_ANS ga inner join GUIA gg
                                            on ga.id_solicitacao = gg.id_solicitacao
                                    where gg.id_guia = ISNULL(dd.id_guia_associada, dd.id_guia_atendimento)
                                    order by ga.id_solicitacao) ,
                                    @valor_padrao_regime_internacao
                                )          
    from #dados dd
    where tpGuia = 'I'

    /* cdTipAcomodacao */
    update dd
    set cdTipAcomodacao = ( select top 1 gha.id_acomodacao
                            from GUIA gg left join GUIA_HISTORICO_ACOMODACAO gha
                            on gg.id_guia = gha.id_guia
                            and gg.id_tipo_guia = gha.id_tipo_guia
                            where gg.id_guia = ISNULL(dd.id_guia_associada, dd.id_guia_atendimento)
                            and CONVERT(date, gha.dt_inicio_acomodacao) <= CONVERT(date, dd.dt_atendimento_atendimento)
                            order by gha.dt_inicio_acomodacao desc, gha.id_prorrogacao desc
                            )
    from #dados dd
    where tpGuia = 'I'

    /* cdTipoFaturamento */
    update dd
    set cdTipoFaturamento = case dd.f_cobranca_parcial_atendimento
                                when 1 then '1'
                                when 0 then '4'
                            end
    from #dados dd
    where dd.tipo_guia_mv = 'I'
    
    /* cdTipoAtendimentoTiss */
    update dd
    set cdTipoAtendimentoTiss = isnull(
                                        ( select top 1 ta.cod_ans
                                            from GUIA gg left join GUIA_ANS ga
                                                on gg.id_solicitacao = ga.id_solicitacao
                                            LEFT join TIPO_ATENDIMENTO ta
                                                on CONVERT(int, ga.d_tipo_atendimento) = ta.id_tipo_atendimento
                                            where gg.id_guia = dd.id_guia_atendimento
                                            order by ga.id_solicitacao) ,
                                        @valor_padrao_atendimento_tiss
                                        )
    from #dados dd
    where tpGuia = 'S'

    /* cdTipoSaidaGuiaSadt */
    update dd
    set cdTipoSaidaGuiaSadt = ISNULL(
                                (select top 1 gg.id_motivo_encerramento
                                    from GUIA gg
                                    where gg.id_guia = dd.id_guia_atendimento),
                                @valor_padrao_tipo_saida_guia_sadt) 
    from #dados dd
    where tpGuia = 'S'

    /* dthr_inicio_faturamento_atendimento e dthr_fim_faturamento_atedimento */
    update dd
    set dthr_inicio_faturamento_atendimento = case when @inicio_mes_ano_vigencia_efetivo < gg.dt_internacao then convert(date, gg.dt_internacao)
                                                   when @inicio_mes_ano_vigencia_efetivo >= gg.dt_internacao then convert(date, @inicio_mes_ano_vigencia_efetivo)
                                                   else convert(date,  @inicio_mes_ano_vigencia_efetivo)
                                              end
                                                                                                    
    from #dados dd inner join GUIA gg
        on dd.nrGuia = gg.id_guia
    where dd.f_cobranca_parcial_atendimento = 1
    and dd.dthr_inicio_faturamento_atendimento is null

    update dd
    set dthr_fim_faturamento_atendimento    = case when @fim_mes_ano_vigencia_efetivo <= gg.dt_alta then @fim_mes_ano_vigencia_efetivo
                                                   when @fim_mes_ano_vigencia_efetivo > gg.dt_alta  then convert(date, gg.dt_alta)
                                                   else @fim_mes_ano_vigencia_efetivo
                                              end        
    from #dados dd inner join GUIA gg
        on dd.nrGuia = gg.id_guia
    where dd.f_cobranca_parcial_atendimento = 1
    and dd.dthr_fim_faturamento_atendimento is null

    /* dtAlta */
    update dd
    set dtAlta = case dd.f_cobranca_parcial_atendimento
                      when 1 then dd.dthr_fim_faturamento_atendimento
                      when 0 then (select top 1 gg.dt_alta
                                    from GUIA gg
                                    where gg.id_guia = ISNULL(dd.id_guia_associada, dd.id_guia_atendimento)
                                    order by gg.id_guia) 
                  end 
    from #dados dd
    where dd.tipo_guia_mv = 'I'

    /* dtAtendimento */
    update dd
    set dtAtendimento = null
    from #dados dd
    where dd.tipo_guia_mv <> 'I'


    /* dtInternacao */
    update dd
    set dtInternacao = case dd.f_cobranca_parcial_atendimento
                            when 1 then dd.dthr_inicio_faturamento_atendimento
                            when 0 then (select top 1 gg.dt_internacao
                                            from GUIA gg
                                            where gg.id_guia = isnull(dd.id_tipo_guia_associada, dd.id_guia_atendimento)
                                            order by gg.id_guia) 
                       end 
    from #dados dd
    where dd.tipo_guia_mv = 'I'

    /* tpCaraterInternacao */
    update dd
    set tpCaraterInternacao = (select top 1 case ga.d_carater_internacao
                                                when 'U' then 'U'
                                                else 'E'
                                            end
                                from GUIA gg LEFT join GUIA_ANS ga
                                on gg.id_solicitacao = ga.id_solicitacao
                                where gg.id_guia = ISNULL(dd.id_guia_associada, dd.id_guia_atendimento)
                                order by gg.id_solicitacao) 
    from #dados dd
    where dd.tipo_guia_mv = 'I'

    /* tpContaHospitalar */
    update dd
    set tpContaHospitalar = (   select top 1 
                                case 
                                    when ss.id_tipo_internacao = 2 then 'P'
                                    when ss.id_tipo_internacao = 3 then 'O'
                                    when ss.id_tipo_internacao = 4 then 'S'
                                    when ss.id_tipo_internacao = 5 then
                                    case ss.d_classificacao_internacao
                                        when 'L' then 'N'
                                        when 'R' then 'C'
                                    else 'I' 
                                    end
                                else 'I' 
                                end 
                                from GUIA gg inner join SOLICITACAO ss
                                    on gg.id_solicitacao = ss.id_solicitacao
                                where gg.id_guia = ISNULL(dd.id_guia_associada, dd.id_guia_atendimento)
                                and ss.id_tipo_guia in (2, 12, 13, 14)
                                order by ss.id_solicitacao)
    from #dados dd
    where dd.tipo_guia_mv = 'I'
    


    /* Corrigindo valores que são obrigatórios mas podem ter ficado nulos devido aos dados da nossa base: */

    /* cdTipoConsulta */
    update #dados
    set cdTipoConsulta = @valor_padrao_tipo_consulta
    where tpGuia = 'C'
    and cdTipoConsulta is null
    
    /* cdTipoSaidaGuiaConsulta */
    update #dados
    set cdTipoSaidaGuiaConsulta = @valor_padrao_tipo_saida_guia_consulta
    where tpGuia = 'C'
    and cdTipoSaidaGuiaConsulta is null    
    
    /* cdEspecialidade: usar o código 105 da MV (CBO desconhecido ou não informado) */
    update #dados
    set cdEspecialidade = 105
    where cdEspecialidade is null

    /* cdMotivoAlta: obrigatório para todas as internações, porém por falta de campos não é gravado nos casos de faturamento parcial */
    update #dados
    set cdMotivoAlta = @VALOR_PADRAO_MOTIVO_ALTA_FATURAMENTO_PARCIAL
    where cdMotivoAlta is null
    and tpGuia = 'I'
    and f_cobranca_parcial_atendimento = 1
    
    /* cdContaMedica */
    update #dados
    set cdContaMedica = @cd_lote * POWER(10, 4) + seq_conta_medica
    

    
    select * from #dados
    order by linha
    
    if OBJECT_ID('TempDb..#dados') is not null
    begin
        drop table #dados
    end
end    
GO

GRANT EXECUTE on dbo.SPR_ICM_LISTA_CONTAS_MEDICAS to LogCacMv
GO

GRANT EXECUTE on dbo.f_inicio_do_mes to LogCacMv
GO

GRANT EXECUTE on dbo.f_fim_do_mes to LogCacMv
GO