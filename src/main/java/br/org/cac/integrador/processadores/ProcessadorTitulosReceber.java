package br.org.cac.integrador.processadores;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.persistence.PersistenceException;
import javax.ws.rs.core.Response;

import org.jboss.logging.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;

import br.org.cac.cacmvmiddleware.modelo.retorno.Beneficiario;
import br.org.cac.cacmvmiddleware.services.ReembolsoWebServices;
import br.org.cac.cacmvmiddleware.services.TitulosReceberWebServices;
import br.org.cac.integrador.dao.DeParaGrupoItemMensalidadeDAO;
import br.org.cac.integrador.dao.DeParaMensalidadeDAO;
import br.org.cac.integrador.dao.ErroProcessamentoMensalidadeDAO;
import br.org.cac.integrador.dao.GrupoItemMensalidadeTrabalhoDAO;
import br.org.cac.integrador.dao.ImpCoparticipacaoDAO;
import br.org.cac.integrador.dao.ItemMensalidadeTrabalhoDAO;
import br.org.cac.integrador.dao.MensalidadeTrabalhoDAO;
import br.org.cac.integrador.dao.ProjecaoGrupoItemMensalidadeTrabalhoDAO;
import br.org.cac.integrador.dao.ProjecaoMensalidadeContratoDAO;
import br.org.cac.integrador.dao.ProjecaoMensalidadeDevolucaoDAO;
import br.org.cac.integrador.dao.StatusEnvioGrupoItemMensalidadeDAO;
import br.org.cac.integrador.exception.ProcessadorException;
import br.org.cac.integrador.modelo.ImpCoparticipacao;
import br.org.cac.integrador.modelo.ItemMensalidadeDevolucao;
import br.org.cac.integrador.modelo.ItemMensalidadeUsuario;
import br.org.cac.integrador.modelo.MensalidadeContrato;
import br.org.cac.integrador.modelo.MensalidadeDevolucao;
import br.org.cac.integrador.modelo.ProjecaoMensalidadeContrato;
import br.org.cac.integrador.modelo.ProjecaoMensalidadeDevolucao;
import br.org.cac.integrador.modelo.RetornoMensagem;
import br.org.cac.integrador.modelo.TipoRubricaCac;
import br.org.cac.integrador.modelo.infraestrutura.DeParaGrupoItemMensalidade;
import br.org.cac.integrador.modelo.infraestrutura.DeParaItemMensalidade;
import br.org.cac.integrador.modelo.infraestrutura.DeParaMensalidade;
import br.org.cac.integrador.modelo.infraestrutura.ErroProcessamentoMensalidade;
import br.org.cac.integrador.modelo.infraestrutura.GrupoItemMensalidadeTrabalho;
import br.org.cac.integrador.modelo.infraestrutura.ItemMensalidadeTrabalho;
import br.org.cac.integrador.modelo.infraestrutura.MensalidadeTrabalho;
import br.org.cac.integrador.modelo.infraestrutura.Processamento;
import br.org.cac.integrador.modelo.infraestrutura.ProjecaoGrupoItemMensalidadeTrabalho;
import br.org.cac.integrador.modelo.infraestrutura.StatusEnvioGrupoItemMensalidade;
import br.org.cac.integrador.modelo.infraestrutura.StatusProcessamento;
import br.org.cac.integrador.util.Constantes;
import br.org.cac.integrador.util.NumberUtil;
import br.org.cac.integrador.util.ThrowableUtil;

/**
 * Processador responsável por realizar o processamento de títulos a receber;
 * 
 * @author JCJ
 * @version 1.1
 * @since 1.1, 2017-03-28
 *
 */
public class ProcessadorTitulosReceber extends AbstractProcessador{
		
	@Inject
	private DeParaMensalidadeDAO dpmDao;
	
	@Inject
	private ErroProcessamentoMensalidadeDAO epmDao;
	
	@Inject
	private MensalidadeTrabalhoDAO mtDao;
	
	@Inject
	private ItemMensalidadeTrabalhoDAO imtDao;
	
	@Inject
	private ProjecaoMensalidadeContratoDAO pmcDao;
	
	@Inject
	private ProjecaoMensalidadeDevolucaoDAO pmdDao;
	
	@Inject
	private ImpCoparticipacaoDAO icDao;
	
	@Inject
	private ProjecaoGrupoItemMensalidadeTrabalhoDAO pgimtDao;
	
	@Inject
	private GrupoItemMensalidadeTrabalhoDAO gimtDao;
	
	@Inject
	private StatusEnvioGrupoItemMensalidadeDAO segimDao;
	
	@Inject
	private DeParaGrupoItemMensalidadeDAO dpgimDao;
	
	@Inject
	private TitulosReceberWebServices webServiceTitulosReceber;
	
	@Inject
	private ReembolsoWebServices webServiceReembolso;
	
	@Inject
	private Logger logger;
	
	private Map<String, List<StatusEnvioGrupoItemMensalidade>> todosStatusEnvioGrupoItemMensalidade;
	
	/**
	 * Construtor padrão.
	 */
	public ProcessadorTitulosReceber(){
		super();
	}
	
	private StatusEnvioGrupoItemMensalidade getStatusEnvioGrupoItemMensalidade(String codStatus){
		if (todosStatusEnvioGrupoItemMensalidade == null){
			initTodosStatusEnvioGrupoItemMensalidade();
		}
		
		List<StatusEnvioGrupoItemMensalidade> candidatos = todosStatusEnvioGrupoItemMensalidade.get(codStatus);
		
		// TODO: Melhorar a implementação para ser um Map<String, StatusProcessamento>, usando Collectors.toMap()
		if ((candidatos != null) && (!candidatos.isEmpty())){
			return todosStatusEnvioGrupoItemMensalidade.get(codStatus).get(0);	
		}
		
		return null;
		
	}
	
	private void initTodosStatusEnvioGrupoItemMensalidade(){		
		List<StatusEnvioGrupoItemMensalidade> statusEnvioGrupoItemMensalidade = segimDao.findAll();
		
		todosStatusEnvioGrupoItemMensalidade = statusEnvioGrupoItemMensalidade.stream()
				.collect(Collectors.groupingBy(c -> c.getCodStatus()));	
	}	
	
	public List<DeParaGrupoItemMensalidade> verificaMensalidadesInconsistentes() {			
		return dpgimDao.listByStatusConsistencia(true);
	}

	public List<String> geraMensagensDetalheStatus(
			List<DeParaGrupoItemMensalidade> gruposInconsistentes) {		
		return gruposInconsistentes.stream()
				.map(gi -> gi.detalhaStatus())
				.collect(Collectors.toList());
	}
	
