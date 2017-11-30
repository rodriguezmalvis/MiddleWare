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

/**<p><strong>PrestadorWebServices</strong> é uma classe de controle para os WebServices do tipo Prestador</p>
 * @author grr
 * @version 1.0
 * @since 2017-01-11
 * 
 * <p>O Webservice funciona como um Middleware, servindo de ponte para fazer requisições ao Webservice de Prestador do Soul MV</p>
 * <p>As operações do Webservice PrestadorWS recebem seus parametros, fazem uma requisição no Webservice Soul Mv e retornam um Json como resposta</p>
 * <p>o Webservice <strong>PrestadorWS</strong> possui as seguintes operações:</p>
 * <ul>
 * 	<li><strong>getprestador</strong> - Recupera as Informações de um Prestador</li>
 * 	<li><strong>delete</strong> - Exclui um Prestador</li>
 * 	<li><strong>incluir</strong> - Inclui um Prestador</li>
 * 	<li><strong>alterar</strong> - Altera as informações de um Prestador</li>
 * </ul>
 */

@Path("PrestadorWS")
public class PrestadorWebServices {
	
	public static final String MV_WS_PORT = System.getProperty("MV_WS_PORT");
	String server = "http://10.10.20.4:"+MV_WS_PORT+"/";
	String mvPath = "mvservices/prestador/";
	
	@Inject
	LogMiddlewareDAO dao;
	
	WebServiceFactory wsf = new WebServiceFactory();
	RespostaMiddleware respostaMiddleware = new RespostaMiddleware();
	
	/**<p>Recuperar as Informações de um Prestador:</p>
	 *	Link: {@link http://10.10.20.32:8080/CacMvMiddleware/PrestadorWS/recuperar/{id}}
	 * <p>Serviço GET que recebe um id como parametro, faz uma requisição GET ao Serviço prestador/get do Soul MV e retorna um JSON como resposta</p>
	 * 
	 * @param id_prestador Integer - id do Prestador a ser Buscado
	 * 
	 * @return Response Response - no Padrão HTTP que contem um JSON com as informações da Resposta do Soul MV
	 * <p>O JSON retornado possui o Statuscode da Operação e as informações de Prestador</p>
	 * 
	 * @throws NotFoundException Soul MV fora do Ar.
	 * 
	 * @throws Exception Soul Mv Retornou com Erro.
	 * 
	 * <p><strong>Exemplos:</strong></p>
	 * <p>Prestador Não Encontrado</p>
	 * <p>{"status": 200,"Prestador": null}</p>
	 * 
	 * <p>Prestador Encontrado</p>
	 * <p>{"status":200,"prestador":{"id":28,"cdMultiEmpresa":1,"nmPrestador":"PRESTADOR DE TESTE 1","tpSituacao":"A","cdInterno":"29","nmFantasia":"SAO BERNARDO APART H" ... }</p> 
	 * 
	 * <p>Soul MV Fora do Ar</p>
	 * <p>{"entidadeId":1,"status":404,"mensagem":"Webservice Soul Mv |http://10.10.20.4:8789/mvservices/prestador/| Status: Offline"}</p>
	 */

	@GET
	@Path("recuperar/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getprestador(@PathParam("id") Integer id_prestador) {
		String respostaRecebidaMv = null;
		String path = mvPath + "get/" + id_prestador;
		try {
			respostaRecebidaMv = wsf.criaRequisicaoGet(path, server);
			wsf.criaWsLog(dao, server+path, 5, "", respostaRecebidaMv, Response.Status.OK.getStatusCode(), "");
		}
		catch (Exception e) {
			e.printStackTrace();
			respostaMiddleware = wsf.respostaMiddlewareFactory(dao, e, server+path);
			wsf.criaWsLog(dao, server+path, 5, "", respostaMiddleware.getMensagem(), respostaMiddleware.getStatus(), e.getMessage());
			return Response.status(respostaMiddleware.getStatusDescricao()).entity(wsf.respostaMiddlewareToJson(respostaMiddleware)).build();
		}
		return Response.ok(respostaRecebidaMv, MediaType.APPLICATION_JSON).build();
	}
	
