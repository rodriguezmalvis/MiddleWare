SET ANSI_PADDING on
GO

CREATE TABLE dbo.MV_CAC_DE_PARA_REEMBOLSO(
    id_de_para_reembolso int not null identity (1,1),
    id_processamento int not null,
    ano_apresentacao numeric(4,0) not null,
    id_representacao int not null,
    id_processo int not null,
    d_sub_processo char(1) not null,
    d_natureza char(1) not null,
    id_sequencial_natureza tinyint not null,
    id_atendimento smallint not null,
    cd_reembolso_inclusao int not null,
    cd_reembolso_efetivo int not null,
    [timestamp] datetime not null CONSTRAINT DEF_MV_CAC_DPR_TIMESTAMP DEFAULT (GETDATE()),
    
    CONSTRAINT PK_MV_CAC_DE_PARA_REEMBOLSO PRIMARY KEY CLUSTERED(
        id_de_para_reembolso asc),
    CONSTRAINT FK_MV_CAC_DPR_ICM_PROCES FOREIGN KEY (id_processamento)
        REFERENCES dbo.ICM_PROCESSAMENTO (id_processamento),
    CONSTRAINT FK_MV_CAC_DPR_ATENDIMENTO FOREIGN KEY (id_processo, ano_apresentacao, id_representacao, 
            d_sub_processo, d_natureza, id_sequencial_natureza, id_atendimento)
        REFERENCES dbo.ATENDIMENTO (id_processo, ano_apresentacao, id_representacao, 
            d_sub_processo, d_natureza, id_sequencial_natureza, id_atendimento)         
)
GO

GRANT SELECT, INSERT, UPDATE, DELETE on dbo.MV_CAC_DE_PARA_REEMBOLSO to LogCacMv
GO