	/* (non-Javadoc)
	 * @see br.org.cac.integrador.processadores.AbstractProcessador#processa(br.org.cac.integrador.modelo.infraestrutura.Processamento)
	 */
	@Override
	public ResumoProcessamento processa(Processamento processamento) throws ProcessadorException {
		
		// 1o passo: Testar argumento para null
		if (processamento == null){
			throw new ProcessadorException("O parâmetro processamento não pode ser null.");
		}
		
		ResumoProcessamento resumoProcessamento = new ResumoProcessamento(processamento);
		
		logger.info("Iniciando processamento. Id: " + 
				processamento.getIdProcessamento());
		
		// 2o passo: Buscar MensalidadesTrabalho associadas a esse processamento
		List<MensalidadeTrabalho> mensalidadesTrabalho = mtDao.findByProcessamento(processamento);
		
		// 3o passo: Iniciar WS de Titulos a Receber, assim como DAOs dos de-para
		Response response = null;
		
		logger.infof("%d Mensalidade%sTrabalho para processamento",
											mensalidadesTrabalho.size(),
											(mensalidadesTrabalho.size() == 1 ? "" : "s")
				);
		resumoProcessamento.setTotalItens(mensalidadesTrabalho.size());

		// 4o passo: Para cada MensalidadeTrabalho encontrada:
		for (MensalidadeTrabalho mt : mensalidadesTrabalho){
			//TODO: Tratar possíveis exceções deste trecho
			logger.infof("Iniciando processamento da MensalidadeTrabalho Id: %d.", 
						mt.getIdMensalidadeTrabalho()
					);
			
			mt.setStatusProcessamento(getStatusProcessamento(StatusProcessamento.EM_PROCESSAMENTO));
			mtDao.merge(mt);
						
			Map<GrupoItemMensalidadeTrabalho, MensalidadeContrato> gruposComMensalidadesContrato = new HashMap<>();
			Map<GrupoItemMensalidadeTrabalho, MensalidadeDevolucao> gruposComMensalidadesDevolucao = new HashMap<>();
			
			// Primeiro, é preciso agrupar os itens de trabalho, criando grupos para classificá-los.
			// Esses grupos servirão de base para o envio de cobranças e devoluções desta mensalidade.
			try {
				// TODO: verificar teste para saber se precisa realmente gerar os grupos
				geraGruposItemMensalidadeTrabalho(mt);
			} catch (ProcessadorException e) {
				gerarErroProcessamentoMensalidadeTrabalho(mt, 
						String.format("Erro na geração dos grupos de itens da Mensalidade de Trabalho %d: %s.", 
								mt.getIdMensalidadeTrabalho(),
								ThrowableUtil.getRootMessage(e))
						);
				resumoProcessamento.incItensComErro();
				continue;
			}
			
			// Com os grupos gerados, serão listados novamente (pois não é possível dizer se foram efetivamente gerados
			// no passo anterior ou se já existiam).
			
			List<GrupoItemMensalidadeTrabalho> gruposItemMensalidadeTrabalho;
			try {
				gruposItemMensalidadeTrabalho = gimtDao.findByMensalidadeTrabalho(mt);
			} catch (PersistenceException e) {
				gerarErroProcessamentoMensalidadeTrabalho(mt, 
						String.format("Erro na listagem dos grupos de itens da Mensalidade de Trabalho %d: %s.", 
								mt.getIdMensalidadeTrabalho(),
								ThrowableUtil.getRootMessage(e))
						);
				resumoProcessamento.incItensComErro();
				continue;
			}
			
			if (gruposItemMensalidadeTrabalho.isEmpty()) {
				gerarErroProcessamentoMensalidadeTrabalho(mt, 
						String.format("Não foram encontrados grupos de itens para a Mensalidade de Trabalho %d.", 
								mt.getIdMensalidadeTrabalho())
						);
				resumoProcessamento.incItensComErro();
				continue;				
			}
						
			// A partir desses grupos, separo os de cobrança e devolução, e processo separadamente.
			// Apenas os grupos que ainda não tenham de/para de grupoItemMensalidade (ou seja, que ainda não foram enviados)
			Map<Boolean, List<GrupoItemMensalidadeTrabalho>> gruposItemMensalidadeTrabalhoPorTipo = 
					gruposItemMensalidadeTrabalho.stream()
					.filter(g -> null == g.getDeParaGrupoItemMensalidade())
					.collect(Collectors.groupingBy(GrupoItemMensalidadeTrabalho::getfDevolucao));
			
			if (gruposItemMensalidadeTrabalhoPorTipo.isEmpty()) {
				gerarErroProcessamentoMensalidadeTrabalho(mt, 
						String.format("Não há grupos de itens a serem enviados para a Mensalidade de Trabalho "
								+ "Id: %d. Talvez todos já tenham sido enviados.", 
								mt.getIdMensalidadeTrabalho())
						);
				resumoProcessamento.incItensComErro();
				continue;
			}
			
			List<GrupoItemMensalidadeTrabalho> gruposItemMensalidadeTrabalhoAgrupados;
			
			// Se existe grupo de trabalho de cobrança...
			if (gruposItemMensalidadeTrabalhoPorTipo.containsKey(false)) {
				gruposItemMensalidadeTrabalhoAgrupados = gruposItemMensalidadeTrabalhoPorTipo.get(false);
				
				if (gruposItemMensalidadeTrabalhoAgrupados.size() > 1) {
					gerarErroProcessamentoMensalidadeTrabalho(mt, 
							String.format("Foi encontrado mais de um grupo de cobrança para a Mensalidade de Trabalho %d. "
									+ "Não é possível enviar uma mensalidade deste tipo.", 
									mt.getIdMensalidadeTrabalho())
							);
					resumoProcessamento.incItensComErro();
					continue;						
				}
				
				MensalidadeContrato mensalidadeContrato;
				
				// ... processa-o, transformando em MensalidadeContrato
				try {
					mensalidadeContrato = this.processaGrupoItemMensalidadeTrabalhoCobranca(
							gruposItemMensalidadeTrabalhoAgrupados.get(0));

				} catch (ProcessadorException e) {
					gerarErroProcessamentoMensalidadeTrabalho(mt, 
							String.format("Erro de processamento de cobranças da Mensalidade de Trabalho %d: %s", 
									mt.getIdMensalidadeTrabalho(),
									ThrowableUtil.getRootMessage(e))
							);
					resumoProcessamento.incItensComErro();
					continue;
				}
				
				if (mensalidadeContrato == null) {
					gerarErroProcessamentoMensalidadeTrabalho(mt, 
							String.format("Não foi possível gerar Mensalidades Contrato a partir da Mensalidade de Trabalho Id: %d", 
									mt.getIdMensalidadeTrabalho())
							);
					resumoProcessamento.incItensComErro();
					continue;
				}
				
				gruposComMensalidadesContrato.put(gruposItemMensalidadeTrabalhoAgrupados.get(0), mensalidadeContrato);
				
			}
			
			// Se existem grupos de trabalho de devolução...
			if (gruposItemMensalidadeTrabalhoPorTipo.containsKey(true)) {			
				
				gruposItemMensalidadeTrabalhoAgrupados = gruposItemMensalidadeTrabalhoPorTipo.get(true);
								
				boolean doContinue = false;
				
				for (GrupoItemMensalidadeTrabalho gimt : gruposItemMensalidadeTrabalhoAgrupados) {				
					try {
						MensalidadeDevolucao mensalidadeDevolucao = this.processaGrupoItemMensalidadeTrabalhoDevolucao(gimt);
						if (mensalidadeDevolucao == null) {
							throw new ProcessadorException("Não foi possível gerar MensalidadeDevolução a "
									+ "partir do grupo de devolução %d",
									gimt.getIdGrupoItemMensalidadeTrabalho());
						}
						
						gruposComMensalidadesDevolucao.put(gimt, mensalidadeDevolucao);				
					} catch (ProcessadorException e) {
						gerarErroProcessamentoMensalidadeTrabalho(mt, 
								String.format("Erro de processamento de devoluções na Mensalidade de Trabalho %d, "
										+ "grupo de devolução %d: %s", 
										mt.getIdMensalidadeTrabalho(),
										gimt.getIdGrupoItemMensalidadeTrabalho(),
										ThrowableUtil.getRootMessage(e))
								);
						doContinue = true;
						resumoProcessamento.incItensComErro();
						break; // este break interrompe o for-each de gimt, porém indicando que o for-each externo também deve ser interrompido.
					}
				}
				
				if (doContinue) {
					continue;
				}
				
				logger.infof("Existem devoluções na Mensalidade de Trabalho %d. Serão enviadas após as cobranças.", 
						mt.getIdMensalidadeTrabalho()); 
				
			} else {
				logger.infof("Nenhuma devolução encontrada para a Mensalidade de Trabalho %d.",
						mt.getIdMensalidadeTrabalho());
			}
			
			
			// Por fim, é feito o envio dos dados pelo middleware
			RetornoMensagem retornoMensagem = null;
			DeParaMensalidade deParaMensalidade = null;
			Integer cdMensContratoEfetivo = null;
			
			// Envio das cobranças:
			if (!gruposComMensalidadesContrato.isEmpty()) {
				
				// Como, por construção, gruposComMensalidadesContrato só possui um elemento, basta pegar o primeiro								
				Map.Entry<GrupoItemMensalidadeTrabalho, MensalidadeContrato> grupoComMensalidadeContrato = 
						gruposComMensalidadesContrato.entrySet()
						.stream()
						.findFirst()
						.get();
			
				try {
					boolean tentarNovamente = false;
					boolean erroMensalidadeJaEnviada = false;
					boolean erroProcedimentoInvalido = false;
					boolean doContinue = false;
					
					do {
						
						if (erroMensalidadeJaEnviada) {
							tentarNovamente = false;
							logger.warn("Detectado erro por parte do SOUL de mensalidade já enviada. "
									+ "Tentando reenviar com valor de tpReceita igual a \"O\":");
							
							grupoComMensalidadeContrato.getValue().setTpReceita("O");
						
						}
						
						if (erroProcedimentoInvalido) {
							tentarNovamente = false;
							logger.warn("Detectado erro por parte do SOUL de procedimento inválido. "
									+ "Tentando reenviar utilizando valor do Procedimento Padrão...");
							
							if (!corrigirProcedimentoMensalidadeContrato(grupoComMensalidadeContrato.getValue(), retornoMensagem)) {
								logger.warnf("Detectada tentativa de correção de procedimentos inválidos, porém não foi possível identificar "
										+ "os procedimentos que sofreram esse erro. MensalidadeTrabalho Id: %d.", 
										mt.getIdMensalidadeTrabalho() );								
								break;
							}
							logger.warnf("Tentando enviar novamente a MensalidadeTrabalho %d, com os procedimentos ajustados.", 
									mt.getIdMensalidadeTrabalho() );							
						}
						
						// Enviando o JSON do objeto gerado para a MV, via middleware
						logger.info("Iniciando conexão com o middleware (TítulosReceber) para envio de cobranças.");
						response = webServiceTitulosReceber
								.incluir(this.getMapper().writeValueAsString(grupoComMensalidadeContrato.getValue()));
						String retorno = (String) response.getEntity();
						retornoMensagem = this.getMapper().readValue(retorno, RetornoMensagem.class);
						
						logger.info("Envio de dados ao middleware realizado. Analisando resultados");
						if ((response.getStatus() != 200)) {
							gerarErroProcessamentoMensalidadeTrabalho(mt, retornoMensagem);
							doContinue = true;
						} else {							
							if (!retornoMensagem.getStatus().equals("200")) {
								gerarErroProcessamentoMensalidadeTrabalho(mt, retornoMensagem);
								
								// Só permite detectar o erro de mensalidade já enviada apenas uma vez. Se ocorrer pela segunda vez, não
								// será considerado erro.
								erroMensalidadeJaEnviada = (!erroMensalidadeJaEnviada) && 
										(retornoMensagem.buscaPadraoMensagem(RetornoMensagem.REGEX_ERRO_MENSALIDADE_JA_ENVIADA));
								// Da mesma forma, só permite identificar o erro de procedimento inválido uma vez.
								erroProcedimentoInvalido = (!erroProcedimentoInvalido) &&
										(retornoMensagem.buscaPadraoMensagem(RetornoMensagem.REGEX_ERRO_PROCEDIMENTO_INVALIDO_MENSALIDADE_COBRANCA));
								
								tentarNovamente = erroMensalidadeJaEnviada || erroProcedimentoInvalido;
								
								// Caso ocorra erro mas não seja de mensalidade já enviada ou procedimento inválido, informa que o 
								// laço externo (que itera sobre as MensalidadeTrabalho) deve ter um continue, ou seja, que não 
								// deve continuar tentando processar esta MensalidadeTrabalho e deve seguir para a próxima.
								doContinue = !(erroMensalidadeJaEnviada || erroProcedimentoInvalido); 
								
							} else {
								// Armazenando o cdMensContratoEfetivo atual, pois será usado no possível envio de devoluções
								cdMensContratoEfetivo = Integer.valueOf(retornoMensagem.getEntidadeId());
								deParaMensalidade = new DeParaMensalidade();
								deParaMensalidade.setProcessamento(mt.getProcessamento());
								deParaMensalidade.setIdTitular(mt.getIdTitular());
								deParaMensalidade.setFormaPagamento(mt.getFormaPagamento());
								deParaMensalidade.setDtEmissao(mt.getDtEmissao());
								deParaMensalidade.setDtReferenciaEfetiva(mt.getDtReferenciaPrevista());
								deParaMensalidade.setDtVencimento(mt.getDtVencimento());
								deParaMensalidade.setDeParaGruposItensMensalidade(new ArrayList<>());
								
								DeParaGrupoItemMensalidade deParaGrupoItemMensalidade = new DeParaGrupoItemMensalidade();
								deParaGrupoItemMensalidade.setDeParaMensalidade(deParaMensalidade);
								deParaGrupoItemMensalidade.setfDevolucao(Boolean.FALSE);
								deParaGrupoItemMensalidade.setCdInclusaoInterno(mt.getIdMensalidadeTrabalho());
								deParaGrupoItemMensalidade.setCdInclusaoEfetivo(cdMensContratoEfetivo);
								deParaGrupoItemMensalidade.setDeParaItensMensalidade(new ArrayList<>());
								deParaMensalidade.getDeParaGruposItensMensalidade().add(deParaGrupoItemMensalidade);

								// Atribuindo o status correto para este grupo, dependendo se há ou não devoluções a serem enviadas
								if (gruposComMensalidadesDevolucao.isEmpty()) {
									// Se não há devoluções, o status desta cobrança é Enviado Totalmente.
									deParaGrupoItemMensalidade.setStatusEnvioGrupoItemMensalidade(
											getStatusEnvioGrupoItemMensalidade(StatusEnvioGrupoItemMensalidade.ENVIADO_TOTALMENTE));
									
								} else {
									// Caso contrário, o status será Enviado Parcialmente (para indicar que ainda há grupos a serem
									// enviados nesta mensalidade
									deParaGrupoItemMensalidade.setStatusEnvioGrupoItemMensalidade(
											getStatusEnvioGrupoItemMensalidade(StatusEnvioGrupoItemMensalidade.ENVIADO_PARCIALMENTE));
								}
								
								// Associando o grupo de itens de mensalidade trabalho ao de para criado
								grupoComMensalidadeContrato.getKey().setDeParaGrupoItemMensalidade(deParaGrupoItemMensalidade);
								
								for (ItemMensalidadeTrabalho imt : imtDao
										.findByGrupoItemMensalidadeTrabalho(grupoComMensalidadeContrato.getKey())) {
									DeParaItemMensalidade deParaItemMensalidade = new DeParaItemMensalidade();
		
									deParaItemMensalidade.setDeParaGrupoItemMensalidade(deParaGrupoItemMensalidade);
									deParaItemMensalidade.setIdComando(imt.getIdComando());
		
									deParaGrupoItemMensalidade.getDeParaItensMensalidade().add(deParaItemMensalidade);
		
								}
								// TODO: verificar maneira de isso estar em uma única transação
								// TODO: tratar os possíveis erros de banco
								dpmDao.persist(deParaMensalidade);
								gimtDao.merge(grupoComMensalidadeContrato.getKey());
								logger.infof("Cobranças da mensalidade Id %s enviada para a MV com sucesso. Id do envio: %d",
										deParaGrupoItemMensalidade.getCdInclusaoInterno(),
										deParaGrupoItemMensalidade.getCdInclusaoEfetivo());
								mt.setStatusProcessamento(getStatusProcessamento(StatusProcessamento.PROCESSADO_PARCIALMENTE));
								mtDao.merge(mt);
							}
						}
						
					} while (tentarNovamente);	
					
					if (doContinue) {
						resumoProcessamento.incItensComErro();
						continue;
					}
					
					
				} catch (JsonProcessingException e) {
					gerarErroProcessamentoMensalidadeTrabalho(mt, "Erro ao converter Mensalidade para JSON, "
							+ "para envio ao SOUL. Lote de Trabalho " + mt.getIdMensalidadeTrabalho());
					resumoProcessamento.incItensComErro();
					continue;
					
					//throw new ProcessadorException(e);
				} catch (IOException e) {
					gerarErroProcessamentoMensalidadeTrabalho(mt, "Erro ao ler o retorno do Middleware para "
							+ "JSON. Não é possível dizer se a mensalidade foi incluída ou não. Mensalidade Id " + mt.getIdMensalidadeTrabalho());
					resumoProcessamento.incItensComErro();
					continue;
				}
			
			}
			
			// Envio das devoluções
			if (!gruposComMensalidadesDevolucao.isEmpty() ) {
				logger.infof("Iniciando envio das devoluções desta mensalidade. %d grupos de devoluções para envio",
						gruposComMensalidadesDevolucao.size()
						);
				
				if (deParaMensalidade == null) {
					// TODO: Melhorar esse código para evitar duplicação deste bloco
					deParaMensalidade = new DeParaMensalidade();
					
					deParaMensalidade.setProcessamento(mt.getProcessamento());
					deParaMensalidade.setIdTitular(mt.getIdTitular());
					deParaMensalidade.setFormaPagamento(mt.getFormaPagamento());
					deParaMensalidade.setDtEmissao(mt.getDtEmissao());
					deParaMensalidade.setDtReferenciaEfetiva(mt.getDtReferenciaPrevista());
					deParaMensalidade.setDtVencimento(mt.getDtVencimento());
					deParaMensalidade.setDeParaGruposItensMensalidade(new ArrayList<>());
				}
				
				try {
				
					for (Map.Entry<GrupoItemMensalidadeTrabalho, MensalidadeDevolucao> grupoComMensalidadeDevolucao : 
						gruposComMensalidadesDevolucao.entrySet()) {
						// Neste ponto, é necessário verificar se já houve envio de cobrança para esta mensalidadeTrabalho.
						// No momento, por construção, basta verificar o valor de cdMensContratoEfetivo (ou até mesmo se
						// deParaMensalidade é novo ou já criado antes), mas o correto é verificar no banco de dados.
						
						// Caso já exista cdMensContratoEfetivo, atribuo-o à MensalidadeDevolucao atual, removendo os valores de
						// nrMes e nrAno (pois são mutuamente exclusivos)
						if (cdMensContratoEfetivo != null) {
							grupoComMensalidadeDevolucao.getValue().setCdMensContrato(cdMensContratoEfetivo);
							grupoComMensalidadeDevolucao.getValue().setNrMes(null);
							grupoComMensalidadeDevolucao.getValue().setNrAno(null);
						}
						
					
						// FIXME: A estrutura de try dentro de try é ruim, mas a ideia é mover o try interno para um método separado, então
						// 		  será feito assim por hora
						try {
							logger.info("Iniciando conexão com o middleware (Reembolso) para envio de devoluções.");											
							response = webServiceReembolso.incluir(this.getMapper().writeValueAsString(grupoComMensalidadeDevolucao.getValue()));
							String retorno = (String)response.getEntity();
							retornoMensagem = this.getMapper().readValue(retorno, RetornoMensagem.class);
							logger.info("Envio de dados ao middleware realizado. Analisando resultados");
							
							if ( (200 != response.getStatus()) || (!"200".equals(retornoMensagem.getStatus())) ) {
								gerarErroProcessamentoMensalidadeTrabalho(mt, retornoMensagem);
								throw new ProcessadorException("Não foi possível enviar a devolução do GrupoItemMensalidade Id: %d",
										grupoComMensalidadeDevolucao.getKey().getIdGrupoItemMensalidadeTrabalho());
							}
							
							// Supondo que tenha dado tudo certo, é preciso gravar os dados do que foi gerado.
							
							DeParaGrupoItemMensalidade deParaGrupoItemMensalidade = new DeParaGrupoItemMensalidade();
							deParaGrupoItemMensalidade.setDeParaMensalidade(deParaMensalidade);
							deParaGrupoItemMensalidade.setfDevolucao(Boolean.TRUE);
							deParaGrupoItemMensalidade.setCdInclusaoInterno(mt.getIdMensalidadeTrabalho());
							deParaGrupoItemMensalidade.setCdInclusaoEfetivo(Integer.valueOf(retornoMensagem.getEntidadeId()));
							deParaGrupoItemMensalidade.setStatusEnvioGrupoItemMensalidade(getStatusEnvioGrupoItemMensalidade(
									StatusEnvioGrupoItemMensalidade.ENVIADO_PARCIALMENTE));
							deParaGrupoItemMensalidade.setDeParaItensMensalidade(new ArrayList<>());
							
							deParaMensalidade.getDeParaGruposItensMensalidade().add(deParaGrupoItemMensalidade);
							
							grupoComMensalidadeDevolucao.getKey().setDeParaGrupoItemMensalidade(deParaGrupoItemMensalidade);
							
							for (ItemMensalidadeTrabalho imt : imtDao.findByGrupoItemMensalidadeTrabalho(grupoComMensalidadeDevolucao.getKey())) {
								DeParaItemMensalidade deParaItemMensalidade = new DeParaItemMensalidade();
								
								deParaItemMensalidade.setDeParaGrupoItemMensalidade(deParaGrupoItemMensalidade);
								deParaItemMensalidade.setIdComando(imt.getIdComando());
								
								deParaGrupoItemMensalidade.getDeParaItensMensalidade().add(deParaItemMensalidade);							
							}
							
							// TODO: verificar maneira de isso estar em uma única transação
							// TODO: tratar os possíveis erros de banco
							
							// Como deParaMensalidade pode existir ou não no banco, é feita a operação merge
							deParaMensalidade = dpmDao.merge(deParaMensalidade);
							
							// Com os grupos salvos, é preciso recuperar a versão gerenciada de deParaGrupoItemMensalidade. É possível fazer isso pelo 
							// cdInclusaoInterno ou pelo cdInclusaoEfetivo (que são únicos para este deParaMensalidade)
							Optional<DeParaGrupoItemMensalidade> deParaGrupoItemMensalidadeGerenciado = deParaMensalidade
									.getDeParaGruposItensMensalidade()
									.stream()
									.filter(g -> (g.getfDevolucao()) && (deParaGrupoItemMensalidade.getCdInclusaoInterno().equals(g.getCdInclusaoInterno())) )
									.findFirst();
							if (!deParaGrupoItemMensalidadeGerenciado.isPresent()) {
								//TODO: pegar do banco
							}
							
							grupoComMensalidadeDevolucao.getKey().setDeParaGrupoItemMensalidade(deParaGrupoItemMensalidadeGerenciado.get());
							
							gimtDao.merge(grupoComMensalidadeDevolucao.getKey());
							
							logger.infof("Devoluções do grupo de itens %d da mensalidade Id %s enviadas para a "
									+ "MV com sucesso. Id do envio: %d",
									grupoComMensalidadeDevolucao.getKey().getIdGrupoItemMensalidadeTrabalho(),
									deParaGrupoItemMensalidade.getCdInclusaoInterno(),
									deParaGrupoItemMensalidade.getCdInclusaoEfetivo()
									);
							
							mt.setStatusProcessamento(getStatusProcessamento(StatusProcessamento.PROCESSADO_PARCIALMENTE));
							mtDao.merge(mt);		
							
							
						} catch (JsonProcessingException e) {
							// TODO: Implementar um tipo diferente de erro, para informar meio processado (o que é
							//       um erro grave)
							String mensagemErro = String.format("Erro ao converter devoluções da Mensalidade para JSON, "
									+ "para envio ao SOUL: %s. MensalidadeTrabalho Id: %d", 
									ThrowableUtil.getRootMessage(e),
									mt.getIdMensalidadeTrabalho());
							
							gerarErroProcessamentoMensalidadeTrabalho(mt, mensagemErro);							
							throw new ProcessadorException(mensagemErro);
						} catch (IOException e) {
							String mensagemErro = String.format("Erro ao ler o retorno de devoluções do Middleware para "
									+ "JSON: %s. Não é possível dizer se a mensalidade foi incluída ou não. MensalidadeTrabalho "
									+ "Id: %d ", 
									ThrowableUtil.getRootMessage(e),
									mt.getIdMensalidadeTrabalho()
									);
							
							gerarErroProcessamentoMensalidadeTrabalho(mt, mensagemErro);							
							throw new ProcessadorException(mensagemErro);
						} catch (PersistenceException e) {
							throw new ProcessadorException(e);
						} 										
					}
					
				} catch (ProcessadorException e) {
					resumoProcessamento.incItensComErro();
					
					logger.errorf("Ocorreu um erro ao tentar enviar alguma das devoluções da MensalidadeTrabalho "
							+ "Id: %d. Será feito o cancelamento de tudo o que já foi enviado desta mensalidade.",
							mt.getIdMensalidadeTrabalho());
					
					if (null != deParaMensalidade.getIdDeParaMensalidade()) {
						try {
							cancelaMensalidade(deParaMensalidade);
						} catch (ProcessadorException innerE) {
							logger.errorf("Ocorreu um erro ao cancelar as mensalidades do DeParaMensalidade %d. "
									+ "Seu status atual é inconsistente. Mensagem de erro: %s", 
									deParaMensalidade.getIdDeParaMensalidade(),
									ThrowableUtil.getRootMessage(innerE)
									);
						}
					} else {
						logger.errorf("Não parece haver nada enviado para a MensalidadeTrabalho %d. Caso haja, não "
								+ "foi cancelado e ela se encontra em estado inconsistente ou incompleto no SOUL.",
								mt.getIdMensalidadeTrabalho());
					}
					// Depois de tudo cancelado (ou não), o processamento continua
					continue;
							
				} 					
				
			}	
			
			// Caso chegue a esse ponto, significa que todos os grupos daquela mensalidade foram enviados com sucesso. Portanto,
			// o status de cada grupo enviado é atualizado para ENVIADO_TOTALMENTE
			for (DeParaGrupoItemMensalidade dpgim : deParaMensalidade.getDeParaGruposItensMensalidade()) {
				dpgim.setStatusEnvioGrupoItemMensalidade(getStatusEnvioGrupoItemMensalidade(
						StatusEnvioGrupoItemMensalidade.ENVIADO_TOTALMENTE));
			}
			dpmDao.merge(deParaMensalidade);
			
			mt.setStatusProcessamento(getStatusProcessamento(StatusProcessamento.PROCESSADO_TOTALMENTE));
			mtDao.merge(mt);
			
			resumoProcessamento.incItensEnviados();
		}
		
		return resumoProcessamento;
	}

