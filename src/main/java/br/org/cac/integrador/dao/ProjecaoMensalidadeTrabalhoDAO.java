/**
 * 
 */
package br.org.cac.integrador.dao;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.StoredProcedureQuery;

import br.org.cac.integrador.exception.ProcessadorException;
import br.org.cac.integrador.modelo.ProjecaoMensalidadeTrabalho;

/**
 * @author JCJ
 * @since 2017-10-02
 * 
 */
public class ProjecaoMensalidadeTrabalhoDAO {

	@Inject
	private EntityManager manager;
	
	public ProjecaoMensalidadeTrabalhoDAO() {
		// Construtor padrão
	}
	
	@SuppressWarnings("unchecked")
	public List<ProjecaoMensalidadeTrabalho> listaProjecoesMensalidadeTrabalho(Date dtReferenciaAtual, Date dtInicial, Date dtFinal) 
			throws ProcessadorException{
		List<ProjecaoMensalidadeTrabalho> retorno = null;

		try {		
			StoredProcedureQuery procedure = manager
					.createNamedStoredProcedureQuery("listaProjecoesMensalidadesTrabalho")
					.setParameter("dt_referencia_atual", dtReferenciaAtual)
					.setParameter("dt_geracao_inicial", dtInicial)
					.setParameter("dt_geracao_final", dtFinal);
			retorno = procedure.getResultList();
		} catch(PersistenceException e) {
			throw new ProcessadorException(e);
		}
		
		return retorno;
	}	
	
	@SuppressWarnings("unchecked")
	public List<ProjecaoMensalidadeTrabalho> listaProjecoesMensalidadeTrabalhoPorDiops(Date dtReferenciaDiops, Date dtVencimentoLimite) 
			throws ProcessadorException{
		try {		
			StoredProcedureQuery procedure = manager
					.createNamedStoredProcedureQuery("listaProjecoesMensalidadesTrabalhoPorDiops")
					.setParameter("dt_referencia_diops", dtReferenciaDiops)
					.setParameter("dt_vencimento_limite", dtVencimentoLimite);
			return procedure.getResultList();
		} catch(PersistenceException e) {
			throw new ProcessadorException(e);
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<ProjecaoMensalidadeTrabalho> listaProjecoesMensalidadeTrabalhoBoleto(Date dtReferencia, Date dtVencimentoBase,
			Date dtVencimentoLimite) throws ProcessadorException {
		List<ProjecaoMensalidadeTrabalho> retorno = null;

		try {		
			StoredProcedureQuery procedure = manager
					.createNamedStoredProcedureQuery("listaProjecoesMensalidadesTrabalhoBoleto")
					.setParameter("dt_referencia", dtReferencia)
					.setParameter("dt_vencimento_base", dtVencimentoBase)
					.setParameter("dt_vencimento_limite", dtVencimentoLimite);
			retorno = procedure.getResultList();
		} catch(PersistenceException e) {
			throw new ProcessadorException(e);
		}
		
		return retorno;
	}
	
	@SuppressWarnings("unchecked")
	public List<ProjecaoMensalidadeTrabalho> listaProjecoesMensalidadeTrabalhoFolha(Date dtReferencia) 
			throws ProcessadorException {
		List<ProjecaoMensalidadeTrabalho> retorno = null;

		try {		
			StoredProcedureQuery procedure = manager
					.createNamedStoredProcedureQuery("listaProjecoesMensalidadesTrabalhoFolha")
					.setParameter("dt_referencia", dtReferencia);
			retorno = procedure.getResultList();
		} catch(PersistenceException e) {
			throw new ProcessadorException(e);
		}
		
		return retorno;
	}
}
