package br.org.cac.integrador.services;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.StoredProcedureQuery;
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

import br.org.cac.integrador.dao.ProcessamentoDAO;
import br.org.cac.integrador.dao.ReembolsoTrabalhoDAO;
import br.org.cac.integrador.dao.StatusProcessamentoDAO;
import br.org.cac.integrador.dao.TipoProcessamentoDAO;
import br.org.cac.integrador.exception.ProcessadorException;
import br.org.cac.integrador.jacksonutil.ObjectMapperFactory;
import br.org.cac.integrador.modelo.Atendimento;
import br.org.cac.integrador.modelo.Periodo;
import br.org.cac.integrador.modelo.PeriodoDataCredito;
import br.org.cac.integrador.modelo.SubProcessoId;
import br.org.cac.integrador.modelo.infraestrutura.Processamento;
import br.org.cac.integrador.modelo.infraestrutura.ReembolsoTrabalho;
import br.org.cac.integrador.modelo.infraestrutura.StatusProcessamento;
import br.org.cac.integrador.modelo.infraestrutura.TipoProcessamento;
import br.org.cac.integrador.processadores.ProcessadorReembolsos;
import br.org.cac.integrador.processadores.ResumoProcessamento;
import br.org.cac.integrador.util.ResponseUtil;
import br.org.cac.integrador.util.ThrowableUtil;

/**
 * 
 * @author JCJ
 * @version 1.1
 * @since 1.1, 2017-04-01
 */
@Path("IntegradorReembolsos")
public class IntegradorReembolsos {

	private ObjectMapper mapper;
	
	@Inject
	private ProcessamentoDAO pDao;
	
	@Inject
	private TipoProcessamentoDAO tpDao;
	
	@Inject
	private ReembolsoTrabalhoDAO rtDao;
	
	@Inject
	private StatusProcessamentoDAO spDao;
	
	@Inject
	private ProcessadorReembolsos processador;
	
	@Inject
	private EntityManager manager;
	
	@Inject
	private Logger logger;
	
	/**
	 * Construtor padrão para a classe.
	 */
	public IntegradorReembolsos(){
		this.mapper = ObjectMapperFactory.getDefaultConfiguredObjectMapper(false);
	}
	
	/**
	 * Servico de integração de reembolsos, que recebe um sub-processo e gera vários
	 * reembolsos (um para cada atendimento de reembolso), como definidos pela MV.
	 * 
	 * @param jsonSubProcesso
	 * @return
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
			TipoProcessamento tipoProcessamentoContaUnica = tpDao.findByCodProcessamento(TipoProcessamento.REEMBOLSO_INDIVIDUAL);
			
			processamento = processador.inicializaProcessamento(Calendar.getInstance().getTime(), 
					tipoProcessamentoContaUnica, null);			
			pDao.persist(processamento);
			
			logger.info("Processamento criado. Id: " + processamento.getIdProcessamento());
			
			geraReembolsosTrabalho(processamento, subProcesso);
			
			resumoProcessamento = processador.processa(processamento);

		} catch (JsonParseException e) {
			response = ResponseUtil.gerarResponse(Response.Status.BAD_REQUEST,
					"Formato inválido de JSON. Verifique se o conteúdo enviado é um JSON válido.");	
		} catch (JsonMappingException e) {
			response = ResponseUtil.gerarResponse(Response.Status.BAD_REQUEST,
						"Formato inválido de JSON. Deve estar na estutrura de SubProcesso.");
			
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
	 * Serviço de integração de reembolsos, que recebe um período de homologação e
	 * gera vários reembolsos (um para cada atendimento de cada subprocesso listado),
	 * conforme definidos pela MV.
	 * 
	 * @param jsonPeriodo
	 * @return
	 */
	@PUT
	@Path("processarPorPeriodo")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response processarPorPeriodo(String jsonPeriodo){
		/* Passos para o desenvolvimento:
		 * 1- Tentar ler o período informado. Caso não consiga, assumir dia de hoje;
		 * 2- Criar processamento deste tipo;
		 * 3- Listar os atendimentos que se tornarão ReembolsoTrabalho (usando SPR_ICM_LISTA_ATENDIMENTOS_REEMBOLSO_POR_HOMOLOGACAO)
		 * 4- Gravar tudo em ICM_REEMBOLSO_TRABALHO
		 * 5- Chamar ProcessadorReembolsos.processa()
		 * 6- Com tudo OK ou não, finalizar o processamento (gravar hora de fim de processamento) 
		 * 
		 */
		
		// Passo 1: tentando ler o período informado:
		
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
			
			logger.infof("Período informado: %s", periodoHomologacao.toString());	
									
			TipoProcessamento tipoProcessamentoLoteTitulos = tpDao.findByCodProcessamento(TipoProcessamento.LOTE_REEMBOLSOS);
			
			processamento = processador.inicializaProcessamento(Calendar.getInstance().getTime(), 
					tipoProcessamentoLoteTitulos, periodoHomologacao);
			pDao.persist(processamento);
			
			logger.infof("Processamento criado. Id: %d", 
						processamento.getIdProcessamento()
					);
			
			geraReembolsosTrabalho(processamento, periodoHomologacao);

			resumoProcessamento = processador.processa(processamento);			
			
		} catch (JsonParseException e) {
			response = ResponseUtil.gerarResponse(Response.Status.BAD_REQUEST,
					"Formato inválido de JSON. Verifique se o conteúdo enviado é um JSON válido.");	
		} catch (JsonProcessingException e){
			response = ResponseUtil.gerarResponse(Response.Status.INTERNAL_SERVER_ERROR,
					"Formato inválido de JSON. Deve estar na estutrura de Período. " + e.getMessage());
		} catch (IOException e) {
			response = ResponseUtil.gerarResponse(Response.Status.INTERNAL_SERVER_ERROR,
					"Erro genérico de IO. Mensagem de erro: " + e.getMessage());
			
		} catch (ProcessadorException e) {
			response = ResponseUtil.gerarResponse(Response.Status.INTERNAL_SERVER_ERROR,
					"Erro no processamento do subprocesso informado: " + e.getMessage()
					);
		}
		
