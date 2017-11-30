package br.org.cac.integrador.dao;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.StoredProcedureQuery;

import br.org.cac.integrador.exception.ProcessadorException;
import br.org.cac.integrador.modelo.ContaMedica;
import br.org.cac.integrador.modelo.ItemContaMedica;

public class ItemContaMedicaDAO {

	@Inject
	private EntityManager manager;

	public ItemContaMedicaDAO() {

	}

	/**
	 * @param contaMedica
	 * @return
	 * @throws ProcessadorException
	 */
	@SuppressWarnings("unchecked")
	public List<ItemContaMedica> findItensContaMedicaByContaMedica(ContaMedica contaMedica)
			throws ProcessadorException {
		StoredProcedureQuery listaItensContaMedicaProcedure = manager
				.createNamedStoredProcedureQuery("listaItensContaMedica");

		listaItensContaMedicaProcedure
				.setParameter("ano_apresentacao", contaMedica.getAtendimento().getAnoApresentacao())
				.setParameter("id_representacao", contaMedica.getAtendimento().getIdRepresentacao())
				.setParameter("id_processo", contaMedica.getAtendimento().getIdProcesso())
				.setParameter("d_sub_processo", contaMedica.getAtendimento().getdSubProcesso())
				.setParameter("d_natureza", contaMedica.getAtendimento().getdNatureza())
				.setParameter("id_sequencial_natureza", contaMedica.getAtendimento().getIdSequencialNatureza())
				.setParameter("id_atendimento", contaMedica.getAtendimento().getIdAtendimento())
				.setParameter("cd_conta_medica", contaMedica.getCdContaMedica());

		List<ItemContaMedica> itensContaMedica = null;

		try {
			itensContaMedica = listaItensContaMedicaProcedure.getResultList();
		} catch (PersistenceException e) {
			throw new ProcessadorException(e);
		} catch (Exception e){
			throw new ProcessadorException(e);
		}
		return itensContaMedica;
	}
}
