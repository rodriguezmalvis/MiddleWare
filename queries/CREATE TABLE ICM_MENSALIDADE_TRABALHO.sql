SET ANSI_PADDING ON
GO

CREATE TABLE dbo.ICM_MENSALIDADE_TRABALHO(
    id_mensalidade_trabalho int not null identity(1,1),
    id_processamento int not null,
    id_status_processamento tinyint not null,
    id_titular int not null,
    d_forma_pagamento char(1) not null,
    dt_emissao datetime not null,
    dt_referencia_prevista datetime not null,
    dt_vencimento datetime not null,
    id_mensalidade_reprocessando int null,
    [timestamp] datetime not null CONSTRAINT DEF_ICM_MENS_TRAB_TIMESTAMP DEFAULT (GETDATE()),
    
    CONSTRAINT PK_ICM_MENSALIDADE_TRABALHO PRIMARY KEY CLUSTERED (
        id_mensalidade_trabalho asc),
    CONSTRAINT FK_ICM_MENS_TRAB_ICM_PROCESS FOREIGN KEY (id_processamento)
        REFERENCES dbo.ICM_PROCESSAMENTO (id_processamento),
    CONSTRAINT FK_ICM_MENS_TRAB_ICM_STAT_PROCESS FOREIGN KEY (id_status_processamento)
        REFERENCES dbo.ICM_STATUS_PROCESSAMENTO (id_status_processamento),
    CONSTRAINT FK_ICM_MENS_TRAB_PESSOA FOREIGN KEY (id_titular)
        REFERENCES dbo.PESSOA (id_pessoa),
    CONSTRAINT CK_ICM_MENS_TRAB_FORMA_PGTO check 
        ( ([d_forma_pagamento] = 'T') or ([d_forma_pagamento] = 'F') or ([d_forma_pagamento] = 'I') or ([d_forma_pagamento] = 'C') or ([d_forma_pagamento] = 'R') ),
    CONSTRAINT FK_ICM_MENS_TRAB_ICM_MENS_TRABALHO FOREIGN KEY (id_mensalidade_reprocessando)
        REFERENCES dbo.ICM_MENSALIDADE_TRABALHO (id_mensalidade_trabalho)    
)
GO

GRANT SELECT, INSERT, UPDATE, DELETE on dbo.ICM_MENSALIDADE_TRABALHO to LogCacMv
GO