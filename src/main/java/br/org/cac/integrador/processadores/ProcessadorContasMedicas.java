package br.org.cac.integrador.processadores;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
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
import com.fasterxml.jackson.databind.ObjectMapper;

import br.org.cac.cacmvmiddleware.services.ContaMedicaWebServices;
import br.org.cac.integrador.dao.ContaMedicaDAO;
import br.org.cac.integrador.dao.DeParaAjusteSubProcessoDAO;
import br.org.cac.integrador.dao.DeParaSubProcessoDAO;
import br.org.cac.integrador.dao.ErroProcessamentoDAO;
import br.org.cac.integrador.dao.ItemContaMedicaDAO;
import br.org.cac.integrador.dao.LoteContaMedicaDAO;
import br.org.cac.integrador.dao.LoteTrabalhoDAO;
import br.org.cac.integrador.dao.SubProcessoIdDAO;
import br.org.cac.integrador.exception.ProcessadorException;
import br.org.cac.integrador.jacksonutil.ObjectMapperFactory;
import br.org.cac.integrador.modelo.ContaMedica;
import br.org.cac.integrador.modelo.ItemContaMedica;
import br.org.cac.integrador.modelo.ItemContaMedicaPagamento;
import br.org.cac.integrador.modelo.LoteContaMedica;
import br.org.cac.integrador.modelo.RetornoMensagem;
import br.org.cac.integrador.modelo.SubProcessoId;
import br.org.cac.integrador.modelo.infraestrutura.DeParaAjusteSubProcesso;
import br.org.cac.integrador.modelo.infraestrutura.DeParaSubProcesso;
import br.org.cac.integrador.modelo.infraestrutura.ErroProcessamento;
import br.org.cac.integrador.modelo.infraestrutura.LoteTrabalho;
import br.org.cac.integrador.modelo.infraestrutura.MotivoAjuste;
import br.org.cac.integrador.modelo.infraestrutura.Processamento;
import br.org.cac.integrador.modelo.infraestrutura.StatusProcessamento;
import br.org.cac.integrador.util.Constantes;
import br.org.cac.integrador.util.ThrowableUtil;

/**
 * Processador responsável pelo processamento de contas médicas.
 * 
 * @author JCJ
 * @version 1.1
 * @since 1.0, 2017-03-13 
 *
 */
public class ProcessadorContasMedicas extends AbstractProcessador{
	
	private static final double ZERO_DOUBLE = 0D;
	
	@Inject
	private DeParaSubProcessoDAO dpspDao;
	
	@Inject
	private ErroProcessamentoDAO epDao;
	
	@Inject
	private LoteTrabalhoDAO ltDao;
	
	@Inject
	private LoteContaMedicaDAO lcmDao;
	
	@Inject
	private ContaMedicaDAO cmDao;
	
	@Inject
	private ItemContaMedicaDAO icmDao;
	
	@Inject
	private SubProcessoIdDAO spiDao;
	
	@Inject
	private DeParaAjusteSubProcessoDAO dpaspDao;
	
	@Inject
	private ContaMedicaWebServices webServiceContaMedica;
	
	@Inject
	private Logger logger;

	public ProcessadorContasMedicas() {
		super();
	}
	
