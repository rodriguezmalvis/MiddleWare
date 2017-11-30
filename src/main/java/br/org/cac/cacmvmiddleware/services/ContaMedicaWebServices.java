package br.org.cac.cacmvmiddleware.services;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.fasterxml.jackson.core.JsonProcessingException;

import br.org.cac.cacmvmiddleware.dao.LogMiddlewareDAO;
import br.org.cac.cacmvmiddleware.modelo.RespostaMiddleware;
import br.org.cac.cacmvmiddleware.util.WebServiceFactory;

/**<p><strong>ContaMedicaWebServices</strong> é uma classe de controle para os WebServices do tipo ContaMedica</p>
 * @author grr
 * @version 1.0
 * @since 2017-01-30
 * 
 * <p>O Webservice funciona como um Middleware, servindo de ponte para fazer requisições ao Webservice de ContaMedica do Soul MV</p>
 * <p>As operações do Webservice ContaMedicaWS recebem seus parametros, fazem uma requisição no Webservice Soul Mv e retornam um Json como resposta</p>
 * <p>o Webservice <strong>ContaMedicaWS</strong> possui as seguintes operações:</p>
 * <ul>
 * 	<li><strong>incluir</strong> - Inclui uma ContaMedica</li>
 * </ul>
 */

@Path("ContaMedicaWS")
public class ContaMedicaWebServices {
	
	public static final String MV_WS_PORT = System.getProperty("MV_WS_PORT");
	String server = "http://10.10.20.4:"+MV_WS_PORT+"/";
	String mvPath = "mvservices/contaMedica/";
	
	@Inject
	LogMiddlewareDAO dao;
	
	WebServiceFactory wsf = new WebServiceFactory();
	RespostaMiddleware respostaMiddleware = new RespostaMiddleware();
	
	/**<p>Incluir as Informações de uma ContaMedica:</p>
	 *	Link: {http://10.10.20.32:8080/CacMvMiddleware/ContaMedicaWS/incluir}
	 * <p>Serviço PUT que recebe um JSON como parametro, faz uma requisição PUT ao Serviço contaMedica/insere do Soul MV e retorna um JSON como resposta</p>
	 * 
	 * @param contaMedica String - Json com dados da ContaMedica a ser Incluida
	 * 
	 * @return Response Response - no Padrão HTTP que contem um JSON com as informações da Resposta do Soul MV
	 * <p>O JSON retornado possui o Statuscode da Operação, as informações da Inclusão, e ID da ContaMedica Incluida</p>
	 * @throws JsonProcessingException 
	 * 
	 * @throws NotFoundException Soul MV fora do Ar.
	 * 
	 * @throws Exception Soul Mv Retornou com Erro.
	 * 
	 * <p><strong>Exemplos:</strong></p>
	 * <p>ContaMedica Incluida</p>
	 * <p></p> 
	 * 
	 * <p>Soul MV Fora do Ar</p>
	 * <p>{"entidadeId":1,"status":404,"mensagem":"Webservice Soul Mv |http://10.10.20.4:8789/mvservices/contaMedica/| Status: Offline"}</p>
	 */

	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("incluir")
	public Response incluir(String contaMedica) throws JsonProcessingException {
		String respostaRecebidaMv = null;
		String path = mvPath + "insere";
		try {
			respostaRecebidaMv = wsf.criaRequisicaoIncluir(path, server, contaMedica);
			wsf.criaWsLog(dao, server+path, 17, contaMedica, respostaRecebidaMv, Response.Status.OK.getStatusCode(), "");
		}
		catch (Exception e) {
			e.printStackTrace();
			respostaMiddleware = wsf.respostaMiddlewareFactory(dao, e, server+path);
			wsf.criaWsLog(dao, server+path, 17, "", respostaMiddleware.getMensagem(), respostaMiddleware.getStatus(), e.getMessage());
			return Response.status(respostaMiddleware.getStatusDescricao()).entity(wsf.respostaMiddlewareToJson(respostaMiddleware)).build();
		}
		return Response.ok(respostaRecebidaMv, MediaType.APPLICATION_JSON).build();
	}
}
