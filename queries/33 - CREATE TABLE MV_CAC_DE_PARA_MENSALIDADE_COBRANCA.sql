SET ANSI_PADDING on
GO

CREATE TABLE dbo.MV_CAC_DE_PARA_MENSALIDADE_COBRANCA(
    id_de_para_mensalidade_cobranca int not null,
    cd_mens_contrato_interno int not null,
    cd_mens_contrato_efetivo int not null,
    [timestamp] datetime not null CONSTRAINT DEF_MV_CAC_DP_MENS_COBR_TIMESTAMP DEFAULT(GETDATE()),
    CONSTRAINT PK_MV_CAC_DE_PARA_MENS_COBR PRIMARY KEY CLUSTERED(
        id_de_para_mensalidade_cobranca),
    CONSTRAINT FK_MV_CAC_DP_MENS_COBR_MV_CAC_MENSALIDADE FOREIGN KEY (id_de_para_mensalidade_cobranca)
        REFERENCES dbo.MV_CAC_DE_PARA_MENSALIDADE (id_de_para_mensalidade)
)
GO

GRANT SELECT, INSERT, UPDATE, DELETE on dbo.MV_CAC_DE_PARA_MENSALIDADE_COBRANCA to LogCacMv
GO
