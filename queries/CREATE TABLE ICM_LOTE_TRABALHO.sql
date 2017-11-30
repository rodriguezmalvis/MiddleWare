SET ANSI_PADDING ON
GO

CREATE TABLE dbo.ICM_LOTE_TRABALHO(
    id_lote_trabalho int not null identity(1,1),
    id_processamento int not null,    
    ano_apresentacao numeric(4,0) not null,
    id_representacao int not null,
    id_processo int not null,
    d_sub_processo char(1) not null,
    d_natureza char(1) not null,
    id_sequencial_natureza tinyint not null,
    id_status_processamento tinyint not null,
    id_lote_reprocessando int null,
    [timestamp] datetime not null CONSTRAINT DEF_ICM_LOTE_TRAB_TIMESTAMP DEFAULT (GETDATE()),
    CONSTRAINT PK_ICM_LOTE_TRABALHO PRIMARY KEY CLUSTERED (
        id_lote_trabalho),
    CONSTRAINT FK_ICM_LOTE_TRAB_ICM_PROCESS FOREIGN KEY (id_processamento)
        REFERENCES dbo.ICM_PROCESSAMENTO (id_processamento),
    CONSTRAINT FK_ICM_LOTE_TRAB_SUB_PROCESSO FOREIGN KEY (id_processo, ano_apresentacao, id_representacao, d_sub_processo, d_natureza, id_sequencial_natureza)
        REFERENCES dbo.SUB_PROCESSO (id_processo, ano_apresentacao, id_representacao, d_sub_processo, d_natureza, id_sequencial_natureza),
    CONSTRAINT FK_ICM_LOTE_TRAB_ICM_STATUS_PROCES FOREIGN KEY (id_status_processamento)
        REFERENCES dbo.ICM_STATUS_PROCESSAMENTO (id_status_processamento),
    CONSTRAINT FK_ICM_LOTE_TRAB_ICM_LOTE_TRAB FOREIGN KEY (id_lote_reprocessando)
        REFERENCES dbo.ICM_LOTE_TRABALHO (id_lote_trabalho)
)
GO

GRANT SELECT, INSERT, UPDATE, DELETE on dbo.ICM_LOTE_TRABALHO to LogCacMv
GO
