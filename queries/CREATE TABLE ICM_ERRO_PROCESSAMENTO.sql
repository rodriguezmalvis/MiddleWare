SET ANSI_PADDING ON
GO

CREATE TABLE dbo.ICM_ERRO_PROCESSAMENTO(
    id_erro_processamento int not null identity(1, 1),
    id_lote_trabalho int not null,
    mensagem varchar(800) not null,
    [timestamp] datetime not null CONSTRAINT DEF_ICM_ERRO_PROCES DEFAULT (GETDATE()),
    CONSTRAINT PK_ICM_ERRO_PROCESSAMENTO PRIMARY KEY CLUSTERED(
        id_erro_processamento),
    CONSTRAINT FK_ICM_ERRO_PROCESS_ICM_LOTE_TRAB FOREIGN KEY (id_lote_trabalho)
        REFERENCES dbo.ICM_LOTE_TRABALHO (id_lote_trabalho)
)
GO

GRANT SELECT, INSERT, UPDATE, DELETE on dbo.ICM_ERRO_PROCESSAMENTO to LogCacMv
GO
