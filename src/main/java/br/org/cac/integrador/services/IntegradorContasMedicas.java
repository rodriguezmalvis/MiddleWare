package br.org.cac.integrador.services;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jboss.logging.Logger;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.org.cac.cacmvmiddleware.services.ContaMedicaWebServices;
import br.org.cac.integrador.dao.LoteTrabalhoDAO;
import br.org.cac.integrador.dao.ProcessamentoDAO;
import br.org.cac.integrador.dao.StatusProcessamentoDAO;
import br.org.cac.integrador.dao.SubProcessoDAO;
import br.org.cac.integrador.dao.TipoProcessamentoDAO;
import br.org.cac.integrador.exception.ProcessadorException;
import br.org.cac.integrador.jacksonutil.ObjectMapperFactory;
import br.org.cac.integrador.modelo.LoteContaMedica;
import br.org.cac.integrador.modelo.Periodo;
import br.org.cac.integrador.modelo.PeriodoDataCredito;
import br.org.cac.integrador.modelo.RetornoMensagem;
import br.org.cac.integrador.modelo.SubProcesso;
import br.org.cac.integrador.modelo.SubProcessoId;
import br.org.cac.integrador.modelo.infraestrutura.LoteTrabalho;
import br.org.cac.integrador.modelo.infraestrutura.Processamento;
import br.org.cac.integrador.modelo.infraestrutura.StatusProcessamento;
import br.org.cac.integrador.modelo.infraestrutura.TipoProcessamento;
import br.org.cac.integrador.processadores.ProcessadorContasMedicas;
import br.org.cac.integrador.processadores.ResumoProcessamento;
import br.org.cac.integrador.util.ResponseUtil;
import br.org.cac.integrador.util.ThrowableUtil;

/**
 * <p>Classe que contém o serviço responsável por fazer o processamento de 
 * subprocessos, como entendidos na base de dados da CAC, transformando-os em lotes de guias,
 * como entendidos pela MV, e realizando sua inclusão na base de dados da MV, através de 
 * {@link ContaMedicaWebServices}.</p>
 * 
 * <p>Atualmente contém os seguintes serviços:
 * 	<ul>
 * 		<li>{@link #incluir(String)} - Faz a integração de um único subprocesso.</li>
 * 		<li>{@link #processarPorPeriodo(String)} - Faz a integração de vários subprocessos, considerando
 * 			as datas de homologação passadas por parâmetro.</li>
 * 		<li>{@link #gerarJson(String)} - Gera um JSON a partir do subprocesso informado e o retorna.
 * 			Este serviço é auxiliar, e não grava nenhuma informação, nem na base de dados da CAC e
 * 			nem na da MV.</li>
 * 	</ul>
 * </p>
 * 
 * <p>O JSON produzido por este serviço possui a estrutura definida em {@link RetornoMensagem}.
 * 
 * 
 * @author JCJ
 * @version 1.0
 * @since 2017-03-09
 */
@Path("IntegradorContasMedicas")
public class IntegradorContasMedicas {
	
	private ObjectMapper mapper;
	
	@Inject
	private LoteTrabalhoDAO ltDao;
	
	@Inject
	private ProcessamentoDAO pDao;
	
	@Inject
	private StatusProcessamentoDAO spDao;
	
	@Inject
	private TipoProcessamentoDAO tpDao;
	
	@Inject
	private SubProcessoDAO sprDao;
	
	@Inject
	private ProcessadorContasMedicas processador;
	
	@Inject
	private Logger logger;
		
	/**
	 * Construtor padrão para a classe.
	 */
	public IntegradorContasMedicas(){
		this.mapper = ObjectMapperFactory.getDefaultConfiguredObjectMapper(false);
	}

