package br.org.cac.integrador.dao;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.StoredProcedureQuery;

import br.org.cac.integrador.exception.ProcessadorException;
import br.org.cac.integrador.modelo.LoteContaMedica;
import br.org.cac.integrador.modelo.SubProcessoId;

public class LoteContaMedicaDAO {
	@Inject
	private EntityManager manager;

	public LoteContaMedicaDAO() {

	}

	/**
	 * @param subProcessoId
	 * @return
	 * @throws ProcessadorException
	 */
	@SuppressWarnings("unchecked")
	public List<LoteContaMedica> findLotesContaMedicaBySubProcessoId(SubProcessoId subProcessoId)
			throws ProcessadorException {		
		StoredProcedureQuery listaLotesProcedure = manager.createNamedStoredProcedureQuery("listaLotes");
		listaLotesProcedure
				.setParameter("ano_apresentacao", subProcessoId.getAnoApresentacao())
				.setParameter("id_representacao", subProcessoId.getIdRepresentacao())
				.setParameter("id_processo", subProcessoId.getIdProcesso())
				.setParameter("d_sub_processo", subProcessoId.getdSubProcesso())
				.setParameter("d_natureza", subProcessoId.getdNatureza())
				.setParameter("id_sequencial_natureza", subProcessoId.getIdSequencialNatureza());
		
		List<LoteContaMedica> lotesContaMedica = null;
		
		try {
			lotesContaMedica = listaLotesProcedure.getResultList();
		} catch (PersistenceException e) {
			throw new ProcessadorException(e);
		}
		return lotesContaMedica;
	}
}
