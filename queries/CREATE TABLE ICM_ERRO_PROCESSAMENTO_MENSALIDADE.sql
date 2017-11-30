SET ANSI_PADDING ON
GO

CREATE TABLE dbo.ICM_ERRO_PROCESSAMENTO_MENSALIDADE(
    id_erro_processamento_mensalidade int not null identity(1,1),
    id_mensalidade_trabalho int not null,
    mensagem varchar(800) not null,
    [timestamp] datetime not null CONSTRAINT DEF_ICM_ERRO_PROCESS_MENS_TIMESTAMP DEFAULT (GETDATE()),
    
    CONSTRAINT PK_ICM_ERRO_PROCESSAMENTO_MENSALIDADE PRIMARY KEY CLUSTERED (
        id_erro_processamento_mensalidade asc),
    CONSTRAINT FK_ICM_ERRO_PROCESS_MENS_ICM_MENS_TRAB FOREIGN KEY (id_mensalidade_trabalho)
        REFERENCES dbo.ICM_MENSALIDADE_TRABALHO (id_mensalidade_trabalho)    
)
GO

GRANT SELECT, INSERT, UPDATE, DELETE on dbo.ICM_ERRO_PROCESSAMENTO_MENSALIDADE to LogCacMv
GO