	@Override
	public ResumoProcessamento processa(Processamento processamento) throws ProcessadorException {
		
		if (processamento == null) {
			throw new IllegalArgumentException("O parâmetro processamento não pode ser null.");
		}
		
		ResumoProcessamento resumoProcessamento = new ResumoProcessamento(processamento);
		
		logger.info("Iniciando processamento. Id: " + processamento.getIdProcessamento());

		List<LoteTrabalho> lotesTrabalho = ltDao.findByProcessamento(processamento);
		List<LoteContaMedica> lotesContaMedica = null;
		Response response = null;
		ObjectMapper mapper = ObjectMapperFactory.getDefaultConfiguredObjectMapper(false);

		logger.infof("%d Lote%sTrabalho para processamento",
											lotesTrabalho.size(),
											(lotesTrabalho.size() == 1 ? "" : "s")
				);
		// Atualizando dados de resumoProcessamento
		resumoProcessamento.setTotalItens(lotesTrabalho.size());
			
		for (LoteTrabalho lt : lotesTrabalho) {
			logger.infof("Iniciando processamento do LoteTrabalho Id: %d.", 
					lt.getIdLoteTrabalho()
					);
			
			lt.setStatusProcessamento(getStatusProcessamento(StatusProcessamento.EM_PROCESSAMENTO));
			ltDao.merge(lt);
			
			try {
				lotesContaMedica = this.processaLoteTrabalho(lt);
			} catch (ProcessadorException e) {				
				gerarErroProcessamentoLoteTrabalho(lt, 
						String.format("Erro de processamento no Lote de Trabalho %d: %s",
								lt.getIdLoteTrabalho(),
								ThrowableUtil.getRootMessage(e))
						);
				resumoProcessamento.incItensComErro();
				continue;
			}

			if ((lotesContaMedica == null) || (lotesContaMedica.isEmpty())) {
				gerarErroProcessamentoLoteTrabalho(lt, "Lote de Trabalho não gerou lotes de contas médicas. "
						+ "Lote de Trabalho: " + lt.getIdLoteTrabalho());
				resumoProcessamento.incItensComErro();
				continue;
			}
			
			RetornoMensagem retornoMensagem = null;

			for (LoteContaMedica lcm : lotesContaMedica) {
				if (lt.getStatusProcessamento() != getStatusProcessamento(StatusProcessamento.EM_PROCESSAMENTO)){
					continue;
				}
				
				try {
					boolean cnesInvalido = false;
					boolean procedimentoInvalido = false;
					boolean tentarNovamente = false;
					
					do {
						// Caso seja recebido o erro de CNES inválido, altera o CNES atual para 
						// "Não informado" (9999999) e tenta enviar novamente
						if (cnesInvalido) {	
							tentarNovamente = false;
							logger.warn("Detectado erro por parte da MV de CNES inválido ou não cadastrado. "
									+ "Tentando substituir pelo valor de \"CNES não informado\"...");
							
							// Caso não consiga corrigir o CNES inválido, não adianta tentar novamente,
							// então pode-se quebrar o laço.
							if (!corrigirCNESLoteContaMedica(lcm, retornoMensagem)) {
								logger.warnf("Detectada tentativa de correção de CNES inválidos, porém não foi possível identificar "
										+ "os CNES que sofreram esse erro. Lote de Trabalho %d.", 
										lt.getIdLoteTrabalho() );								
								break;
							}
							logger.warnf("Tentando enviar novamente o Lote de Trabalho %d, com os CNES ajustados.", 
									lt.getIdLoteTrabalho());
						}
						
						if (procedimentoInvalido) {
							tentarNovamente = false;
							logger.warnf("Detectado erro por parte da MV de procedimento inválido ou não cadastrado. "
									+ "Tentando substituir pelo valor de \"Procedimento padrão (%d)\"...",
									Constantes.PROCEDIMENTO_PADRAO);
							
							if (!corrigirProcedimentoLoteContaMedica(lcm, retornoMensagem)) {
								logger.warnf("Detectada tentativa de correção de procedimentos inválidos, porém não foi possível identificar "
										+ "os procedimentos que sofreram esse erro. Lote de Trabalho %d.", 
										lt.getIdLoteTrabalho() );								
								break;
							}
							logger.warnf("Tentando enviar novamente o Lote de Trabalho %d, com os procedimentos ajustados.", 
									lt.getIdLoteTrabalho());
						}
					
						// Enviando o JSON do objeto gerado para a MV, via middleware
						logger.infof("Iniciando envio do Lote de Trabalho Id: %d", lt.getIdLoteTrabalho());
						response = webServiceContaMedica.incluir(mapper.writeValueAsString(lcm));
						
						String retorno = (String)response.getEntity();
						retornoMensagem = mapper.readValue(retorno, RetornoMensagem.class);
						
						// TODO: Ajustar melhor esses if/else para evitar o aninhamento
						if (response.getStatus() != 200){
							gerarErroProcessamentoLoteTrabalho(lt, retornoMensagem);
							resumoProcessamento.incItensComErro();
							
						} else {
							if (!retornoMensagem.getStatus().equals("200")){
								gerarErroProcessamentoLoteTrabalho(lt, retornoMensagem);
								
								// Só tenta novamente uma vez, caso encontre um cnesInvalido
								cnesInvalido = (!cnesInvalido) && (retornoMensagem.buscaPadraoMensagem(RetornoMensagem.REGEX_ERRO_CNES));
								procedimentoInvalido = (!procedimentoInvalido) && (retornoMensagem.buscaPadraoMensagem(RetornoMensagem.REGEX_ERRO_PROCEDIMENTO_INVALIDO_CONTA_MEDICA));
								
								tentarNovamente = cnesInvalido || procedimentoInvalido;
								
								if (!tentarNovamente) {
									resumoProcessamento.incItensComErro();
								}
							} else {
								DeParaSubProcesso deParaSubProcesso = new DeParaSubProcesso();
								
								deParaSubProcesso.setProcessamento(processamento);
								deParaSubProcesso.setSubProcesso(lt.getSubProcesso());
								deParaSubProcesso.setCdLoteInclusao(lcm.getCdLote());
								deParaSubProcesso.setCdLoteEfetivo(Integer.valueOf(retornoMensagem.getEntidadeId()));
								deParaSubProcesso.setTipoGuia(lcm.getTpGuia());
								
								dpspDao.persist(deParaSubProcesso);
								
								lt.setStatusProcessamento(getStatusProcessamento(StatusProcessamento.PROCESSADO_TOTALMENTE));
								ltDao.merge(lt);
								
								List<DeParaAjusteSubProcesso> ajustesSubProcesso = extrairAjustesSubProcesso(lcm);
								
								if (!ajustesSubProcesso.isEmpty()) {
									ajustesSubProcesso.stream()
										.forEach(e -> e.setDeParaSubProcesso(deParaSubProcesso));
									
									try {
										dpaspDao.mergeAll(ajustesSubProcesso);
									} catch (PersistenceException e) {
										logger.errorf("Foram realizados ajustes para o envio deste lote de conta médica (Id: %d), porém não foi possível gravá-los. "
												+ "Será necessário consultar o log da operação no servidor para identificar os ajustes feitos.", 
												lt.getIdLoteTrabalho());
									}
									
								}
								
								logger.infof("Lote %s enviado para a MV com sucesso. Id do envio: %d", 
										lt.getSubProcesso(),
										deParaSubProcesso.getIdDeParaSubProcesso()
										);
								resumoProcessamento.incItensEnviados();
							}
						}
					} while (tentarNovamente);
					

					
				} catch (JsonProcessingException e) {
					gerarErroProcessamentoLoteTrabalho(lt, "Erro ao converter Lote de Conta médica para JSON, "
							+ "para envio ao SOUL. Lote de Trabalho " + lt.getIdLoteTrabalho());
					resumoProcessamento.incItensComErro();
					continue;
					
					//throw new ProcessadorException(e);
				} catch (IOException e) {
					gerarErroProcessamentoLoteTrabalho(lt, "Erro ao ler o retorno do Middleware para "
							+ "JSON. Não é possível dizer se o lote foi incluído ou não. Lote de Trabalho " + lt.getIdLoteTrabalho());
					resumoProcessamento.incItensComErro();
					continue;
				}
			}
			
			if (lt.getStatusProcessamento().equals(getStatusProcessamento(StatusProcessamento.EM_PROCESSAMENTO))){
				lt.setStatusProcessamento(getStatusProcessamento(StatusProcessamento.PROCESSADO_TOTALMENTE));
				ltDao.merge(lt);
			}

		}
		
		return resumoProcessamento;

	}
	
