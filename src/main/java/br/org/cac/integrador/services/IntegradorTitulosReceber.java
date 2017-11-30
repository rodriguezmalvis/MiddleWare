package br.org.cac.integrador.services;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.persistence.PersistenceException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jboss.logging.Logger;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.org.cac.cacmvmiddleware.services.TitulosReceberWebServices;
import br.org.cac.integrador.dao.ItemMensalidadeTrabalhoDAO;
import br.org.cac.integrador.dao.MensalidadeTrabalhoDAO;
import br.org.cac.integrador.dao.ProcessamentoDAO;
import br.org.cac.integrador.dao.ProjecaoMensalidadeTrabalhoDAO;
import br.org.cac.integrador.dao.StatusProcessamentoDAO;
import br.org.cac.integrador.dao.TipoProcessamentoDAO;
import br.org.cac.integrador.exception.ProcessadorException;
import br.org.cac.integrador.jacksonutil.ObjectMapperFactory;
import br.org.cac.integrador.modelo.MensalidadeContrato;
import br.org.cac.integrador.modelo.Periodo;
import br.org.cac.integrador.modelo.ProjecaoMensalidadeTrabalho;
import br.org.cac.integrador.modelo.RetornoMensagem;
import br.org.cac.integrador.modelo.infraestrutura.DeParaGrupoItemMensalidade;
import br.org.cac.integrador.modelo.infraestrutura.ItemMensalidadeTrabalho;
import br.org.cac.integrador.modelo.infraestrutura.MensalidadeTrabalho;
import br.org.cac.integrador.modelo.infraestrutura.Processamento;
import br.org.cac.integrador.modelo.infraestrutura.StatusProcessamento;
import br.org.cac.integrador.modelo.infraestrutura.TipoProcessamento;
import br.org.cac.integrador.processadores.ProcessadorTitulosReceber;
import br.org.cac.integrador.processadores.ResumoProcessamento;
import br.org.cac.integrador.util.ResponseUtil;
import br.org.cac.integrador.util.ThrowableUtil;

/**
 * <p>Classe responsável por fazer o processamento de comandos, como entendidos pela
 * CAC, agrupando-os e transformando-os em mensalidades, como entendidos pela MV, e
 * realizando sua inclusão na base de dados da MV, através de {@link TitulosReceberWebServices}.
 * </p>
 * 
 * <p>Atualmente, contém os seguintes serviços:
 * <ul>
 *     <li>{@link #processarPorPeriodo(String)} - faz a integração de vários comandos, levando em 
 *     consideração suas datas de geração e referência;</li>
 * </ul>
 * </p>
 * 
 * <p>O JSON produzido por estes serviços possui a estrutura definida em {@link RetornoMensagem}.</p>
 * 
 * @author JCJ
 * @version 1.1
 * @since 1.1, 2017-03-28
 */
@Path("IntegradorTitulosReceber")
public class IntegradorTitulosReceber {
	
	private static final String SERVICO_TITULOS_INUTILIZADO = "Este serviço foi inutilizado. Use uma das opções 'processarFolha' ou 'processarBoleto'";
	
	@Inject
	private ItemMensalidadeTrabalhoDAO imtDao;

	@Inject
	private MensalidadeTrabalhoDAO mtDao;
	
	@Inject
	private ProcessamentoDAO pDao;
	
	@Inject
	private StatusProcessamentoDAO spDao;
	
	@Inject
	private TipoProcessamentoDAO tpDao;
	
	@Inject
	private ProjecaoMensalidadeTrabalhoDAO pmtDao;	
	
	@Inject
	private ProcessadorTitulosReceber processador;
	
	@Inject
	private Logger logger;
	
	private ObjectMapper mapper;
	
