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

import com.fasterxml.jackson.core.JsonProcessingException;

import br.org.cac.cacmvmiddleware.dao.LogMiddlewareDAO;
import br.org.cac.cacmvmiddleware.modelo.RespostaMiddleware;
import br.org.cac.cacmvmiddleware.util.Verificador;
import br.org.cac.cacmvmiddleware.util.WebServiceFactory;

/**<p><strong>BeneficiarioWebServices</strong> é uma classe de controle para os WebServices do tipo Beneficiario</p>
 * @author grr
 * @version 1.0
 * @since 2016-12-26
 * 
 * <p>O Webservice funciona como um Middleware, servindo de ponte para fazer requisições ao Webservice de Beneficiarios do Soul MV</p>
 * <p>As operações do Webservice BeneficiarioWS recebem seus parametros, fazem uma requisição no Webservice Soul Mv e retornam um Json como resposta</p>
 * <p>o Webservice <strong>BeneficiarioWS</strong> possui as seguintes operações:</p>
 * <ul>
 * 	<li><strong>getBeneficiario</strong> - Recupera as Informações de um Beneficiario</li>
 * 	<li><strong>getSituacaoFinanceira</strong> Recuperar a Situação Financeira de um Beneficiario</li>
 * 	<li><strong>delete</strong> - Exclui um Beneficiario</li>
 * 	<li><strong>incluir</strong> - Inclui um Beneficiario</li>
 * 	<li><strong>alterar</strong> - Altera as informações de um Beneficiario</li>
 * </ul>
 */

@Path("BeneficiarioWS")
public class BeneficiarioWebServices {

	public static final String MV_WS_PORT = System.getProperty("MV_WS_PORT");
	String server = "http://10.10.20.4:"+MV_WS_PORT+"/";
	String mvPath = "mvservices/beneficiario/";
	
	@Inject
	LogMiddlewareDAO dao;
	
	WebServiceFactory wsf = new WebServiceFactory();
	RespostaMiddleware respostaMiddleware = new RespostaMiddleware();
	
	/**<p>Recuperar as Informações de um Beneficiario:</p>
	 *	Link: {@link http://10.10.20.32:8080/CacMvMiddleware/BeneficiarioWS/recuperar/{id}}
	 * <p>Serviço GET que recebe um id como parametro, faz uma requisição GET ao Serviço beneficiario/get do Soul MV e retorna um JSON como resposta</p>
	 * 
	 * @param id_beneficiario Integer - id do Beneficiario a ser Buscado
	 * 
	 * @return Response Response - no Padrão HTTP que contem um JSON com as informações da Resposta do Soul MV
	 * <p>O JSON retornado possui o Statuscode da Operação e as informações de Beneficiario</p>
	 * 
	 * @throws JsonProcessingException 
	 * 
	 * @throws NotFoundException Soul MV fora do Ar.
	 * 
	 * @throws Exception Soul Mv Retornou com Erro.
	 * 
	 * <p><strong>Exemplos:</strong></p>
	 * <p>Beneficiario Não Encontrado</p>
	 * <p>{"status": 200,"beneficiario": null}</p>
	 * 
	 * <p>Beneficiario Encontrado</p>
	 * <p>{"status":200,"beneficiario":{"id":123456,"nmSegurado":"Beneficiario Teste","nmMae":"Mae Teste ... }}</p> 
	 * 
	 * <p>Soul MV Fora do Ar</p>
	 * <p>{"entidadeId":1,"status":404,"mensagem":"Webservice Soul Mv |http://10.10.20.4:8789/mvservices/beneficiario/| Status: Offline"}</p>
	 */

