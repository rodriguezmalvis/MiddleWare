SET ANSI_PADDING ON
GO

CREATE TABLE dbo.MV_CAC_DE_PARA_MENSALIDADE(
    id_de_para_mensalidade int not null identity(1,1),
    id_processamento int not null,
    id_titular int not null,
    d_forma_pagamento char(1) not null,
    dt_emissao datetime not null,
    dt_referencia_efetiva datetime not null,
    dt_vencimento datetime not null,
    cd_mens_contrato_interno int not null,
    cd_mens_contrato_efetivo int not null,
    [timestamp] datetime not null CONSTRAINT DEF_MV_CAC_DP_MENS_TIMESTAMP DEFAULT (GETDATE()),
    
    CONSTRAINT PK_MV_CAC_DE_PARA_MENSALIDADE PRIMARY KEY CLUSTERED (
        id_de_para_mensalidade asc),
    CONSTRAINT FK_MV_CAC_DP_MENS_ICM_PROCESS FOREIGN KEY (id_processamento)
        REFERENCES dbo.ICM_PROCESSAMENTO (id_processamento),
    CONSTRAINT FK_MV_CAC_DP_MENS_PESSOA FOREIGN KEY (id_titular)
        REFERENCES dbo.PESSOA (id_pessoa),
    CONSTRAINT CK_MV_CAC_DP_MENS_FORMA_PGTO check 
        ( ([d_forma_pagamento] = 'T') or ([d_forma_pagamento] = 'F') or ([d_forma_pagamento] = 'I') or ([d_forma_pagamento] = 'C') or ([d_forma_pagamento] = 'R') )
)
GO

GRANT SELECT, INSERT, UPDATE, DELETE on dbo.MV_CAC_DE_PARA_MENSALIDADE to LogCacMv
GO