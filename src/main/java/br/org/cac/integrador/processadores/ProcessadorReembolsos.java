package br.org.cac.integrador.processadores;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.persistence.PersistenceException;
import javax.ws.rs.core.Response;

import org.jboss.logging.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;

import br.org.cac.cacmvmiddleware.modelo.retorno.Beneficiario;
import br.org.cac.cacmvmiddleware.services.ReembolsoWebServices;
import br.org.cac.integrador.dao.DeParaReembolsoDAO;
import br.org.cac.integrador.dao.ErroProcessamentoReembolsoDAO;
import br.org.cac.integrador.dao.ItemReembolsoDAO;
import br.org.cac.integrador.dao.ReembolsoDAO;
import br.org.cac.integrador.dao.ReembolsoTrabalhoDAO;
import br.org.cac.integrador.dao.SubProcessoIdDAO;
import br.org.cac.integrador.exception.ProcessadorException;
import br.org.cac.integrador.modelo.ItemReembolso;
import br.org.cac.integrador.modelo.Reembolso;
import br.org.cac.integrador.modelo.RetornoMensagem;
import br.org.cac.integrador.modelo.SubProcessoId;
import br.org.cac.integrador.modelo.infraestrutura.DeParaReembolso;
import br.org.cac.integrador.modelo.infraestrutura.ErroProcessamentoReembolso;
import br.org.cac.integrador.modelo.infraestrutura.Processamento;
import br.org.cac.integrador.modelo.infraestrutura.ReembolsoTrabalho;
import br.org.cac.integrador.modelo.infraestrutura.StatusProcessamento;
import br.org.cac.integrador.util.ThrowableUtil;

/**
 * Processador responsável pelo processamento de reembolsos.
 * 
 * @author JCJ
 * @version 1.2
 * @since 1.1, 2017-04-01
 */
public class ProcessadorReembolsos extends AbstractProcessador {
	
	@Inject
	private ReembolsoTrabalhoDAO rtDao;
	
	@Inject
	private DeParaReembolsoDAO dprDao;	
	
	@Inject
	private ErroProcessamentoReembolsoDAO eprDao;
	
	@Inject
	private ReembolsoDAO rDao;
	
	@Inject
	private ItemReembolsoDAO irDao;
	
	@Inject
	private SubProcessoIdDAO spiDao;
	
	@Inject
	private ReembolsoWebServices webServiceReembolso;
	
	@Inject
	private Logger logger;
	
	private Set<SubProcessoId> coparticipacoesCalculadas;
	
	public ProcessadorReembolsos(){
		super();
		coparticipacoesCalculadas = new HashSet<>();
	}
	