	/**<p>Desativa um Prestador:</p>
	 *	Link: {@link http://10.10.20.32:8080/CacMvMiddleware/PrestadorWS/desativar}
	 * <p>Serviço POST que recebe um JSON como parametro, faz uma requisição POST ao Serviço prestador/desliga do Soul MV e retorna um JSON como resposta</p>
	 * 
	 * @param prestador String - Json com dados do Prestador a ser Desativado
	 * 
	 * @return Response Response - no Padrão HTTP que contem um JSON com as informações da Resposta do Soul MV
	 * <p>O JSON retornado possui o Statuscode da Operação, as informações da Desativação, e ID do Prestador Desativado</p>
	 * 
	 * @throws NotFoundException Soul MV fora do Ar.
	 * 
	 * @throws Exception Soul Mv Retornou com Erro.
	 * 
	 * <p><strong>Exemplos:</strong></p>
	 * <p>Prestador Desativado</p>
	 * <p></p> 
	 * 
	 * <p>Soul MV Fora do Ar</p>
	 * <p>{"entidadeId":1,"status":404,"mensagem":"Webservice Soul Mv |http://10.10.20.4:8789/mvservices/prestador/| Status: Offline"}</p>
	 */

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("desativar")
	public Response desativar(String prestador) {
		String respostaRecebidaMv = null;
		String path = mvPath + "desliga";
		try {
			respostaRecebidaMv = wsf.criaRequisicaoAlterar(path, server, prestador);
			wsf.criaWsLog(dao, server+path, 18, prestador, respostaRecebidaMv, Response.Status.OK.getStatusCode(), "");
		}
		catch (Exception e) {
			e.printStackTrace();
			respostaMiddleware = wsf.respostaMiddlewareFactory(dao, e, server+path);
			wsf.criaWsLog(dao, server+path, 18, prestador, respostaMiddleware.getMensagem(), respostaMiddleware.getStatus(), e.getMessage());
			return Response.status(respostaMiddleware.getStatusDescricao()).entity(wsf.respostaMiddlewareToJson(respostaMiddleware)).build();
		}
		return Response.ok(respostaRecebidaMv, MediaType.APPLICATION_JSON).build();
	}
	
	/**<p>Ativa um Prestador:</p>
	 *	Link: {@link http://10.10.20.32:8080/CacMvMiddleware/PrestadorWS/ativar}
	 * <p>Serviço POST que recebe um JSON como parametro, faz uma requisição POST ao Serviço prestador/reativa do Soul MV e retorna um JSON como resposta</p>
	 * 
	 * @param prestador String - Json com dados do Prestador a ser Ativado
	 * 
	 * @return Response Response - no Padrão HTTP que contem um JSON com as informações da Resposta do Soul MV
	 * <p>O JSON retornado possui o Statuscode da Operação, as informações da Ativação, e ID do Prestador Ativado</p>
	 * 
	 * @throws NotFoundException Soul MV fora do Ar.
	 * 
	 * @throws Exception Soul Mv Retornou com Erro.
	 * 
	 * <p><strong>Exemplos:</strong></p>
	 * <p>Prestador Ativado</p>
	 * <p></p> 
	 * 
	 * <p>Soul MV Fora do Ar</p>
	 * <p>{"entidadeId":1,"status":404,"mensagem":"Webservice Soul Mv |http://10.10.20.4:8789/mvservices/prestador/| Status: Offline"}</p>
	 */

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("ativar")
	public Response ativar(String prestador) {
		String respostaRecebidaMv = null;
		String path = mvPath + "reativa";
		try {
			respostaRecebidaMv = wsf.criaRequisicaoAlterar(path, server, prestador);
			wsf.criaWsLog(dao, server+path, 19, prestador, respostaRecebidaMv, Response.Status.OK.getStatusCode(), "");
		}
		catch (Exception e) {
			e.printStackTrace();
			respostaMiddleware = wsf.respostaMiddlewareFactory(dao, e, server+path);
			wsf.criaWsLog(dao, server+path, 19, prestador, respostaMiddleware.getMensagem(), respostaMiddleware.getStatus(), e.getMessage());
			return Response.status(respostaMiddleware.getStatusDescricao()).entity(wsf.respostaMiddlewareToJson(respostaMiddleware)).build();
		}
		return Response.ok(respostaRecebidaMv, MediaType.APPLICATION_JSON).build();
	}

