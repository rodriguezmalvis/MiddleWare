package br.org.cac.integrador.dao;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.StoredProcedureQuery;

import br.org.cac.integrador.exception.ProcessadorException;
import br.org.cac.integrador.modelo.ProjecaoMensalidadeDevolucao;
import br.org.cac.integrador.modelo.infraestrutura.GrupoItemMensalidadeTrabalho;
import br.org.cac.integrador.modelo.infraestrutura.MensalidadeTrabalho;

public class ProjecaoMensalidadeDevolucaoDAO {

	@Inject
	private EntityManager manager;

	public ProjecaoMensalidadeDevolucaoDAO() {

	}

	/**
	 * Use {@link #findByGrupoItemMensalidadeTrabalho}.
	 * 
	 * @param mensalidadeTrabalho
	 * @return
	 * @throws ProcessadorException
	 */
	@Deprecated
	public List<ProjecaoMensalidadeDevolucao> findByMensalidadeTrabalho(MensalidadeTrabalho mensalidadeTrabalho)
			throws ProcessadorException {
		StoredProcedureQuery listaMensalidadesDevolucaoProcedure = manager
				.createNamedStoredProcedureQuery("listaMensalidadesDevolucao")
				.setParameter("id_mensalidade_trabalho", mensalidadeTrabalho.getIdMensalidadeTrabalho());

		try {
			@SuppressWarnings("unchecked")
			List<ProjecaoMensalidadeDevolucao> projecoesMensalidadeDevolucao = listaMensalidadesDevolucaoProcedure
					.getResultList();

			return projecoesMensalidadeDevolucao;

		} catch (PersistenceException e) {
			throw new ProcessadorException(e);
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<ProjecaoMensalidadeDevolucao> findByGrupoItemMensalidadeTrabalho(
			GrupoItemMensalidadeTrabalho grupoItemMensalidadeTrabalho) throws PersistenceException {
		StoredProcedureQuery listaMensalidadesContratoProcedure = manager.createNamedStoredProcedureQuery("listaMensalidadesDevolucao")
				.setParameter("id_grupo_item_mensalidade_trabalho", grupoItemMensalidadeTrabalho.getIdGrupoItemMensalidadeTrabalho());

		return listaMensalidadesContratoProcedure.getResultList();
	}
}