	private boolean corrigirProcedimentoMensalidadeContrato(MensalidadeContrato mensalidadeContrato,
			RetornoMensagem retornoMensagem) {
		// O objetivo deste método é, dada uma lista de mensagens de erro, extrair os possíveis erros
		// de procedimentos inválidos encontrados e corrigir nos itens de coparticipação, buscando as 
		// mensalidades que possuem esses procedimentos e substituindo-o pelo procedimento padrão 
		// (5001000 - diagnose)
				
		boolean result = false;

		// Primeiro, deve-se extrair os CNES inválidos informados na mensagem de erro:
		Set<Integer> procedimentosInvalidos = new HashSet<>();

		Pattern padraoErroProcedimento = Pattern.compile(RetornoMensagem.REGEX_ERRO_PROCEDIMENTO_INVALIDO_MENSALIDADE_COBRANCA);

		for (String s : retornoMensagem.getMensagem()){
			Matcher matcher = padraoErroProcedimento.matcher(s);
			if (matcher.find()){
				try {
					procedimentosInvalidos.add(Integer.parseInt(matcher.group(1)));
				} catch (NumberFormatException e) {
					logger.errorf("Foi encontrado um código de procedimento inválido que não parece ser um número: %s. "
							+ "Não será possível corrigir os procedimentos da MensalidadeTrabalho devido "
							+ "a esse erro.", 
							matcher.group(1)
							);
				}
			}
		}

		// Se não houver nenhuma informação de CNES inválido, não faz sentido continuar o processamento.
		// Então, a função retorna false.
		if (procedimentosInvalidos.isEmpty()){
			return false;
		}

		// Uma vez com os procedimentos inválidos, deve-se percorrer a lista itens de mensalidade no
		// lote e substituir, em suas coparticipações, pelo procedimento padrão. 
		for (ItemMensalidadeUsuario itemMensalidadeUsuario : mensalidadeContrato.getItensMensalidadeUsuario()){
			if (null == itemMensalidadeUsuario.getImpCoparticipacoes()) {
				/* Se é um ItemMensalidadeUsuario sem coparticipação, apenas avança. */
				continue;
			}
			
			for (ImpCoparticipacao impCoparticipacao : itemMensalidadeUsuario.getImpCoparticipacoes()) {
				
				if ( (null != impCoparticipacao.getCdProcedimento()) && (procedimentosInvalidos.contains(impCoparticipacao.getCdProcedimento()) ) ) {
					logger.warnf("Código de procedimento inválido associado ao id_comando %s: %d. Será substituído "
							+ "pelo Procedimento padrão (%d)", 
							(impCoparticipacao.getIdComando() != null ? impCoparticipacao.getIdComando().toString() : "[não identificado]"),
							impCoparticipacao.getCdProcedimento(),
							Constantes.PROCEDIMENTO_PADRAO
							);
					impCoparticipacao.setCdProcedimento(Constantes.PROCEDIMENTO_PADRAO);
					result = true;
				}
			}


		}

		return result;
	}

