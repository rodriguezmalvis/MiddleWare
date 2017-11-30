package br.org.cac.cacmvmiddleware.services;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
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

/**<p><strong>PrestadorEspecialidadeWebServices</strong> é uma classe de controle para os WebServices do tipo PrestadorEspecialidade</p>
 * @author grr
 * @version 1.0
 * @since 2017-01-30
 * 
 * <p>O Webservice funciona como um Middleware, servindo de ponte para fazer requisições ao Webservice de PrestadorEspecialidade do Soul MV</p>
 * <p>As operações do Webservice PrestadorEspecialidadeWS recebem seus parametros, fazem uma requisição no Webservice Soul Mv e retornam um Json como resposta</p>
 * <p>o Webservice <strong>PrestadorEspecialidadeWS</strong> possui as seguintes operações:</p>
 * <ul>
 * 	<li><strong>getprestadorEspecialidade</strong> - Recupera as Informações de um PrestadorEspecialidade</li>
 * 	<li><strong>delete</strong> - Exclui um PrestadorEspecialidade</li>
 * 	<li><strong>incluir</strong> - Inclui um PrestadorEspecialidade</li>
 * 	<li><strong>alterar</strong> - Altera as informações de um PrestadorEspecialidade</li>
 * </ul>
 */

@Path("PrestadorEspecialidadeWS")
public class PrestadorEspecialidadeWebServices {
	
	public static final String MV_WS_PORT = System.getProperty("MV_WS_PORT");
	String server = "http://10.10.20.4:"+MV_WS_PORT+"/";
	String mvPath = "mvservices/prestadorEspecialidade/";
	
	@Inject
	LogMiddlewareDAO dao;
	
	WebServiceFactory wsf = new WebServiceFactory();
	RespostaMiddleware respostaMiddleware = new RespostaMiddleware();

	/**<p>Recuperar as Informações de um PrestadorEspecialidade:</p>
	 *	Link: {@link http://10.10.20.32:8080/CacMvMiddleware/PrestadorEspecialidadeWS/recuperar/{id}}
	 * <p>Serviço GET que recebe um id como parametro, faz uma requisição GET ao Serviço prestadorEspecialidade/get do Soul MV e retorna um JSON como resposta</p>
	 * 
	 * @param id_prestadorEspecialidade Integer - id do PrestadorEspecialidade a ser Buscado
	 * 
	 * @return Response Response - no Padrão HTTP que contem um JSON com as informações da Resposta do Soul MV
	 * <p>O JSON retornado possui o Statuscode da Operação e as informações de PrestadorEspecialidade</p>
	 * 
	 * @throws NotFoundException Soul MV fora do Ar.
	 * 
	 * @throws Exception Soul Mv Retornou com Erro.
	 * 
	 * <p><strong>Exemplos:</strong></p>
	 * <p>PrestadorEspecialidade Não Encontrado</p>
	 * <p>{"status": 200,"PrestadorEspecialidade": null}</p>
	 * 
	 * <p>PrestadorEspecialidade Encontrado</p>
	 * <p></p> 
	 * 
	 * <p>Soul MV Fora do Ar</p>
	 * <p>{"entidadeId":1,"status":404,"mensagem":"Webservice Soul Mv |http://10.10.20.4:8789/mvservices/prestadorEspecialidade/| Status: Offline"}</p>
	 */
	
	@GET
	@Path("recuperar/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getprestadorEspecialidade(@PathParam("id") Integer id_prestadorEspecialidade) {
		String respostaRecebidaMv = null;
		String path = mvPath + "get/" + id_prestadorEspecialidade;
		try {
			respostaRecebidaMv = wsf.criaRequisicaoGet(path, server);
			wsf.criaWsLog(dao, server+path, 13, "", respostaRecebidaMv, Response.Status.OK.getStatusCode(), "");
		}
		catch (Exception e) {
			e.printStackTrace();
			respostaMiddleware = wsf.respostaMiddlewareFactory(dao, e, server+path);
			wsf.criaWsLog(dao, server+path, 13, "", respostaMiddleware.getMensagem(), respostaMiddleware.getStatus(), e.getMessage());
			return Response.status(respostaMiddleware.getStatusDescricao()).entity(wsf.respostaMiddlewareToJson(respostaMiddleware)).build();
		}
		return Response.ok(respostaRecebidaMv, MediaType.APPLICATION_JSON).build();
	}

