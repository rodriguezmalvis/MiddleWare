SET ANSI_PADDING ON
GO


CREATE TABLE dbo.ICM_TIPO_PROCESSAMENTO(
    id_tipo_processamento tinyint not null identity(1,1),
    cod_processamento char(2) not null,
    descricao varchar(200) not null,
    [timestamp] datetime not null CONSTRAINT DEF_ICM_TIPO_PROCES_TIMESTAMP DEFAULT (GETDATE()),
    CONSTRAINT PK_ICM_TIPO_PROCESSAMENTO PRIMARY KEY CLUSTERED(
        id_tipo_processamento asc
    )
)
GO

insert into ICM_TIPO_PROCESSAMENTO (cod_processamento, descricao)
values ('LC', 'Lote de contas: trata um grupo de subprocessos, transformando-os em diversos lotes para envio.'),
('CI', 'Conta individual: trata um único subprocesso, transformando-o em um único lote para envio.'),
('LT', 'Lote de títulos a receber: trata vários conjuntos de títulos a receber, agrupando por grupo familiar, forma de pagamento e data de vencimento'),
('LR', 'Lote de reembolsos: trata vários subprocessos e gera vários reembolsos, para cada atendimento individual.');

GO

GRANT SELECT on ICM_TIPO_PROCESSAMENTO to LogCacMv
GO