	@Override
	public ResumoProcessamento processa(Processamento processamento) throws ProcessadorException {
		/*
		 * Passos para o processamento:
		 * 1- a partir do processamento, listar todos os ReembolsoTrabalho que ele deve ter
		 * 2- Cada ReembolsoTrabalho irá gerar um Reembolso:
		 *   2.1- Através de cada ReembolsoTrabalho, procurar o atendimento correspondente e 
		 *        carregar Reembolso (usando SPR_ICM_LISTA_REEMBOLSOS);
		 *        !! O campo cdFornecedor deve ser buscado através de uma consulta ao cdMatricula do titular
		 *   2.2- Através também do atendimento correspondente, carregar os ItemReembolso
		 *        (usando SPR_ICM_LISTA_ITENS_REEMBOLSO);
		 *   2.3- Juntar ambos e retornar;
		 * 3- Tentar enviar o Reembolso à MV, usando o webservice.
		 * 4- Caso consiga, finalizar o Reembolso e gravar em MV_CAC_DE_PARA_REEMBOLSO/ITEM_REEMBOLSO
		 * 5- Caso não consiga, gravar em ERRO_PROCESSAMENTO_REEMBOLSO;
		 * 6- Repetir para todos os ReembolsoTrabalho deste processamento
		 */
				
		if (processamento == null) {
			throw new IllegalArgumentException("O parâmetro processamento não pode ser null.");
		}
		
		ResumoProcessamento resumoProcessamento = new ResumoProcessamento(processamento);
		
		logger.infof("Iniciando processamento. Id: %d", 
					processamento.getIdProcessamento()
				);		
		
		// Passo 1 - A partir do processamento, listar todos os ReembolsoTrabalho que ele possui
		
		List<ReembolsoTrabalho> reembolsosTrabalho = rtDao.findByProcessamento(processamento);
		Reembolso reembolso = null;
		
		Response response = null;		
		
		logger.infof("%d Reembolso%sTrabalho para processamento",
						reembolsosTrabalho.size(),
						(reembolsosTrabalho.size() == 1 ? "" : "s")
				);		
		resumoProcessamento.setTotalItens(reembolsosTrabalho.size());
		
		
		for (ReembolsoTrabalho reembolsoTrabalho : reembolsosTrabalho){
			logger.infof("Iniciando processamento do ReembolsoTrabalho Id: %d.",
						reembolsoTrabalho.getIdReembolsoTrabalho()
					);
			
			reembolsoTrabalho.setStatusProcessamento(getStatusProcessamento(StatusProcessamento.EM_PROCESSAMENTO));
			rtDao.merge(reembolsoTrabalho);
			
			try {
				reembolso = this.processaReembolsoTrabalho(reembolsoTrabalho, true);
			} catch (ProcessadorException e) {
				gerarErroProcessamentoReembolsoTrabalho(reembolsoTrabalho, "Erro de processamento no "
						+ "Reembolso de Trabalho: " + reembolsoTrabalho.getIdReembolsoTrabalho() + ": " + e.getMessage());
				resumoProcessamento.incItensComErro();
				continue;
			}
				
			if (reembolso == null){
				gerarErroProcessamentoReembolsoTrabalho(reembolsoTrabalho, 
						String.format("Não foi possível localizar reembolsos para o atendimento "
							+ "informado. Atendimento %s", 
							reembolsoTrabalho.getAtendimentoId().toString()
						));
				resumoProcessamento.incItensComErro();
				continue;
			}
			

			try {
				// Enviando o JSON do objeto gerado para a MV, via middleware
				response = webServiceReembolso.incluir(this.getMapper().writeValueAsString(reembolso));
				
				String retorno = (String)response.getEntity();
				RetornoMensagem retornoMensagem = this.getMapper().readValue(retorno, RetornoMensagem.class);
				
				if ( (response.getStatus() != 200) || (!retornoMensagem.getStatus().equals("200")) ){
					gerarErroProcessamentoReembolsoTrabalho(reembolsoTrabalho, retornoMensagem);
					resumoProcessamento.incItensComErro();
					continue;						
				} 
				
				DeParaReembolso deParaReembolso = new DeParaReembolso();
				
				deParaReembolso.setProcessamento(processamento);
				deParaReembolso.setAtendimentoId(reembolso.getAtendimentoId());
				deParaReembolso.setCdReembolsoInclusao(reembolso.getCdReembolso()) ;
				deParaReembolso.setCdReembolsoEfetivo(Integer.valueOf(retornoMensagem.getEntidadeId()) );
				
				dprDao.persist(deParaReembolso);
				
				reembolsoTrabalho.setStatusProcessamento(getStatusProcessamento(StatusProcessamento.PROCESSADO_TOTALMENTE));
				rtDao.merge(reembolsoTrabalho);
				
				logger.infof("Reembolso do atendimento %s enviado para a MV com sucesso. Id do envio: %d", 
						reembolso.getAtendimentoId(),
						deParaReembolso.getIdDeParaReembolso()
						);
				resumoProcessamento.incItensEnviados();
				
			} catch (JsonProcessingException e) {
				gerarErroProcessamentoReembolsoTrabalho(reembolsoTrabalho, String.format("Erro ao converter Reembolso para JSON, "
						+ "para envio ao SOUL. ReembolsoTrabalho Id: %d",
						reembolsoTrabalho.getIdReembolsoTrabalho() )
						);
				resumoProcessamento.incItensComErro();
				continue;
			} catch (IOException e) {
				gerarErroProcessamentoReembolsoTrabalho(reembolsoTrabalho, String.format(
						"Erro ao ler o retorno do Middleware para "
						+ "JSON. Não é possível dizer se o reembolso foi incluído ou não. Reembolso Id: %d", 
						reembolsoTrabalho.getIdReembolsoTrabalho())
						);
				resumoProcessamento.incItensComErro();
				continue;
			}
			
		}
				
		return resumoProcessamento;
	}
	
	public Reembolso processaReembolsoTrabalho(ReembolsoTrabalho reembolsoTrabalho) throws ProcessadorException{
		return this.processaReembolsoTrabalho(reembolsoTrabalho, false);
	}
	
