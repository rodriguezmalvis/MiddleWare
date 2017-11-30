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

/**<p><strong>TitulosReceberWebServices</strong> é uma classe de controle para os WebServices do tipo TitulosReceber</p>
 * @author grr
 * @version 1.0
 * @since 2017-02-13
 * 
 * <p>O Webservice funciona como um Middleware, servindo de ponte para fazer requisições ao Webservice de MensContrato do Soul MV</p>
 * <p>As operações do Webservice TitulosReceberWS recebem seus parametros, fazem uma requisição no Webservice Soul Mv e retornam um Json como resposta</p>
 * <p>o Webservice <strong>TitulosReceberWS</strong> possui as seguintes operações:</p>
 * <ul>
 * 	<li><strong>cancelaTitulosReceber</strong> - Cancela um TituloReceber</li>
 * 	<li><strong>getTitulosReceber</strong> - Recupera um TituloReceber</li>
 * 	<li><strong>incluir</strong> - Inclui um TituloReceber</li>
 * </ul>
 */

@Path("TitulosReceberWS")
public class TitulosReceberWebServices {
	
	public static final String MV_WS_PORT = System.getProperty("MV_WS_PORT");
	String server = "http://10.10.20.4:"+MV_WS_PORT+"/";
	String mvPath = "mvservices/menscontrato/";
	
	@Inject
	LogMiddlewareDAO dao;
	
	WebServiceFactory wsf = new WebServiceFactory();
	RespostaMiddleware respostaMiddleware = new RespostaMiddleware();
	
	/**<p>Recuperar as Informações de um TitulosReceber:</p>
	 *	Link: {@link http://10.10.20.32:8080/CacMvMiddleware/TitulosReceberWS/recuperar/{id}}
	 * <p>Serviço GET que recebe um id como parametro, faz uma requisição GET ao Serviço menscontrato/get do Soul MV e retorna um JSON como resposta</p>
	 * 
	 * @param id_tituloReceber Integer - id do TitulosReceber a ser Buscado
	 * 
	 * @return Response Response - no Padrão HTTP que contem um JSON com as informações da Resposta do Soul MV
	 * <p>O JSON retornado possui o Statuscode da Operação e as informações de TitulosReceber</p>
	 * 
	 * @throws NotFoundException Soul MV fora do Ar.
	 * 
	 * @throws Exception Soul Mv Retornou com Erro.
	 * 
	 * <p><strong>Exemplos:</strong></p>
	 * <p>TitulosReceber Não Encontrado</p>
	 * <p>{"entidadeId":0,"status":500,"mensagem":null}</p>
	 * 
	 * <p>TitulosReceber Encontrado</p>
	 * <p></p> 
	 * 
	 * <p>Soul MV Fora do Ar</p>
	 * <p>{"entidadeId":1,"status":404,"mensagem":"Webservice Soul Mv |http://10.10.20.4:8789/mvservices/menscontrato/| Status: Offline"}</p>
	 */

	@GET
	@Path("recuperar/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getTitulosReceber(@PathParam("id") Integer id_tituloReceber) {
		String respostaRecebidaMv = null;
		String path = mvPath + "get/" + id_tituloReceber;
		try {
			respostaRecebidaMv = wsf.criaRequisicaoGet(path, server);
			wsf.criaWsLog(dao, server+path, 26, "", respostaRecebidaMv, Response.Status.OK.getStatusCode(), "");
		}
		catch (Exception e) {
			e.printStackTrace();
			respostaMiddleware = wsf.respostaMiddlewareFactory(dao, e, server+path);
			wsf.criaWsLog(dao, server+path, 41, "", respostaMiddleware.getMensagem(), respostaMiddleware.getStatus(), e.getMessage());
			return Response.status(respostaMiddleware.getStatusDescricao()).entity(wsf.respostaMiddlewareToJson(respostaMiddleware)).build();
		}
		return Response.ok(respostaRecebidaMv, MediaType.APPLICATION_JSON).build();
	}
	
