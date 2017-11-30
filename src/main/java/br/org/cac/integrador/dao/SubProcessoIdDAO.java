package br.org.cac.integrador.dao;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.StoredProcedureQuery;
import javax.transaction.Transactional;

import br.org.cac.integrador.modelo.SubProcessoId;

@Transactional
public class SubProcessoIdDAO {
	
	@Inject
	private EntityManager manager;
	
	public SubProcessoIdDAO() {
	
	}
	
	public void calculaCoparticipacaoSubProcesso(SubProcessoId subProcessoId) throws PersistenceException{
		StoredProcedureQuery calculaCoparticipacaoProcedimento = manager
				.createNamedStoredProcedureQuery("calculaCoparticipacaoProcedimento")
				.setParameter("ano_apresentacao", subProcessoId.getAnoApresentacao())
				.setParameter("id_representacao", subProcessoId.getIdRepresentacao())
				.setParameter("id_processo", subProcessoId.getIdProcesso())
				.setParameter("d_sub_processo", subProcessoId.getdSubProcesso())
				.setParameter("d_natureza", subProcessoId.getdNatureza())
				.setParameter("id_sequencial_natureza", subProcessoId.getIdSequencialNatureza());
				
		calculaCoparticipacaoProcedimento.execute();
		
	}
}
