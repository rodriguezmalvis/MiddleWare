package br.org.cac.integrador.dao;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.StoredProcedureQuery;

import br.org.cac.integrador.exception.ProcessadorException;
import br.org.cac.integrador.modelo.ProjecaoMensalidadeContrato;
import br.org.cac.integrador.modelo.infraestrutura.GrupoItemMensalidadeTrabalho;
import br.org.cac.integrador.modelo.infraestrutura.MensalidadeTrabalho;

public class ProjecaoMensalidadeContratoDAO {
	@Inject
	private EntityManager manager;
	
	public ProjecaoMensalidadeContratoDAO() {
	
	}
	
	/**Use 
	 * 
	 * @param mensalidadeTrabalho
	 * @return
	 * @throws ProcessadorException
	 */
	@SuppressWarnings("unchecked")
	@Deprecated
	public List<ProjecaoMensalidadeContrato> findByMensalidadeTrabalho(MensalidadeTrabalho mensalidadeTrabalho)
			throws ProcessadorException {
		StoredProcedureQuery listaMensalidadesContratoProcedure = manager.createNamedStoredProcedureQuery("listaMensalidadesContrato")
				.setParameter("id_mensalidade_trabalho", mensalidadeTrabalho.getIdMensalidadeTrabalho());
		
		List<ProjecaoMensalidadeContrato> projecoesMensalidadeContrato = null;
		
		try {
			projecoesMensalidadeContrato = listaMensalidadesContratoProcedure.getResultList();
		} catch (PersistenceException e){
			throw new ProcessadorException(e);
		}

		return projecoesMensalidadeContrato;
	}	
	
	@SuppressWarnings("unchecked")
	public List<ProjecaoMensalidadeContrato> findByGrupoItemMensalidadeTrabalho(
			GrupoItemMensalidadeTrabalho grupoItemMensalidadeTrabalho) throws PersistenceException {
		StoredProcedureQuery listaMensalidadesContratoProcedure = manager.createNamedStoredProcedureQuery("listaMensalidadesContrato")
				.setParameter("id_grupo_item_mensalidade_trabalho", grupoItemMensalidadeTrabalho.getIdGrupoItemMensalidadeTrabalho());

		return listaMensalidadesContratoProcedure.getResultList();
	}
	

}
