IF  EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[SPR_ICM_LISTA_ITENS_REEMBOLSO]') AND type in (N'P', N'PC'))
begin
    DROP PROCEDURE [dbo].[SPR_ICM_LISTA_ITENS_REEMBOLSO]
end
GO

CREATE /*ALTER*/ PROCEDURE dbo.SPR_ICM_LISTA_ITENS_REEMBOLSO(
    @ano_apresentacao int,
    @id_representacao int,
    @id_processo int,
    @d_sub_processo char(1),
    @d_natureza char(1),
    @id_sequencial_natureza tinyint,
    @id_atendimento smallint,
    @cd_reembolso int
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
                
    declare @atendimento varchar(50) = CONVERT(varchar, @ano_apresentacao) + '-' +
                                       CONVERT(varchar, @id_representacao) + '-' +
                                       CONVERT(varchar, @id_processo) + '-' +
                                       @d_sub_processo + '-' +
                                       @d_natureza + '-' +
                                       convert(varchar, @id_sequencial_natureza) + '-' +
                                       convert(varchar, @id_atendimento)                                    

    if OBJECT_ID('TempDb..#dados') is not null
    begin
        drop table #dados
    end 
    
    select 
        pp.ano_apresentacao,
        pp.id_representacao,
        pp.id_processo,
        pp.d_sub_processo,
        pp.d_natureza,
        pp.id_sequencial_natureza,
        pp.id_atendimento,
        RANK() over (partition by pp.ano_apresentacao, pp.id_representacao, pp.id_processo, pp.d_sub_processo, pp.d_natureza,
                                  pp.id_sequencial_natureza, pp.id_atendimento order by pp.id_amb, pp.id_tipo_tabela)
                as id_procedimento,
        cdReembolso                             = @cd_reembolso,
        cdProcedimento                          = isnull(dpp.id_amb_destino, pp.id_amb),
        dsAdicional                             = @atendimento,
        qtCobrado                               = sum(pp.quantidade_amb),
        vlCobrado                               = sum(pp.valor_apresentado),
        vlProcedimento                          = sum(isnull(pp.valor_pago, 0)),
        vlCoparticipacao                        = sum(pp.valor_participacao_bruto)
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
    group by pp.ano_apresentacao, pp.id_representacao, pp.id_processo, pp.d_sub_processo, pp.d_natureza,
        pp.id_sequencial_natureza, pp.id_atendimento, pp.id_amb, pp.id_tipo_tabela, dpp.id_amb_destino
    
    select * from #dados
    
    if OBJECT_ID('TempDb..#dados') is not null
    begin
        drop table #dados
    end 

END
GO

GRANT EXECUTE on dbo.SPR_ICM_LISTA_ITENS_REEMBOLSO to LogCacMv
GO