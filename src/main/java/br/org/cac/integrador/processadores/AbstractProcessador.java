package br.org.cac.integrador.processadores;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.ws.rs.core.Response;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.org.cac.cacmvmiddleware.modelo.retorno.Beneficiario;
import br.org.cac.cacmvmiddleware.modelo.retorno.RetornoBeneficiario;
import br.org.cac.cacmvmiddleware.services.BeneficiarioWebServices;
import br.org.cac.integrador.dao.StatusProcessamentoDAO;
import br.org.cac.integrador.exception.ProcessadorException;
import br.org.cac.integrador.jacksonutil.ObjectMapperFactory;
import br.org.cac.integrador.modelo.Periodo;
import br.org.cac.integrador.modelo.infraestrutura.Processamento;
import br.org.cac.integrador.modelo.infraestrutura.StatusProcessamento;
import br.org.cac.integrador.modelo.infraestrutura.TipoProcessamento;

/**
 * Classe abstrata que representa o esqueleto base de um processador;
 * 
 * @author JCJ
 * @version 1.1
 * @since 1.1, 2017-03-28
 *
 */
public abstract class AbstractProcessador {
	
	private ObjectMapper mapper;
	
	@Inject
	private StatusProcessamentoDAO spDao;
	
	@Inject
	private BeneficiarioWebServices webServiceBeneficiario;	
	
	private Map<String, List<StatusProcessamento>> todosStatusProcessamento;
	
	protected AbstractProcessador(){
		this.mapper = ObjectMapperFactory.getDefaultConfiguredObjectMapper(false);
	}
	
	/** Realiza um processamento, gerando todos os dados necessários, inclusive
	 *  a gravação nos serviços da MV.
	 * 
	 * @param processamento O {@link Processamento} a ser processado.
	 * @return Uma {@code String} que representa o JSON da última operação realizada
	 * pelo processador. 
	 * @throws ProcessadorException Caso ocorra algum erro no processamento que não puder
	 * ser tratado.
	 */
	public abstract ResumoProcessamento processa(Processamento processamento) throws ProcessadorException;
	
	
	public Processamento inicializaProcessamento(Date dthrInicioProcessamento, TipoProcessamento tipoProcessamento){
		return this.inicializaProcessamento(dthrInicioProcessamento, tipoProcessamento, null);
	}

	public Processamento inicializaProcessamento(Date dthrInicioProcessamento, TipoProcessamento tipoProcessamento,
			Periodo periodoReferencia){
		Processamento processamento = new Processamento();
		processamento.setDthrInicioProcessamento(dthrInicioProcessamento);
		processamento.setTipoProcessamento(tipoProcessamento);
		if (periodoReferencia != null){
			processamento.setDthrReferenciaInicial(periodoReferencia.getDtInicial());
			processamento.setDthrReferenciaFinal(periodoReferencia.getDtFinal());
		}
		
		return processamento;		
	}	
	
	protected StatusProcessamento getStatusProcessamento(String codStatus){
		if (todosStatusProcessamento == null){
			initTodosStatusProcessamento();
		}
		
		List<StatusProcessamento> candidatos = todosStatusProcessamento.get(codStatus);
		
		// TODO: Melhorar a implementação para ser um Map<String, StatusProcessamento>, usando Collectors.toMap()
		if ((candidatos != null) && (!candidatos.isEmpty())){
			return todosStatusProcessamento.get(codStatus).get(0);	
		}
		
		return null;
		
	}
	
	protected void initTodosStatusProcessamento(){
		
		List<StatusProcessamento> statusProcessamento = spDao.findAll();
		
		todosStatusProcessamento = statusProcessamento.stream()
				.collect(Collectors.groupingBy(c -> c.getCodStatus()));
	
	}
	
	protected Beneficiario buscaBeneficiario(int codMatricula) throws ProcessadorException{
		Beneficiario beneficiario = null;	
		
		Response responseBeneficiario = webServiceBeneficiario.getBeneficiario(codMatricula);
		RetornoBeneficiario retBeneficiario = null;
		
		try {
			retBeneficiario = mapper.readValue(responseBeneficiario.getEntity().toString(), RetornoBeneficiario.class);
		} catch (IOException e) {
			throw new ProcessadorException(String.format("Erro na leitura da resposta do middleware ao buscar o beneficiário "
						+ "%d. Mensagem de erro: %s.",
						codMatricula,
						e.getMessage()
					));
		}
		
		
		if (retBeneficiario.getStatus().equalsIgnoreCase("200")) {

			if (retBeneficiario.getBeneficiario() != null){				
				beneficiario = retBeneficiario.getBeneficiario();					
			} else {
				throw new ProcessadorException(String.format("Erro ao buscar o beneficiário com codMatricula %d. "
							+ "Talvez o beneficiário não exista?",
							codMatricula
						));
			}
			
        } else {
        	throw new ProcessadorException(String.format("Erro ao buscar o beneficiário com codMatricula %d. "
        			+ "Mensagem de erro do middleware: %s",
        			codMatricula,
        			responseBeneficiario.getEntity().toString()
        			));        	        	
        }
		
		return beneficiario;
	}

	/** Getter para mapper.
	 * @return o valor de mapper.
	 */
	protected ObjectMapper getMapper() {
		return mapper;
	}	
}