	/**
	 * <p>Serviço {@code PUT} que realiza a integração de um único subprocesso, através do serviço 
	 * {@link ContaMedicaWebServices}. <br/>
	 * Link: {@link {http://10.10.20.32:8080/CacMvMiddleware/IntegradorContasMedicas/incluir}
	 * </p>
	 * 
	 * <p>O JSON consumido por este serviço possui a seguinte estrutura:
	 * <pre>{
	 *  "anoApresentacao": int,
	 *  "idRepresentacao": int,
	 *  "idProcesso": int,
	 *  "dSubProcesso": string,
	 *  "dNatureza": string,
	 *  "idSequencialNatureza": int
	 *}</pre>
	 * </p>
	 * 
	 * @param subProcesso Representação {@code String} de um JSON que indica o subprocesso a ser integrado com a MV.
	 * @return {@link Response} no Padrão HTTP que contém um JSON com as informações da Resposta do Soul MV.
	 * O JSON retornado possui o {@code StatusCode} da Operação, as informações da Inclusão, e ID da ContaMedica incluída,
	 * ou com mensagens de erro, de acordo com a estrutura em {@link RetornoMensagem}.
	 * 
	 * <p>Possíveis códigos de retorno:
	 * <ul>
	 * 	<li>{@code 200} (OK) - A comunicação com o serviço da MV ocorreu com sucesso. Deve-se analisar o conteúdo do retorno
	 *  para saber o resultado efetivo da operação (particularmente, o campo {@code status}).</li>
	 *  <li>{@code 400} (Bad Request) - O JSON de origem é inválido (seja estruturalmente ou que não corresponda 
	 *  à estrutura esperada do JSON de SubProcesso</li>
	 *  <li>{@code 500} (Internal Server Error) - Quando ocorre algum erro de processamento deste serviço 
	 *  ou de comunicação com o serviço da MV.</li> 
	 *  <li>{@code 404} (Not Found) - Quando o serviço do SOUL está fora do ar.
	 * </ul>
	 * 
	 * </p>
	 * 
	 * @author JCJ
	 * @version 1.0
	 * @since 2017-03-09
	 */
	@PUT
	@Path("incluir")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response incluir(String jsonSubProcesso){
		logger.info("Iniciando processamento PUT: incluir");
		
		Response response = null;

		ResumoProcessamento resumoProcessamento = null;
		
		SubProcessoId subProcesso = null;
		Processamento processamento = null;
		
		try {
			subProcesso = mapper.readValue(jsonSubProcesso, SubProcessoId.class);
			logger.info("SubProcesso recebido: " + subProcesso);
					
			// TODO: Separar melhor a criação do Processamento
			TipoProcessamento tipoProcessamentoContaUnica = tpDao.findByCodProcessamento(TipoProcessamento.CONTA_INDIVIDUAL);
			
			processamento = processador.inicializaProcessamento(Calendar.getInstance().getTime(), 
					tipoProcessamentoContaUnica, null);			
			pDao.persist(processamento);
			
			logger.info("Processamento criado. Id: " + processamento.getIdProcessamento());
			
			LoteTrabalho lote = inicializaLoteTrabalho(subProcesso);
			StatusProcessamento statusProcessamentoInicial = spDao.findByCodStatus(StatusProcessamento.ADICIONADO);			
			
			lote.setStatusProcessamento(statusProcessamentoInicial);
			lote.setProcessamento(processamento);
			
			ltDao.persist(lote);
			
			resumoProcessamento = processador.processa(processamento);

		} catch (JsonParseException e) {
			response = ResponseUtil.gerarResponse(Response.Status.BAD_REQUEST,
					"Formato inválido de JSON. Verifique se o conteúdo enviado é um JSON válido.");	
			/* TODO: Por algum motivo a implementação abaixo não funciona (aparentemente o servidor não consegue converter
			 * Retorno para json). Verificar o motivo.
			 * 
			retorno.setStatus(String.valueOf(Response.Status.BAD_REQUEST.getStatusCode()));
			retorno.getMensagem().add("Formato inválido de JSON. Verifique se o conteúdo enviado é um JSON válido.");
			
			response = Response.status(Response.Status.BAD_REQUEST)
						.entity(retorno)
						.type(MediaType.APPLICATION_JSON)
						.build(); */
		} catch (JsonMappingException e) {
			response = ResponseUtil.gerarResponse(Response.Status.BAD_REQUEST,
						"Formato inválido de JSON. Deve estar na estutrura de SubProcesso.");
			
		} catch (JsonProcessingException e){
			response = ResponseUtil.gerarResponse(Response.Status.INTERNAL_SERVER_ERROR,
					"Erro ao gerar JSON para o lote referente ao subprocesso informado: " + e.getMessage());
		} catch (IOException e) {
			response = ResponseUtil.gerarResponse(Response.Status.INTERNAL_SERVER_ERROR,
					"Erro genérico de IO. Mensagem de erro: " + e.getMessage());
			
			/* TODO: Por algum motivo a implementação abaixo não funciona (aparentemente o servidor não consegue converter
			 * Retorno para json). Verificar o motivo.
			retorno.setStatus(String.valueOf(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()));
			retorno.getMensagem().add("Erro genérico de IO. Mensagem de erro: " + e.getLocalizedMessage());
			
			response = Response.status(Response.Status.INTERNAL_SERVER_ERROR)
						.entity(retorno)
						.type(MediaType.APPLICATION_JSON)
						.build();
						*/
		} catch (ProcessadorException e) {
			response = ResponseUtil.gerarResponse(Response.Status.INTERNAL_SERVER_ERROR,
					"Erro no processamento do subprocesso informado: " + ThrowableUtil.getRootMessage(e)
					);
		}
		
		if (processamento != null){
			processamento.setDthrFimProcessamento(Calendar.getInstance().getTime());
			pDao.merge(processamento);
			logger.info("Fim do processamento. Id: " + processamento.getIdProcessamento());
		}
		
		if (response == null) {
			response = ResponseUtil.geraResponseResumoProcessamento(Response.Status.OK, resumoProcessamento);
		}
		
		return response;
	}

	
	/**
	 * <p>Serviço {@code PUT} que realiza a integração de um conjunto de subprocessos, baseado em suas
	 * datas de homologação, através do serviço {@link ContaMedicaWebServices}. <br/>
	 * Link: {@link http://10.10.20.32:8080/CacMvMiddleware/IntegradorContasMedicas/processarPorPeriodo}
	 * </p>
	 *
	 * <p>O JSON consumido por este serviço possui a seguinte estrutura:
	 * <pre>{
	 *  "dtInicial": &lt;data no formato DD/MM/AAAA&gt;,
	 *  "dtFinal": &lt;data no formato DD/MM/AAAA&gt;
	 *}</pre>
	 * </p>
	 * 
	 * @param jsonPeriodo Representação {@code String} de um JSON que indica o período de homologação dos
	 * subprocessos a serem integrados com a MV. Caso não seja enviado, o serviço irá gerar e utilizar
	 * um JSON com os campos {@code dtInicial} e {@code dtFinal} (conforme definidos acima) iguais à data de hoje.
	 * @return {@link Response} no Padrão HTTP que contém um JSON com as informações da Resposta do Soul MV.
	 * O JSON retornado possui o {@code StatusCode} da Operação, as informações da Inclusão, e ID da última 
	 * ContaMedica incluída, ou com mensagens de erro, de acordo com a estrutura em {@link RetornoMensagem}.
	 * 
	 * <p>Possíveis códigos de retorno:
	 * <ul>
	 * 	<li>{@code 200} (OK) - A comunicação com o serviço da MV ocorreu com sucesso. Deve-se analisar o conteúdo do retorno
	 *  para saber o resultado efetivo da operação (particularmente, o campo {@code status}).</li>
	 *  <li>{@code 400} (Bad Request) - O JSON de origem é inválido (seja estruturalmente ou que não corresponda 
	 *  à estrutura esperada do JSON de SubProcesso</li>
	 *  <li>{@code 500} (Internal Server Error) - Quando ocorre algum erro de processamento deste serviço 
	 *  ou de comunicação com o serviço da MV.</li> 
	 *  <li>{@code 404} (Not Found) - Quando o serviço do SOUL está fora do ar.
	 * </ul>
	 * 
	 * </p>
	 * @author JCJ
	 * @version 1.0
	 * @since 2017-03-20
	 */
	@PUT
	@Path("processarPorPeriodo")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response processarPorPeriodo(String jsonPeriodo){
		logger.info("Iniciando processamento PUT: processarPorPeriodo");
		
		Response response = null;
		Periodo periodoHomologacao = null;
		Processamento processamento = null;

		ResumoProcessamento resumoProcessamento = null;
		
		try {
			if ("".equals(jsonPeriodo) ){
				periodoHomologacao = new Periodo();				
				LocalDate dateToConvert = LocalDate.now();
				Date hoje =
				    Date.from(dateToConvert.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
				periodoHomologacao.setDtInicial(hoje);
				periodoHomologacao.setDtFinal(hoje);
			} else {
				periodoHomologacao = mapper.readValue(jsonPeriodo, Periodo.class);	
			}
			
			logger.info("Período informado: " + periodoHomologacao.toString());
			
			TipoProcessamento tipoProcessamentoLoteContas = tpDao.findByCodProcessamento(TipoProcessamento.LOTE_CONTAS);			
			
			processamento = processador.inicializaProcessamento(Calendar.getInstance().getTime(), 
					tipoProcessamentoLoteContas, periodoHomologacao);
			pDao.persist(processamento);
			
			// TODO: Refatorar para separar esse pré-processamento para outra classe
			List<SubProcesso> subProcessos = sprDao.getSubProcessos(periodoHomologacao);
			
			StatusProcessamento spAdicionado = spDao.findByCodStatus(StatusProcessamento.ADICIONADO);
			
			
			for (SubProcesso sp : subProcessos){
				LoteTrabalho lote = new LoteTrabalho();
				lote.setSubProcesso(sp.getSubProcessoId());
				lote.setProcessamento(processamento);
				lote.setStatusProcessamento(spAdicionado);
				
				ltDao.persist(lote);
			}
			
			resumoProcessamento = processador.processa(processamento);
			
			logger.info("Fim do processamento do período " + periodoHomologacao.toString());

				
		} catch (JsonParseException e) {
			response = ResponseUtil.gerarResponse(Response.Status.BAD_REQUEST,
					"Formato inválido de JSON. Verifique se o conteúdo enviado é um JSON válido.");	
			/* TODO: Por algum motivo a implementação abaixo não funciona (aparentemente o servidor não consegue converter
			 * Retorno para json). Verificar o motivo.
			 * 
			retorno.setStatus(String.valueOf(Response.Status.BAD_REQUEST.getStatusCode()));
			retorno.getMensagem().add("Formato inválido de JSON. Verifique se o conteúdo enviado é um JSON válido.");
			
			response = Response.status(Response.Status.BAD_REQUEST)
						.entity(retorno)
						.type(MediaType.APPLICATION_JSON)
						.build(); */
		} catch (JsonMappingException e) {
			response = ResponseUtil.gerarResponse(Response.Status.BAD_REQUEST,
						"Formato inválido de JSON. Deve estar na estutrura de Periodo.");
			
		} catch (JsonProcessingException e){
			response = ResponseUtil.gerarResponse(Response.Status.INTERNAL_SERVER_ERROR,
					"Erro ao gerar JSON para o lote referente ao subprocesso informado: " + e.getMessage());
		} catch (IOException e) {
			response = ResponseUtil.gerarResponse(Response.Status.INTERNAL_SERVER_ERROR,
					"Erro genérico de IO. Mensagem de erro: " + e.getMessage());
			
			/* TODO: Por algum motivo a implementação abaixo não funciona (aparentemente o servidor não consegue converter
			 * Retorno para json). Verificar o motivo.
			retorno.setStatus(String.valueOf(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()));
			retorno.getMensagem().add("Erro genérico de IO. Mensagem de erro: " + e.getLocalizedMessage());
			
			response = Response.status(Response.Status.INTERNAL_SERVER_ERROR)
						.entity(retorno)
						.type(MediaType.APPLICATION_JSON)
						.build();
						*/
		} catch (ProcessadorException e) {
			response = ResponseUtil.gerarResponse(Response.Status.INTERNAL_SERVER_ERROR,
					"Erro no processamento do subprocesso informado: " + ThrowableUtil.getRootMessage(e)
					);
		}
		
		
		if (processamento != null) {
			// TODO: Separar este trecho para outro classe?
			processamento.setDthrFimProcessamento(Calendar.getInstance().getTime());
			pDao.merge(processamento);
			logger.info("Fim do processamento. Id: " + processamento.getIdProcessamento());
		}
		
		if (response == null) {
			response = ResponseUtil.geraResponseResumoProcessamento(Response.Status.OK, resumoProcessamento);
		}
		
		return response;		
	}
	
	/**
	 * 
	 * @param jsonPeriodoDataCredito
	 * @return
	 * @since 2017-07-22
	 */
	@PUT
	@Path("processarPorDataCredito")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response processarPorDataCredito(String jsonPeriodoDataCredito){
		logger.info("Iniciando processamento PUT: processarPorDataCredito");
		
		Response response = null;
		PeriodoDataCredito periodoDataCredito = null;
		Processamento processamento = null;
		
		ResumoProcessamento resumoProcessamento = null;
		
		try {
			periodoDataCredito = mapper.readValue(jsonPeriodoDataCredito, PeriodoDataCredito.class);
			
			logger.info("Período informado: " + periodoDataCredito.toString());
			
			TipoProcessamento tipoProcessamentoLoteContas = tpDao.findByCodProcessamento(TipoProcessamento.LOTE_CONTAS);			
			
			processamento = processador.inicializaProcessamento(Calendar.getInstance().getTime(), 
					tipoProcessamentoLoteContas, periodoDataCredito);
			pDao.persist(processamento);
			
			// TODO: Refatorar para separar esse pré-processamento para outra classe
			List<SubProcesso> subProcessos = sprDao.getSubProcessos(periodoDataCredito);
			
			StatusProcessamento spAdicionado = spDao.findByCodStatus(StatusProcessamento.ADICIONADO);
			
			
			for (SubProcesso sp : subProcessos){
				LoteTrabalho lote = new LoteTrabalho();
				lote.setSubProcesso(sp.getSubProcessoId());
				lote.setProcessamento(processamento);
				lote.setStatusProcessamento(spAdicionado);
				
				ltDao.persist(lote);
			}
			
			// TODO: Ver se está certo
			resumoProcessamento = processador.processa(processamento);
			
			logger.info("Fim do processamento do período " + periodoDataCredito.toString());
				
		} catch (JsonParseException e) {
			response = ResponseUtil.gerarResponse(Response.Status.BAD_REQUEST,
					"Formato inválido de JSON. Verifique se o conteúdo enviado é um JSON válido.");	
		} catch (JsonMappingException e) {
			response = ResponseUtil.gerarResponse(Response.Status.BAD_REQUEST,
						"Formato inválido de JSON. Deve estar na estutrura de PeriodoDataCredito.");
			
		} catch (JsonProcessingException e){
			response = ResponseUtil.gerarResponse(Response.Status.INTERNAL_SERVER_ERROR,
					"Erro ao gerar JSON para o lote referente ao subprocesso informado: " + e.getMessage());
		} catch (IOException e) {
			response = ResponseUtil.gerarResponse(Response.Status.INTERNAL_SERVER_ERROR,
					"Erro genérico de IO. Mensagem de erro: " + e.getMessage());
		} catch (ProcessadorException e) {
			response = ResponseUtil.gerarResponse(Response.Status.INTERNAL_SERVER_ERROR,
					"Erro no processamento do subprocesso informado: " + ThrowableUtil.getRootMessage(e)
					);
		}
		
		
		if (processamento != null) {
			// TODO: Separar este trecho para outro classe?
			processamento.setDthrFimProcessamento(Calendar.getInstance().getTime());
			pDao.merge(processamento);
			logger.info("Fim do processamento. Id: " + processamento.getIdProcessamento());
		}
		
		if (response == null) {
			response = ResponseUtil.geraResponseResumoProcessamento(Response.Status.OK, resumoProcessamento);
		}
		
		return response;		
	}	
	
	/**
	 * <p>Serviço {@code PUT} auxiliar que gera o JSON de Contas médicas da MVde um único subprocesso.
	 * Link: {@link {http://10.10.20.32:8080/CacMvMiddleware/IntegradorContasMedicas/gerarJson}
	 * </p>
	 * 
	 * <p>O JSON consumido por este serviço possui a seguinte estrutura:
	 * <pre>{
	 *  "anoApresentacao": int,
	 *  "idRepresentacao": int,
	 *  "idProcesso": int,
	 *  "dSubProcesso": string,
	 *  "dNatureza": string,
	 *  "idSequencialNatureza": int
	 *}</pre>
	 * </p>
	 * 
	 * @param subProcesso Representação {@code String} de um JSON que indica o subprocesso a ser integrado com a MV.
	 * @return {@link Response} no Padrão HTTP que contém um JSON de Contas Médicas no padrão da MV.
	 * 
	 * <p>Possíveis códigos de retorno:
	 * <ul>
	 * 	<li>{@code 200} (OK) - A comunicação com o serviço da MV ocorreu com sucesso. Deve-se analisar o conteúdo do retorno
	 *  para saber o resultado efetivo da operação (particularmente, o campo {@code status}).</li>
	 *  <li>{@code 400} (Bad Request) - O JSON de origem é inválido (seja estruturalmente ou que não corresponda 
	 *  à estrutura esperada do JSON de SubProcesso</li>
	 *  <li>{@code 500} (Internal Server Error) - Quando ocorre algum erro de processamento deste serviço 
	 *  ou de comunicação com o serviço da MV.</li> 
	 *  <li>{@code 404} (Not Found) - Quando o serviço do SOUL está fora do ar.
	 * </ul>
	 * 
	 * </p>
	 * 
	 * @author JCJ
	 * @version 1.1
	 * @since 2017-03-27
	 */
	@PUT
	@Path("gerarJson")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response gerarJson(String jsonSubProcesso){
		logger.info("Iniciando processamento PUT: gerarJson");
		Response response = null;
			
		SubProcessoId subProcesso = null;
		
		try {
			subProcesso = mapper.readValue(jsonSubProcesso, SubProcessoId.class);
			logger.info("SubProcesso recebido: " + subProcesso);
			
			List<LoteContaMedica> lotesContaMedica = processador.processaSubProcesso(subProcesso, 1);
			
			if ( (lotesContaMedica == null) || (lotesContaMedica.isEmpty()) ){
				throw new ProcessadorException("Subprocesso não gerou lotes de conta médica: " + subProcesso);
			}
			
			// TODO: Essa implementação foi rápida e para testes. Separar para a classe correta.
			LoteContaMedica lote = lotesContaMedica.get(0);
			
			// TODO: Melhorar essa implementação
			response = Response.status(Response.Status.OK)
					.entity(mapper.writeValueAsString(lote))
					.type(MediaType.APPLICATION_JSON)
					.build();

			
			return response;
		} catch (JsonParseException e) {
			response = ResponseUtil.gerarResponse(Response.Status.BAD_REQUEST,
					"Formato inválido de JSON. Verifique se o conteúdo enviado é um JSON válido.");	
			/* TODO: Por algum motivo a implementação abaixo não funciona (aparentemente o servidor não consegue converter
			 * Retorno para json). Verificar o motivo.
			 * 
			retorno.setStatus(String.valueOf(Response.Status.BAD_REQUEST.getStatusCode()));
			retorno.getMensagem().add("Formato inválido de JSON. Verifique se o conteúdo enviado é um JSON válido.");
			
			response = Response.status(Response.Status.BAD_REQUEST)
						.entity(retorno)
						.type(MediaType.APPLICATION_JSON)
						.build(); */
		} catch (JsonMappingException e) {
			response = ResponseUtil.gerarResponse(Response.Status.BAD_REQUEST,
						"Formato inválido de JSON. Deve estar na estutrura de SubProcesso.");
			
		} catch (JsonProcessingException e){
			response = ResponseUtil.gerarResponse(Response.Status.INTERNAL_SERVER_ERROR,
					"Erro ao gerar JSON para o lote referente ao subprocesso informado: " + e.getMessage());
		} catch (IOException e) {
			response = ResponseUtil.gerarResponse(Response.Status.INTERNAL_SERVER_ERROR,
					"Erro genérico de IO. Mensagem de erro: " + e.getMessage());
			
			/* TODO: Por algum motivo a implementação abaixo não funciona (aparentemente o servidor não consegue converter
			 * Retorno para json). Verificar o motivo.
			retorno.setStatus(String.valueOf(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()));
			retorno.getMensagem().add("Erro genérico de IO. Mensagem de erro: " + e.getLocalizedMessage());
			
			response = Response.status(Response.Status.INTERNAL_SERVER_ERROR)
						.entity(retorno)
						.type(MediaType.APPLICATION_JSON)
						.build();
						*/
		} catch (ProcessadorException e) {
			response = ResponseUtil.gerarResponse(Response.Status.INTERNAL_SERVER_ERROR,
					"Erro no processamento do subprocesso informado: " + ThrowableUtil.getRootMessage(e)
					);
		}

		return response;										
	}
	
	private LoteTrabalho inicializaLoteTrabalho(SubProcessoId subProcesso){
		LoteTrabalho lote = new LoteTrabalho();	
		lote.setSubProcesso(subProcesso);
		
		return lote;
	}	
	
}
