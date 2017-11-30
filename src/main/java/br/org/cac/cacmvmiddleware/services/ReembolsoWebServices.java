package br.org.cac.cacmvmiddleware.services;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import br.org.cac.cacmvmiddleware.dao.LogMiddlewareDAO;
import br.org.cac.cacmvmiddleware.modelo.RespostaMiddleware;
import br.org.cac.cacmvmiddleware.util.WebServiceFactory;

/**<p><strong>ReembolsoWebServices</strong> é uma classe de controle para os WebServices do tipo Reembolso</p>
 * @author grr
 * @version 1.0
 * @since 2017-03-21
 * 
 * <p>O Webservice funciona como um Middleware, servindo de ponte para fazer requisições ao Webservice de Reembolso do Soul MV</p>
 * <p>As operações do Webservice ReembolsoWS recebem seus parametros, fazem uma requisição no Webservice Soul Mv e retornam um Json como resposta</p>
 * <p>o Webservice <strong>ReembolsoWS</strong> possui as seguintes operações:</p>
 * <ul>
 * 	<li><strong>getReembolso</strong> - Recupera as Informações de um Reembolso</li>
 * 	<li><strong>incluir</strong> - Inclui um Reembolso</li>
 * </ul>
 */

@Path("ReembolsoWS")
public class ReembolsoWebServices {
	
	public static final String MV_WS_PORT = System.getProperty("MV_WS_PORT");
	String server = "http://10.10.20.4:"+MV_WS_PORT+"/";
	String mvPath = "mvservices/reembolso/";
	
	@Inject
	LogMiddlewareDAO dao;
	
	WebServiceFactory wsf = new WebServiceFactory();
	RespostaMiddleware respostaMiddleware = new RespostaMiddleware();

	/**<p>Recuperar as Informações de um Reembolso:</p>
	 *	Link: {@link http://10.10.20.32:8080/CacMvMiddleware/ReembolsoWS/recuperar/{id}}
	 * <p>Serviço GET que recebe um id como parametro, faz uma requisição GET ao Serviço reembolso/get do Soul MV e retorna um JSON como resposta</p>
	 * 
	 * @param id_reembolso Integer - id do Reembolso a ser Buscado
	 * 
	 * @return Response Response - no Padrão HTTP que contem um JSON com as informações da Resposta do Soul MV
	 * <p>O JSON retornado possui o Statuscode da Operação e as informações de Reembolso</p>
	 * 
	 * @throws NotFoundException Soul MV fora do Ar.
	 * 
	 * @throws Exception Soul Mv Retornou com Erro.
	 * 
	 * <p><strong>Exemplos:</strong></p>
	 * <p>Reembolso Não Encontrado</p>
	 * <p>{"entidadeId":0,"status":500,"mensagem":"Reembolso: 1 não encontrado!"}</p>
	 * 
	 * <p>Reembolso Encontrado</p>
	 * <p></p> 
	 * 
	 * <p>Soul MV Fora do Ar</p>
	 * <p>{"entidadeId":1,"status":404,"mensagem":"Webservice Soul Mv |http://10.10.20.4:8789/mvservices/reembolso/| Status: Offline"}</p>
	 */

	@GET
	@Path("recuperar/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getReembolso(@PathParam("id") Integer id_reembolso) {
		String respostaRecebidaMv = null;
		String path = mvPath + "get/" + id_reembolso;
		try {
			respostaRecebidaMv = wsf.criaRequisicaoGet(path, server);
			wsf.criaWsLog(dao, server+path, 22, "", respostaRecebidaMv, Response.Status.OK.getStatusCode(), "");
		}
		catch (Exception e) {
			e.printStackTrace();
			respostaMiddleware = wsf.respostaMiddlewareFactory(dao, e, server+path);
			wsf.criaWsLog(dao, server+path, 22, "", respostaMiddleware.getMensagem(), respostaMiddleware.getStatus(), e.getMessage());
			return Response.status(respostaMiddleware.getStatusDescricao()).entity(wsf.respostaMiddlewareToJson(respostaMiddleware)).build();
		}
		return Response.ok(respostaRecebidaMv, MediaType.APPLICATION_JSON).build();
	}
	
	/**<p>Incluir as Informações de um Reembolso:</p>
	 *	Link: {@link http://10.10.20.32:8080/CacMvMiddleware/ReembolsoWS/incluir}
	 * <p>Serviço PUT que recebe um JSON como parametro, faz uma requisição PUT ao Serviço reembolso/insere do Soul MV e retorna um JSON como resposta</p>
	 * 
	 * @param reembolso String - Json com dados do Reembolso a ser Incluido
	 * 
	 * @return Response Response - no Padrão HTTP que contem um JSON com as informações da Resposta do Soul MV
	 * <p>O JSON retornado possui o Statuscode da Operação, as informações da Inclusão, e ID do Reembolso Incluido</p>
	 * 
	 * @throws NotFoundException Soul MV fora do Ar.
	 * 
	 * @throws Exception Soul Mv Retornou com Erro.
	 * 
	 * <p><strong>Exemplos:</strong></p>
	 * <p>Reembolso Incluido</p>
	 * <p></p> 
	 * 
	 * <p>Soul MV Fora do Ar</p>
	 * <p>{"entidadeId":1,"status":404,"mensagem":"Webservice Soul Mv |http://10.10.20.4:8789/mvservices/reembolso/| Status: Offline"}</p>
	 */

	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("incluir")
	public Response incluir(String reembolso) {
		String respostaRecebidaMv = null;
		String path = mvPath + "insere";
		try {
			respostaRecebidaMv = wsf.criaRequisicaoIncluir(path, server, reembolso);
			wsf.criaWsLog(dao, server+path, 23, reembolso, respostaRecebidaMv, Response.Status.OK.getStatusCode(), "");
		}
		catch (Exception e) {
			e.printStackTrace();
			respostaMiddleware = wsf.respostaMiddlewareFactory(dao, e, server+path);
			wsf.criaWsLog(dao, server+path, 23, reembolso, respostaMiddleware.getMensagem(), respostaMiddleware.getStatus(), e.getMessage());
			return Response.status(respostaMiddleware.getStatusDescricao()).entity(wsf.respostaMiddlewareToJson(respostaMiddleware)).build();
		}
		return Response.ok(respostaRecebidaMv, MediaType.APPLICATION_JSON).build();
	}
	