	@GET
	@Path("recuperar/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getBeneficiario(@PathParam("id") Integer id_beneficiario) {
		String respostaRecebidaMv = null;
		String path = mvPath + "get/" + id_beneficiario;
		try {
			respostaRecebidaMv = wsf.criaRequisicaoGet(path, server);
			wsf.criaWsLog(dao, server+path, 1, "", respostaRecebidaMv, Response.Status.OK.getStatusCode(), "");
		}
		catch (Exception e) {
			e.printStackTrace();
			respostaMiddleware = wsf.respostaMiddlewareFactory(dao, e, server+path);
			wsf.criaWsLog(dao, server+path, 1, "", respostaMiddleware.getMensagem(), respostaMiddleware.getStatus(), e.getMessage());
			return Response.status(respostaMiddleware.getStatusDescricao()).entity(wsf.respostaMiddlewareToJson(respostaMiddleware)).build();
		}
		return Response.ok(respostaRecebidaMv, MediaType.APPLICATION_JSON).build();
	}
	
	/**<p>Recuperar a Situação Financeira de um Beneficiario:</p>
	 *	Link: {@link http://10.10.20.32:8080/CacMvMiddleware/BeneficiarioWS/getSituacaoFinanceira/{id}/{dia}/{mes}/{ano}}
	 * <p>Serviço GET que recebe um id e uma data como parametro, faz uma requisição GET ao Serviço beneficiario/situacaoFinanceira do Soul MV e retorna um JSON como resposta</p>
	 * 
	 * @param id_beneficiario Integer - id do Beneficiario a ser Buscado
	 * 
	 * @param dia String - dia da Situação Financeira a ser Buscada
	 * 
	 * @param mes String - mes da Situação Financeira a ser Buscada
	 * 
	 * @param ano String - ano da Situação Financeira a ser Buscada
	 * 
	 * @return Response Response - no Padrão HTTP que contem um JSON com as informações da Resposta do Soul MV
	 * <p>O JSON retornado possui o Statuscode da Operação e as informações de Beneficiario</p>
	 * @throws JsonProcessingException 
	 * 
	 * @throws NotFoundException Soul MV fora do Ar.
	 * 
	 * @throws Exception Soul Mv Retornou com Erro.
	 * 
	 * <p><strong>Exemplos:</strong></p>
	 * <p>Beneficiario Não Encontrado</p>
	 * <p>{"status": 200,"beneficiario": null}</p>
	 * 
	 * <p>Beneficiario Encontrado</p>
	 * <p>{"status":200,"mensagem":null,"entidadeId":0,"beneficiario":{"id":1234567,"nmSegurado":"JAMES T. KIRK", ... }</p> 
	 * 
	 * <p>Data Informada Inválida</p>
	 * <p>{"entidadeId":1,"status":404,"mensagem":"Data Informada Inválida, Formato Correto Deve Ser: dd/mm/aaaa"}</p>
	 * 
	 * <p>Soul MV Fora do Ar</p>
	 * <p>{"entidadeId":1,"status":404,"mensagem":"Webservice Soul Mv |http://10.10.20.4:8789/mvservices/beneficiario/| Status: Offline"}</p>
	 */
	
	@GET
	@Path("getSituacaoFinanceira/{id}/{dia}/{mes}/{ano}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getSituacaoFinanceira(@PathParam("id") Integer id_beneficiario, @PathParam("dia") String dia, @PathParam("mes") String mes, @PathParam("ano") String ano) {
		String respostaRecebidaMv = null;
		String path = mvPath + "situacaoFinanceira/" + id_beneficiario + "/" + dia + mes + ano;
		try {
			new Verificador().verificaData(dia, mes, ano);
			respostaRecebidaMv = wsf.criaRequisicaoGet(path, server);
			wsf.criaWsLog(dao, server+path, 21, "", respostaRecebidaMv, Response.Status.OK.getStatusCode(), "");
		}
		catch (Exception e) {
			e.printStackTrace();
			respostaMiddleware = wsf.respostaMiddlewareFactory(dao, e, server+path);
			wsf.criaWsLog(dao, server+path, 21, "", respostaMiddleware.getMensagem(), respostaMiddleware.getStatus(), e.getMessage());
			return Response.status(respostaMiddleware.getStatusDescricao()).entity(wsf.respostaMiddlewareToJson(respostaMiddleware)).build();
		}
		return Response.ok(respostaRecebidaMv, MediaType.APPLICATION_JSON).build();
	}
	