	/**<p>Incluir as Informações de um Prestador:</p>
	 *	Link: {@link http://10.10.20.32:8080/CacMvMiddleware/PrestadorWS/incluir}
	 * <p>Serviço PUT que recebe um JSON como parametro, faz uma requisição PUT ao Serviço prestador/insere do Soul MV e retorna um JSON como resposta</p>
	 * 
	 * @param prestador String - Json com dados do Prestador a ser Incluido
	 * 
	 * @return Response Response - no Padrão HTTP que contem um JSON com as informações da Resposta do Soul MV
	 * <p>O JSON retornado possui o Statuscode da Operação, as informações da Inclusão, e ID do Prestador Incluido</p>
	 * 
	 * @throws NotFoundException Soul MV fora do Ar.
	 * 
	 * @throws Exception Soul Mv Retornou com Erro.
	 * 
	 * <p><strong>Exemplos:</strong></p>
	 * <p>Prestador Incluido</p>
	 * <p>{"status":200,"mensagem":["405 - IMP_PRESTADOR(CD_INTERNO): PRESTADOR:27 INSERIDO COM SUCESSO!","405 - IMP_PRESTADOR_ENDERECO(CD_PRESTADOR): PRESTADOR ENDERECO:3 INSERIDO COM SUCESSO!","405 - IMP_PRESTADOR_ENDERECO(CD_PRESTADOR): PRESTADOR ENDERECO:3 INSERIDO COM SUCESSO!PRESTADOR ENDERECO:4 INSERIDO COM SUCESSO!","405 - IMP_ESPECIALIDADE_PRESTADOR(CD_ESPECIALIDADE_PRESTADOR): PRESTADOR :28 ESPECIALIDADE :49 INSERIDO COM SUCESSO!","405 - IMP_ESPECIALIDADE_PRESTADOR(CD_ESPECIALIDADE_PRESTADOR): PRESTADOR :28 ESPECIALIDADE :46 INSERIDO COM SUCESSO!"],"entidadeId":27}</p> 
	 * 
	 * <p>Soul MV Fora do Ar</p>
	 * <p>{"entidadeId":1,"status":404,"mensagem":"Webservice Soul Mv |http://10.10.20.4:8789/mvservices/prestador/| Status: Offline"}</p>
	 */

	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("incluir")
	public Response incluir(String prestador) {
		String respostaRecebidaMv = null;
		String path = mvPath + "insere";
		try {
			respostaRecebidaMv = wsf.criaRequisicaoIncluir(path, server, prestador);
			wsf.criaWsLog(dao, server+path, 7, prestador, respostaRecebidaMv, Response.Status.OK.getStatusCode(), "");
		}
		catch (Exception e) {
			e.printStackTrace();
			respostaMiddleware = wsf.respostaMiddlewareFactory(dao, e, server+path);
			wsf.criaWsLog(dao, server+path, 7, prestador, respostaMiddleware.getMensagem(), respostaMiddleware.getStatus(), e.getMessage());
			return Response.status(respostaMiddleware.getStatusDescricao()).entity(wsf.respostaMiddlewareToJson(respostaMiddleware)).build();
		}
		return Response.ok(respostaRecebidaMv, MediaType.APPLICATION_JSON).build();
	}

	/**<p>Alterar as Informações de um Prestador:</p>
	 *	Link: {@link http://10.10.20.32:8080/CacMvMiddleware/PrestadorWS/alterar}
	 * <p>Serviço POST que recebe um JSON como parametro, faz uma requisição POST ao Serviço prestador/atualiza do Soul MV e retorna um JSON como resposta</p>
	 * 
	 * @param prestador String - Json com dados do Prestador a ser Alterado
	 * 
	 * @return Response Response - no Padrão HTTP que contem um JSON com as informações da Resposta do Soul MV
	 * <p>O JSON retornado possui o Statuscode da Operação, as informações da Inclusão, e ID do Prestador Alterado</p>
	 * 
	 * @throws NotFoundException Soul MV fora do Ar.
	 * 
	 * @throws Exception Soul Mv Retornou com Erro.
	 * 
	 * <p><strong>Exemplos:</strong></p>
	 * <p>Prestador Alterado</p>
	 * <p>{"status":200,"mensagem":["409 - IMP_PRESTADOR(CD_PRESTADOR): PRESTADOR:28 ATUALIZADO COM SUCESSO!"],"entidadeId":28}</p> 
	 * 
	 * <p>Soul MV Fora do Ar</p>
	 * <p>{"entidadeId":1,"status":404,"mensagem":"Webservice Soul Mv |http://10.10.20.4:8789/mvservices/prestador/| Status: Offline"}</p>
	 */

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("alterar")
	public Response alterar(String prestador) {
		String respostaRecebidaMv = null;
		String path = mvPath + "atualiza";
		try {
			respostaRecebidaMv = wsf.criaRequisicaoAlterar(path, server, prestador);
			wsf.criaWsLog(dao, server+path, 8, prestador, respostaRecebidaMv, Response.Status.OK.getStatusCode(), "");
		}
		catch (Exception e) {
			e.printStackTrace();
			respostaMiddleware = wsf.respostaMiddlewareFactory(dao, e, server+path);
			wsf.criaWsLog(dao, server+path, 8, prestador, respostaMiddleware.getMensagem(), respostaMiddleware.getStatus(), e.getMessage());
			return Response.status(respostaMiddleware.getStatusDescricao()).entity(wsf.respostaMiddlewareToJson(respostaMiddleware)).build();
		}
		return Response.ok(respostaRecebidaMv, MediaType.APPLICATION_JSON).build();
	}
}