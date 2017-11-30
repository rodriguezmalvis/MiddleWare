SET ANSI_PADDING on
GO

CREATE TABLE dbo.MV_CAC_DE_PARA_MENSALIDADE_DEVOLUCAO(
    id_de_para_mensalidade_devolucao int not null identity(1,1),
    id_de_para_mensalidade int not null,
    cd_devolucao_interno int not null,
    cd_devolucao_efetivo int not null,
    [timestamp] datetime not null CONSTRAINT DEF_MV_CAC_DP_MENS_DEV_TIMESTAMP DEFAULT(GETDATE()),
    CONSTRAINT PK_MV_CAC_DP_MENS_DEV PRIMARY KEY CLUSTERED(
        id_de_para_mensalidade_devolucao),
    CONSTRAINT FK_MV_CAC_DP_MENS_DEV_MV_CAC_DP_MENSALIDADE FOREIGN KEY (id_de_para_mensalidade)
        REFERENCES dbo.MV_CAC_DE_PARA_MENSALIDADE (id_de_para_mensalidade)
)
GO

GRANT SELECT, INSERT, UPDATE, DELETE on dbo.MV_CAC_DE_PARA_MENSALIDADE_DEVOLUCAO to LogCacMv
GO
