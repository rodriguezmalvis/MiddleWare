SET ANSI_PADDING ON
GO

CREATE TABLE dbo.ICM_ITEM_MENSALIDADE_TRABALHO(
    id_item_mensalidade_trabalho int not null identity(1,1),
    id_mensalidade_trabalho int not null,
    id_comando int not null,
    [timestamp] datetime not null CONSTRAINT DEF_ICM_IT_MENS_TRAB_TIMESTAMP DEFAULT (GETDATE()),
    
    CONSTRAINT PK_ICM_ITEM_MENSALIDADE_TRABALHO PRIMARY KEY CLUSTERED(
        id_item_mensalidade_trabalho asc),
    CONSTRAINT FK_ICM_IT_MENS_TRAB_ICM_MENS_TRAB FOREIGN KEY (id_mensalidade_trabalho)
        REFERENCES dbo.ICM_MENSALIDADE_TRABALHO (id_mensalidade_trabalho),
    CONSTRAINT FK_ICM_IT_MENS_TRAB_COMANDO FOREIGN KEY (id_comando)
        REFERENCES dbo.COMANDO (id_comando)
        
)
GO

GRANT SELECT, INSERT, UPDATE, DELETE on dbo.ICM_ITEM_MENSALIDADE_TRABALHO to LogCacMv
GO