	/**<p>Excluir as Informações de um PrestadorEspecialidade:</p>
	 *	Link: {@link http://10.10.20.32:8080/CacMvMiddleware/PrestadorEspecialidadeWS/excluir/{id}}
	 * <p>Serviço DELETE que recebe um id como parametro, faz uma requisição DELETE ao Serviço PrestadorEspecialidade/delete do Soul MV e retorna um JSON como resposta</p>
	 * 
	 * @param id_prestadorEspecialidade Integer - id do PrestadorEspecialidade a ser Excluido
	 * 
	 * @return Response Response - no Padrão HTTP que contem um JSON com as informações da Resposta do Soul MV
	 * <p>O JSON retornado possui o Statuscode da Operação, ID do PrestadorEspecialidade a ser Excluido e as informações da Exclusão</p>
	 * 
	 * @throws NotFoundException Soul MV fora do Ar.
	 * 
	 * @throws Exception Soul Mv Retornou com Erro.
	 * 
	 * <p><strong>Exemplos:</strong></p>
	 * <p>PrestadorEspecialidade Não Encontrado</p>
	 * <p>{"entidadeId": 1,"status": 500,"mensagem": "Entidade não foi encontrada para deleção."}</p>
	 * 
	 * <p>PrestadorEspecialidade Excluido</p>
	 * <p>{"entidadeId": 116450088, "status": 200, "mensagem": "PrestadorEspecialidade deletado(a) com sucesso!"}</p> 
	 * 
	 * <p>Soul MV Fora do Ar</p>
	 * <p>{"entidadeId":1,"status":404,"mensagem":"Webservice Soul Mv |http://10.10.20.4:8789/mvservices/prestadorEspecialidade/| Status: Offline"}</p>
	 */

	@DELETE
	@Path("excluir/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response delete(@PathParam("id") Integer id_prestadorEspecialidade) {
		String respostaRecebidaMv = null;
		String path = mvPath + "delete/" + id_prestadorEspecialidade;
		try {
			respostaRecebidaMv = wsf.criaRequisicaoDelete(path, server);
			wsf.criaWsLog(dao, server+path, 14, "", respostaRecebidaMv, Response.Status.OK.getStatusCode(), "");
		}
		catch (Exception e) {
			e.printStackTrace();
			respostaMiddleware = wsf.respostaMiddlewareFactory(dao, e, server+path);
			wsf.criaWsLog(dao, server+path, 14, "", respostaMiddleware.getMensagem(), respostaMiddleware.getStatus(), e.getMessage());
			return Response.status(respostaMiddleware.getStatusDescricao()).entity(wsf.respostaMiddlewareToJson(respostaMiddleware)).build();
		}
		return Response.ok(respostaRecebidaMv, MediaType.APPLICATION_JSON).build();
	}

