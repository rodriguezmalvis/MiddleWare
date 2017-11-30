package br.org.cac.integrador.util;

import java.util.Arrays;
import org.jboss.logging.Logger;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.org.cac.integrador.jacksonutil.ObjectMapperFactory;
import br.org.cac.integrador.modelo.RetornoMensagem;
import br.org.cac.integrador.processadores.ResumoProcessamento;
import br.org.cac.integrador.services.IntegradorContasMedicas;

/**
 * Classe de utilidade para facilitar a interação com {@link Response}.
 * 
 * @author JCJ
 * @version 1.1
 * @since 1.1, 2017-03-28
 */
public class ResponseUtil {
	
	private static Logger logger = Logger.getLogger(ResponseUtil.class.getSimpleName());
	
	public static final String PROCESSAMENTO_CONCLUIDO = "{\"mensagem\":[\"Processamento concluído\"]}"; 
	
	/**
	 * Gera um {@link Response}, com um JSON preenchido com as informações de {@code status} e com 
	 * uma ou mais {@code mensagens}, utilizando o valor padrão 0 para {@code entidadeId}.
	 * @param status um {@link Response.Status} indicando o {@code status code} que será enviado
	 * no {@code Response}.
	 * @param mensagens Uma ou mais mensagens que serão enviadas na resposta.
	 * @return um {@code Response} HTTP com o JSON preenchido de acordo com os parâmetros
	 * @see IntegradorContasMedicas#gerarResponse(String, javax.ws.rs.core.Response.Status, String...)
	 */
	public static Response gerarResponse(Response.Status status, String... mensagens){
		return ResponseUtil.gerarResponse("0", status, mensagens);
	}
	
	
	/**
	 * Gera um {@link Response}, com um JSON preenchido com as informações de {@code status} e com 
	 * uma ou mais {@code mensagens}, utilizando o valor informado para {@code entidadeId}.
	 * 
	 * @param entidadeId uma {@code String} contendo o valor de entidadeId a ser enviado
	 * @param status um {@link Response.Status} indicando o {@code status code} que será enviado
	 * no {@code Response}.
	 * @param mensagens Uma ou mais mensagens que serão enviadas na resposta.
	 * @return um {@code Response} HTTP com o JSON preenchido de acordo com os parâmetros
	 */
	public static Response gerarResponse(String entidadeId, Response.Status status, String... mensagens) {
		ObjectMapper mapper = ObjectMapperFactory.getDefaultConfiguredObjectMapper(true);
		
		RetornoMensagem retorno = new RetornoMensagem();
		retorno.setEntidadeId(entidadeId);
		retorno.setStatus(String.valueOf(status.getStatusCode()));	
		retorno.setMensagem(Arrays.asList(mensagens));
		
		Response response;
		
		String jsonRetorno = "";
		try {
			jsonRetorno = mapper.writeValueAsString(retorno);
		} catch (JsonProcessingException e) {
			logger.warn("Não foi possível serializar corretamente RetornoMensagem para JSON. Utilizando mensagem de erro padrão.");
			jsonRetorno = "{\"mensagem\":[\"Erro na exibição dos motivos que causaram esse erro.\"]}";
		}
				
		response = Response.status(Response.Status.BAD_REQUEST)
					.entity(jsonRetorno)
					.type(MediaType.APPLICATION_JSON)
					.build();
		return response;
	}
	
	/** Gera um {@link Response} com um determinado {@code status} de resposta, contendo as informações de um
	 * {@link ResumoProcessamento}, ou uma mensagem padrão caso não seja possível utilizar este resumo. 
	 * @param responseStatus o código de status para o {@code Response} a ser gerado.
	 * @param resumoProcessamento o {@code ResumoProcessamento} com as informações a serem incluídas.
	 * @return um {@code Response} com o status informado contendo os dados do resumo, ou com uma mensagem
	 * informativa genérica.
	 * @see {@link Response.Status}
	 */
	public static Response geraResponseResumoProcessamento(Response.Status responseStatus, ResumoProcessamento resumoProcessamento) {
		ObjectMapper mapper = ObjectMapperFactory.getDefaultConfiguredObjectMapper(true);
		
		Object entity;
		
		Response response;
		try {
			if (resumoProcessamento == null) {
				entity = PROCESSAMENTO_CONCLUIDO;
				logger.warn("Recebido ResumoProcessamento null. Utilizando mensagem padrão");
			} else {
				entity = mapper.writeValueAsString(resumoProcessamento);
			}
			
			response = Response.status(responseStatus)
					.entity(entity)
					.type(MediaType.APPLICATION_JSON)
					.build();
		} catch (JsonProcessingException e) {
			logger.warn("Erro ao gerar o JSON de resposta para o processamento. Gerando mensagem padrão...");
			response = Response.status(responseStatus)
					.entity(PROCESSAMENTO_CONCLUIDO)
					.type(MediaType.APPLICATION_JSON)
					.build();
		}
		return response;
	}
}