	private Reembolso processaReembolsoTrabalho(ReembolsoTrabalho reembolsoTrabalho, boolean calculaCoparticipacao) 
			throws ProcessadorException {
		Reembolso reembolso = null;
		
		if (calculaCoparticipacao){
			SubProcessoId subProcessoId = SubProcessoId.fromAtendimentoId(reembolsoTrabalho.getAtendimentoId());
			
			if (!coparticipacoesCalculadas.contains(subProcessoId)){
				logger.infof("Iniciando cálculo de coparticipação para o subprocesso %s.", subProcessoId);
				try {
					spiDao.calculaCoparticipacaoSubProcesso(subProcessoId);
				} catch (PersistenceException e) {
					logger.errorf("Ocorreu um erro no cálculo de coparticipação do sub-processo %s: %s", subProcessoId,
							ThrowableUtil.getRootCause(e));

					throw new ProcessadorException(
							String.format("Ocorreu um erro no cálculo de coparticipação do sub-processo %s: %s", 
									subProcessoId, 
									ThrowableUtil.getRootMessage(e)),
							ThrowableUtil.getRootCause(e));
				}
				logger.infof("Finalizado cálculo de coparticipação do subprocesso %s. Continuando processamento...",
						subProcessoId);
				
				coparticipacoesCalculadas.add(subProcessoId);
			}
			
		}
						
		List<Reembolso> reembolsos = rDao.findByReembolsoTrabalho(reembolsoTrabalho);
		
		if (reembolsos == null) {
			throw new ProcessadorException(String.format("Não foi encontrado nenhum reembolso com os dados informados. Atendimento: %s",
						reembolsoTrabalho.getAtendimentoId().toString()
					));
		}
		
		if (reembolsos.size() > 1){
			throw new ProcessadorException(String.format("Foi encontrado mais que um Reembolso para um mesmo ReembolsoTrabalho. "
						+ "Id do ReembolsoTrabalho: %d; Atendimento %s", 
						reembolsoTrabalho.getIdReembolsoTrabalho(),
						reembolsoTrabalho.getAtendimentoId().toString()
					));
		}
		
		reembolso = reembolsos.get(0);
		reembolso.setCdReembolso(reembolsoTrabalho.getIdReembolsoTrabalho());
		
		if (reembolso.getCdMatricula() == null) {
			throw new ProcessadorException(String.format("O beneficiário do reembolso do atendimento %s "
					+ "não possui código de matrícula cadastrado na MV. ReembolsoTrabalho Id: %d", 
					reembolsoTrabalho.getAtendimentoId().toString(),
					reembolsoTrabalho.getIdReembolsoTrabalho()						
				));
		}
		
		if (reembolso.getCdMatriculaTitular() == null){
			throw new ProcessadorException(String.format("O titular do reembolso do atendimento %s "
						+ "não possui código de matrícula cadastrado na MV. ReembolsoTrabalho Id: %d", 
						reembolsoTrabalho.getAtendimentoId().toString(),
						reembolsoTrabalho.getIdReembolsoTrabalho()						
					));
		}
		
		// Utilizando reembolso.cdMatriculaTitular, acessar o ws de Beneficiário e tentar recuperar cdFornecedor
		Beneficiario beneficiario = buscaBeneficiario(reembolso.getCdMatriculaTitular());
		
		if (beneficiario.getCdFornecedor() == null){
			throw new ProcessadorException(String.format("O beneficiario titular do reembolso %s não possui "
					+ "código de fornecedor cadastrado na MV. Código de matrícula do beneficiário: %d",
					reembolso.getAtendimentoId().toString(),
					reembolso.getCdMatriculaTitular()
					));
		}
		
		reembolso.setCdFornecedor(beneficiario.getCdFornecedor());
		
		
		List<ItemReembolso> itensReembolsoTrabalho = irDao.findByReembolsoTrabalho(reembolsoTrabalho, reembolso.getCdReembolso());
		
		if ((itensReembolsoTrabalho == null) || (itensReembolsoTrabalho.isEmpty()))  {
			throw new ProcessadorException(String.format("Não foi encontrado nenhum item de reembolso com os dados informados. Atendimento: %s",
						reembolsoTrabalho.getAtendimentoId().toString()
					));
		}
		
		// Como existem problemas com alguns dados da tabela PROCEDIMENTO que podem ser nulos quando não deveriam,
		// é preciso verificar se existe algum desses casos. Se sim, o processamento deste reembolsoTrabalo deve
		// ser interrompido.
		
		final String NAO_PODE_ENVIAR_REEMBOLSO_SOUL = "não pode ser enviado ao SOUL. ReembolsoTrabalho Id: %d.";
		
		List<ItemReembolso> verificacao;
		// Valor apresentado null:
		verificacao = itensReembolsoTrabalho.stream()
				.filter(ir -> null == ir.getVlCobrado())
				.collect(Collectors.toList());
		if (!verificacao.isEmpty()){
			throw new ProcessadorException("O procedimento %s possui valor apresentado nulo e, por isso, "
					+ NAO_PODE_ENVIAR_REEMBOLSO_SOUL,
					verificacao.get(0).getProcedimentoId().toString(),
					reembolsoTrabalho.getIdReembolsoTrabalho());
		}		
		
		// Código do procedimento null
		verificacao = itensReembolsoTrabalho.stream()
				.filter(ir -> null == ir.getCdProcedimento())
				.collect(Collectors.toList());
		if (!verificacao.isEmpty()){
			throw new ProcessadorException("O procedimento %s possui código de procedimento nulo e, por isso, "
					+ NAO_PODE_ENVIAR_REEMBOLSO_SOUL,
					verificacao.get(0).getProcedimentoId().toString(),
					reembolsoTrabalho.getIdReembolsoTrabalho());
		}		
		
		// Com tudo certo, o processamento segue.
		reembolso.setItensReembolso(itensReembolsoTrabalho);
		reembolso.setVlTotalOriginal(itensReembolsoTrabalho.stream()
				.collect( Collectors.summingDouble(i -> i.getVlCobrado()) ) );
		
		if ( Double.valueOf(0.0).equals(reembolso.getVlTotalOriginal()) ){
			throw new ProcessadorException("O reembolso %s possui valor total apresentado igual a zero e, por isso, "
					+ NAO_PODE_ENVIAR_REEMBOLSO_SOUL,
					reembolso.getAtendimentoId().toString(),
					reembolsoTrabalho.getIdReembolsoTrabalho()
					);
		}
		


		return reembolso;				
	}
	