	/**<p>Cancelar as Informações de um Reembolso:</p>
	 *	Link: {@link http://10.10.20.32:8080/CacMvMiddleware/ReembolsoWS/cancelar}
	 * <p>Serviço POST que recebe um JSON como parametro, faz uma requisição POST ao Serviço reembolso/cancela do Soul MV e retorna um JSON como resposta</p>
	 * 
	 * @param reembolso String - Json com dados do Reembolso a ser Cancelado
	 * 
	 * @return Response Response - no Padrão HTTP que contem um JSON com as informações da Resposta do Soul MV
	 * <p>O JSON retornado possui o Statuscode da Operação, as informações do Cancelamento, e ID do Reembolso Cancelado</p>
	 * 
	 * @throws NotFoundException Soul MV fora do Ar.
	 * 
	 * @throws Exception Soul Mv Retornou com Erro.
	 * 
	 * <p><strong>Exemplos:</strong></p>
	 * <p>Reembolso Cancelado</p>
	 * <p></p> 
	 * 
	 * <p>Soul MV Fora do Ar</p>
	 * <p>{"entidadeId":1,"status":404,"mensagem":"Webservice Soul Mv |http://10.10.20.4:8789/mvservices/reembolso/| Status: Offline"}</p>
	 */

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("cancelar")
	public Response cancelar(String reembolso) {
		String respostaRecebidaMv = null;
		String path = mvPath + "cancela";
		try {
			respostaRecebidaMv = wsf.criaRequisicaoAlterar(path, server, reembolso);
			wsf.criaWsLog(dao, server+path, 38, reembolso, respostaRecebidaMv, Response.Status.OK.getStatusCode(), "");
		}
		catch (Exception e) {
			e.printStackTrace();
			respostaMiddleware = wsf.respostaMiddlewareFactory(dao, e, server+path);
			wsf.criaWsLog(dao, server+path, 38, reembolso, respostaMiddleware.getMensagem(), respostaMiddleware.getStatus(), e.getMessage());
			return Response.status(respostaMiddleware.getStatusDescricao()).entity(wsf.respostaMiddlewareToJson(respostaMiddleware)).build();
		}
		return Response.ok(respostaRecebidaMv, MediaType.APPLICATION_JSON).build();
	}
	
	/**<p>Realizar Baixa das Informações de um Reembolso:</p>
	 *	Link: {@link http://10.10.20.32:8080/CacMvMiddleware/ReembolsoWS/baixa}
	 * <p>Serviço POST que recebe um JSON como parametro, faz uma requisição POST ao Serviço reembolso/baixa do Soul MV e retorna um JSON como resposta</p>
	 * 
	 * @param reembolso String - Json com dados do Reembolso a ter Baixa
	 * 
	 * @return Response Response - no Padrão HTTP que contem um JSON com as informações da Resposta do Soul MV
	 * <p>O JSON retornado possui o Statuscode da Operação, as informações da Baixa</p>
	 * 
	 * @throws NotFoundException Soul MV fora do Ar.
	 * 
	 * @throws Exception Soul Mv Retornou com Erro.
	 * 
	 * <p><strong>Exemplos:</strong></p>
	 * <p>Baixa Realizada</p>
	 * <p></p> 
	 * 
	 * <p>Soul MV Fora do Ar</p>
	 * <p>{"entidadeId":1,"status":404,"mensagem":"Webservice Soul Mv |http://10.10.20.4:8789/mvservices/reembolso/| Status: Offline"}</p>
	 */
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("baixa")
	public Response baixa(String reembolso) {
		String respostaRecebidaMv = null;
		String path = mvPath + "baixa";
		try {
			respostaRecebidaMv = wsf.criaRequisicaoAlterar(path, server, reembolso);
			wsf.criaWsLog(dao, server+path, 42, reembolso, respostaRecebidaMv, Response.Status.OK.getStatusCode(), "");
		}
		catch (Exception e) {
			e.printStackTrace();
			respostaMiddleware = wsf.respostaMiddlewareFactory(dao, e, server+path);
			wsf.criaWsLog(dao, server+path, 42, reembolso, respostaMiddleware.getMensagem(), respostaMiddleware.getStatus(), e.getMessage());
			return Response.status(respostaMiddleware.getStatusDescricao()).entity(wsf.respostaMiddlewareToJson(respostaMiddleware)).build();
		}
		return Response.ok(respostaRecebidaMv, MediaType.APPLICATION_JSON).build();
	}


}