SET ANSI_PADDING on
GO

CREATE TABLE dbo.ICM_SUB_PROCESSO_DIRETO(
    ano_apresentacao numeric(4,0) not null,
    id_representacao int not null,
    id_processo int not null,
    d_sub_processo char(1) not null,
    d_natureza char(1) not null,
    id_sequencial_natureza tinyint not null,
    [timestamp] datetime not null CONSTRAINT DEF_ICM_SUB_PROC_DIRETO_TIMESTAMP DEFAULT (GETDATE()),
    CONSTRAINT PK_ICM_SUB_PROCESSO_DIRETO PRIMARY KEY CLUSTERED(
        ano_apresentacao,
        id_representacao,
        id_processo,
        d_sub_processo,
        d_natureza,
        id_sequencial_natureza),
    CONSTRAINT FK_ICM_SUB_PROC_DIRETO_SUB_PROCESSO FOREIGN KEY (id_processo, ano_apresentacao, id_representacao, d_sub_processo, d_natureza, id_sequencial_natureza)
        REFERENCES dbo.SUB_PROCESSO (id_processo, ano_apresentacao, id_representacao, d_sub_processo, d_natureza, id_sequencial_natureza)        
)
GO

GRANT SELECT, INSERT, UPDATE, DELETE on dbo.ICM_SUB_PROCESSO_DIRETO to LogCacMv
GO