	private void gerarErroProcessamentoReembolsoTrabalho(ReembolsoTrabalho reembolsoTrabalho, 
			RetornoMensagem retornoMensagem) {
		// Tenta buscar a lista de erros proveniente de retornoMensagem. Se for null, utiliza uma lista vazia.
		gerarErroProcessamentoReembolsoTrabalho(reembolsoTrabalho,
					Optional.ofNullable(retornoMensagem.getMensagem())
					.orElse(Collections.emptyList())
					.stream()
					.map(String::new)
					.toArray(String[]::new)
				);
	}

	// XXX: Marcado para possível refactoring, devido à similaridade dessa estrutura com a de outros processadores.
	private void gerarErroProcessamentoReembolsoTrabalho(ReembolsoTrabalho reembolsoTrabalho, 
			String... mensagens) {
		ErroProcessamentoReembolso erroProcessamento = null;	
		
		if (mensagens.length == 0){
			erroProcessamento = new ErroProcessamentoReembolso();
			erroProcessamento.setReembolsoTrabalho(reembolsoTrabalho);
			erroProcessamento.setMensagem("Erro genérico do SOUL MV. Nenhuma mensagem de erro foi retornada");
			eprDao.persist(erroProcessamento);	
			
		} else {		
			for (String mensagem : mensagens){
				erroProcessamento = new ErroProcessamentoReembolso();
				erroProcessamento.setReembolsoTrabalho(reembolsoTrabalho);
				erroProcessamento.setMensagem(mensagem.substring(0,  (mensagem.length() > 800 ? 800 : mensagem.length()) ) );
				
				eprDao.persist(erroProcessamento);
			}
		}
		
		reembolsoTrabalho.setStatusProcessamento(getStatusProcessamento(StatusProcessamento.PROCESSADO_COM_ERROS));
		rtDao.merge(reembolsoTrabalho);
		
		logger.warnf("Ocorreram erros no processamento de ReembolsoTrabalho Id: %s. "
						+ "Não foi possível enviá-lo à MV. ",
					reembolsoTrabalho.getIdReembolsoTrabalho()
				);
	}	

}