	/**<p>Incluir as Informações de um PrestadorEspecialidade:</p>
	 *	Link: {@link http://10.10.20.32:8080/CacMvMiddleware/PrestadorEspecialidadeWS/incluir}
	 * <p>Serviço PUT que recebe um JSON como parametro, faz uma requisição PUT ao Serviço prestadorEspecialidade/insere do Soul MV e retorna um JSON como resposta</p>
	 * 
	 * @param prestador String - Json com dados do PrestadorEspecialidade a ser Incluido
	 * 
	 * @return Response Response - no Padrão HTTP que contem um JSON com as informações da Resposta do Soul MV
	 * <p>O JSON retornado possui o Statuscode da Operação, as informações da Inclusão, e ID do PrestadorEspecialidade Incluido</p>
	 * 
	 * @throws NotFoundException Soul MV fora do Ar.
	 * 
	 * @throws Exception Soul Mv Retornou com Erro.
	 * 
	 * <p><strong>Exemplos:</strong></p>
	 * <p>PrestadorEspecialidade Incluido</p>
	 * <p></p> 
	 * 
	 * <p>Soul MV Fora do Ar</p>
	 * <p>{"entidadeId":1,"status":404,"mensagem":"Webservice Soul Mv |http://10.10.20.4:8789/mvservices/prestadorEspecialidade/| Status: Offline"}</p>
	 */

	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("incluir")
	public Response incluir(String prestadorEspecialidade) {
		String respostaRecebidaMv = null;
		String path = mvPath + "insere";
		try {
			respostaRecebidaMv = wsf.criaRequisicaoIncluir(path, server, prestadorEspecialidade);
			wsf.criaWsLog(dao, server+path, 15, prestadorEspecialidade, respostaRecebidaMv, Response.Status.OK.getStatusCode(), "");
		}
		catch (Exception e) {
			e.printStackTrace();
			respostaMiddleware = wsf.respostaMiddlewareFactory(dao, e, server+path);
			wsf.criaWsLog(dao, server+path, 15, prestadorEspecialidade, respostaMiddleware.getMensagem(), respostaMiddleware.getStatus(), e.getMessage());
			return Response.status(respostaMiddleware.getStatusDescricao()).entity(wsf.respostaMiddlewareToJson(respostaMiddleware)).build();
		}
		return Response.ok(respostaRecebidaMv, MediaType.APPLICATION_JSON).build();
	}

	/**<p>Alterar as Informações de um PrestadorEspecialidade:</p>
	 *	Link: {@link http://10.10.20.32:8080/CacMvMiddleware/PrestadorEspecialidadeWS/alterar}
	 * <p>Serviço POST que recebe um JSON como parametro, faz uma requisição POST ao Serviço prestadorEspecialidade/atualiza do Soul MV e retorna um JSON como resposta</p>
	 * 
	 * @param prestador String - Json com dados do PrestadorEspecialidade a ser Alterado
	 * 
	 * @return Response Response - no Padrão HTTP que contem um JSON com as informações da Resposta do Soul MV
	 * <p>O JSON retornado possui o Statuscode da Operação, as informações da Inclusão, e ID do PrestadorEspecialidade Alterado</p>
	 * 
	 * @throws NotFoundException Soul MV fora do Ar.
	 * 
	 * @throws Exception Soul Mv Retornou com Erro.
	 * 
	 * <p><strong>Exemplos:</strong></p>
	 * <p>PrestadorEspecialidade Alterado</p>
	 * <p></p> 
	 * 
	 * <p>Soul MV Fora do Ar</p>
	 * <p>{"entidadeId":1,"status":404,"mensagem":"Webservice Soul Mv |http://10.10.20.4:8789/mvservices/prestadorEspecialidade/| Status: Offline"}</p>
	 */

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("alterar")
	public Response alterar(String prestadorEspecialidade) {
		String respostaRecebidaMv = null;
		String path = mvPath + "atualiza";
		try {
			respostaRecebidaMv = wsf.criaRequisicaoAlterar(path, server, prestadorEspecialidade);
			wsf.criaWsLog(dao, server+path, 16, prestadorEspecialidade, respostaRecebidaMv, Response.Status.OK.getStatusCode(), "");
		}
		catch (Exception e) {
			e.printStackTrace();
			respostaMiddleware = wsf.respostaMiddlewareFactory(dao, e, server+path);
			wsf.criaWsLog(dao, server+path, 16, prestadorEspecialidade, respostaMiddleware.getMensagem(), respostaMiddleware.getStatus(), e.getMessage());
			return Response.status(respostaMiddleware.getStatusDescricao()).entity(wsf.respostaMiddlewareToJson(respostaMiddleware)).build();
		}
		return Response.ok(respostaRecebidaMv, MediaType.APPLICATION_JSON).build();
	}
}
