package br.org.cac.integrador.dao;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.StoredProcedureQuery;

import br.org.cac.integrador.exception.ProcessadorException;
import br.org.cac.integrador.modelo.ContaMedica;
import br.org.cac.integrador.modelo.LoteContaMedica;

public class ContaMedicaDAO {

	@Inject
	private EntityManager manager;

	public ContaMedicaDAO() {

	}

	/**
	 * @param subProcesso
	 * @param lote
	 * @return
	 * @throws ProcessadorException
	 */
	@SuppressWarnings("unchecked")
	public List<ContaMedica> findContasMedicasByLoteContaMedica(LoteContaMedica lote) throws ProcessadorException {
		StoredProcedureQuery listaContasMedicasProcedure = manager
				.createNamedStoredProcedureQuery("listaContasMedicas");
		listaContasMedicasProcedure.setParameter("ano_apresentacao", lote.getSubProcesso().getAnoApresentacao())
				.setParameter("id_representacao", lote.getSubProcesso().getIdRepresentacao())
				.setParameter("id_processo", lote.getSubProcesso().getIdProcesso())
				.setParameter("d_sub_processo", lote.getSubProcesso().getdSubProcesso())
				.setParameter("d_natureza", lote.getSubProcesso().getdNatureza())
				.setParameter("id_sequencial_natureza", lote.getSubProcesso().getIdSequencialNatureza())
				.setParameter("cd_lote", lote.getCdLote());

		List<ContaMedica> contasMedicas = null;

		try {
			contasMedicas = listaContasMedicasProcedure.getResultList();
		} catch (PersistenceException e) {
			throw new ProcessadorException(e);
		}

		if ((contasMedicas == null) || (contasMedicas.isEmpty())) {
			throw new ProcessadorException(
					"Não foi encontrado nenhum atendimento com os dados informados. Subprocesso: "
							+ lote.getSubProcesso());
		}
		return contasMedicas;
	}
}