	private void cancelaMensalidade(DeParaMensalidade deParaMensalidade) throws ProcessadorException {
		// O primeiro ponto será listar os grupos existentes para esta mensalidade, já que não se pode assumir que eles 
		// estão presentes nesse parâmetro
		
		if ( (null == deParaMensalidade) || (null == deParaMensalidade.getIdDeParaMensalidade())) {
			throw new IllegalArgumentException("O parâmetro deParaMensalidade é null ou possui seu id null. "
					+ "Não é possível cancelar as mensalidades nessa situação.");
		}
		
		List<DeParaGrupoItemMensalidade> deParaGruposItemMensalidade = dpgimDao.listByDeParaMensalidade(deParaMensalidade);
		
		if (deParaGruposItemMensalidade.isEmpty()) {
			logger.errorf("Não foram encontrados grupos de item de mensalidade para o DeParaMensalidade Id: %d "
					+ "Nada será cancelado.",
					deParaMensalidade.getIdDeParaMensalidade());
			return;
		}
		// Depois de listados, é preciso marcar todos os grupos como "A cancelar" (pois não é possível
		// garantir que os grupos que precisam ser cancelados serão efetivamente cancelados.
		
		for (DeParaGrupoItemMensalidade dpgim : deParaGruposItemMensalidade) {
			dpgim.setStatusEnvioGrupoItemMensalidade(getStatusEnvioGrupoItemMensalidade(
					StatusEnvioGrupoItemMensalidade.A_CANCELAR));
		}
		
		dpgimDao.mergeAll(deParaGruposItemMensalidade);
		
		// Com todos os itens marcados, cada um será cancelado indivualmente.
		for (DeParaGrupoItemMensalidade dpgim : deParaGruposItemMensalidade) {
			logger.infof("Tentando realizar o cancelamento do GrupoItemMensalidade Id: %d (Id do envio: %d) (%s)", 
					dpgim.getIdDeParaGrupoItemMensalidade(),
					dpgim.getCdInclusaoEfetivo(),
					(dpgim.getfDevolucao() ? "Devolução" : "Cobrança")
					);
			try {
				if (cancelaGrupoItemMensalidade(dpgim)) {
					dpgim.setStatusEnvioGrupoItemMensalidade(getStatusEnvioGrupoItemMensalidade(
							StatusEnvioGrupoItemMensalidade.CANCELADO));
					dpgimDao.merge(dpgim);
				}
								
			} catch (ProcessadorException e) {
				logger.errorf("Ocorreu um erro ao tentar cancelar o GrupoItemMensalidade Id: %d. "
						+ "Seu estado atual é inconsistente e precisará ser cancelado manualmente depois. "
						+ "Mensagem de erro: %s", 
						dpgim.getIdDeParaGrupoItemMensalidade(),
						ThrowableUtil.getRootMessage(e));
			}					
		}
		
	}

	private boolean cancelaGrupoItemMensalidade(DeParaGrupoItemMensalidade deParaGrupoItemMensalidade) throws ProcessadorException {
		CancelaMensalidade cancelaMensalidade = CancelaMensalidade.criaCancelaMensalidadeAnulacao(
				deParaGrupoItemMensalidade.getCdInclusaoEfetivo(),
				deParaGrupoItemMensalidade.getDeParaMensalidade().getDtEmissao());
				
		try {
			Response response;
			
			if (deParaGrupoItemMensalidade.getfDevolucao()) {
				response = webServiceReembolso
						.cancelar(this.getMapper().writeValueAsString(cancelaMensalidade));
			} else {
				response = webServiceTitulosReceber
						.cancelaTitulosReceber(this.getMapper().writeValueAsString(cancelaMensalidade));
			}
			
			String retorno = (String) response.getEntity();
			RetornoMensagem retornoMensagem = this.getMapper().readValue(retorno, RetornoMensagem.class);
			
			if ( (200 != response.getStatus()) || (!"200".equals(retornoMensagem.getStatus())) ) {
				throw new ProcessadorException("Não foi possível cancelar a cobrança via webservice. Mensagens de erro do "
						+ "serviço: %s",
						retornoMensagem.getMensagem()
						.stream()
						.map(Object::toString)
						.collect(Collectors.joining(", "))
						);
			}
			
		} catch (JsonProcessingException e) {
			logger.errorf("Ocorreu um erro na geração do JSON para envio do cancelamento do grupo de mensalidade "
					+ "Id: %d. Nâo foi possível cancelá-lo", 
					deParaGrupoItemMensalidade.getIdDeParaGrupoItemMensalidade()
					);
			throw new ProcessadorException(e);
		} catch (IOException e) {
			logger.errorf("Ocorreu um erro na leitura do JSON de retorno do envio do cancelamento do grupo de mensalidade "
					+ "Id: %d. Nâo é possível identificar se o grupo foi realmente cancelado.", 
					deParaGrupoItemMensalidade.getIdDeParaGrupoItemMensalidade()
					);
			throw new ProcessadorException(e);
		}
		
		return true;
	}

