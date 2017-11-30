IF  EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[SPR_ICM_LISTA_REEMBOLSOS]') AND type in (N'P', N'PC'))
begin
    DROP PROCEDURE [dbo].[SPR_ICM_LISTA_REEMBOLSOS]
end
GO

CREATE /*ALTER*/ PROCEDURE dbo.SPR_ICM_LISTA_REEMBOLSOS(
    @ano_apresentacao int,
    @id_representacao int,
    @id_processo int,
    @d_sub_processo char(1),
    @d_natureza char(1),
    @id_sequencial_natureza tinyint,
    @id_atendimento smallint
)
as
BEGIN
    SET NOCOUNT ON;

    /*declare     @ano_apresentacao int = 2017,
                @id_representacao int = 0,
                @id_processo int = 2,
                @d_sub_processo char(1) = '1',
                @d_natureza char(1) = 'O',
                @id_sequencial_natureza tinyint = 1; */
                
    declare @sub_processo varchar(40) = CONVERT(varchar, @ano_apresentacao) + '-' +
                                       CONVERT(varchar, @id_representacao) + '-' +
                                       CONVERT(varchar, @id_processo) + '-' +
                                       @d_sub_processo + '-' +
                                       @d_natureza + '-' +
                                       convert(varchar, @id_sequencial_natureza)
    declare @msg varchar(800)
    
    declare @cdMatriculaTitular int;
    
    select @cdMatriculaTitular =    case when spb.id_plano <> 7 then
                                        (select dpba.cod_pessoa
                                         from MV_CAC_DE_PARA_BENEFICIARIO_ASSISTENCIAL dpba
                                         where dpba.id_pessoa = spb.id_pessoa
                                         )
                                    else (select dpbo.cod_pessoa
                                          from MV_CAC_DE_PARA_BENEFICIARIO_OCUPACIONAL dpbo
                                          where dpbo.id_pessoa = spb.id_pessoa)
                                    end 
    from PROCESSO pp left join SUB_PLANO_BENEFICIARIO spb
            on pp.numero_carteira = spb.numero_carteira
    where pp.ano_apresentacao = @ano_apresentacao
    and pp.id_representacao = @id_representacao
    and pp.id_processo = @id_processo
    -- TODO: Ver se tem reembolso ocupacional            
                   
        

    
    if OBJECT_ID('TempDb..#dados') is not null
    begin
        drop table #dados
    end 
    
    ;WITH PRESTADORES as (
        select  pp.id_prestador,
                pp.nome,
                pp.d_tipo_pessoa,
                ISNULL(pc.registro_conselho, pnc.registro_conselho) as registro_conselho,
                ISNULL(pc.sigla_conselho, pnc.sigla_conselho) as sigla_conselho,
                case when (pp.d_tipo_pessoa = 'F') then convert(varchar(14),REPLICATE('0', 11 - LEN(pp.cpf_cgc)) + cpf_cgc) 
                     when (pp.d_tipo_pessoa = 'J') then convert(varchar(14),REPLICATE('0', 14 - LEN(pp.cpf_cgc)) + cpf_cgc)
                else null
                end as cpf_cgc_formatado
        from PRESTADOR pp left join PRESTADOR_CADASTRADO pc on pp.id_prestador = pc.id_prestador
                          left join PRESTADOR_NAO_CADASTRADO pnc on pp.id_prestador = pnc.id_prestador    
    )
    select
        aa.ano_apresentacao,
        aa.id_representacao,
        aa.id_processo,
        aa.d_sub_processo,
        aa.d_natureza,
        aa.id_sequencial_natureza,
        aa.id_atendimento,  
        cdAgencia                               = null,
        cdBanco                                 = null,
        cdConPag                                = null,
        cdControleInterno                       = null,
        cdEspecialidade                         = null,
        cdExpContabilidade                      = null,
        cdFornecedor                            = convert(varchar, null),
        cdMatriculaTitular                      = @cdMatriculaTitular,
        cdMatricula                             = ( select 
                                                    case when spb.id_plano <> 7 then
                                                        (select dpba.cod_pessoa
                                                         from MV_CAC_DE_PARA_BENEFICIARIO_ASSISTENCIAL dpba
                                                         where dpba.id_pessoa = spb.id_pessoa
                                                         )
                                                    else (select dpbo.cod_pessoa
                                                          from MV_CAC_DE_PARA_BENEFICIARIO_OCUPACIONAL dpbo
                                                          where dpbo.id_pessoa = spb.id_pessoa)
                                                    end ),
        cdMultiEmpresa                          = 1,
        cdMunicipio                             = vpe.id_cidade,                                                  
        cdPrestador                             = (select top 1 dpp.cod_prestador
                                                   from MV_CAC_DE_PARA_PRESTADOR dpp
                                                   where dpp.id_prestador = ISNULL(aa.id_prestador_reembolso, 2)),
        cdReembolso                             = ROW_NUMBER() over (order by aa.ano_apresentacao, aa.id_representacao, aa.id_processo, 
                                                                     aa.d_sub_processo, aa.d_natureza, aa.id_sequencial_natureza, 
                                                                     aa.id_atendimento),
        cdReembolsoPai                          = null,
        --JsonIgnore. Servirá para o campo cdTipoAtendimento
        tpGuia                                  = dd.tipo_guia_mv,
        cdTipoEndereco                          = vpe.tipo_endereco,
        cdUf                                    = vpe.id_sigla_estado,
        cdUsuarioInclusao                       = 'DBAMV',
        dsBairro                                = vpe.bairro,
        dsCodigoConselho                        = vpp.registro_conselho,
        dsComplemento                           = null,
        dsConselho                              = case when vpp.sigla_conselho <> '' then vpp.sigla_conselho end,
        dsConsideracoesInternas                 = CONVERT(varchar, null),
        dsEmail                                 = CONVERT(varchar, null),
        dsEndereco                              = vpe.logradouro,
        dsMunicipio                             = CONVERT(varchar, null),
        dsObservacao                            = @sub_processo + '-' + CONVERT(varchar, aa.id_atendimento),
        dsPrestador                             = vpp.nome,
        dsReciboNotaFiscal                      = convert(varchar, null),
        dtAtendimento                           = aa.dt_atendimento,
        dtFechamento                            = sp.dthr_homologacao,
        dtInclusao                              = sp.dthr_apresentacao,
        dtVencimento                            = sp.dt_prevista_pagamento,
        dvAgencia                               = convert(varchar, null),
        dvContaCorrente                         = convert(varchar, null),
        nmBanco                                 = convert(varchar, null),
        nrCep                                   = vpe.cep,
        nrConta                                 = convert(varchar, null),
        nrCpfCnpj                               = vpp.cpf_cgc_formatado,
        nrEndereco                              = vpe.porta,
        nrProtocoloAns                          = sp.id_protocolo,
        nrTelefone                              = null,
        snRecusado                              = 'N',
        tpLogradouro                            = null,
        tpPessoa                                = vpp.d_tipo_pessoa,
        tpReembolso                             = 'A',
        tpStatus                                = null,
        tpStatusReembolso                       = 3,
        vlTotalOriginal                         = CONVERT(float, 0.0)
    into #dados
    from ATENDIMENTO aa inner join SUB_PROCESSO sp
                on aa.ano_apresentacao = sp.ano_apresentacao
                and aa.id_representacao = sp.id_representacao
                and aa.id_processo = sp.id_processo
                and aa.d_sub_processo = sp.d_sub_processo
                and aa.d_natureza = sp.d_natureza
                and aa.id_sequencial_natureza = sp.id_sequencial_natureza
            inner join DOCUMENTO dd
                on aa.id_documento = dd.id_documento
            left join SUB_PLANO_BENEFICIARIO spb
                on aa.numero_carteira = spb.numero_carteira
            left join VW_PRESTADORES_ENDERECOS vpe
                on ISNULL(aa.id_prestador_reembolso, 2) = vpe.id_prestador                
                and aa.id_endereco = vpe.id_endereco           
            left join PRESTADORES vpp
                on isnull(aa.id_prestador_reembolso, 2) = vpp.id_prestador         
    where aa.ano_apresentacao = @ano_apresentacao
    and aa.id_representacao = @id_representacao
    and aa.id_processo = @id_processo
    and aa.d_sub_processo = @d_sub_processo
    and aa.d_natureza = @d_natureza
    and aa.id_sequencial_natureza = @id_sequencial_natureza
    and aa.id_atendimento = @id_atendimento
    -- TODO: incluir condição para que realmente só liste reembolsos. No momento, lista qq processo
    
    select * from #dados                                   
                
    if OBJECT_ID('TempDb..#dados') is not null
    begin
        drop table #dados
    end   

END
GO

GRANT EXECUTE on SPR_ICM_LISTA_REEMBOLSOS to LogCacMv
GO