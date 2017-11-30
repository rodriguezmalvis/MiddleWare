SET ANSI_PADDING on
GO

CREATE TABLE dbo.MV_CAC_STATUS_ENVIO_GRUPO_ITEM_MENS(
    id_status_envio_grupo_item_mens int not null identity(1,1),
    cod_status char(2) not null,
    descricao varchar(150) not null,
    f_final bit not null,
    f_gera_inconsistência bit not null,
    [timestamp] datetime not null CONSTRAINT DEF_MV_CAC_STST_ENV_GRP_IT_MENS_TIMESTAMP DEFAULT GETDATE(),
    CONSTRAINT PK_MV_CAC_STATUS_ENVIO_GRUPO_ITEM_MENS PRIMARY KEY CLUSTERED (
        id_status_envio_grupo_item_mens asc)
)
GO

GRANT SELECT, INSERT, UPDATE, DELETE on dbo.MV_CAC_STATUS_ENVIO_GRUPO_ITEM_MENS to LogCacMv
GO

INSERT INTO dbo.MV_CAC_STATUS_ENVIO_GRUPO_ITEM_MENS (cod_status, descricao, f_final, f_gera_inconsistência) values
('ET', 'Enviado totalmente: o grupo foi enviado e não há mais grupos da mesma mensalidade a serem enviados', 1, 0),
('EP', 'Enviado parcialmente: o grupo foi enviado mas há outros grupos da mesma mensalidade a serem enviados', 0, 1),
('AC', 'A Cancelar: o grupo foi enviado, porém por erro no envio de outro grupo da mesma mensalidade e, portanto, ele deverá ser cancelado', 0, 1),
('CA', 'Cancelado: o grupo foi enviado anteriormente, mas foi cancelado no SOUL', 1, 0)
GO
