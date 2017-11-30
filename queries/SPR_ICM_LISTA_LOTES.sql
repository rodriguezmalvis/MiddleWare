IF  EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[SPR_ICM_LISTA_LOTE]') AND type in (N'P', N'PC'))
begin
    DROP PROCEDURE [dbo].[SPR_ICM_LISTA_LOTE]
end
GO

CREATE /*ALTER*/ PROCEDURE dbo.SPR_ICM_LISTA_LOTE(
    @ano_apresentacao int,
    @id_representacao int,
    @id_processo int,
    @d_sub_processo char(1),
    @d_natureza char(1),
    @id_sequencial_natureza tinyint
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
    declare @msg varchar(200)
                                               
    /* Validações */

    /* 1- O subprocesso deve existir */
    if not exists ( select 1 from SUB_PROCESSO sp
                    where sp.ano_apresentacao = @ano_apresentacao
                    and sp.id_representacao = @id_representacao
                    and sp.id_processo = @id_processo
                    and sp.d_sub_processo = @d_sub_processo
                    and sp.d_natureza = @d_natureza
                    and sp.id_sequencial_natureza = @id_sequencial_natureza )
    begin
        set @msg = 'O subprocesso informado não existe! Subprocesso: ' + @sub_processo
        RAISERROR(@msg, 16, 1)
        RETURN                               
    end

    /* 2- Não pode ser processo de reembolso */
    if exists (select 1 from PROCESSO pp
               where pp.ano_apresentacao = @ano_apresentacao
               and pp.id_representacao = @id_representacao
               and pp.id_processo = @id_processo
               and pp.numero_carteira is not null)
    begin
        set @msg = 'O processo informado é de reembolso! Subprocesso: ' + @sub_processo
        RAISERROR(@msg, 16, 1)
        RETURN
    end 

    /* 3- O prestador do subprocesso deve existir no de-para de prestadores para a MV*/              
    if not exists (select 1 from SUB_PROCESSO sp inner join MV_CAC_DE_PARA_PRESTADOR dpp
                        on sp.id_prestador = dpp.id_prestador
                    where sp.ano_apresentacao = @ano_apresentacao
                    and sp.id_representacao = @id_representacao
                    and sp.id_processo = @id_processo
                    and sp.d_sub_processo = @d_sub_processo
                    and sp.d_natureza = @d_natureza
                    and sp.id_sequencial_natureza = @id_sequencial_natureza )
    begin
        set @msg = 'O prestador do subprocesso não existe no de-para de prestadores para a MV! Subprocesso: ' +
                    @sub_processo
        RAISERROR(@msg, 16, 1)
        RETURN
    end   
    
    /* 4- O processo deve estar homologado */
    if exists (select 1 from SUB_PROCESSO sp
                where sp.ano_apresentacao = @ano_apresentacao
                and sp.id_representacao = @id_representacao
                and sp.id_processo = @id_processo
                and sp.d_sub_processo = @d_sub_processo
                and sp.d_natureza = @d_natureza
                and sp.id_sequencial_natureza = @id_sequencial_natureza 
                and sp.dthr_homologacao is null)
    begin
        set @msg = 'O subprocesso informado não está homologado! Subprocesso: ' + @sub_processo
        RAISERROR(@msg, 16, 1)
        RETURN            
    end
    
    /* 5- O processo não pode ter sido pago pelo SPAG (não pode existir a chave 
          deste processo em LANCAMENTO_BRUTO_PAGAMENTO com id_tipo_movimento
          igual a 1 e com id_lancamento_liquido preenchido) */
    /* COMENTADO PARA FINS DE DESENVOLVIMENTO */
    /*if exists (select 1 from SUB_PROCESSO sp inner join LANCAMENTO_BRUTO_PAGAMENTO lbp
                    on sp.ano_apresentacao = lbp.ano_apresentacao
                    and sp.id_representacao = lbp.id_representacao
                    and sp.id_processo = lbp.id_processo
                    and sp.d_sub_processo = lbp.d_sub_processo
                    and sp.d_natureza = lbp.d_natureza
                    and sp.id_sequencial_natureza = lbp.id_sequencial_natureza
                where sp.ano_apresentacao = @ano_apresentacao
                and sp.id_representacao = @id_representacao
                and sp.id_processo = @id_processo
                and sp.d_sub_processo = @d_sub_processo
                and sp.d_natureza = @d_natureza
                and sp.id_sequencial_natureza = @id_sequencial_natureza 
                and lbp.id_tipo_movimento = 1
                and lbp.id_lancamento_liquido is not null)
    begin
        set @msg = 'O subprocesso informado já teve pagamento gerado pelo SPAG! Subprocesso: ' + @sub_processo
        RAISERROR(@msg, 16, 1)
        RETURN    
    end */ 
    
    /* 6- O processo não pode ter sido enviado ao SOUL */              
    if exists (select 1 from MV_CAC_DE_PARA_SUB_PROCESSO dpsp
                where dpsp.ano_apresentacao = @ano_apresentacao
                and dpsp.id_representacao = @id_representacao
                and dpsp.id_processo = @id_processo
                and dpsp.d_sub_processo = @d_sub_processo
                and dpsp.d_natureza = @d_natureza
                and dpsp.id_sequencial_natureza = @id_sequencial_natureza)
    begin
        set @msg = 'O subprocesso informado já foi enviado ao SOUL! Subprocesso: ' + @sub_processo
        RAISERROR(@msg, 16, 1)
        RETURN    
    end
                    
     select  sp.ano_apresentacao, sp.id_representacao, sp.id_processo,
             sp.d_sub_processo, sp.d_natureza, sp.id_sequencial_natureza,
             cd_prestador   = (select dpp.cod_prestador
                               from MV_CAC_DE_PARA_PRESTADOR dpp
                               where dpp.id_prestador = sp.id_prestador),
             ds_lote        =  @sub_processo,
             dt_inicial     = (select convert(date, MIN(aa.dt_atendimento))
                               from ATENDIMENTO aa
                               where aa.ano_apresentacao = sp.ano_apresentacao
                               and aa.id_representacao = sp.id_representacao
                               and aa.id_processo = sp.id_processo
                               and aa.d_sub_processo = sp.d_sub_processo
                               and aa.d_natureza = sp.d_natureza
                               and aa.id_sequencial_natureza = sp.id_sequencial_natureza),
             dt_final       = (select convert(date, MAX(aa.dt_atendimento))
                               from ATENDIMENTO aa
                               where aa.ano_apresentacao = sp.ano_apresentacao
                               and aa.id_representacao = sp.id_representacao
                               and aa.id_processo = sp.id_processo
                               and aa.d_sub_processo = sp.d_sub_processo
                               and aa.d_natureza = sp.d_natureza
                               and aa.id_sequencial_natureza = sp.id_sequencial_natureza),
             dt_lote        =  convert(date, sp.dthr_apresentacao), 
             dt_vencimento  = convert(date, sp.dt_prevista_pagamento),
             nr_ano         = DATEPART(YEAR,sp.dthr_apresentacao), -- DATEPART(YEAR,sp.mes_ano_vigencia), -- Substituído temporariamente devido à possível diferença de significado do campo (nr_mes e nr_ano devem ser do mesmo mês e ano que dt_lote)
             nr_ano_pagto   = DATEPART(YEAR, sp.dt_prevista_pagamento),
             nr_mes         = DATEPART(MONTH, sp.dthr_apresentacao), -- DATEPART(MONTH, sp.mes_ano_vigencia), -- Substituído temporariamente devido à possível diferença de significado do campo (nr_mes e nr_ano devem ser do mesmo mês e ano que dt_lote)
             nr_mes_pagto   = DATEPART(MONTH, sp.dt_prevista_pagamento),
             sn_fechado     = 'S',
             tp_guia        = 'C',
             tp_lote        = 'P',
             tp_origem      = 'L' 
     
     from SUB_PROCESSO sp
     where sp.ano_apresentacao = @ano_apresentacao
     and sp.id_representacao = @id_representacao
     and sp.id_processo = @id_processo
     and sp.d_sub_processo = @d_sub_processo
     and sp.d_natureza = @d_natureza
     and sp.id_sequencial_natureza = @id_sequencial_natureza
     and not exists (select 1 from MV_CAC_DE_PARA_SUB_PROCESSO dpsp
                     where dpsp.ano_apresentacao = sp.ano_apresentacao
                     and dpsp.id_representacao = sp.id_representacao
                     and dpsp.id_processo = sp.id_processo
                     and dpsp.d_sub_processo = sp.d_sub_processo
                     and dpsp.d_natureza = sp.d_natureza
                     and dpsp.id_sequencial_natureza = sp.id_sequencial_natureza)
                     

END
GO

GRANT EXECUTE on SPR_ICM_LISTA_LOTE to LogCacMv
GO