package br.org.cac.cacmvmiddleware.util;

import java.util.Date;

import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.jboss.logging.Logger;
import org.jboss.logging.Logger.Level;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.org.cac.cacmvmiddleware.dao.LogMiddlewareDAO;
import br.org.cac.cacmvmiddleware.modelo.LogMiddleware;
import br.org.cac.cacmvmiddleware.modelo.RespostaMiddleware;

public class WebServiceFactory {

	private static final Logger LOGGER = Logger.getLogger(WebServiceFactory.class.getName());
	private Mail mail = new Mail();

	public String criaRequisicaoGet(String path, String server) throws Exception {
		String resposta;
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target(server);
		resposta = target.path(path).request().get(String.class);
		return resposta;
	}

	public String criaRequisicaoDelete(String path, String server) throws Exception {
		String resposta;
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target(server);
		resposta = target.path(path).request().delete(String.class);
		return resposta;
	}

	public String criaRequisicaoIncluir(String path, String server, String json) throws Exception {
		String respostaMvJson;
		Response respostaMvResponse;
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target(server);
		respostaMvResponse = target.path(path).request().put(Entity.entity(json, MediaType.APPLICATION_JSON));

		if (respostaMvResponse.getStatus() == 404) {
			throw new NotFoundException("HTTP 404 Not Found");
		}
		respostaMvJson = respostaMvResponse.readEntity(String.class);
		return respostaMvJson;
	}

	public String criaRequisicaoAlterar(String path, String server, String json) throws Exception {
		String respostaMvJson;
		Response respostaMvResponse;
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target(server);
		respostaMvResponse = target.path(path).request().post(Entity.entity(json, MediaType.APPLICATION_JSON));
		if (respostaMvResponse.getStatus() == 404) {
			throw new NotFoundException("HTTP 404 Not Found");
		}
		respostaMvJson = respostaMvResponse.readEntity(String.class);
		return respostaMvJson;
	}

	public void criaWsLog(LogMiddlewareDAO dao, String serverPath, Integer codWebservice, String menssagemEnviadaParaMv,
			String respostaRecebidaMv, Integer statusCode, String statusCodeDescricao) {

		LogMiddleware log = new LogMiddleware();
		try {
			log.setDate(new Date());
			log.setPath(serverPath);
			log.setLogWebservice(dao.getWebservice(codWebservice));
			log.setMensagem(menssagemEnviadaParaMv);
			log.setStatusCode(statusCode);
			log.setStatusCodeDescricao(statusCodeDescricao);
			log.setResposta(respostaRecebidaMv);
			LOGGER.log(Level.INFO, log.toString());
			dao.incluiLog(log);
		} catch (Exception e) {
			LOGGER.log(Level.ERROR, e.toString(), e);
		}
	}

	public RespostaMiddleware respostaMiddlewareFactory(LogMiddlewareDAO dao, Exception e, String serverPath) {
		RespostaMiddleware respostaMiddleware = new RespostaMiddleware();
		String resposta;
		Status status;
		Integer statusCode;
		try {
			if (e.getClass().isInstance(NotFoundException.class) || e.getMessage().equals("HTTP 404 Not Found")) {
				resposta = "Webservice Soul Mv - Status: Não Encontrado";
				status = Response.Status.NOT_FOUND;
				statusCode = Response.Status.NOT_FOUND.getStatusCode();
			} else if (e.getClass().toString().equals(ProcessingException.class.toString())) {
				resposta = "Servidor Soul Mv - Status: Offline";
				status = Response.Status.NOT_FOUND;
				statusCode = Response.Status.NOT_FOUND.getStatusCode();
				
				Date ultimoLog404 = dao.getUltimoLog404().getDate();
				if ( (ultimoLog404 == null) || (Util.isMaior20Minutos(ultimoLog404)) ) {
					mail.enviarEmail("Ocorreu uma tentativa de acesso ao Webservice "+serverPath+" do SOUL às "+Util.formataData(new Date())+" , porém ele aparenta estar offline no momento.");
				}
			} else if (e.getClass().isInstance(InternalServerErrorException.class)) {
				resposta = "Erro Interno SoulMV";
				status = Response.Status.INTERNAL_SERVER_ERROR;
				statusCode = Response.Status.INTERNAL_SERVER_ERROR.getStatusCode();
			} else {
				resposta = e.getMessage();
				status = Response.Status.INTERNAL_SERVER_ERROR;
				statusCode = Response.Status.INTERNAL_SERVER_ERROR.getStatusCode();
			}
			respostaMiddleware.setStatusDescricao(status);
			respostaMiddleware.setStatus(statusCode);
			respostaMiddleware.setMensagem(resposta);
			return respostaMiddleware;
		} catch (Exception ex) {
			ex.printStackTrace();
			resposta = "Erro Interno CaCMiddleware";
			status = Response.Status.INTERNAL_SERVER_ERROR;
			statusCode = Response.Status.INTERNAL_SERVER_ERROR.getStatusCode();
			respostaMiddleware.setStatusDescricao(status);
			respostaMiddleware.setStatus(statusCode);
			respostaMiddleware.setMensagem(resposta);
			return respostaMiddleware;
		}
	}	

	public String respostaMiddlewareToJson(RespostaMiddleware respostaMiddleware) {
		ObjectMapper mapper = new ObjectMapper();
		String respostaMiddlewareJson = null;
		try {
			respostaMiddlewareJson = mapper.writeValueAsString(respostaMiddleware);
		} catch (JsonProcessingException e) {
			LOGGER.log(Level.ERROR, e.toString(), e);
		}
		return respostaMiddlewareJson;
	}
}