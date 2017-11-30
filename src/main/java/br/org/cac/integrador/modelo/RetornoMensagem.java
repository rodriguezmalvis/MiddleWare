package br.org.cac.integrador.modelo;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import br.org.cac.cacmvmiddleware.modelo.Retorno;

/**
 * <p>Classe que estende {@link Retorno}, para incluir a geração de mensagens.
 * A classe {@code Retorno} existe sem mensagens pois, em algumas situações, não é
 * possível desserializar corretamente o JSON de retorno da MV com essa estrutura
 * (às vezes o campo {@code mensagem} do JSON é retornado como {@code String}, ao 
 * invés de um {@code Array}.</p>
 * 
 * <p>A estrutura completa do JSON é:
 * <pre>{
 *  "status": string,
 *  "mensagem:[
 *  	string &lt;, ...&gt;
 *  ],
 *  "entidadeId": string
 * }</pre>
 * </p>
 * 
 *  
 * @author JCJ
 * @version 1.0
 * @since 2017-03-09
 *
 */
public class RetornoMensagem extends Retorno {
	/**
	 * Representação de uma expressão regular para a busca de erros de CNES nas mensagens retornadas
	 * pelo SOUL, com um grupo de captura para o número do CNES propriamente dito.
	 * Um exemplo de mensagens dessa é:
	 * <pre>14183 - IMP_CONTA_MEDICA(CD_CNES): O CAMPO CÓDIGO DO CNES : [ 3333809 ], NÃO EXISTE!  NÚMERO DA GUIA :[ 45422860] <pre>
	 */
	public static final String REGEX_ERRO_CNES = "CNES\\s?\\:\\s?\\[\\s?(\\d+).*NÃO EXISTE\\!";

	/**
	 * Representação de uma expressão regular para a busca de erros de código de procedimento inválido em contas médicas.
	 */
	public static final String REGEX_ERRO_PROCEDIMENTO_INVALIDO_CONTA_MEDICA = "C[OÓ]DIGO DO PROCEDIMENTO\\s?\\:\\s?\\[\\s?(\\-?\\d+).*NÃO EXISTE\\!";
	
	public static final String REGEX_ERRO_MENSALIDADE_JA_ENVIADA = "JÁ EXISTE UMA MENSALIDADE PARA ESSA COMPETÊNCIA";

	/**
	 * Representação de uma expressão regular para a busca de erros de código de procedimento inválido no retorno de cobranças
	 * de títulos a receber.
	 */
	public static final String REGEX_ERRO_PROCEDIMENTO_INVALIDO_MENSALIDADE_COBRANCA = "C[OÓ]DIGO PROCEDIMENTO\\s?(\\-?\\d+)\\s?N[AÃ]O CADASTRADO NO SISTEMA\\!";

	private List<String> mensagem;
	
	public RetornoMensagem(){
		super();
	}
	
	
	@SuppressWarnings("unchecked")
	@JsonCreator	
	public RetornoMensagem(	@JsonProperty("status") String status, 
							@JsonProperty("entidadeId") String entidadeId,
							@JsonProperty("mensagem") Object mensagem) {
		super(status, entidadeId);
		
		if (mensagem instanceof List){
			this.mensagem = (List<String>) mensagem;
		} else {
			this.mensagem = new ArrayList<>();
			this.mensagem.add(mensagem.toString());			
		}		
		
	}

	public List<String> getMensagem() {
		return mensagem;
	}

	public void setMensagem(List<String> mensagem) {
		this.mensagem = mensagem;
	}
	
	/**
	 * Retorna verdadeiro se o texto de alguma das mensagens contida neste {@code RetornoMensagem}
	 * for compatível com a expressão regular contida em {@code padrao}.
	 * @param padrao Uma expressão regular a ser buscada nas mensagens.
	 * @return {@code true}, se alguma das mensagens existentes for compatível com {@code padrao},
	 * 		{@code false} se não existirem mensagens ou se nenhuma delas for compatível.
	 * @see Pattern
	 * @throws NullPointerException caso a mensagem deste {@code RetornoMensagem} for {@code null};
	 * @throws PatternSyntaxException se {@code padrao} não for uma expressão
	 * 		regular válida.
	 * @author JCJ
	 * @since 1.4, 2017-05-29
	 */
	public boolean buscaPadraoMensagem(String padrao){
		if (mensagem == null){
			throw new NullPointerException("Campo mensagem não foi inicializado.");
		}
		
		Pattern pattern = Pattern.compile(padrao);				
		for (String s : mensagem){
			if(pattern.matcher(s).find()){
				return true;
			}
		}
		
		return false;
	}
		
}
