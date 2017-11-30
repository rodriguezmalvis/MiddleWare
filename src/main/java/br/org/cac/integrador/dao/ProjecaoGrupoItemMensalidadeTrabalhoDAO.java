package br.org.cac.integrador.dao;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.StoredProcedureQuery;

import br.org.cac.integrador.exception.ProcessadorException;
import br.org.cac.integrador.modelo.infraestrutura.MensalidadeTrabalho;
import br.org.cac.integrador.modelo.infraestrutura.ProjecaoGrupoItemMensalidadeTrabalho;

public class ProjecaoGrupoItemMensalidadeTrabalhoDAO {
	
	@Inject
	private EntityManager manager;
	
	public ProjecaoGrupoItemMensalidadeTrabalhoDAO() {
	
	}
	
	@SuppressWarnings("unchecked")
	public List<ProjecaoGrupoItemMensalidadeTrabalho> findByMensalidadeTrabalho(MensalidadeTrabalho mensalidadeTrabalho)
		throws ProcessadorException{
		StoredProcedureQuery listaLotesProcedure = manager.createNamedStoredProcedureQuery("listaGruposItensMensalidade");
		listaLotesProcedure
				.setParameter("id_mensalidade_trabalho", mensalidadeTrabalho.getIdMensalidadeTrabalho());
				
		try {
			return listaLotesProcedure.getResultList();
		} catch (PersistenceException e) {
			throw new ProcessadorException(e);
		}
				
	}
}
