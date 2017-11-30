SET ANSI_PADDING on
GO

CREATE TABLE dbo.MV_CAC_DE_PARA_SUB_PROCESSO(
    id_de_para_sub_processo int not null identity (1,1),
    id_processamento int not null,
    ano_apresentacao numeric(4,0) not null,
    id_representacao int not null,
    id_processo int not null,
    d_sub_processo char(1) not null,
    d_natureza char(1) not null,
    id_sequencial_natureza tinyint not null,
    cd_lote_inclusao int not null,
    cd_lote_efetivo int not null,
    tipo_guia_mv char(1) not null,
    [timestamp] datetime not null CONSTRAINT DEF_MV_CAC_DPSP_TIMESTAMP DEFAULT (GETDATE()),
    CONSTRAINT PK_MV_CAC_DE_PARA_SUB_PROC PRIMARY KEY CLUSTERED(
        id_de_para_sub_processo),
    CONSTRAINT FK_MV_CAC_DPSP_ICM_PROCES FOREIGN KEY (id_processamento)
        REFERENCES dbo.ICM_PROCESSAMENTO (id_processamento),
    CONSTRAINT FK_MV_CAC_DPSP_SUB_PROCESSO FOREIGN KEY (id_processo, ano_apresentacao, id_representacao, d_sub_processo, d_natureza, id_sequencial_natureza)
        REFERENCES dbo.SUB_PROCESSO (id_processo, ano_apresentacao, id_representacao, d_sub_processo, d_natureza, id_sequencial_natureza),
    CONSTRAINT [CK_MV_CAC_DPSP_TIPO_GUIA_MV] CHECK  (([tipo_guia_mv]='C' OR [tipo_guia_mv]='S' OR [tipo_guia_mv]='D' 
        OR [tipo_guia_mv]='H' OR [tipo_guia_mv]='I' OR [tipo_guia_mv]='P' OR [tipo_guia_mv]='Q' OR [tipo_guia_mv]='R'))           
)
GO

GRANT SELECT, INSERT, UPDATE, DELETE on dbo.MV_CAC_DE_PARA_SUB_PROCESSO to LogCacMv
GO