		if (processamento != null){
			processamento.setDthrFimProcessamento(Calendar.getInstance().getTime());
			pDao.merge(processamento);
			logger.infof("Fim do processamento. Id: %d", processamento.getIdProcessamento());
		}	
		
		if (response == null) {
			response = ResponseUtil.geraResponseResumoProcessamento(Response.Status.OK, resumoProcessamento);
		}				
		
		return response;
	}

	/**
	 * Serviço de integração de reembolsos, que recebe um período de homologação e
	 * gera vários reembolsos (um para cada atendimento de cada subprocesso listado),
	 * conforme definidos pela MV.
	 * 
	 * @param jsonPeriodo
	 * @return
	 */
	@PUT
	@Path("processarPorDataCredito")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response processarPorDataCredito(String jsonPeriodo){
		/* Passos para o desenvolvimento:
		 * 1- Tentar ler o período informado. Caso não consiga, assumir dia de hoje;
		 * 2- Criar processamento deste tipo;
		 * 3- Listar os atendimentos que se tornarão ReembolsoTrabalho (usando SPR_ICM_LISTA_ATENDIMENTOS_REEMBOLSO_POR_HOMOLOGACAO)
		 * 4- Gravar tudo em ICM_REEMBOLSO_TRABALHO
		 * 5- Chamar ProcessadorReembolsos.processa()
		 * 6- Com tudo OK ou não, finalizar o processamento (gravar hora de fim de processamento) 
		 * 
		 */
		
		// Passo 1: tentando ler o período informado:
		
		logger.info("Iniciando processamento PUT: processarPorDataCredito");
		
		Response response = null;
		PeriodoDataCredito periodoDataCredito = null;
		Processamento processamento = null;		
		
		ResumoProcessamento resumoProcessamento = null;
		
		try {
			periodoDataCredito = mapper.readValue(jsonPeriodo, PeriodoDataCredito.class);				
			
			logger.infof("Período informado: %s", periodoDataCredito.toString());	
									
			TipoProcessamento tipoProcessamentoLoteTitulos = tpDao.findByCodProcessamento(TipoProcessamento.LOTE_REEMBOLSOS);
			
			processamento = processador.inicializaProcessamento(Calendar.getInstance().getTime(), 
					tipoProcessamentoLoteTitulos, periodoDataCredito);
			pDao.persist(processamento);
			
			logger.infof("Processamento criado. Id: %d", 
						processamento.getIdProcessamento()
					);
			
			geraReembolsosTrabalho(processamento, periodoDataCredito);

			resumoProcessamento = processador.processa(processamento);
						
		} catch (JsonParseException e) {
			response = ResponseUtil.gerarResponse(Response.Status.BAD_REQUEST,
					"Formato inválido de JSON. Verifique se o conteúdo enviado é um JSON válido.");	
		} catch (JsonProcessingException e){
			response = ResponseUtil.gerarResponse(Response.Status.INTERNAL_SERVER_ERROR,
					"Formato inválido de JSON. Deve estar na estutrura de PeriodoDataCredito. " + e.getMessage());
		} catch (IOException e) {
			response = ResponseUtil.gerarResponse(Response.Status.INTERNAL_SERVER_ERROR,
					"Erro genérico de IO. Mensagem de erro: " + e.getMessage());
			
		} catch (ProcessadorException e) {
			response = ResponseUtil.gerarResponse(Response.Status.INTERNAL_SERVER_ERROR,
					"Erro no processamento do período de reembolso informado: " + e.getMessage()
					);
		}
		
		if (processamento != null){
			processamento.setDthrFimProcessamento(Calendar.getInstance().getTime());
			pDao.merge(processamento);
			logger.infof("Fim do processamento. Id: %d", processamento.getIdProcessamento());
		}
		
		if (response == null) {
			response = ResponseUtil.geraResponseResumoProcessamento(Response.Status.OK, resumoProcessamento);
		}
				
		
		return response;
	}	
	
	
	private void gravaReembolsosTrabalho(Processamento processamento, List<Atendimento> atendimentos) {
		StatusProcessamento spAdicionado = spDao.findByCodStatus(StatusProcessamento.ADICIONADO);
		
		
		for (Atendimento at : atendimentos){
			ReembolsoTrabalho reembolsoTrabalho = new ReembolsoTrabalho();
			reembolsoTrabalho.setAtendimentoId(at.getAtendimentoId());
			reembolsoTrabalho.setProcessamento(processamento);
			reembolsoTrabalho.setStatusProcessamento(spAdicionado);
			
			rtDao.persist(reembolsoTrabalho);
		}
	}	
	
	private void geraReembolsosTrabalho(Processamento processamento, Periodo periodoHomologacao) throws ProcessadorException{
		// TODO: Refatorar para separar esse pré-processamento para outra classe
		try {
			List<Atendimento> atendimentos = listaAtendimentosReembolso(periodoHomologacao);		
					
			gravaReembolsosTrabalho(processamento, atendimentos);
		} catch (PersistenceException e) {
			throw new ProcessadorException(e);
		}	
				
	}
	
	private void geraReembolsosTrabalho(Processamento processamento, PeriodoDataCredito periodoDataCredito) throws ProcessadorException{
		// TODO: Refatorar para separar esse pré-processamento para outra classe
		try {
			List<Atendimento> atendimentos = listaAtendimentosReembolso(periodoDataCredito);		
					
			gravaReembolsosTrabalho(processamento, atendimentos);
		} catch (PersistenceException e) {
			throw new ProcessadorException(e);
		}	
				
	}	
	
	private void geraReembolsosTrabalho(Processamento processamento, SubProcessoId subProcesso) throws ProcessadorException{
		try {
			List<Atendimento> atendimentos = listaAtendimentosReembolso(subProcesso);
			
			if ( (atendimentos == null) || (atendimentos.isEmpty()) ){
				throw new ProcessadorException("Não foram encontrados reembolsos válidos para o sub-processo %s",
						subProcesso.toString()
						);
				
			}
			
			gravaReembolsosTrabalho(processamento, atendimentos);
		} catch (PersistenceException e) {
			throw new ProcessadorException(e);
		}
	}
	
	@SuppressWarnings("unchecked")
	private List<Atendimento> listaAtendimentosReembolso(Periodo periodoHomologacao){
		StoredProcedureQuery listaAtendimentosReembolsoProcedure = manager.createNamedStoredProcedureQuery("listaAtendimentosReembolsoPorHomologacao")
				.setParameter("dt_homologacao_inicial", periodoHomologacao.getDtInicial())
				.setParameter("dt_homologacao_final", periodoHomologacao.getDtFinal());
		
		return listaAtendimentosReembolsoProcedure.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	private List<Atendimento> listaAtendimentosReembolso(PeriodoDataCredito periodoDataCredito){
		StoredProcedureQuery listaAtendimentosReembolsoProcedure = manager.createNamedStoredProcedureQuery("listaAtendimentosReembolsoPorDataCredito")
				.setParameter("dt_credito_inicial", periodoDataCredito.getDtInicial())
				.setParameter("dt_credito_final", periodoDataCredito.getDtFinal());
		
		return listaAtendimentosReembolsoProcedure.getResultList();
	}	
	
	@SuppressWarnings("unchecked")
	private List<Atendimento> listaAtendimentosReembolso(SubProcessoId subProcesso){	
		StoredProcedureQuery listaAtendimentosReembolsoProcedure = manager.createNamedStoredProcedureQuery("listaAtendimentosReembolso")
				.setParameter("ano_apresentacao", subProcesso.getAnoApresentacao())
				.setParameter("id_representacao", subProcesso.getIdRepresentacao())
				.setParameter("id_processo", subProcesso.getIdProcesso())
				.setParameter("d_sub_processo", subProcesso.getdSubProcesso())
				.setParameter("d_natureza", subProcesso.getdNatureza())
				.setParameter("id_sequencial_natureza", subProcesso.getIdSequencialNatureza());
		
		return listaAtendimentosReembolsoProcedure.getResultList();
	}
}