	public IntegradorTitulosReceber(){
		this.mapper = ObjectMapperFactory.getDefaultConfiguredObjectMapper(false);
	}
	
	
	/**
	 * <p>Serviço {@code PUT} que realiza a integração de conjuntos de comandos, baseado
	 * em suas datas de geração e de referência.<br/>
	 * URL: <a href="http://10.10.20.32:8080/CacMvMiddleware/IntegradorContasMedicas/processarPorPeriodo">
	 * http://10.10.20.32:8080/CacMvMiddleware/IntegradorTitulosReceber/processarPorPeriodo</a>
	 * </p>
	 * 
	 * <p>O JSON consumido por esse serviço possui a seguinte estrutura:
	 * <pre>{
	 *  "dtReferencia": &lt;data no formato DD/MM/AAAA&gt;,
	 *  "dtInicial": &lt;data no formato DD/MM/AAAA&gt;,
	 *  "dtFinal": &lt;data no formato DD/MM/AAAA&gt;
	 *}</pre>
	 * Esses campos possuem o seguinte significado:
	 * <ul>
	 *     <li>dtReferencia: Data de referência base: Só serão incluídos comandos com data de referência 
	 *     igual ou menor que dtReferencia;</li>
	 *     <li>dtInicial e dtFinal: Datas de geração inicial e final: Só serão incluídos comandos cujas
	 *     datas de geração estejam dentro do período definido por dtInicial e dtFinal.</li>
	 * </ul>
	 * </p>
	 *
	 * @param jsonPeriodo Representação {@code String} de um JSON que indica o período de datas de
	 * geração dos comandos a serem enviados, e a data de referência base que deve ser considerada.
	 * @return {@link Response} no Padrão HTTP que contém um JSON com as informações da Resposta do Soul MV.
	 * O JSON retornado possui o {@code StatusCode} da Operação, as informações da Inclusão, e ID da última 
	 * Mensalidade incluída, ou com mensagens de erro, de acordo com a estrutura em {@link RetornoMensagem}.
	 * 
	 * @deprecated A ser substituído pelos novos serviços em {@link #processarBoleto} e {@link #processarFolha}.
	 */
	@PUT
	@Path("processarPorPeriodo")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)	
	public Response processarPorPeriodo(String jsonPeriodo){		
		return ResponseUtil.gerarResponse(Response.Status.GONE, SERVICO_TITULOS_INUTILIZADO);		
	}
	
	/**
	 * @deprecated A ser substituído pelos novos serviços em {@link #processarBoleto} e {@link #processarFolha}.
	 */
	@PUT
	@Path("processarPorDiops")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)	
	@Deprecated
	public Response processarPorDiops(String jsonPeriodoDiops) {
		return ResponseUtil.gerarResponse(Response.Status.GONE, SERVICO_TITULOS_INUTILIZADO);	
	}


	@POST
	@Path("continuarProcessamento/{idProcessamento}")
	@Produces(MediaType.APPLICATION_JSON)	
	public Response continuarProcessamento(@PathParam("idProcessamento") String idProcessamento){
		logger.info("Iniciando processamento POST: continuarProcessamento/" + idProcessamento);
		
		Response response = null;
		ResumoProcessamento resumoProcessamento = null;
		
		Processamento processamento = null;
		
		try {
			Integer iIdProcessamento = Integer.valueOf(idProcessamento);
			
			// TODO: Criar separação entre continuar processamento (pega apenas os que estão nos estados "em processamento" ou "adicionado" )
			//       e o reprocessa (pega todos os que não estão fechados)
			
			logger.info("Tentando localizar processamento de Id " + idProcessamento);
			
			processamento = pDao.findById(iIdProcessamento);
			
			if (processamento == null){
				throw new ProcessadorException(String.format("Processamento de id %d não encontrado.", 
						iIdProcessamento
						));				
			}
			
			// TODO: Adicionar teste para verificar se o processamento realmente foi interrompido, ou se já está finalizado.
			// 		 No primeiro momento, esse teste não será implementado, pois o continuaProcessamento será usado como reprocessa
			
			logger.info("Processamento localizado. Id: " + processamento.getIdProcessamento());
			logger.info("Continuando processamento...");
						
			resumoProcessamento = processador.processa(processamento);			
			
		} catch (NumberFormatException e){
			response = ResponseUtil.gerarResponse(Response.Status.BAD_REQUEST,
					"Id de processamento recebido como parâmetro não é um número: " + idProcessamento
					);
		} catch (ProcessadorException e) {
			response = ResponseUtil.gerarResponse(Response.Status.INTERNAL_SERVER_ERROR,
					"Erro no processamento de títulos: " + ThrowableUtil.getRootMessage(e));
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

	@GET
	@Path("gerarJson/{idMensalidadeTrabalho}")
	@Produces(MediaType.APPLICATION_JSON)	
	public Response gerarJson(@PathParam("idMensalidadeTrabalho") String sIdMensalidadeTrabalho){
		logger.info("Iniciando processamento GET: gerarJson");
		Response response = null;		
		
		try {
			
			Integer idMensalidadeTrabalho = Integer.valueOf(sIdMensalidadeTrabalho);
						
			logger.infof("Tentando localizar MensalidadeTrabalho de Id %d.",
					idMensalidadeTrabalho);
			
			MensalidadeTrabalho mensalidadeTrabalho = mtDao.findById(idMensalidadeTrabalho);
			
			if (mensalidadeTrabalho == null){
				throw new ProcessadorException(String.format("MensalidadeTrabalho de id %d não encontrada.", 
						idMensalidadeTrabalho
						));				
			}
			
			MensalidadeContrato mensalidadeContrato = new MensalidadeContrato();
			
			// TODO: Removida a chamada ao método processaMensalidadeTrabalhoCobranca, devido à migração. Futuramente será
			//       retornado mas de outra maneira.
			// processador.processaMensalidadeTrabalhoCobranca(mensalidadeTrabalho);
			
			response = Response.status(Response.Status.OK)
					.entity(mapper.writeValueAsString(mensalidadeContrato))
					.type(MediaType.APPLICATION_JSON)
					.build();
			
		
		} catch (NumberFormatException e){
			response = ResponseUtil.gerarResponse(Response.Status.BAD_REQUEST,
						String.format("Id de processamento recebido como parâmetro não é um número: %s.", 
								sIdMensalidadeTrabalho)
					);
		} catch (JsonProcessingException e){
			response = ResponseUtil.gerarResponse(Response.Status.INTERNAL_SERVER_ERROR,
					"Erro ao gerar JSON para a MensalidadeTrabalho informada: " + ThrowableUtil.getRootMessage(e));
		} catch (ProcessadorException e) {
			response = ResponseUtil.gerarResponse(Response.Status.INTERNAL_SERVER_ERROR,
					"Erro no processamento da MensalidadeTrabalho informada: " + ThrowableUtil.getRootMessage(e)
					);
		}

		return response;										
	}

	@PUT
	@Path("processarBoleto")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)	
	public Response processarBoleto(String jsonBoleto){
		logger.info("Iniciando processamento PUT: processarBoleto");
		
		Response response = null;
		ResumoProcessamento resumoProcessamento = null;
		
		Processamento processamento = null;
		
		try {
			PeriodoBoleto periodoBoleto = mapper.readValue(jsonBoleto, PeriodoBoleto.class);
			logger.info("PeriodoBoleto recebido: " + periodoBoleto.toString());			
			
			logger.info("Verificando se existem mensalidades inconsistentes ainda não resolvidas");
			
			List<DeParaGrupoItemMensalidade> gruposInconsistentes = processador.verificaMensalidadesInconsistentes();
			
			if (!Optional.ofNullable(gruposInconsistentes).orElse(Collections.emptyList()).isEmpty()) {
				List<String> erros = new ArrayList<>();
				
				erros.add("Existem mensalidades inconsistentes ainda não resolvidas. Não é possível criar um novo "
						+ "processamento enquanto essas mensalidades não forem resolvidas.");
				
				erros.addAll(processador.geraMensagensDetalheStatus(gruposInconsistentes));
				
				response = ResponseUtil.gerarResponse(Response.Status.INTERNAL_SERVER_ERROR, erros.toArray(new String[0]));
				
			} else {							
				TipoProcessamento tipoProcessamentoLoteBoleto = tpDao.findByCodProcessamento(TipoProcessamento.LOTE_TITULOS_BOLETO);
				
				processamento = processador.inicializaProcessamento(Calendar.getInstance().getTime(), 
						tipoProcessamentoLoteBoleto, periodoBoleto.toPeriodo());
				pDao.persist(processamento);
				
				logger.info("Processamento criado. Id: " + processamento.getIdProcessamento());
				
				// TODO: Gerar mensalidades trabalho em separado, em outra classe
				// TODO: Tratar possíveis exceções lançadas por este método
				geraMensalidadesTrabalho(processamento, periodoBoleto);
				
				resumoProcessamento = processador.processa(processamento);

			}
			
		} catch (JsonParseException e) {
			response = ResponseUtil.gerarResponse(Response.Status.BAD_REQUEST,
					"Formato inválido de JSON. Verifique se o conteúdo enviado é um JSON válido.");	

		} catch (JsonMappingException e) {
			response = ResponseUtil.gerarResponse(Response.Status.BAD_REQUEST,
					"Formato inválido de JSON. Deve estar na estutrura de PeriodoBoleto.");

		} catch (IOException e) {
			response = ResponseUtil.gerarResponse(Response.Status.INTERNAL_SERVER_ERROR,
					"Erro genérico de IO. Mensagem de erro: " + e.getMessage());

		} catch (ProcessadorException e) {
			response = ResponseUtil.gerarResponse(Response.Status.INTERNAL_SERVER_ERROR,
					"Erro no processamento de títulos: " + ThrowableUtil.getRootMessage(e));
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
	
	@PUT
	@Path("processarFolha")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)	
	public Response processarFolha(String jsonFolha){
		logger.info("Iniciando processamento PUT: processarFolha");
		
		Response response = null;
		ResumoProcessamento resumoProcessamento = null;
		
		Processamento processamento = null;
		
		try {
			PeriodoFolha periodoFolha = mapper.readValue(jsonFolha, PeriodoFolha.class);
			logger.info("PeriodoFolha recebido: " + periodoFolha.toString());			
			
			logger.info("Verificando se existem mensalidades inconsistentes ainda não resolvidas");
			
			List<DeParaGrupoItemMensalidade> gruposInconsistentes = processador.verificaMensalidadesInconsistentes();
			
			if (!Optional.ofNullable(gruposInconsistentes).orElse(Collections.emptyList()).isEmpty()) {
				List<String> erros = new ArrayList<>();
				
				erros.add("Existem mensalidades inconsistentes ainda não resolvidas. Não é possível criar um novo "
						+ "processamento enquanto essas mensalidades não forem resolvidas.");
				
				erros.addAll(processador.geraMensagensDetalheStatus(gruposInconsistentes));
				
				response = ResponseUtil.gerarResponse(Response.Status.INTERNAL_SERVER_ERROR, erros.toArray(new String[0]));
				
			} else {							
				TipoProcessamento tipoProcessamentoLoteFolha = tpDao.findByCodProcessamento(TipoProcessamento.LOTE_TITULOS_FOLHA);
				
				processamento = processador.inicializaProcessamento(Calendar.getInstance().getTime(), 
						tipoProcessamentoLoteFolha, periodoFolha.toPeriodo());
				pDao.persist(processamento);
				
				logger.info("Processamento criado. Id: " + processamento.getIdProcessamento());
				
				geraMensalidadesTrabalho(processamento, periodoFolha);
				
				resumoProcessamento = processador.processa(processamento);

			}
			
		} catch (JsonParseException e) {
			response = ResponseUtil.gerarResponse(Response.Status.BAD_REQUEST,
					"Formato inválido de JSON. Verifique se o conteúdo enviado é um JSON válido.");	

		} catch (JsonMappingException e) {
			response = ResponseUtil.gerarResponse(Response.Status.BAD_REQUEST,
					"Formato inválido de JSON. Deve estar na estutrura de PeriodoFolha.");

		} catch (IOException e) {
			response = ResponseUtil.gerarResponse(Response.Status.INTERNAL_SERVER_ERROR,
					"Erro genérico de IO. Mensagem de erro: " + e.getMessage());

		} catch (ProcessadorException e) {
			response = ResponseUtil.gerarResponse(Response.Status.INTERNAL_SERVER_ERROR,
					"Erro no processamento de títulos: " + ThrowableUtil.getRootMessage(e));
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
	
	private void geraMensalidadesTrabalho(Processamento processamento, PeriodoBoleto periodoBoleto)
			throws ProcessadorException {
		List<ProjecaoMensalidadeTrabalho> projecoesMensalidadeTrabalho = pmtDao.listaProjecoesMensalidadeTrabalhoBoleto(
				periodoBoleto.getDtReferencia(), 
				periodoBoleto.getDtVencimentoBase(),
				periodoBoleto.getDtVencimentoLimite());

		try {
			gravaProjecoesMensalidadeTrabalho(processamento, projecoesMensalidadeTrabalho);
		} catch (PersistenceException e) {
			throw new ProcessadorException(e);
		}
	}
	
	private void geraMensalidadesTrabalho(Processamento processamento, PeriodoFolha periodoFolha) 
			throws ProcessadorException {
		List<ProjecaoMensalidadeTrabalho> projecoesMensalidadeTrabalho = pmtDao.listaProjecoesMensalidadeTrabalhoFolha(
				periodoFolha.getDtReferencia());
		
		try {
			gravaProjecoesMensalidadeTrabalho(processamento, projecoesMensalidadeTrabalho);
		} catch (PersistenceException e) {
			throw new ProcessadorException(e);
		}
	}


	/**
	 * @param processamento
	 * @param projecoesMensalidadeTrabalho
	 */
	private void gravaProjecoesMensalidadeTrabalho(Processamento processamento,
			List<ProjecaoMensalidadeTrabalho> projecoesMensalidadeTrabalho) {
		StatusProcessamento spAdicionado = spDao.findByCodStatus(StatusProcessamento.ADICIONADO);
		
		Map<Integer, List<ProjecaoMensalidadeTrabalho>> projecoesAgrupadas =
				projecoesMensalidadeTrabalho.stream()
				.collect(Collectors.groupingBy(p -> p.getSeqMensalidade()));
		
		for (Integer seqMensalidade : projecoesAgrupadas.keySet()){
			List<ProjecaoMensalidadeTrabalho> projecoes = projecoesAgrupadas.get(seqMensalidade);
			
			MensalidadeTrabalho mensalidadeTrabalho = new MensalidadeTrabalho();
			
			// Como as ProjecaoMensalidadeTrabalho estão agrupadas pelos dados que definem um MensalidadeTrabalho,
			// a primeira ProjecaoMensalidadeTrabalho irá servir como base para preenchimento de MensalidadeTrabalho
			ProjecaoMensalidadeTrabalho projecaoBase = projecoes.get(0);
			
			mensalidadeTrabalho.setProcessamento(processamento);
			mensalidadeTrabalho.setStatusProcessamento(spAdicionado);
			mensalidadeTrabalho.setIdTitular(projecaoBase.getIdTitular());
			mensalidadeTrabalho.setFormaPagamento(projecaoBase.getFormaPagamento());
			mensalidadeTrabalho.setDtEmissao(projecaoBase.getDtEmissao());
			mensalidadeTrabalho.setDtReferenciaPrevista(projecaoBase.getDtReferenciaPrevista());
			mensalidadeTrabalho.setDtVencimento(projecaoBase.getDtVencimento());
			mensalidadeTrabalho.setItensMensalidadeTrabalho(new ArrayList<>());
			mtDao.persist(mensalidadeTrabalho);
			
			// Então, as ProjecaoMensalidadeTrabalho são iteradas, a fim de gerar os ItemMensalidadeTrabalho
			for (ProjecaoMensalidadeTrabalho pmt : projecoes){
				ItemMensalidadeTrabalho itemMensalidadeTrabalho = new ItemMensalidadeTrabalho();
				
				itemMensalidadeTrabalho.setMensalidadeTrabalho(mensalidadeTrabalho);
				itemMensalidadeTrabalho.setIdComando(pmt.getIdComando());
				mensalidadeTrabalho.getItensMensalidadeTrabalho().add(itemMensalidadeTrabalho);
				
				imtDao.persist(itemMensalidadeTrabalho);
			}
		}
	}
	
	/**
	 * Classe utilizada para representar o JSON esperado no serviço {@code processarFolha}.
	 * @author JCJ
	 * @since 2017-10-02
	 * @see #processarFolha
	 *
	 */
	public static class PeriodoFolha{
		private Date dtReferencia;
		
		/**
		 * Construtor padrão.
		 */
		public PeriodoFolha() {
			// Construtor padrão.
		}

		/** Getter para dtReferencia.
		 * @return o valor de dtReferencia.
		 */
		public Date getDtReferencia() {
			return dtReferencia;
		}

		/** Setter para dtReferencia.
		 * @param dtReferencia o novo valor de dtReferencia.
		 */
		public void setDtReferencia(Date dtReferencia) {
			this.dtReferencia = dtReferencia;
		}
		
		public Periodo toPeriodo() {
			Periodo periodo = new Periodo();
			periodo.setDtInicial(this.dtReferencia);
			
			if (this.dtReferencia != null) {
				Calendar fimMesReferencia = Calendar.getInstance();
				fimMesReferencia.setTime(dtReferencia);
				fimMesReferencia.add(Calendar.MONTH, 1);
				fimMesReferencia.add(Calendar.DATE, -1);
				periodo.setDtFinal(fimMesReferencia.getTime());
			}
			
			return periodo;			
		}
		
		/* (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			
			return String.format("PeriodoFolha: Data de Referência: %s",
						this.dtReferencia == null ? "não preenchida" : sdf.format(this.dtReferencia)
					); 
		}
				
	}
	
	/**
	 * Classe utilizada para representar o JSON esperado no serviço {@code processarBoleto}.
	 * @author JCJ
	 * @since 2017-10-02
	 * @see #processarBoleto
	 *
	 */
	public static class PeriodoBoleto{
		private Date dtReferencia;
		
		private Date dtVencimentoBase;
		
		private Date dtVencimentoLimite;
		
		/**
		 * Construtor padrão.
		 */
		public PeriodoBoleto() {
			// Construtor padrão.
		}

		/** Getter para dtReferencia.
		 * @return o valor de dtReferencia.
		 */
		public Date getDtReferencia() {
			return dtReferencia;
		}

		/** Setter para dtReferencia.
		 * @param dtReferencia o novo valor de dtReferencia.
		 */
		public void setDtReferencia(Date dtReferencia) {
			this.dtReferencia = dtReferencia;
		}

		/** Getter para dtVencimentoBase.
		 * @return o valor de dtVencimentoBase.
		 */
		public Date getDtVencimentoBase() {
			return dtVencimentoBase;
		}

		/** Setter para dtVencimentoBase.
		 * @param dtVencimentoBase o novo valor de dtVencimentoBase.
		 */
		public void setDtVencimentoBase(Date dtVencimentoBase) {
			this.dtVencimentoBase = dtVencimentoBase;
		}

		/** Getter para dtVencimentoLimite.
		 * @return o valor de dtVencimentoLimite.
		 */
		public Date getDtVencimentoLimite() {
			return dtVencimentoLimite;
		}

		/** Setter para dtVencimentoLimite.
		 * @param dtVencimentoLimite o novo valor de dtVencimentoLimite.
		 */
		public void setDtVencimentoLimite(Date dtVencimentoLimite) {
			this.dtVencimentoLimite = dtVencimentoLimite;
		}
		
		public Periodo toPeriodo() {
			return new Periodo(dtReferencia, dtVencimentoLimite);
		}
		
		/* (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			
			return String.format("PeriodoBoleto: Data de Referência: %s; Data de vencimento base: %s; Data de vencimento limite: %s",
						this.dtReferencia == null ? "não preenchida" : sdf.format(this.dtReferencia),
						this.dtVencimentoBase == null ? "não preenchida" : sdf.format(this.dtVencimentoBase),
						this.dtVencimentoLimite == null ? "não preenchida" : sdf.format(this.dtVencimentoLimite)
					); 
		}
		
	}	
}
