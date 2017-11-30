SET ANSI_PADDING ON
GO

CREATE TABLE dbo.ICM_STATUS_PROCESSAMENTO(
    id_status_processamento tinyint not null identity(1,1),
    cod_status char(2) not null,
    descricao varchar(100) not null,
    f_final bit not null,
    [timestamp] datetime not null CONSTRAINT DEF_ICM_STATUS_PROC_TIMESTAMP DEFAULT (GETDATE()),
    CONSTRAINT PK_ICM_STATUS_PROCESSAMENTO PRIMARY KEY CLUSTERED(
        id_status_processamento asc)
)
GO

INSERT INTO dbo.ICM_STATUS_PROCESSAMENTO(cod_status, descricao, f_final)
values ('AD', 'Adicionado: o item foi adicionado e aguarda processamento', 0),
('EP', 'Em processamento: o item está sendo processado no momento', 0),
('PE', 'Processado com erros: o item foi processado mas ocorreram erros durante o processamento', 0),
('PT', 'Processado totalmente: o item foi completamente processado e enviado para seu destino', 1),
('ES', 'Enviado para SPAG: o item estava com processamento pendente mas foi enviado para o SPAG', 1),
('RE', 'Reprocessado: o item foi reprocessado em um novo processamento', 1),
('CA', 'Cancelado: Por algum motivo, este item não será mais enviado a seu destino', 1);
GO

GRANT SELECT on dbo.ICM_STATUS_PROCESSAMENTO to LogCacMv
GO