	public List<LoteContaMedica> processaLoteTrabalho(LoteTrabalho loteTrabalho) throws ProcessadorException{
		if (loteTrabalho == null) {
			throw new IllegalArgumentException("O parâmetro loteTrabalho não pode ser null.");
		}
		
		return this.processaSubProcesso(loteTrabalho.getSubProcesso(), loteTrabalho.getIdLoteTrabalho(), true);
	}
	
	public List<LoteContaMedica> processaSubProcesso(SubProcessoId subProcesso, int cdLote) throws ProcessadorException{
		return this.processaSubProcesso(subProcesso, cdLote, false);
	}

	private List<LoteContaMedica> processaSubProcesso(SubProcessoId subProcesso, int cdLote, boolean calculaCoparticipacao) 
			throws ProcessadorException {
		logger.info("Processando Subprocesso " + subProcesso);
		
		if (subProcesso == null) {
			throw new IllegalArgumentException("O parâmetro subProcesso não pode ser null.");
		}
		
		if (calculaCoparticipacao) {
			// Para poder extrair os dados corretamente, deve ser calculado o valor de coparticipação dos procedimentos deste
			// subprocesso.
			logger.infof("Iniciando cálculo de coparticipação para o subprocesso %s.", subProcesso);
			try {
				spiDao.calculaCoparticipacaoSubProcesso(subProcesso);
			} catch (PersistenceException e) {
				logger.errorf("Ocorreu um erro no cálculo de coparticipação do sub-processo %s: %s", subProcesso,
						ThrowableUtil.getRootCause(e));

				throw new ProcessadorException(
						String.format("Ocorreu um erro no cálculo de coparticipação do sub-processo %s: %s", 
								subProcesso, 
								ThrowableUtil.getRootMessage(e)),
						ThrowableUtil.getRootCause(e));
			}
			logger.infof("Finalizado cálculo de coparticipação do subprocesso %s. Continuando processamento...",
					subProcesso);
		}
			
		// listaLotesProcedure converte o subprocesso informado na procedure nos dados correspondentes
		// para a criação de um LoteContaMedica (estrutura da MV).
		// Apesar de tentar buscar mais de um LoteContaMedica para o lote de trabalho, por construção
		// a geração deverá ser 1:1 entre LoteTrabalho e LoteContaMedica
		List<LoteContaMedica> lotesContaMedica = lcmDao.findLotesContaMedicaBySubProcessoId(subProcesso);
		
		if ((lotesContaMedica == null) || (lotesContaMedica.isEmpty())) {
			throw new ProcessadorException(
					"Não foi encontrado nenhum subprocesso com os dados informados, ou ele já "
					+ "foi enviado à MV. Subprocesso: " + subProcesso);
		}
			
		for (Iterator<LoteContaMedica> iterator = lotesContaMedica.iterator(); iterator.hasNext(); ) { 
			LoteContaMedica lote = iterator.next(); 
			lote.setCdLote(cdLote);
			
			ajustesLoteContaMedica(lote);

			ArrayList<ContaMedica> contasDoLote = new ArrayList<>();
			
			List<ContaMedica> contasMedicas = cmDao.findContasMedicasByLoteContaMedica(lote);
			
			// TODO: ver o tratamento a ser dado quando não forem localizadas
			// contas médicas para o lote
			if (!contasMedicas.isEmpty()) {
				lote.setTpGuia(contasMedicas.get(0).getTpGuia());

				/*
				 * Em alguns casos, existem atendimentos distintos que
				 * representam uma mesma conta médica (como tratamento seriado).
				 * Nesses casos, será preciso agrupá-los numa única ContaMedica.
				 * 
				 */
				Map<Integer, List<ContaMedica>> contasAgrupadas = contasMedicas.stream()
						.collect(Collectors.groupingBy(c -> c.getCdContaMedica()));

				ContaMedica contaBase;
				int sequencialItemContaMedica;

				for (Integer cdContaMedica : contasAgrupadas.keySet()) {
					List<ContaMedica> contasPorCdContaMedica = contasAgrupadas.get(cdContaMedica);

					// Dado que as contas agrupadas são iguais, será utilizada
					// como base a primeira
					contaBase = contasPorCdContaMedica.get(0);
					contaBase.setItensConta(new ArrayList<ItemContaMedica>());
					double valorTotalFranquia = ZERO_DOUBLE;
					
					// TODO: Ver o lugar correto para chamar ajusteContaMedica
					
					// Verificando campos obrigatórios:
					if (null == contaBase.getCdMatricula() ) {
						throw new ProcessadorException("O beneficiário do atendimento %s não possui cdMatricula associado. "
								+ "Lote de Trabalho Id: %d",
								contaBase.getAtendimento(),
								cdLote);
					}
					

					sequencialItemContaMedica = 0;
					for (ContaMedica conta : contasPorCdContaMedica) {											
						ItemContaMedicaPagamento itemContaMedicaPagamentoBase = new ItemContaMedicaPagamento();
						itemContaMedicaPagamentoBase.setCdPrestador(lote.getCdPrestador());
						// TODO: tirar o valor chumbado de tpSituacao daqui (provavelmente para um Enum?)
						itemContaMedicaPagamentoBase.setTpSituacao("AA");
						itemContaMedicaPagamentoBase.setVlPago(0.0);
						itemContaMedicaPagamentoBase.setVlTotalCobrado(0.0);

						List<ItemContaMedica> itensContaMedica = icmDao.findItensContaMedicaByContaMedica(conta);

						if ((itensContaMedica == null) || (itensContaMedica.isEmpty())) {
							logger.warnf("Não foi encontrado nenhum procedimento para o atendimento "
									+ "informado. Atendimento %s",
									conta.getAtendimento().toString());
							continue;
						}

						// TODO: Ver o que será feito quando não há itens de
						// conta médica
						if (!itensContaMedica.isEmpty()) {
							
							for (ItemContaMedica item : itensContaMedica) {
								item.setCdItContaMedica(Math.round(
										conta.getCdContaMedica() * Math.pow(10, 3) + ++sequencialItemContaMedica));
								
								ajustesItemContaMedica(item);

								ItemContaMedicaPagamento itemContaMedicaPagamento = itemContaMedicaPagamentoBase
										.clone();
								itemContaMedicaPagamento.setCdItContaMedica(item.getCdItContaMedica());
								itemContaMedicaPagamento.setCdMotivo(item.getCdMotivo());
								itemContaMedicaPagamento.setVlPago(item.getVlPago());
								itemContaMedicaPagamento.setVlTotalCobrado(item.getVlTotalCobrado());

								item.setItensContaMedicaPagamento(new ArrayList<>());
								item.getItensContaMedicaPagamento().add(itemContaMedicaPagamento);
								
								// Como há um valor total de franquia, ele deve ser acumulado a partir do
								// valor individual de cada item
								valorTotalFranquia += Optional.ofNullable(item.getVlFranquia()).orElse(ZERO_DOUBLE);

								// Apesar de estar olhando neste laço para o ContaMedica conta, os
								// itens encontrados serão adicionados na conta agrupadora (que é
								// contaBase)								

								// TODO: Verificar a necessidade de implementar um Comparator para ItemContaMedica, 
								// de modo que contaBase.itensConta tenha os ItemContaMedica ordenados (para que o 
								// JSON gerado seja sempre na mesma ordem)
								contaBase.getItensConta().add(item);

							}
							
							// TODO: implementar Comparator manager
							// ItemContaMedica e adicionar aqui ordenado
							// contaBase.getItensConta().addAll(itensContaMedica);
						}
					}
					
					// Depois de todo o processamento, adiciona a conta médica
					// na lista de contas do lote
					if ( (contaBase != null) && (!contaBase.getItensConta().isEmpty()) ) {
						// Com todos os itens da conta médica processados, verifica-se se há alguma coparticipação.
						// Caso sim, ele é definido na conta.
						if (Double.compare(valorTotalFranquia, ZERO_DOUBLE) != 0){
							contaBase.setVlFranquia(valorTotalFranquia);
						}						
						
						contasDoLote.add(contaBase);
					}

				}

			}

			if (!contasDoLote.isEmpty()) {
				// Ordena as contas no lote, para tentar garantir que a geração
				// sempre será igual				
				lote.setContasMedicas(contasDoLote.stream().sorted().collect(Collectors.toList()));
			} else {
				// Isso indica que o lote não gerou nenhuma conta médica válida. Portanto, é removido da lista
				// retornada por este método.
				iterator.remove();
			}

		} 


		return lotesContaMedica;
	}