	private MensalidadeContrato processaGrupoItemMensalidadeTrabalhoCobranca(
			GrupoItemMensalidadeTrabalho grupoItemMensalidadeTrabalho) throws ProcessadorException {
		// TODO: Melhorar o tratamento de exceções, principalmente as relacionadas a PersistenceException
		
		if (grupoItemMensalidadeTrabalho == null) {
			throw new IllegalArgumentException("grupoItemMensalidadeTrabalho não pode ser null");
		}
		
		if (!Boolean.FALSE.equals(grupoItemMensalidadeTrabalho.getfDevolucao()) )  {
			throw new IllegalArgumentException("O grupoItemMensalidadeTrabalho informado não é de cobrança! Id: " 
					+ grupoItemMensalidadeTrabalho.getIdGrupoItemMensalidadeTrabalho() );
		}
		
		MensalidadeTrabalho mensalidadeTrabalho;
		try {
			mensalidadeTrabalho = gimtDao.findMensalidadeTrabalho(grupoItemMensalidadeTrabalho);
		} catch (PersistenceException e) {
			throw new ProcessadorException("Ocorreu um erro na identificação da MensalidadeTrabalho "
					+ "associada ao Grupo de ItemMensalidadeTrabalho Id: %d: %s",
					grupoItemMensalidadeTrabalho.getIdGrupoItemMensalidadeTrabalho(),
					ThrowableUtil.getRootMessage(e));
		}		
		
		logger.info("Buscando candidatos a ItensMensalidadeTrabalho de cobrança");
		
		List<ProjecaoMensalidadeContrato> projecoesMensalidadeContrato;
		try {
			projecoesMensalidadeContrato = pmcDao
					.findByGrupoItemMensalidadeTrabalho(grupoItemMensalidadeTrabalho);
		} catch (PersistenceException e) {
			throw new ProcessadorException("Ocorreu um erro na geração de ProjecoesMensalidadeContrato "
					+ "para o Grupo de ItemMensalidadeTrabalho Id: %d (MensalidadeTrabalho Id: %d): %s",
					grupoItemMensalidadeTrabalho.getIdGrupoItemMensalidadeTrabalho(),
					mensalidadeTrabalho.getIdMensalidadeTrabalho(),
					ThrowableUtil.getRootMessage(e)
					);
		}
				
		if ( (projecoesMensalidadeContrato == null) || (projecoesMensalidadeContrato.isEmpty()) ){
			throw new ProcessadorException("Não foi encontrada nenhuma mensalidade com os dados informados. "
					+ "Grupo de ItemMensalidadeTrabalho Id: %d; MensalidadeTrabalho Id: %d",					
					grupoItemMensalidadeTrabalho.getIdGrupoItemMensalidadeTrabalho(),
					mensalidadeTrabalho.getIdMensalidadeTrabalho());
		}
		
		logger.infof("%d candidato%s a Ite%sMensalidadeTrabalho de cobrança para processamento",
					projecoesMensalidadeContrato.size(),
					(projecoesMensalidadeContrato.size() == 1 ? "" : "s"),
					(projecoesMensalidadeContrato.size() == 1 ? "m" : "ns")
				);
		
		// ProjecaoMensalidadeContrato possui a maioria dos dados para a criação
		// de um MensalidadeContrato e de seus ItemMensalidadeUsuario. Os demais estão
		// no próprio MensalidadeTrabalho.
		
		MensalidadeContrato mensalidadeContrato = new MensalidadeContrato();
		
		// Além disso, como os dados que definem MensalidadeContrato se repetem nos
		// vários ProjecaoMensalidadeContrato, será selecionado um como base, o qual
		// irá alimentar mensalidadeContrato
		
		ProjecaoMensalidadeContrato projecaoBase = projecoesMensalidadeContrato.get(0);
		
		logger.info("Preenchendo dados de MensalidadeContrato");
		
		// Dados de MensalidadeTrabalho
		
		// TODO: Alterar para preencher corretamente os dados: os que são de MensalidadeTrabalho
		//       e os que vêm efetivamente da query (projecaoBase)
		
		// cdContrato: a partir de projecaoBase
		if (projecaoBase.getCdContrato() == null){
			throw new ProcessadorException("O beneficiário do comando %d (id_pessoa %d) não possui " +
					    	"cdContrato associado. Grupo de ItemMensalidadeTrabalho Id: %d; MensalidadeTrabalho Id: %d", 
						projecaoBase.getIdComando(),
						projecaoBase.getIdPessoa(),
						grupoItemMensalidadeTrabalho.getIdGrupoItemMensalidadeTrabalho(),
						mensalidadeTrabalho.getIdMensalidadeTrabalho()							
					);
		}
		mensalidadeContrato.setCdContrato(projecaoBase.getCdContrato());
		
		// TODO: adicionar forma de pagamento a mensalidadeContrato
		
		// cdMensContratoInterno: a partir de grupoItemMensalidadeTrabalho (já que cada grupo irá 
		//   gerar as cobranças correspondentes)
		// Atualmente, só é permitida uma cobrança por mensalidade, mas caso isso venha a ser alterado, 
		// ainda será mantido o cdMensContratoInterno único
		mensalidadeContrato.setCdMensContratoInterno(grupoItemMensalidadeTrabalho.getIdGrupoItemMensalidadeTrabalho());
		
		// cdMotivoCancelamento: null
		mensalidadeContrato.setCdMotivoCancelamento(projecaoBase.getCdMotivoCancelamento());			
		
		// cdMultiEmpresa: fixo 1
		// TODO: Passar cdMultiEmpresa para uma constante
		mensalidadeContrato.setCdMultiEmpresa(1);
		
		// cdNossoNumero: null
		mensalidadeContrato.setCdNossoNumero(projecaoBase.getCdNossoNumero());
		
		// dsObservacao: por hora, null
		mensalidadeContrato.setDsObservacao(projecaoBase.getDsObservacao());
		
		// dtCancelamento: por hora, null
		mensalidadeContrato.setDtCancelamento(projecaoBase.getDtCancelamento());
		
		// dtEmissao: por hora, fixo como a data de hoje
		// TODO: alterar para que MensalidadeTrabalho e DeParaMensalidade possuam data de emissao
		mensalidadeContrato.setDtEmissao(projecaoBase.getDtEmissao());
		
		// dtVencimento: a partir de mensalidadeTrabalho
		mensalidadeContrato.setDtVencimento(mensalidadeTrabalho.getDtVencimento());
					
		// dtVencimentoOriginal: por hora, null
		mensalidadeContrato.setDtVencimentoOriginal(projecaoBase.getDtVencimentoOriginal());
		
		// nrAno: 
		mensalidadeContrato.setNrAno(projecaoBase.getNrAno());
		
		// nrMes
		mensalidadeContrato.setNrMes(projecaoBase.getNrMes());
		
		// nrAgencia: por hora, null
		mensalidadeContrato.setNrAgencia(projecaoBase.getNrAgencia());
		
		// nrBanco
		mensalidadeContrato.setNrBanco(projecaoBase.getNrBanco());
		
		// nrConta
		mensalidadeContrato.setNrConta(projecaoBase.getNrConta());
		
		// nrDocumento
		mensalidadeContrato.setNrDocumento(projecaoBase.getNrDocumento());
		
		// nrParcela
		mensalidadeContrato.setNrParcela(projecaoBase.getNrParcela());
		
		// tpQuitacao
		mensalidadeContrato.setTpQuitacao(projecaoBase.getTpQuitacao());
		
		// tpReceita
		mensalidadeContrato.setTpReceita(projecaoBase.getTpReceita());
		
		// vlJurosMora
		mensalidadeContrato.setVlJurosMora(projecaoBase.getVlJurosMora());
		
		// vlMensalidade
		if ( null == projecaoBase.getVlMensalidade() ){
			throw new ProcessadorException(String.format("Impossível determinar valor "
					+ "da mensalidade (Grupo de ItemMensalidadeTrabalho Id: %d; MensalidadeTrabalho Id: %d)", 
					grupoItemMensalidadeTrabalho.getIdGrupoItemMensalidadeTrabalho(),
					mensalidadeTrabalho.getIdMensalidadeTrabalho()));
		}
		
		if ( projecaoBase.getVlMensalidade() > 0 ) {
			// Saldo de cobranças menos devoluções positivo: o valor da mensalidade é o informado				
			mensalidadeContrato.setVlMensalidade(projecaoBase.getVlMensalidade());				
		} else {
			// Saldo de cobranças menos devoluções zerado: não é possível enviar a mensalidade.
			throw new ProcessadorException( String.format("Mensalidade com valor zerado. "
					+ "(Grupo de ItemMensalidadeTrabalho Id: %d, MensalidadeTrabalho Id: %d)",
					grupoItemMensalidadeTrabalho.getIdGrupoItemMensalidadeTrabalho(),
					mensalidadeTrabalho.getIdMensalidadeTrabalho())							
				);				
		}
		
		
		// vlPago
		mensalidadeContrato.setVlPago(projecaoBase.getVlPago());
		
		//vlPercentualMulta
		mensalidadeContrato.setVlPercentualMulta(projecaoBase.getVlPercentualMulta());
								
		mensalidadeContrato.setItensMensalidadeUsuario(new ArrayList<>());
		
		// Para transformar os ProjecaoMensalidadeContrato em ItemMensalidadeUsuario,
		// é preciso primeiro agrupar os ProjecaoMensalidadeContrato pelo campo 
		// seqItemMensalidade (identificador gerado pela storedProcedure para 
		// classificar os comandos). Então, para os possíveis diversos itens criados,
		// é necessário acumular o valor em um deles e incluir apenas esse único
		
		// Primeiro, agrupamento por seqItemMensalidade
		Map<Integer, List<ProjecaoMensalidadeContrato>> todasProjecoesAgrupadas = projecoesMensalidadeContrato
				.stream()
				.collect(Collectors.groupingBy(p -> p.getSeqItemMensalidade()));
		
		for (Integer seqItemMensalidade : todasProjecoesAgrupadas.keySet()){
			List<ProjecaoMensalidadeContrato> projecoesAgrupadas = todasProjecoesAgrupadas.get(seqItemMensalidade);
			
			// Como todas as projecoes de um determinado item de mensalidade são iguais, 
			// exceto pelo código da rubrica e beneficiário (tratados em seqItemMensalidade) 
			// e pelo valor, será considerada a primeira de cada grupo como base
			
			ProjecaoMensalidadeContrato projecaoAgrupadaBase = projecoesAgrupadas.get(0);
			
			// Validando cdMatricula
			if (projecaoAgrupadaBase.getCdMatricula() == null){
				throw new ProcessadorException(String.format("O beneficiário do comando %d (id_pessoa %d) não possui "
							+ "cdMatricula associado. MensalidadeTrabalho Id: %d", 
							projecaoAgrupadaBase.getIdComando(),
							projecaoAgrupadaBase.getIdPessoa(),
							mensalidadeTrabalho.getIdMensalidadeTrabalho()
						));
			}				
			
			Double vlTotalItemMensalidade = projecoesAgrupadas.stream()
					.collect(Collectors.summingDouble(p -> p.getVlLancamento()));
			//projecaoAgrupadaBase.setVlLancamento(vlTotalItemMensalidade);
			//mensalidadeContrato.setVlMensalidade(vlTotalItemMensalidade);
			
			ItemMensalidadeUsuario itemMensalidade = new ItemMensalidadeUsuario();
			// cdMensContratoInterno
			itemMensalidade.setCdMensContratoInterno(mensalidadeContrato.getCdMensContratoInterno());
			// cdLctoMensalidade
			itemMensalidade.setCdLctoMensalidade(projecaoAgrupadaBase.getCdLctoMensalidade());
			// cdMatricula
			itemMensalidade.setCdMatricula(projecaoAgrupadaBase.getCdMatricula());
			// dsObservacao
			itemMensalidade.setDsObservacao(projecaoAgrupadaBase.getDsObservacaoItemMensalidade());
			// vlLancamento
			itemMensalidade.setVlLancamento(vlTotalItemMensalidade);
			
			// Composição dos itemMensalidade de coparticipação:
			
			// Devido à forma como o agrupamento é feito, todas as projeções em todasProjecoesAgrupadas
			// têm os seguintes dados em comum, baseado no comando o qual gerou a projeção: 
			// id_titular (do beneficiário do comando), forma de pagamento, data de vencimento calculada
			// (considerando as regras de folha e boleto), data de referência (apenas para Folha), id_pessoa
			// e id_rubrica_cac. Desses campos, em particular, o id_rubrica_cac é comum. Então, identificando
			// o tipo de rubrica de projecaoAgrupadaBase é suficiente para identificar o tipo de rubrica de
			// todas as projeções do grupo que a contém.
			// Se o tipo de rubrica for coparticipação, é necessário buscar os procedimentos que geraram
			// essa coparticipação.
			
			if ( (!projecaoAgrupadaBase.getfProvento()) && (TipoRubricaCac.PARTICIPACAO.equals(projecaoAgrupadaBase.getTipoRubricaCac())) ){
				itemMensalidade.setImpCoparticipacoes(listaImpCoparticipacao(projecoesAgrupadas));
			}
			
			mensalidadeContrato.getItensMensalidadeUsuario().add(itemMensalidade);	
			
		}
	
		return mensalidadeContrato;
	}

