SET ANSI_PADDING on
GO

CREATE TABLE dbo.MV_CAC_DE_PARA_ITEM_MENSALIDADE_DEVOLUCAO(
    id_de_para_item_mensalidade_devolucao int not null identity(1,1),
    id_de_para_mensalidade_devolucao int not null,
    id_comando int not null,
    [timestamp] datetime not null CONSTRAINT DEF_MV_CAC_DP_IT_MENS_DEV_TIMESTAMP DEFAULT(GETDATE()),
    CONSTRAINT PK_MV_CAC_DP_IT_MENS_DEVOLUCAO PRIMARY KEY CLUSTERED(
        id_de_para_item_mensalidade_devolucao),
    CONSTRAINT FK_MV_CAC_DP_IT_MENS_DEV_MV_CAC_DP_MENS_DEV FOREIGN KEY (id_de_para_mensalidade_devolucao)
        REFERENCES dbo.MV_CAC_DE_PARA_MENSALIDADE_DEVOLUCAO (id_de_para_mensalidade_devolucao),
    CONSTRAINT FK_MV_CAC_DP_IT_MENS_DEV_COMANDO FOREIGN KEY (id_comando)
        REFERENCES dbo.COMANDO (id_comando)
)
GO

GRANT SELECT, INSERT, UPDATE, DELETE on dbo.MV_CAC_DE_PARA_ITEM_MENSALIDADE_DEVOLUCAO to LogCacMv
GO