	/**
	 * Realiza ajustes nos dados presentes em um {@link ItemContaMedica}, registrando nele próprio o que foi feito.
	 * @param item o {@code ItemContaMedica} a ser ajustado.
	 */
	private void ajustesItemContaMedica(ItemContaMedica item) {
		// Primeiro ajuste: valor apresentado do item menor que o valor pago.
		// Nesses casos, será assumido o valor pago como sendo o valor apresentado.
		Double vlTotalCobrado = Optional.ofNullable(item.getVlTotalCobrado()).orElse(0.0);
		
		if (vlTotalCobrado < item.getVlPago()) {
			logger.warnf("Detectado procedimento com valor apresentado menor que o pago. "
					+ "Procedimento %s, valor apresentado %.2f, valor pago %.2f. "
					+ "Ajustando valor apresentado para ser igual ao pago.",
					item.getProcedimento().toString(),
					vlTotalCobrado,
					item.getVlPago());
			
			item.addAjusteSubProcesso("valor_apresentado", String.valueOf(vlTotalCobrado), String.valueOf(item.getVlPago()), MotivoAjuste.VALOR_APRESENTADO_MENOR_QUE_PAGO);
			item.setVlTotalCobrado(item.getVlPago());
			
		}
	}

	private void ajustesLoteContaMedica(LoteContaMedica lote) {
		// No momento, este método não faz nada, mas está reservado para futuros ajustes em lotes de conta médica			
	}