	private MensalidadeDevolucao processaGrupoItemMensalidadeTrabalhoDevolucao(
			GrupoItemMensalidadeTrabalho grupoItemMensalidadeTrabalho) throws ProcessadorException {
		// TODO Ajustar os lançamentos de exceção, principalmente os PersistenceException
		if (grupoItemMensalidadeTrabalho == null){
			throw new IllegalArgumentException("O parâmetro grupoItemMensalidadeTrabalho não pode ser null.");
		}
		
		if (!Boolean.TRUE.equals(grupoItemMensalidadeTrabalho.getfDevolucao()) )  {
			throw new IllegalArgumentException("O grupoItemMensalidadeTrabalho informado não é de devolução! Id: " 
					+ grupoItemMensalidadeTrabalho.getIdGrupoItemMensalidadeTrabalho() );
		}
		
		MensalidadeTrabalho mensalidadeTrabalho;
		try {
			mensalidadeTrabalho = gimtDao.findMensalidadeTrabalho(grupoItemMensalidadeTrabalho);
		} catch (PersistenceException e) {
			throw new ProcessadorException("Ocorreu um erro na identificação da MensalidadeTrabalho "
					+ "associada ao Grupo de ItemMensalidadeTrabalho Id: %d: %s",
					grupoItemMensalidadeTrabalho.getIdGrupoItemMensalidadeTrabalho(),
					ThrowableUtil.getRootMessage(e));
		}

		logger.info("Buscando candidatos a ItensMensalidadeTrabalho de devolução");
		
		List<ProjecaoMensalidadeDevolucao> projecoesMensalidadeDevolucao;
		try {
			projecoesMensalidadeDevolucao = pmdDao.findByGrupoItemMensalidadeTrabalho(
					grupoItemMensalidadeTrabalho);
		} catch (PersistenceException e) {
			throw new ProcessadorException("Ocorreu um erro na geração de ProjecoesMensalidadeDevolucao "
					+ "para o Grupo de ItemMensalidadeTrabalho Id: %d (MensalidadeTrabalho Id: %d): %s",
					grupoItemMensalidadeTrabalho.getIdGrupoItemMensalidadeTrabalho(),
					mensalidadeTrabalho.getIdMensalidadeTrabalho(),
					ThrowableUtil.getRootMessage(e)
					);
		}
		
		if ( (projecoesMensalidadeDevolucao == null) || (projecoesMensalidadeDevolucao.isEmpty()) ){
			throw new ProcessadorException("Não foi encontrada nenhuma devolução para o grupo de itens "
					+ "de mensalidade informado. Grupo de ItemMensalidadeTrabalho Id: %d, "
					+ "Mensalidade de trabalho: %d", 
					grupoItemMensalidadeTrabalho.getIdGrupoItemMensalidadeTrabalho(),
					mensalidadeTrabalho.getIdMensalidadeTrabalho());
			
		}
		
		logger.infof("%d candidato%s a Ite%sMensalidadeTrabalho de devolução para processamento",
					 projecoesMensalidadeDevolucao.size(),
					(projecoesMensalidadeDevolucao.size() == 1 ? "" : "s"),
					(projecoesMensalidadeDevolucao.size() == 1 ? "m" : "ns")
				);
		
		// O conteúdo de projecoesMensalidadeDevolucao é base para a geração dos ItemMensalidadeDevolucao
		// a serem enviados para a MV. projecoes contém informações do grupo de origem, assim como dos itens
		// de mensalidade de trabalho deste grupo (os quais gerarão os itens de devolução propriamente ditos).
			
		// Como os dados que compõem MensalidadeDevolucao são comuns em todas as projeções dessa lista, 
		// será pega a primeira como projecaoBase, para preencher esses dados em MensalidadeDevolucao
		ProjecaoMensalidadeDevolucao projecaoMensalidadeBase = projecoesMensalidadeDevolucao.get(0);
		
		MensalidadeDevolucao mensalidadeDevolucao = new MensalidadeDevolucao();
		
		// Dentre os dados presentes na projeção, há um essencial que pode vir muitas vezes como
		// null: cdMatricula. Então, pode-se testar de uma vez para, caso seja de fato null,
		// interromper imediatamente o processamento.
		if (null == projecaoMensalidadeBase.getCdMatricula()){
			throw new ProcessadorException("O beneficiário da devolução do comando %d "
					+ "não possui código de matrícula cadastrado na MV. Grupo de ItemMensalidadeTrabalho "
					+ "Id: %d, MensalidadeTrabalho Id: %d",					
					projecaoMensalidadeBase.getIdComando(),
					grupoItemMensalidadeTrabalho.getIdGrupoItemMensalidadeTrabalho(),
					mensalidadeTrabalho.getIdMensalidadeTrabalho()
				);
		}
		
		// Dos dados disponíveis na projeção, os campos cdMensContrato e cdFornecedor são preenchidos
		// via código (o primeiro só pode ser preenchido após o envio da parte de cobrança desta
		// mensalidade, caso exista e o segundo depende do cdMatriculaTitular, o qual deve ser usado para
		// buscar no ws de Beneficiários. Então, o preenchimento será iniciado por cdFornecedor.			
		if (null == projecaoMensalidadeBase.getCdMatriculaFornecedor()){
			throw new ProcessadorException("O titular da devolução do comando %d "
					+ "não possui código de matrícula cadastrado na MV. Grupo de ItemMensalidadeTrabalho "
					+ "Id: %d, MensalidadeTrabalho Id: %d", 
					projecaoMensalidadeBase.getIdComando(),
					grupoItemMensalidadeTrabalho.getIdGrupoItemMensalidadeTrabalho(),
					mensalidadeTrabalho.getIdMensalidadeTrabalho()
				);			
		}
		
		// Usando cdMatriculaFornecedor, é buscado o beneficiário, e, caso consiga, avalia-se suas 
		// informações.
		Beneficiario beneficiario = this.buscaBeneficiario(projecaoMensalidadeBase.getCdMatriculaFornecedor());
		
		if (beneficiario.getCdFornecedor() == null){
			throw new ProcessadorException("O titular da devolução do comando %d não possui "
					+ "código de fornecedor cadastrado na MV. Código de matrícula do beneficiário: %d. "
					+ "Grupo de ItemMensalidadeTrabalho Id: %d, MensalidadeTrabalho Id: %d.",
					projecaoMensalidadeBase.getIdComando(),
					projecaoMensalidadeBase.getCdMatriculaFornecedor(),
					grupoItemMensalidadeTrabalho.getIdGrupoItemMensalidadeTrabalho(),
					mensalidadeTrabalho.getIdMensalidadeTrabalho()
				);
		}
		
		// Estando tudo certo, preenche-se o cdFornecedor.
		try {
			mensalidadeDevolucao.setCdFornecedor(Integer.parseInt(beneficiario.getCdFornecedor()));
		} catch (NumberFormatException e) {
			throw new ProcessadorException("O código de fornecedor do beneficiário com código de "
					+ "matrícula %d (comando %d) não parece ser um número válido. Código lido: %s. "
					+ "Grupo de ItemMensalidadeTrabalho Id: %d, MensalidadeTrabalho Id: %d.",
					projecaoMensalidadeBase.getCdMatriculaFornecedor(),
					projecaoMensalidadeBase.getIdComando(),
					beneficiario.getCdFornecedor(),
					grupoItemMensalidadeTrabalho.getIdGrupoItemMensalidadeTrabalho(),
					mensalidadeTrabalho.getIdMensalidadeTrabalho());
		}
		
		// Demais campos, de acordo com a ordem do JSON de exemplo:
		
		//tpReembolso
		mensalidadeDevolucao.setTpReembolso(projecaoMensalidadeBase.getTpReembolso());
		
		// cdReembolso: é o próprio identificador do GrupoItemMensalidadeTrabalho (já que
		// cada grupo de devolução irá gerar um único MensalidadeDevolucao)
		mensalidadeDevolucao.setCdReembolso(grupoItemMensalidadeTrabalho.getIdGrupoItemMensalidadeTrabalho());
				
		// cdControleInterno
		mensalidadeDevolucao.setCdControleInterno(projecaoMensalidadeBase.getCdControleInterno());
		
		// cdMatricula
		mensalidadeDevolucao.setCdMatricula(projecaoMensalidadeBase.getCdMatricula());
		
		// cdMensContrato não pode ser preenchido aqui, então é mantido
		// Na implementação atual, há o campo em projecaoBase, mas é preenchido com null.
		// No momento, será lido desse campo (mantendo o null, apenas para facilitar caso
		// este campo passe a vir preenchido da stored procedure);
		mensalidadeDevolucao.setCdMensContrato(projecaoMensalidadeBase.getCdMensContrato());
		
		//vlTotalOriginal
		mensalidadeDevolucao.setVlTotalOriginal(projecaoMensalidadeBase.getVlTotalOriginal());
		
		// cdFornecedor: já preenchido acima;
		
		// dtVencimento
		mensalidadeDevolucao.setDtVencimento(projecaoMensalidadeBase.getDtVencimento());
		
		// dtInclusao
		mensalidadeDevolucao.setDtInclusao(projecaoMensalidadeBase.getDtInclusao());
		
		// cdUsuarioInclusao
		mensalidadeDevolucao.setCdUsuarioInclusao(projecaoMensalidadeBase.getCdUsuarioInclusao());
		
		// cdTipDoc
		mensalidadeDevolucao.setCdTipDoc(projecaoMensalidadeBase.getCdTipDoc());
		
		// cdMultiEmpresa
		mensalidadeDevolucao.setCdMultiEmpresa(projecaoMensalidadeBase.getCdMultiEmpresa());
		
		//snNotificado
		mensalidadeDevolucao.setSnNotificado(projecaoMensalidadeBase.getSnNotificado());
		
		// snRecusado
		mensalidadeDevolucao.setSnRecusado(projecaoMensalidadeBase.getSnRecusado());
		
		// nrAno
		mensalidadeDevolucao.setNrAno(projecaoMensalidadeBase.getNrAno());
		
		// nrMes
		mensalidadeDevolucao.setNrMes(projecaoMensalidadeBase.getNrMes());
		
		// itreembolsos (uma Collection)
		mensalidadeDevolucao.setItensMensalidadeDevolucao(new ArrayList<>());
		

		// Para os itens de devolução, será necessário fazer mais um agrupamento, agora
		// a partir de projecoesAgrupadas, usando seqItemDevolucao
		Map<Integer, List<ProjecaoMensalidadeDevolucao>> todosProjecoesItensAgrupados = projecoesMensalidadeDevolucao
				.stream()
				.collect(Collectors.groupingBy(p -> p.getSeqItemDevolucao()));
		
		for (Entry<Integer, List<ProjecaoMensalidadeDevolucao>> entradaProjecoesItensAgrupados 
				: todosProjecoesItensAgrupados.entrySet()){
			// Da mesma forma que com as devoluções, as projeções agrupadas por seqItemDevolucao
			// possuem dados comuns para o preenchimento de ItemMensalidadeDevolucao. Então, será
			// usada a mesma estratégia de projeção base.
			ProjecaoMensalidadeDevolucao projecaoItemMensalidadeBase = entradaProjecoesItensAgrupados.getValue().get(0);
			
			ItemMensalidadeDevolucao itemMensalidadeDevolucao = new ItemMensalidadeDevolucao();
			
			// Dos campos de ItemMensalidadeDevolucao, o único que precisa de agrupamento
			// é o vlCobrado (que é a soma dos valores de cada ProjecaoMensalidadeDevolucao 
			// no List<>, já que cada ProjecaoMensalidadeDevolucao corresponde a um registro
			// da tabela COMANDO.
			// Os demais podem ser preenchidos pela projecaoItemMensalidadeBase.
			
			// Campos pela ordem do JSON de exemplo:
			
			// cdReembolso: este campo é preenchido através de mensalidadeDevolucao
			itemMensalidadeDevolucao.setCdReembolso(mensalidadeDevolucao.getCdReembolso());
			
			// cdLctoMensalidade
			itemMensalidadeDevolucao.setCdLctoMensalidade(projecaoItemMensalidadeBase.getCdLctoMensalidade());
			
			// cdSetor
			itemMensalidadeDevolucao.setCdSetor(projecaoItemMensalidadeBase.getCdSetor());
			
			// cdItemRes
			itemMensalidadeDevolucao.setCdItemRes(projecaoItemMensalidadeBase.getCdItemRes());
			
			// qtCobrado (fixo em 1 pela projeção/stored procedure, apesar de o valor ser agrupado por rubrica)
			itemMensalidadeDevolucao.setQtCobrado(projecaoItemMensalidadeBase.getQtCobrado());
			
			// vlCobrado
			itemMensalidadeDevolucao.setVlCobrado(NumberUtil.roundToScaleTwo( 
					entradaProjecoesItensAgrupados
					.getValue()
					.stream()
					.collect(Collectors.summingDouble(e -> e.getVlCobrado()))
					));
			
			mensalidadeDevolucao.getItensMensalidadeDevolucao().add(itemMensalidadeDevolucao);			
			
		}
				
		return mensalidadeDevolucao;
	}