	/**<p>Excluir as Informações de um Beneficiario:</p>
	 *	Link: {@link http://10.10.20.32:8080/CacMvMiddleware/BeneficiarioWS/excluir/{id}}
	 * <p>Serviço DELETE que recebe um id como parametro, faz uma requisição DELETE ao Serviço beneficiario/delete do Soul MV e retorna um JSON como resposta</p>
	 * 
	 * @param id_beneficiario Integer - id do Beneficiario a ser Excluido
	 * 
	 * @return Response Response - no Padrão HTTP que contem um JSON com as informações da Resposta do Soul MV
	 * <p>O JSON retornado possui o Statuscode da Operação, ID do Beneficiario a ser Excluido e as informações da Exclusão</p>
	 * @throws JsonProcessingException 
	 * 
	 * @throws NotFoundException Soul MV fora do Ar.
	 * 
	 * @throws Exception Soul Mv Retornou com Erro.
	 * 
	 * <p><strong>Exemplos:</strong></p>
	 * <p>Beneficiario Não Encontrado</p>
	 * <p>{"entidadeId": 1,"status": 500,"mensagem": "Entidade não foi encontrada para deleção."}</p>
	 * 
	 * <p>Beneficiario Excluido</p>
	 * <p>{"entidadeId": 116450088, "status": 200, "mensagem": "Beneficiario deletado(a) com sucesso!"}</p> 
	 * 
	 * <p>Soul MV Fora do Ar</p>
	 * <p>{"entidadeId":1,"status":404,"mensagem":"Webservice Soul Mv |http://10.10.20.4:8789/mvservices/beneficiario/| Status: Offline"}</p>
	 */

	@DELETE
	@Path("excluir/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response delete(@PathParam("id") Integer id_beneficiario){
		String respostaRecebidaMv = null;
		String path = mvPath + "delete/" + id_beneficiario;
		Response beneficiario = getBeneficiario(id_beneficiario);
		try {
			respostaRecebidaMv = wsf.criaRequisicaoDelete(path, server);
			wsf.criaWsLog(dao, server+path, 2, beneficiario.getEntity().toString(), respostaRecebidaMv, Response.Status.OK.getStatusCode(), "");
		}
		catch (Exception e) {
			e.printStackTrace();
			respostaMiddleware = wsf.respostaMiddlewareFactory(dao, e, server+path);
			wsf.criaWsLog(dao, server+path, 2, beneficiario.getEntity().toString(), respostaMiddleware.getMensagem(), respostaMiddleware.getStatus(), e.getMessage());
			return Response.status(respostaMiddleware.getStatusDescricao()).entity(wsf.respostaMiddlewareToJson(respostaMiddleware)).build();
		}
		return Response.ok(respostaRecebidaMv, MediaType.APPLICATION_JSON).build();
	}
	
