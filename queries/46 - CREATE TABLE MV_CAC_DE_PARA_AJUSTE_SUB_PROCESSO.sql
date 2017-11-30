SET ANSI_PADDING on
GO

CREATE TABLE dbo.MV_CAC_DE_PARA_AJUSTE_SUB_PROCESSO(
    id_de_para_ajuste_sub_processo int not null identity(1,1),
    id_de_para_sub_processo int not null,
    id_atendimento smallint null,
    id_procedimento tinyint null,
    campo varchar(100) not null,
    valor_original varchar(100) not null,
    valor_novo varchar(100) not null,
    motivo char(2) not null,
    [timestamp] datetime not null CONSTRAINT DEF_MV_CAC_DP_AJUS_SUB_PROC_TIMESTAMP DEFAULT GETDATE(),
    CONSTRAINT PK_MV_CAC_DE_PARA_AJUSTE_SUB_PROCESSO PRIMARY KEY CLUSTERED (
        id_de_para_ajuste_sub_processo asc),
    CONSTRAINT FK_MV_CAC_DP_AJUS_SUB_PROC_MV_CAC_DP_SUB_PROC FOREIGN KEY (id_de_para_sub_processo)
        REFERENCES dbo.MV_CAC_DE_PARA_SUB_PROCESSO (id_de_para_sub_processo)    
)
GO

GRANT SELECT, INSERT, UPDATE, DELETE on dbo.MV_CAC_DE_PARA_AJUSTE_SUB_PROCESSO to LogCacMv
GO