	/**
	 * Gera os {@link GrupoItemMensalidadeTrabalho}s associados a um {@code MensalidadeTrabalho}.
	 * @param mt O {@code MensalidadeTrabalho} que terá seus {@code GrupoItemMensalidadeTrabalho}
	 * gerados.
	 */
	private void geraGruposItemMensalidadeTrabalho(MensalidadeTrabalho mensalidadeTrabalho) throws ProcessadorException {
		// O primeiro passo é, dado um MensalidadeTrabalho, agrupar seus ItemMensalidadeTrabalho,
		// através da stored procedure SPR_ICM_LISTA_GRUPOS_ITENS_MENSALIDADE.
		
		// TODO: ver se é feito teste para conferir se há mais que um grupo de cobrança		
		List<GrupoItemMensalidadeTrabalho> gruposItemMensalidadeTrabalho = new ArrayList<>();
		
		if (mensalidadeTrabalho == null) {
			throw new IllegalArgumentException("mensalidadeTrabalho não pode ser null!");
		}
		
		// Com a mensalidadeTrabalho, é executada a stored procedure para classificar os itens de mensalidadeTrabalho
		// em grupos, através das projeções;
		List<ProjecaoGrupoItemMensalidadeTrabalho> projecoesGruposItemMensalidadeTrabalho;
		
		try {
			projecoesGruposItemMensalidadeTrabalho = pgimtDao.findByMensalidadeTrabalho(mensalidadeTrabalho);
		} catch (PersistenceException e) {
			throw new ProcessadorException(e);
		}
						
		// Com as projeções em mãos, é preciso separar por tipos (cobrança e devolução, para daí criar os grupos).
		Map<Boolean, List<ProjecaoGrupoItemMensalidadeTrabalho>> projecoesGruposItemMensalidadeTrabalhoAgrupadas = 
				projecoesGruposItemMensalidadeTrabalho.stream()
				.collect(Collectors.groupingBy(ProjecaoGrupoItemMensalidadeTrabalho::getfDevolucao));
		
		// Se existem projeções de cobrança (devolução == false)...
		if (projecoesGruposItemMensalidadeTrabalhoAgrupadas.containsKey(Boolean.FALSE)) {
			List<ProjecaoGrupoItemMensalidadeTrabalho> projecoesCobranca = projecoesGruposItemMensalidadeTrabalhoAgrupadas.get(Boolean.FALSE);
			
			// Como as projeções possuem os id_comando e os sequenciais que definem um grupo, é preciso agora separar por grupo		
			long numGrupos = projecoesCobranca.stream()
					.map(ProjecaoGrupoItemMensalidadeTrabalho::getSeqMensalidade)
					.distinct()
					.count();
			
			if (numGrupos > 1) {
				throw new ProcessadorException("Foi encontrado mais de um grupo de cobranças para a Mensalidade de Trabalho Id %d. "
						+ "Não é possível enviar mensalidades nessa situação.",
						mensalidadeTrabalho.getIdMensalidadeTrabalho());
			}
			
			// Com tudo certo, basta transformar as projecoes em Grupos de itens de mensalidade de trabalho, e associar
			// aos itens em questão.
			GrupoItemMensalidadeTrabalho grupoItemMensalidadeTrabalho = new GrupoItemMensalidadeTrabalho();
			grupoItemMensalidadeTrabalho.setMensalidadeTrabalho(mensalidadeTrabalho);
			grupoItemMensalidadeTrabalho.setfDevolucao(Boolean.FALSE);
			List<ItemMensalidadeTrabalho> itensMensalidadeTrabalho = imtDao.listByIdComando(mensalidadeTrabalho, 
					projecoesCobranca.stream()
					.map(ProjecaoGrupoItemMensalidadeTrabalho::getIdComando)
					.collect(Collectors.toList()) );
			
			// Este setItensMensalidadeTrabalho atribui este grupoItemMensalidadeTrabalho a todos os itensMensalidadeTrabalho
			grupoItemMensalidadeTrabalho.setItensMensalidadeTrabalho(itensMensalidadeTrabalho);
			
			gruposItemMensalidadeTrabalho.add(grupoItemMensalidadeTrabalho);			
		}
		
		// Se existem projeções de devolução (devolucao == true)...
		if (projecoesGruposItemMensalidadeTrabalhoAgrupadas.containsKey(Boolean.TRUE)) {
			List<ProjecaoGrupoItemMensalidadeTrabalho> projecoesDevolucao = projecoesGruposItemMensalidadeTrabalhoAgrupadas.get(Boolean.TRUE);
			
			// Como pode realmente haver mais que uma devolução, não é preciso contar dessa vez.
			// Nesse caso, só é preciso separar pelos sequenciais de mensalidade, pois cada um indica um grupo.
			
			Map<Integer, List<ProjecaoGrupoItemMensalidadeTrabalho>> projecoesDevolucaoAgrupadas = 
					projecoesDevolucao.stream()
					.collect(Collectors.groupingBy(ProjecaoGrupoItemMensalidadeTrabalho::getSeqMensalidade));
			
			for (List<ProjecaoGrupoItemMensalidadeTrabalho> lpgimt : projecoesDevolucaoAgrupadas.values()) {
				GrupoItemMensalidadeTrabalho grupoItemMensalidadeTrabalho = new GrupoItemMensalidadeTrabalho();
				grupoItemMensalidadeTrabalho.setMensalidadeTrabalho(mensalidadeTrabalho);
				grupoItemMensalidadeTrabalho.setfDevolucao(Boolean.TRUE);
				List<ItemMensalidadeTrabalho> itensMensalidadeTrabalho = imtDao.listByIdComando(mensalidadeTrabalho,
						lpgimt.stream()
						.map(ProjecaoGrupoItemMensalidadeTrabalho::getIdComando).
						collect(Collectors.toList()) );
				
				// Este setItensMensalidadeTrabalho atribui este grupoItemMensalidadeTrabalho a todos os itensMensalidadeTrabalho
				grupoItemMensalidadeTrabalho.setItensMensalidadeTrabalho(itensMensalidadeTrabalho);
				
				gruposItemMensalidadeTrabalho.add(grupoItemMensalidadeTrabalho);								
			}
			
		}
		
		if (!gruposItemMensalidadeTrabalho.isEmpty()) {
			try {
				gimtDao.mergeAll(gruposItemMensalidadeTrabalho);
			} catch (PersistenceException e) {
				throw new ProcessadorException("Erro ocorrido ao tentar gravar os grupos de mensalidade de trabalho", 
						ThrowableUtil.getRootCause(e));
			}
		}
		
		
	}
	
