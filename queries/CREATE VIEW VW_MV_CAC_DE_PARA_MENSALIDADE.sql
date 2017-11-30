Use CacDb
GO

if OBJECT_ID('CacDb.dbo.VW_MV_CAC_DE_PARA_MENSALIDADE') is not null
begin
    drop view dbo.VW_MV_CAC_DE_PARA_MENSALIDADE
end
GO

CREATE VIEW dbo.VW_MV_CAC_DE_PARA_MENSALIDADE WITH SCHEMABINDING as
    select  dpm.id_processamento,
            dpm.id_titular,
            dpm.d_forma_pagamento,
            dpm.dt_emissao,
            dpm.dt_referencia_efetiva,
            dpm.dt_vencimento,
            dpgim.f_devolucao,
            dpgim.cd_inclusao_interno,
            dpgim.cd_inclusao_efetivo,
            dpim.id_comando,
            segim.cod_status,
            dpm.id_de_para_mensalidade,
            dpgim.id_de_para_grupo_item_mensalidade,
            dpim.id_de_para_item_mensalidade            
    from dbo.MV_CAC_DE_PARA_MENSALIDADE dpm 
        inner join dbo.MV_CAC_DE_PARA_GRUPO_ITEM_MENSALIDADE dpgim
            on dpm.id_de_para_mensalidade = dpgim.id_de_para_mensalidade
        inner join dbo.MV_CAC_STATUS_ENVIO_GRUPO_ITEM_MENS segim
            on dpgim.id_status_envio_grupo_item_mens = segim.id_status_envio_grupo_item_mens
        inner join dbo.MV_CAC_DE_PARA_ITEM_MENSALIDADE dpim
            on dpgim.id_de_para_grupo_item_mensalidade = dpim.id_de_para_grupo_item_mensalidade
    where not exists (select 1 from dbo.MV_CAC_DE_PARA_GRUPO_ITEM_MENSALIDADE idpgim
                        inner join dbo.MV_CAC_STATUS_ENVIO_GRUPO_ITEM_MENS isegim
                            on idpgim.id_status_envio_grupo_item_mens = isegim.id_status_envio_grupo_item_mens
                      where dpm.id_de_para_mensalidade = idpgim.id_de_para_mensalidade
                      and isegim.cod_status = 'CA')

GO