/**
 * 
 */
package br.org.cac.integrador.dao;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.StoredProcedureQuery;

import br.org.cac.integrador.modelo.Periodo;
import br.org.cac.integrador.modelo.PeriodoDataCredito;
import br.org.cac.integrador.modelo.SubProcesso;

/**
 * @author JCJ
 * @since 2017-09-26
 * 
 */
public class SubProcessoDAO {
	@Inject
	private EntityManager manager;
	
	public SubProcessoDAO() {
		// Construtor padrão
	}
	
	@SuppressWarnings("unchecked")
	public List<SubProcesso> getSubProcessos(Periodo periodoHomologacao){
		StoredProcedureQuery listaSubProcessoProcedure = manager.createNamedStoredProcedureQuery("listaSubProcessoPorHomologacao")
				.setParameter("dt_homologacao_inicial", periodoHomologacao.getDtInicial())
				.setParameter("dt_homologacao_final", periodoHomologacao.getDtFinal());

		return listaSubProcessoProcedure.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<SubProcesso> getSubProcessos(PeriodoDataCredito periodoDataCredito){
		StoredProcedureQuery listaSubProcessoProcedure = manager.createNamedStoredProcedureQuery("listaSubProcessoPorDataCredito")
				.setParameter("dt_credito_inicial", periodoDataCredito.getDtInicial())
				.setParameter("dt_credito_final", periodoDataCredito.getDtFinal());

		return listaSubProcessoProcedure.getResultList();		
	}
	
	@SuppressWarnings("unchecked")
	public List<SubProcesso> getSubProcessosDiretos(Periodo periodoHomologacao){
		StoredProcedureQuery listaSubProcessoProcedure = manager.createNamedStoredProcedureQuery("listaSubProcessoDiretoPorHomologacao")
				.setParameter("dt_homologacao_inicial", periodoHomologacao.getDtInicial())
				.setParameter("dt_homologacao_final", periodoHomologacao.getDtFinal());

		return listaSubProcessoProcedure.getResultList();
	}	
}