	/**
	 * Gera uma lista de {@link ImpCoparticipacao} para uma dada lista
	 * de {@link ProjecaoMensalidadeContrato} recebida, utilizando seus {@code idComando}.
	 * @param projecoesMensalidadeContrato As projeções que contém comandos aos quais se deseja
	 *  identificar os procedimentos que geraram suas coparticipações.
	 * @return um {@link List} com os {@code ImpCoparticipacao} referentes
	 * 	aos {@code id_comando}s recebidos.
	 * @throws ProcessadorException Caso não seja possível localizar o procedimento
	 * 	correspondente a um {@code id_comando}, ou se o procedimento não existir.
	 */
	private List<ImpCoparticipacao> listaImpCoparticipacao(List<ProjecaoMensalidadeContrato> projecoesMensalidadeContrato) throws ProcessadorException {
		List<ImpCoparticipacao> impCoparticipacoes = new ArrayList<>();			
		
		for (ProjecaoMensalidadeContrato projecaoMensalidadeContrato : projecoesMensalidadeContrato) {		
			Integer idComando = projecaoMensalidadeContrato.getIdComando();
			
			if (null == idComando) {
				throw new ProcessadorException("A projecaoMensalidadeTrabalho informada possui idComando null.");
			}
			
			if (!TipoRubricaCac.PARTICIPACAO.equals(projecaoMensalidadeContrato.getTipoRubricaCac()) ) {
				logger.warnf("O id_comando %d não é de coparticipação. Pulando busca de coparticipação para "
						+ "este id_comando", idComando);
				continue;
			}
			
			List<ImpCoparticipacao> impCoparticipacaoComando = icDao.findByIdComando(idComando);
			
			ImpCoparticipacao impCoparticipacao;
			
			// Validando os resultados encontrados
			if ( (impCoparticipacaoComando == null) || (impCoparticipacaoComando.isEmpty()) ){
				logger.warnf("Não foi possível localizar o procedimento cuja coparticipação "
						+ "é cobrada com o id_comando %d. Será criada uma coparticipação com valores padrão.", idComando);
				
				impCoparticipacao = geraImpCoparticipacaoPadrao(projecaoMensalidadeContrato);
				
			} else {
			
				if (1 != impCoparticipacaoComando.size()){			
					throw new ProcessadorException(String.format("Foi encontrado mais que um procedimento associado ao id_comando %d. "
							+ "Não é possível determinar o procedimento de origem correto. Procedimentos encontrados: %s.", 
							idComando,
							impCoparticipacaoComando.toString())
							);
				}
				
				impCoparticipacao = impCoparticipacaoComando.get(0);
				
				if (null == impCoparticipacao.getCdPrestador()){
					logger.warnf("Não foi possível determinar o prestador executante "
							+ "do procedimento %s. Será utilizado o Prestador padrão.", 
							impCoparticipacao.getProcedimentoId().toString()
							);
					impCoparticipacao.setCdPrestador(Constantes.PRESTADOR_PADRAO);
				}
			}
			
			impCoparticipacoes.add(impCoparticipacao);
		}
		
		return (impCoparticipacoes.isEmpty() ? null : impCoparticipacoes);
	}

	private ImpCoparticipacao geraImpCoparticipacaoPadrao(ProjecaoMensalidadeContrato projecaoMensalidadeContrato) {
		ImpCoparticipacao impCoparticipacao = new ImpCoparticipacao();
		impCoparticipacao.setCdPrestador(Constantes.PRESTADOR_PADRAO);
		impCoparticipacao.setCdProcedimento(Constantes.PROCEDIMENTO_PADRAO);
		impCoparticipacao.setDtRealizacao(Constantes.getDataRealizacaoPadrao());
		impCoparticipacao.setIdComando(projecaoMensalidadeContrato.getIdComando());
		
		// TODO: Melhorar a implementação para não usar charAt
		impCoparticipacao.setTpGuia(Constantes.TIPO_GUIA_MENSALIDADE_COPARTICIPACAO_PADRAO);
		impCoparticipacao.setVlCoparticipacao(projecaoMensalidadeContrato.getVlLancamento());
		
		return impCoparticipacao;
	}

	private void gerarErroProcessamentoMensalidadeTrabalho(MensalidadeTrabalho mt, RetornoMensagem retornoMensagem) {
		// Tenta buscar a lista de erros proveniente de retornoMensagem. Se for null, utiliza uma lista vazia.
		gerarErroProcessamentoMensalidadeTrabalho(mt,
					Optional.ofNullable(retornoMensagem.getMensagem())
					.orElse(Collections.emptyList())
					.stream()
					.map(String::new)
					.toArray(String[]::new)
				);
	}

	// XXX: Marcado para possível refactoring, devido à similaridade dessa estrutura com a de outros processadores.
	private void gerarErroProcessamentoMensalidadeTrabalho(MensalidadeTrabalho mt, String... mensagens) {
		ErroProcessamentoMensalidade erroProcessamento = null;
		
		if (mensagens.length == 0){
			erroProcessamento = new ErroProcessamentoMensalidade();
			erroProcessamento.setMensalidadeTrabalho(mt);
			erroProcessamento.setMensagem("Erro genérico do SOUL MV. Nenhuma mensagem de erro foi retornada");
			epmDao.persist(erroProcessamento);		
			
		} else {
			for (String mensagem : mensagens){
				erroProcessamento = new ErroProcessamentoMensalidade();
				erroProcessamento.setMensalidadeTrabalho(mt);
				if (mensagem.length() >= 800){
					erroProcessamento.setMensagem(mensagem.substring(0, 800));
				} else {
					erroProcessamento.setMensagem(mensagem);
				}
				
				epmDao.persist(erroProcessamento);
			}
		}
		mt.setStatusProcessamento(getStatusProcessamento(StatusProcessamento.PROCESSADO_COM_ERROS));
		mtDao.merge(mt);
		
		logger.warnf("Ocorreram erros no processamento da MensalidadeTrabalho %s. "
						+ "Não foi possível enviá-la à MV. ",
					mt.getIdMensalidadeTrabalho()
				);
	}
	
	/**
	 * Classe simples para representar os JSON dos serviços de cancelamento.
	 * @author JCJ
	 * @since 2017-07-15
	 */
	public static class CancelaMensalidade{
		public static final Integer CODIGO_ANULACAO = 26;
		
		private Integer id;
		
		private Date dtCancelamento;
		
		private Integer cdMotivoCancelamento;
		
		public CancelaMensalidade() {
			// Construtor padrão
		}
		
		public static CancelaMensalidade criaCancelaMensalidadeAnulacao(Integer id) {
			CancelaMensalidade cancelaMensalidade = new CancelaMensalidade();
			
			cancelaMensalidade.id = id;
			cancelaMensalidade.dtCancelamento = Calendar.getInstance().getTime();
			cancelaMensalidade.cdMotivoCancelamento = CODIGO_ANULACAO;
			
			return cancelaMensalidade;
		}
		
		public static CancelaMensalidade criaCancelaMensalidadeAnulacao(Integer id,
				Date dtCancelamento) {
			CancelaMensalidade cancelaMensalidade = new CancelaMensalidade();
			
			cancelaMensalidade.id = id;
			cancelaMensalidade.dtCancelamento = dtCancelamento;
			cancelaMensalidade.cdMotivoCancelamento = CODIGO_ANULACAO;
			
			return cancelaMensalidade;
		}

		/** Getter para id.
		 * @return o valor de id.
		 */
		public Integer getId() {
			return id;
		}

		/** Setter para id.
		 * @param id o novo valor de id.
		 */
		public void setId(Integer id) {
			this.id = id;
		}

		/** Getter para dtCancelamento.
		 * @return o valor de dtCancelamento.
		 */
		public Date getDtCancelamento() {
			return dtCancelamento;
		}

		/** Setter para dtCancelamento.
		 * @param dtCancelamento o novo valor de dtCancelamento.
		 */
		public void setDtCancelamento(Date dtCancelamento) {
			this.dtCancelamento = dtCancelamento;
		}

		/** Getter para cdMotivoCancelamento.
		 * @return o valor de cdMotivoCancelamento.
		 */
		public Integer getCdMotivoCancelamento() {
			return cdMotivoCancelamento;
		}

		/** Setter para cdMotivoCancelamento.
		 * @param cdMotivoCancelamento o novo valor de cdMotivoCancelamento.
		 */
		public void setCdMotivoCancelamento(Integer cdMotivoCancelamento) {
			this.cdMotivoCancelamento = cdMotivoCancelamento;
		}
	}

}