	/**<p>Cancelar um TitulosReceber:</p>
	 *	Link: <a href="http://10.10.20.32:8080/CacMvMiddleware/TitulosReceberWS/cancelaTitulosReceber">
	 * 	http://10.10.20.32:8080/CacMvMiddleware/TitulosReceberWS/cancelaTitulosReceber</a>
	 * <p>Serviço POST que recebe um JSON como parâmetro, faz uma requisição POST ao Serviço menscontrato/cancela do Soul MV e retorna um JSON como resposta.</p>
	 * 
	 * @param jsonCancelamentoTituloReceber String - Json com os dados do TituloReceber a ser Cancelado
	 * 
	 * @return Response Response - no Padrão HTTP que contem um JSON com as informações da Resposta do Soul MV
	 * <p>O JSON retornado possui o Statuscode da Operação e as informações de cancelamento</p>
	 * 
	 * @throws NotFoundException Soul MV fora do Ar.
	 * 
	 * @throws Exception Soul Mv Retornou com Erro.
	 * 
	 * <p><strong>Exemplos:</strong></p>
	 * <p>TitulosReceber Não Encontrado</p>
	 * <p></p>
	 * 
	 * <p>TitulosReceber Encontrado</p>
	 * <p></p> 
	 * 
	 * <p>Soul MV Fora do Ar</p>
	 * <p>{"entidadeId":1,"status":404,"mensagem":"Webservice Soul Mv |http://10.10.20.4:8789/mvservices/menscontrato/| Status: Offline"}</p>
	 */
	
	@POST
	@Path("cancelaTitulosReceber")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response cancelaTitulosReceber(String jsonCancelamentoTituloReceber) {
		String respostaRecebidaMv = null;
		String path = mvPath + "cancela";
		try {
			respostaRecebidaMv = wsf.criaRequisicaoAlterar(path, server, jsonCancelamentoTituloReceber);
			wsf.criaWsLog(dao, server+path, 30, jsonCancelamentoTituloReceber, respostaRecebidaMv, Response.Status.OK.getStatusCode(), "");
		}
		catch (Exception e) {
			e.printStackTrace();
			respostaMiddleware = wsf.respostaMiddlewareFactory(dao, e, server+path);
			wsf.criaWsLog(dao, server+path, 30, jsonCancelamentoTituloReceber, respostaMiddleware.getMensagem(), respostaMiddleware.getStatus(), e.getMessage());
			return Response.status(respostaMiddleware.getStatusDescricao()).entity(wsf.respostaMiddlewareToJson(respostaMiddleware)).build();
		}
		return Response.ok(respostaRecebidaMv, MediaType.APPLICATION_JSON).build();
	}

	/**<p>Incluir as Informações de um TitulosReceber:</p>
	 *	Link: {@link http://10.10.20.32:8080/CacMvMiddleware/TitulosReceberWS/incluir}
	 * <p>Serviço PUT que recebe um JSON como parametro, faz uma requisição PUT ao Serviço menscontrato/insere do Soul MV e retorna um JSON como resposta</p>
	 * 
	 * @param tituloReceber String - Json com dados do TitulosReceber a ser Incluido
	 * 
	 * @return Response Response - no Padrão HTTP que contem um JSON com as informações da Resposta do Soul MV
	 * <p>O JSON retornado possui o Statuscode da Operação, as informações da Inclusão, e ID do TitulosReceber Incluido</p>
	 * 
	 * @throws NotFoundException Soul MV fora do Ar.
	 * 
	 * @throws Exception Soul Mv Retornou com Erro.
	 * 
	 * <p><strong>Exemplos:</strong></p>
	 * <p>TitulosReceber Incluido</p>
	 * <p></p> 
	 * 
	 * <p>Soul MV Fora do Ar</p>
	 * <p>{"entidadeId":1,"status":404,"mensagem":"Webservice Soul Mv |http://10.10.20.4:8789/mvservices/menscontrato/| Status: Offline"}</p>
	 */
	
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("incluir")
	public Response incluir(String tituloReceber) {
		String respostaRecebidaMv = null;
		String path = mvPath + "insere";
		try {
			respostaRecebidaMv = wsf.criaRequisicaoIncluir(path, server, tituloReceber);
			wsf.criaWsLog(dao, server+path, 20, tituloReceber, respostaRecebidaMv, Response.Status.OK.getStatusCode(), "");
		}
		catch (Exception e) {
			e.printStackTrace();
			respostaMiddleware = wsf.respostaMiddlewareFactory(dao, e, server+path);
			wsf.criaWsLog(dao, server+path, 20, tituloReceber, respostaMiddleware.getMensagem(), respostaMiddleware.getStatus(), e.getMessage());
			return Response.status(respostaMiddleware.getStatusDescricao()).entity(wsf.respostaMiddlewareToJson(respostaMiddleware)).build();
		}
		return Response.ok(respostaRecebidaMv, MediaType.APPLICATION_JSON).build();
	}
	
