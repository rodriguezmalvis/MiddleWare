SET ANSI_PADDING on 
GO

CREATE TABLE dbo.MV_CAC_DE_PARA_ATENDIMENTO(
    id_de_para_atendimento int not null identity (1,1),
    id_de_para_sub_processo int not null,
    ano_apresentacao numeric(4,0) not null,
    id_representacao int not null,
    id_processo int not null,
    d_sub_processo char(1) not null,
    d_natureza char(1) not null,
    id_sequencial_natureza tinyint not null,
    id_atendimento smallint not null,
    cd_conta_medica_inclusao int not null,
    cd_conta_medica_efetivo int null,
    [timestamp] datetime not null CONSTRAINT DEF_MV_CAC_DPATEND_TIMESTAMP DEFAULT (GETDATE()),
    CONSTRAINT PK_MV_CAC_DE_PARA_ATENDIMENTO PRIMARY KEY CLUSTERED(
        id_de_para_atendimento),
    CONSTRAINT FK_MV_CAC_DPATEND_MV_CAC_DPSP FOREIGN KEY (id_de_para_sub_processo)
        REFERENCES dbo.MV_CAC_DE_PARA_SUB_PROCESSO (id_de_para_sub_processo),
    CONSTRAINT FK_MV_CAC_DPATEND_ATEND FOREIGN KEY (id_processo, ano_apresentacao, id_representacao, d_sub_processo, d_natureza, id_sequencial_natureza, id_atendimento)
        REFERENCES dbo.ATENDIMENTO (id_processo, ano_apresentacao, id_representacao, d_sub_processo, d_natureza, id_sequencial_natureza, id_atendimento)
)
GO

GRANT SELECT, INSERT, UPDATE, DELETE on dbo.MV_CAC_DE_PARA_ATENDIMENTO to LogCacMv
GO
