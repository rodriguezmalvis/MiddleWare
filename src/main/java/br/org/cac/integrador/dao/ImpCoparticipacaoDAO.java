package br.org.cac.integrador.dao;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.StoredProcedureQuery;

import br.org.cac.integrador.exception.ProcessadorException;
import br.org.cac.integrador.modelo.ImpCoparticipacao;
import br.org.cac.integrador.util.ThrowableUtil;

public class ImpCoparticipacaoDAO {

	@Inject
	private EntityManager manager;
	
	public ImpCoparticipacaoDAO() {
	
	}
	

	/**
	 * @param idComando
	 * @return
	 * @throws ProcessadorException
	 */
	public List<ImpCoparticipacao> findByIdComando(Integer idComando) throws ProcessadorException {
		StoredProcedureQuery listaImpCoparticipacaoProcedure = manager.createNamedStoredProcedureQuery("listaImpCoparticipacao");
		listaImpCoparticipacaoProcedure.setParameter("id_comando", idComando);		
		
		try {
			@SuppressWarnings("unchecked")
			List<ImpCoparticipacao> impCoparticipacaoComando = listaImpCoparticipacaoProcedure.getResultList();
			
			return impCoparticipacaoComando;
			
		} catch (PersistenceException e){
			throw new ProcessadorException(ThrowableUtil.getRootCause(e));
		}
	}
}
