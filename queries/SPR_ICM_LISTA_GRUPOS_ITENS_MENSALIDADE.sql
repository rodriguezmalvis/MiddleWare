/* 
    dbo.SPR_ICM_LISTA_GRUPOS_ITENS_MENSALIDADE:
    
    Script que classifica os comandos de uma MensalidadeTrabalho por grupos.
    
    Autor: JCJ
    Data: 03/07/2017
*/


IF  EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[SPR_ICM_LISTA_GRUPOS_ITENS_MENSALIDADE]') AND type in (N'P', N'PC'))
begin
    DROP PROCEDURE [dbo].[SPR_ICM_LISTA_GRUPOS_ITENS_MENSALIDADE]
end
GO


CREATE /*ALTER*/ PROCEDURE dbo.SPR_ICM_LISTA_GRUPOS_ITENS_MENSALIDADE(
    @id_mensalidade_trabalho int
)
AS
BEGIN
    SET NOCOUNT ON;

    declare @resultado table(
        seq_mensalidade int,
        id_comando int,
        f_devolucao bit)

    insert into @resultado (seq_mensalidade, id_comando, f_devolucao)
    select
            /* Para uma dada mensalidade de trabalho, todos os comandos de cobrança deverão ir para o mesmo grupo de itens de mensalidade. Portanto, este valor é constante */
            case when rc.f_provento = 0 then
                    0
           /* Para uma dada mensalidade de trabalho, todos os critérios de agrupamento foram definidos anteriormente, nas procedures de origem (LISTA_MENSALIDADES_TRABALHO). Então, basta
              separar apenas pelo id_pessoa, já que, pela definição do JSON, é preciso enviar as devoluções de cada pessoa de maneira agrupada, independente das cobranças */  
                 when rc.f_provento = 1 then
                    RANK() over (order by cc.id_pessoa) 
            end as seq_mensalidade,
            cc.id_comando,
            rc.f_provento as f_devolucao
    from COMANDO cc inner join RUBRICA_CAC rc
            on cc.id_rubrica_cac = rc.id_rubrica_cac
    where 
        cc.id_comando in (select imt.id_comando
                          from ICM_MENSALIDADE_TRABALHO mt inner join ICM_ITEM_MENSALIDADE_TRABALHO imt
                            on mt.id_mensalidade_trabalho = imt.id_mensalidade_trabalho
                          where mt.id_mensalidade_trabalho = @id_mensalidade_trabalho
                          and imt.id_grupo_item_mensalidade_trabalho is null)
       

    select * from @resultado
    order by seq_mensalidade, f_devolucao, id_comando

END
GO

GRANT EXECUTE ON dbo.SPR_ICM_LISTA_GRUPOS_ITENS_MENSALIDADE to LogCacMv
GO
