SET ANSI_PADDING ON
GO

CREATE TABLE dbo.ICM_PROCESSAMENTO(
    id_processamento int not null identity (1,1),
    id_tipo_processamento tinyint not null,
    dthr_inicio_processamento datetime not null,
    dthr_fim_processamento datetime null,
    dthr_referencia_inicial datetime null,
    dthr_referencia_final datetime null,
    [timestamp] datetime not null CONSTRAINT DEF_ICM_PROCESS_TIMESTAMP DEFAULT (GETDATE()),
    CONSTRAINT PK_ICM_PROCESSAMENTO PRIMARY KEY CLUSTERED(
        id_processamento asc),
    CONSTRAINT FK_ICM_PROCES_ICM_TIPO_PROCESS FOREIGN KEY (id_tipo_processamento) 
        REFERENCES dbo.ICM_TIPO_PROCESSAMENTO (id_tipo_processamento)       
)
GO

GRANT SELECT, INSERT, UPDATE, DELETE on dbo.ICM_PROCESSAMENTO to LogCacMv
GO
