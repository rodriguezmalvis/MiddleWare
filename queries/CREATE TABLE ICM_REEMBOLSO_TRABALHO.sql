SET ANSI_PADDING ON
GO

CREATE TABLE dbo.ICM_REEMBOLSO_TRABALHO(
    id_reembolso_trabalho int not null identity(1,1),
    id_processamento int not null,   
    ano_apresentacao numeric(4,0) not null,
    id_representacao int not null,
    id_processo int not null,
    d_sub_processo char(1) not null,
    d_natureza char(1) not null,
    id_sequencial_natureza tinyint not null,
    id_atendimento smallint not null,
    id_status_processamento tinyint not null,
    id_reembolso_reprocessando int null,
    [timestamp] datetime not null CONSTRAINT DEF_ICM_REEMB_TRAB_TIMESTAMP DEFAULT (GETDATE()),
    CONSTRAINT PK_ICM_REEMBOLSO_TRABALHO PRIMARY KEY CLUSTERED (
        id_reembolso_trabalho asc),
    CONSTRAINT FK_ICM_REEMB_TRAB_ICM_PROCESS FOREIGN KEY (id_processamento)
        REFERENCES dbo.ICM_PROCESSAMENTO (id_processamento),
    CONSTRAINT FK_ICM_REEMB_TRAB_ATEND FOREIGN KEY (id_processo, ano_apresentacao, id_representacao, d_sub_processo, 
            d_natureza, id_sequencial_natureza, id_atendimento)
        REFERENCES dbo.ATENDIMENTO (id_processo, ano_apresentacao, id_representacao, d_sub_processo, 
            d_natureza, id_sequencial_natureza, id_atendimento),
    CONSTRAINT FK_ICM_REEMB_TRAB_ICM_STATUS_PROCES FOREIGN KEY (id_status_processamento)
        REFERENCES dbo.ICM_STATUS_PROCESSAMENTO (id_status_processamento),
    CONSTRAINT FK_ICM_REEMB_TRAB_ICM_REEMB_TRAB FOREIGN KEY (id_reembolso_reprocessando)
        REFERENCES dbo.ICM_REEMBOLSO_TRABALHO (id_reembolso_trabalho)
)
GO

GRANT SELECT, INSERT, UPDATE, DELETE on dbo.ICM_REEMBOLSO_TRABALHO to LogCacMv
GO
