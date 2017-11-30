SET ANSI_PADDING on
GO

CREATE TABLE dbo.MV_CAC_DE_PARA_GRUPO_ITEM_MENSALIDADE(
    id_de_para_grupo_item_mensalidade int not null identity(1,1),
    id_de_para_mensalidade int not null,
    f_devolucao bit not null,
    cd_inclusao_interno int not null,
    cd_inclusao_efetivo int not null,
    [timestamp] datetime not null CONSTRAINT DEF_MV_CAC_DP_GRP_IT_MENS_TIMESTAMP DEFAULT GETDATE(),
    CONSTRAINT PK_MV_CAC_DE_PARA_GRUPO_ITEM_MENSALIDADE PRIMARY KEY CLUSTERED(
        id_de_para_grupo_item_mensalidade),    
    CONSTRAINT FK_MV_CAC_DP_GRP_IT_MENS_MV_CAC_DP_MENS FOREIGN KEY (id_de_para_mensalidade)
        REFERENCES dbo.MV_CAC_DE_PARA_MENSALIDADE (id_de_para_mensalidade)    
)
GO

GRANT SELECT, INSERT, UPDATE, DELETE on dbo.MV_CAC_DE_PARA_GRUPO_ITEM_MENSALIDADE to LogCacMv
GO
