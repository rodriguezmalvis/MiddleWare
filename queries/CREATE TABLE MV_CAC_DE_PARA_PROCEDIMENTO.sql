SET ANSI_PADDING on
GO

CREATE TABLE dbo.MV_CAC_DE_PARA_PROCEDIMENTO(
    id_de_para_procedimento int not null identity(1,1),
    id_de_para_atendimento int not null,
    ano_apresentacao numeric(4,0) not null,
    id_representacao int not null,
    id_processo int not null,
    d_sub_processo char(1) not null,
    d_natureza char(1) not null,
    id_sequencial_natureza tinyint not null,
    id_atendimento smallint not null,
    id_procedimento tinyint not null,
    cd_itconta_medica_inclusao int not null,
    cd_itconta_medica_efetivo int null,
    [timestamp] datetime not null CONSTRAINT DEF_MV_CAC_DPPROC_TIMESTAMP DEFAULT (GETDATE()),
    CONSTRAINT PK_MV_CAC_DE_PARA_PROCEDIMENTO PRIMARY KEY CLUSTERED(
        id_de_para_procedimento),
    CONSTRAINT FK_MV_CAC_DPPROC_MV_CAC_DPATEND FOREIGN KEY (id_de_para_atendimento)
        REFERENCES dbo.MV_CAC_DE_PARA_ATENDIMENTO (id_de_para_atendimento),
    CONSTRAINT FK_MV_CAC_DPPROC_PROCED FOREIGN KEY (id_processo, ano_apresentacao, id_representacao, d_sub_processo, d_natureza, id_sequencial_natureza, id_atendimento, id_procedimento)
        REFERENCES dbo.PROCEDIMENTO (id_processo, ano_apresentacao, id_representacao, d_sub_processo, d_natureza, id_sequencial_natureza, id_atendimento, id_procedimento)
)
GO

GRANT SELECT, INSERT, UPDATE, DELETE on dbo.MV_CAC_DE_PARA_PROCEDIMENTO to LogCacMv
GO
