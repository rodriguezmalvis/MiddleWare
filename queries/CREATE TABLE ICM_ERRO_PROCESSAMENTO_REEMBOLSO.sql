SET ANSI_PADDING ON
GO

CREATE TABLE dbo.ICM_ERRO_PROCESSAMENTO_REEMBOLSO(
    id_erro_processamento_reembolso int not null identity(1,1),
    id_reembolso_trabalho int not null,
    mensagem varchar(800) not null,
    [timestamp] datetime not null CONSTRAINT DEF_ICM_ERRO_PROCES_REEMB_TIMESTAMP DEFAULT (GETDATE()),
    
    CONSTRAINT PK_ICM_ERRO_PROCESSAMENTO_REEMBOLSO PRIMARY KEY CLUSTERED (
        id_erro_processamento_reembolso asc),
    CONSTRAINT FK_ICM_ERRO_PROCES_REEMB_ICM_REEMB_TRAB FOREIGN KEY (id_reembolso_trabalho)
        REFERENCES dbo.ICM_REEMBOLSO_TRABALHO (id_reembolso_trabalho)           
)
GO

GRANT SELECT, INSERT, UPDATE, DELETE on dbo.ICM_ERRO_PROCESSAMENTO_REEMBOLSO to LogCacMv
GO