	private List<DeParaAjusteSubProcesso> extrairAjustesSubProcesso(LoteContaMedica loteContaMedica) {
		List<DeParaAjusteSubProcesso> ajustesSubProcesso = new ArrayList<>();
		
		// TODO: futuramente, adicionar os ajustes provenientes de LoteContaMedica e ContaMedica.
		// Na implementação atual, apenas ItemContaMedica admite ajustes. Então, serão simplesmente extraídos desse nível
		for (ContaMedica contaMedica : loteContaMedica.getContasMedicas()) {
			for (ItemContaMedica itemContaMedica : contaMedica.getItensConta()) {
				ajustesSubProcesso.addAll(itemContaMedica.getDeParaAjustesSubProcesso());
			}
		}
		
		return ajustesSubProcesso;
	}

	/**
	 * Tenta corrigir um lote de conta médica, substituindo os possíveis CNES inválidos de suas contas e
	 * informados em {@link RetornoMensagem} pelo valor padrão para "CNES não informado": 9999999.
	 *  
	 * @param loteContaMedica o {@link LoteContaMedica} que contém as contas médicas a serem corrigidas.
	 * @param retornoMensagem o conjunto do mensagens de retorno enviado pela MV, que pode ou não conter
	 * informações dos CNES inválidos detectados.
	 * @return {@code true}, caso faça ao menos uma alteração de CNES; {@code false}, caso contrário.
	 */
	private boolean corrigirCNESLoteContaMedica(LoteContaMedica loteContaMedica, RetornoMensagem retornoMensagem) {
		// O objetivo deste método é, dada uma lista de mensagens de erro, extrair os possíveis erros
		// de CNES inválidos encontrados e corrigir no lote de contas médicas, buscando as contas
		// que possuem esse CNES e substituindo-o pelo CNES 9999999 (CNES não informado)
		
		boolean result = false;
		
		// Primeiro, deve-se extrair os CNES inválidos informados na mensagem de erro:
		Set<String> cnesInvalidos = new HashSet<>();
		
		Pattern padraoErroCnes = Pattern.compile(RetornoMensagem.REGEX_ERRO_CNES);
		
		for (String s : retornoMensagem.getMensagem()){
			Matcher matcher = padraoErroCnes.matcher(s);
			if (matcher.find()){
				cnesInvalidos.add(matcher.group(1));
			}
		}
		
		// Se não houver nenhuma informação de CNES inválido, não faz sentido continuar o processamento.
		// Então, a função retorna false.
		if (cnesInvalidos.isEmpty()){
			return false;
		}
		
		// Uma vez com os CNES inválidos, deve-se percorrer a lista de contas médicas presentes no
		// lote e substituir pelo CNES não informado.
		for (ContaMedica conta : loteContaMedica.getContasMedicas()){
			// Como não é possível saber, em princípio qual dos dois CNES foi inválido (CNES do 
			// prestador executante ou CNES do local de atendimento), faz-se o teste com ambos.
			if ( (conta.getCdCnes() != null) && (cnesInvalidos.contains(conta.getCdCnes()) ) ) {
				logger.warnf("CNES do Prestador Executante inválido ou não cadastrado encontrado "
						+ "no atendimento %s: %s. Será substituído pelo CNES padrão "
						+ "para não informado (%s).", 
						conta.getAtendimento(),
						conta.getCdCnes(),
						Constantes.CNES_NAO_INFORMADO);
				conta.setCdCnes(Constantes.CNES_NAO_INFORMADO);
				result = true;
			}
			
			
			if ( (conta.getCdCnesLocalAtendimento() != null) && (cnesInvalidos.contains(conta.getCdCnesLocalAtendimento()) ) ) {
				logger.warnf("CNES do Local de Atendimento inválido ou não cadastrado encontrado "
						+ "no atendimento %s: %s. Será substituído pelo CNES padrão "
						+ "para Não Informado (%s).", 
						conta.getAtendimento(),
						conta.getCdCnesLocalAtendimento(),
						Constantes.CNES_NAO_INFORMADO);
				conta.setCdCnesLocalAtendimento(Constantes.CNES_NAO_INFORMADO);
				result = true;
			}
			
			
		}
		
		return result;
	}

