SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
-- =============================================
-- Author:		JCJ
-- Create date: 07/06/2017
-- Description:	Retorna os resultados da função dbo.F_procedimento_comando na forma de tabela (ResultSet)
-- =============================================

if OBJECT_ID('dbo.F_procedimento_comando_tab') is null
begin
    exec('CREATE FUNCTION [dbo].[F_procedimento_comando_tab]() returns @dummy table (dummy int) as begin return end')
    GRANT SELECT on [dbo].[F_procedimento_comando_tab] to LogCacMv
    GRANT EXECUTE on [dbo].[F_procedimento_comando] to LogCacMv
    GRANT EXECUTE on [dbo].[F_decodifica_procedimento] to LogCacMv
end
GO

ALTER FUNCTION [dbo].[F_procedimento_comando_tab]
(
    @id_comando int
)
RETURNS @procedimentos TABLE
(
	ano_apresentacao numeric(4, 0),
    id_representacao int,
    id_processo int,
    d_sub_processo char(1),
    d_natureza char(1),
    id_sequencial_natureza tinyint,
    id_atendimento smallint,
    id_procedimento tinyint	
)
AS
BEGIN
    declare @procedimento varchar(40) = null
    declare @separador char(1) = '-'

    -- Busca o procedimento, de acordo com dbo.F_procedimento_comando (que retorna um varchar com a chave
    -- concatenada de PROCEDIMENTO, ou null caso não encontre)
    select @procedimento = dbo.F_procedimento_comando(@id_comando)

    -- Se for null, significa que não há resultado, então retorna apenas um resultset vazio.
    if @procedimento is null
    begin
        RETURN
    end

    -- Devido à construção de dbo.F_decodifica_procedimento, é necessário que haja um '-' a mais no fim da string que representa o procedimento,
    -- para que consiga recuperar o último índice (8, posição de id_procedimento)
    set @procedimento = @procedimento + '-'
	
    -- Estando tudo certo, inclui no resultset a chave de PROCEDIMENTO encontrada
	insert into @procedimentos(ano_apresentacao, id_representacao, id_processo, d_sub_processo, d_natureza, id_sequencial_natureza, id_atendimento, id_procedimento)
    select  dbo.F_decodifica_procedimento(@procedimento, 1, @separador) ,
            dbo.F_decodifica_procedimento(@procedimento, 2, @separador) ,
            dbo.F_decodifica_procedimento(@procedimento, 3, @separador) ,
            dbo.F_decodifica_procedimento(@procedimento, 4, @separador) ,
            dbo.F_decodifica_procedimento(@procedimento, 5, @separador) ,
            dbo.F_decodifica_procedimento(@procedimento, 6, @separador) ,
            dbo.F_decodifica_procedimento(@procedimento, 7, @separador) ,
            dbo.F_decodifica_procedimento(@procedimento, 8, @separador)

    RETURN
END
GO