	/**<p>Incluir as Informações de um Beneficiario:</p>
	 *	Link: {@link http://10.10.20.32:8080/CacMvMiddleware/BeneficiarioWS/incluir}
	 * <p>Serviço PUT que recebe um JSON como parametro, faz uma requisição PUT ao Serviço beneficiario/insere do Soul MV e retorna um JSON como resposta</p>
	 * 
	 * @param beneficiario String - Json com dados do Beneficiario a ser Incluido
	 * 
	 * @return Response Response - no Padrão HTTP que contem um JSON com as informações da Resposta do Soul MV
	 * <p>O JSON retornado possui o Statuscode da Operação, as informações da Inclusão, e ID do Beneficiario Incluido</p>
	 * @throws JsonProcessingException 
	 * 
	 * @throws NotFoundException Soul MV fora do Ar.
	 * 
	 * @throws Exception Soul Mv Retornou com Erro.
	 * 
	 * <p><strong>Exemplos:</strong></p>
	 * <p>Beneficiario Incluido</p>
	 * <p>{"status": 200, "mensagem": [ "123290 - IMP_USUARIO(CD_CONTRATO): CONTRATO: 31287 INSERIDO COM SUCESSO!", "123290 - IMP_USUARIO(CD_MATRICULA): USUARIO:116440015 INSERIDO COM SUCESSO!", "123290 - IMP_USUARIO(CD_MAT_ALTERNATIVA): USUARIO:116440015 INSERIDO COM SUCESSO!"],"entidadeId": 116440015}</p> 
	 * 
	 * <p>Soul MV Fora do Ar</p>
	 * <p>{"entidadeId":1,"status":404,"mensagem":"Webservice Soul Mv |http://10.10.20.4:8789/mvservices/beneficiario/| Status: Offline"}</p>
	 */

	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("incluir")
	public Response incluir(String beneficiario) {
		String respostaRecebidaMv = null;
		String path = mvPath + "insere";
		try {
			respostaRecebidaMv = wsf.criaRequisicaoIncluir(path, server, beneficiario);
			wsf.criaWsLog(dao, server+path, 3, beneficiario, respostaRecebidaMv, Response.Status.OK.getStatusCode(), "");
		}
		catch (Exception e) {
			e.printStackTrace();
			respostaMiddleware = wsf.respostaMiddlewareFactory(dao, e, server+path);
			wsf.criaWsLog(dao, server+path, 3, beneficiario, respostaMiddleware.getMensagem(), respostaMiddleware.getStatus(), e.getMessage());
			return Response.status(respostaMiddleware.getStatusDescricao()).entity(wsf.respostaMiddlewareToJson(respostaMiddleware)).build();
		}
		return Response.ok(respostaRecebidaMv, MediaType.APPLICATION_JSON).build();
	}

	/**<p>Alterar as Informações de um Beneficiario:</p>
	 *	Link: {@link http://10.10.20.32:8080/CacMvMiddleware/BeneficiarioWS/alterar}
	 * <p>Serviço POST que recebe um JSON como parametro, faz uma requisição POST ao Serviço beneficiario/atualiza do Soul MV e retorna um JSON como resposta</p>
	 * 
	 * @param beneficiario String - Json com dados do Beneficiario a ser Alterado
	 * 
	 * @return Response Response - no Padrão HTTP que contem um JSON com as informações da Resposta do Soul MV
	 * <p>O JSON retornado possui o Statuscode da Operação, as informações da Inclusão, e ID do Beneficiario Alterado</p>
	 * @throws JsonProcessingException 
	 * 
	 * @throws NotFoundException Soul MV fora do Ar.
	 * 
	 * @throws Exception Soul Mv Retornou com Erro.
	 * 
	 * <p><strong>Exemplos:</strong></p>
	 * <p>Beneficiario Alterado</p>
	 * <p>{"status":200,"mensagem":["142433 - IMP_USUARIO(CD_MATRICULA): USUARIO:121820017 ATUALIZADO COM SUCESSO!","142433 - IMP_USUARIO(CD_MAT_ALTERNATIVA): USUARIO:121820017 ATUALIZADO COM SUCESSO!"],"entidadeId":121820017}</p> 
	 * 
	 * <p>Soul MV Fora do Ar</p>
	 * <p>{"entidadeId":1,"status":404,"mensagem":"Webservice Soul Mv |http://10.10.20.4:8789/mvservices/beneficiario/| Status: Offline"}</p>
	 */

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("alterar")
	public Response alterar(String beneficiario) {
		String respostaRecebidaMv = null;
		String path = mvPath + "atualiza";
		try {
			respostaRecebidaMv = wsf.criaRequisicaoAlterar(path, server, beneficiario);
			wsf.criaWsLog(dao, server+path, 4, beneficiario, respostaRecebidaMv, Response.Status.OK.getStatusCode(), "");
		}
		catch (Exception e) {
			e.printStackTrace();
			respostaMiddleware = wsf.respostaMiddlewareFactory(dao, e, server+path);
			wsf.criaWsLog(dao, server+path, 4, beneficiario, respostaMiddleware.getMensagem(), respostaMiddleware.getStatus(), e.getMessage());
			return Response.status(respostaMiddleware.getStatusDescricao()).entity(wsf.respostaMiddlewareToJson(respostaMiddleware)).build();
		}
		return Response.ok(respostaRecebidaMv, MediaType.APPLICATION_JSON).build();
	}
}