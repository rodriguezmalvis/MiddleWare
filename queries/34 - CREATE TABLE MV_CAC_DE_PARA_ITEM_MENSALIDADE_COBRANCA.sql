SET ANSI_PADDING on
GO

CREATE TABLE dbo.MV_CAC_DE_PARA_ITEM_MENSALIDADE_COBRANCA(
    id_de_para_item_mensalidade_cobranca int not null identity(1,1),
    id_de_para_mensalidade_cobranca int not null,
    id_comando int not null,
    [timestamp] datetime not null CONSTRAINT DEF_MV_CAC_DP_IT_MENS_COBR_TIMESTAMP DEFAULT(GETDATE()),
    CONSTRAINT PK_MV_CAC_DE_PARA_ITEM_MENS_COBR PRIMARY KEY CLUSTERED(
        id_de_para_item_mensalidade_cobranca),
    CONSTRAINT FK_MV_CAC_DP_IT_MENS_COBR_MV_CAC_DP_MENS_COBR FOREIGN KEY (id_de_para_mensalidade_cobranca)
        REFERENCES dbo.MV_CAC_DE_PARA_MENSALIDADE_COBRANCA (id_de_para_mensalidade_cobranca),
    CONSTRAINT FK_MV_CAC_DP_IT_MENS_COBR_COMANDO FOREIGN KEY (id_comando)
        REFERENCES dbo.COMANDO (id_comando)
)
GO

GRANT SELECT, INSERT, UPDATE, DELETE on dbo.MV_CAC_DE_PARA_ITEM_MENSALIDADE_COBRANCA to LogCacMv
GO
