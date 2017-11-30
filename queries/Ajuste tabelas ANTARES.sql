/*
    Este script deve ser executado apenas em ambientes onde as outras tabelas ICM_ e MV_CAC_DE_PARA_ j� existem.
    Os scripts de cria��o delas j� inclui as corre��es abaixo.
*/


alter table ICM_MENSALIDADE_TRABALHO
alter column dt_referencia_prevista datetime not null
GO

alter table ICM_MENSALIDADE_TRABALHO
alter column dt_vencimento datetime not null
GO

alter table MV_CAC_DE_PARA_MENSALIDADE
alter column dt_referencia_efetiva datetime not null
GO

alter table MV_CAC_DE_PARA_MENSALIDADE
alter column dt_vencimento datetime not null
GO