	private boolean corrigirProcedimentoLoteContaMedica(LoteContaMedica loteContaMedica, RetornoMensagem retornoMensagem) {
		// O objetivo deste método é, dada uma lista de mensagens de erro, extrair os possíveis erros
		// de procedimentos inválidos encontrados e corrigir no lote de contas médicas, buscando as contas
		// que possuem esses procedimentos e substituindo-o pelo procedimento padrão (5001000 - diagnose)
				
		boolean result = false;

		// Primeiro, deve-se extrair os CNES inválidos informados na mensagem de erro:
		Set<Integer> procedimentosInvalidos = new HashSet<>();

		Pattern padraoErroProcedimento = Pattern.compile(RetornoMensagem.REGEX_ERRO_PROCEDIMENTO_INVALIDO_CONTA_MEDICA);

		for (String s : retornoMensagem.getMensagem()){
			Matcher matcher = padraoErroProcedimento.matcher(s);
			if (matcher.find()){
				try {
					procedimentosInvalidos.add(Integer.parseInt(matcher.group(1)));
				} catch (NumberFormatException e) {
					logger.errorf("Foi encontrado um código de procedimento inválido que não parece ser um número: %s. "
							+ "Não será possível corrigir os procedimentos do Lote de Conta Médica devido"
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

		// Uma vez com os procedimentos inválidos, deve-se percorrer a lista de contas médicas presentes no
		// lote e substituir, em seus itens de conta médica, pelo procedimento padrão. 
		for (ContaMedica conta : loteContaMedica.getContasMedicas()){
			for (ItemContaMedica itemContaMedica : conta.getItensConta()) {
				if ( (null != itemContaMedica.getCdProcedimento()) && (procedimentosInvalidos.contains(itemContaMedica.getCdProcedimento()) ) ) {
					logger.warnf("Código de procedimento inválido encontrado no procedimento %s: %d. Será substituído "
							+ "pelo Procedimento padrão (%d)", 
							itemContaMedica.getProcedimento().toString(),
							itemContaMedica.getCdProcedimento(),
							Constantes.PROCEDIMENTO_PADRAO
							);
					itemContaMedica.setCdProcedimento(Constantes.PROCEDIMENTO_PADRAO);
					result = true;
				}
			}


		}

		return result;
	}

	private void gerarErroProcessamentoLoteTrabalho(LoteTrabalho lt, RetornoMensagem retornoMensagem) {
		// Tenta buscar a lista de erros proveniente de retornoMensagem. Se for null, utiliza uma lista vazia.
		gerarErroProcessamentoLoteTrabalho(lt, 
					Optional.ofNullable(retornoMensagem.getMensagem())
					.orElse(Collections.emptyList())
					.stream()
					.map(String::new)
					.toArray(String[]::new)
				);
	}

	// XXX: Marcado para possível refactoring, devido à similaridade dessa estrutura com a de outros processadores.
	private void gerarErroProcessamentoLoteTrabalho(LoteTrabalho lt, String... mensagens) {
		ErroProcessamento erroProcessamento = null;
		
		if (mensagens.length == 0){
			erroProcessamento = new ErroProcessamento();
			erroProcessamento.setLoteTrabalho(lt);
			erroProcessamento.setMensagem("Erro genérico do SOUL MV. Nenhuma mensagem de erro foi retornada");
			epDao.persist(erroProcessamento);
			
		} else {		
			for (String mensagem : mensagens){
				erroProcessamento = new ErroProcessamento();
				erroProcessamento.setLoteTrabalho(lt);
				if (mensagem.length() >= 800){
					erroProcessamento.setMensagem(mensagem.substring(0, 800));
				} else {
					erroProcessamento.setMensagem(mensagem);
				}
				
				epDao.persist(erroProcessamento);
			}
		}
		lt.setStatusProcessamento(getStatusProcessamento(StatusProcessamento.PROCESSADO_COM_ERROS));
		ltDao.merge(lt);
		
		logger.warnf("Ocorreram erros no processamento do Lote %s. "
						+ "Não foi possível enviá-lo à MV. ",
					lt.getSubProcesso()
				);
	}
}
