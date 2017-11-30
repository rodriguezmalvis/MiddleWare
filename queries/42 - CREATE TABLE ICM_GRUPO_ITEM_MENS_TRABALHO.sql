SET ANSI_PADDING on
GO

CREATE TABLE dbo.ICM_GRUPO_ITEM_MENSALIDADE_TRABALHO(
    id_grupo_item_mensalidade_trabalho int not null identity(1,1),
    id_mensalidade_trabalho int not null,
    f_devolucao bit not null,
    id_de_para_grupo_item_mensalidade int null,
    [timestamp] datetime not null CONSTRAINT DEF_ICM_GRP_IT_MENS_TRAB_TIMESTAMP DEFAULT GETDATE(),
    CONSTRAINT PK_ICM_GRUPO_ITEM_MENSALIDADE_TRABALHO PRIMARY KEY CLUSTERED(
        id_grupo_item_mensalidade_trabalho),
    CONSTRAINT FK_ICM_GRP_IT_MENS_TRAB_ICM_MENS_TRAB FOREIGN KEY (id_mensalidade_trabalho)
        REFERENCES dbo.ICM_MENSALIDADE_TRABALHO (id_mensalidade_trabalho),
    CONSTRAINT FK_ICM_GRP_IT_MENS_TRAB_MV_CAC_DP_GRP_IT_MENS FOREIGN KEY (id_de_para_grupo_item_mensalidade)
        REFERENCES dbo.MV_CAC_DE_PARA_GRUPO_ITEM_MENSALIDADE (id_de_para_grupo_item_mensalidade)
)
GO

/*  Este índice único deve ser substituído por uma trigger, por restrições de índices filtrados, 
    ODBC e grau de compatibilidade (80) dos bancos de dados de homologação e produção.
CREATE UNIQUE INDEX UQ_ICM_GRP_IT_MENS_TRAB_ID_DP_GRP_IT_MENS on dbo.ICM_GRUPO_ITEM_MENSALIDADE_TRABALHO
    (id_de_para_grupo_item_mensalidade asc) where id_de_para_grupo_item_mensalidade is not null
GO*/

GRANT SELECT, INSERT, UPDATE, DELETE on dbo.ICM_GRUPO_ITEM_MENSALIDADE_TRABALHO to LogCacMv
GO