	/**<p>Realizar Baixa das Informações de um TitulosReceber:</p>
	 *	Link: {@link http://10.10.20.32:8080/CacMvMiddleware/TitulosReceberWS/baixa}
	 * <p>Serviço POST que recebe um JSON como parametro, faz uma requisição POST ao Serviço menscontrato/baixa do Soul MV e retorna um JSON como resposta</p>
	 * 
	 * @param reembolso String - Json com dados do TitulosReceber a ter Baixa
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
	 * <p>{"entidadeId":1,"status":404,"mensagem":"Webservice Soul Mv |http://10.10.20.4:8789/mvservices/menscontrato/| Status: Offline"}</p>
	 */
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("baixa")
	public Response baixa(String tituloReceber) {
		String respostaRecebidaMv = null;
		String path = mvPath + "baixa";
		try {
			respostaRecebidaMv = wsf.criaRequisicaoAlterar(path, server, tituloReceber);
			wsf.criaWsLog(dao, server+path, 43, tituloReceber, respostaRecebidaMv, Response.Status.OK.getStatusCode(), "");
		}
		catch (Exception e) {
			e.printStackTrace();
			respostaMiddleware = wsf.respostaMiddlewareFactory(dao, e, server+path);
			wsf.criaWsLog(dao, server+path, 43, tituloReceber, respostaMiddleware.getMensagem(), respostaMiddleware.getStatus(), e.getMessage());
			return Response.status(respostaMiddleware.getStatusDescricao()).entity(wsf.respostaMiddlewareToJson(respostaMiddleware)).build();
		}
		return Response.ok(respostaRecebidaMv, MediaType.APPLICATION_JSON).build();
	}

	/**<p>Recuperar as Informações da Situacao de um Titulo:</p>
	 *	Link: {@link http://10.10.20.32:8080/CacMvMiddleware/TitulosReceberWS/situacaoTitulo/{id}}
	 * <p>Serviço GET que recebe um id como parametro, faz uma requisição GET ao Serviço menscontrato/situacaoTitulo do Soul MV e retorna um JSON como resposta</p>
	 * 
	 * @param id_tituloReceber Integer - id do Titulo a ser Buscado
	 * 
	 * @return Response Response - no Padrão HTTP que contem um JSON com as informações da Resposta do Soul MV
	 * <p>O JSON retornado possui o Statuscode da Operação e as informações da Situacao do Titulo</p>
	 * 
	 * @throws NotFoundException Soul MV fora do Ar.
	 * 
	 * @throws Exception Soul Mv Retornou com Erro.
	 * 
	 * <p><strong>Exemplos:</strong></p>
	 * <p>TitulosReceber Não Encontrado</p>
	 * <p>{"entidadeId":0,"status":500,"mensagem":null}</p>
	 * 
	 * <p>TitulosReceber Encontrado</p>
	 * <p></p> 
	 * 
	 * <p>Soul MV Fora do Ar</p>
	 * <p>{"entidadeId":1,"status":404,"mensagem":"Webservice Soul Mv |http://10.10.20.4:8789/mvservices/menscontrato/| Status: Offline"}</p>
	 */

	@GET
	@Path("situacaoTitulo/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getSituacaoTitulos(@PathParam("id") Integer id_tituloReceber) {
		String respostaRecebidaMv = null;
		String path = mvPath + "situacaoTitulo/" + id_tituloReceber;
		try {
			respostaRecebidaMv = wsf.criaRequisicaoGet(path, server);
			wsf.criaWsLog(dao, server+path, 46, "", respostaRecebidaMv, Response.Status.OK.getStatusCode(), "");
		}
		catch (Exception e) {
			e.printStackTrace();
			respostaMiddleware = wsf.respostaMiddlewareFactory(dao, e, server+path);
			wsf.criaWsLog(dao, server+path, 46, "", respostaMiddleware.getMensagem(), respostaMiddleware.getStatus(), e.getMessage());
			return Response.status(respostaMiddleware.getStatusDescricao()).entity(wsf.respostaMiddlewareToJson(respostaMiddleware)).build();
		}
		return Response.ok(respostaRecebidaMv, MediaType.APPLICATION_JSON).build();
	}

}
