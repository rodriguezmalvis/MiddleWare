package br.org.cac.integrador.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import br.org.cac.integrador.modelo.infraestrutura.GrupoItemMensalidadeTrabalho;
import br.org.cac.integrador.modelo.infraestrutura.MensalidadeTrabalho;

@Transactional
public class GrupoItemMensalidadeTrabalhoDAO {

	@Inject
	private EntityManager manager;
	
	public GrupoItemMensalidadeTrabalhoDAO() {

	}
		
	public List<GrupoItemMensalidadeTrabalho> findByMensalidadeTrabalho(MensalidadeTrabalho mensalidadeTrabalho) 
		throws PersistenceException{
		TypedQuery<GrupoItemMensalidadeTrabalho> query = manager
				.createQuery("select distinct gimt "
						+ "from GrupoItemMensalidadeTrabalho gimt left join fetch gimt.itensMensalidadeTrabalho "
						+ "join fetch gimt.mensalidadeTrabalho "
						+ "left join fetch gimt.deParaGrupoItemMensalidade "
						+ "where gimt.mensalidadeTrabalho = :mensalidadeTrabalho", GrupoItemMensalidadeTrabalho.class);
		
		query.setParameter("mensalidadeTrabalho", mensalidadeTrabalho);
		
		return query.getResultList();
	}
	
	public void persist(GrupoItemMensalidadeTrabalho grupoItemMensalidadeTrabalho) {
		manager.persist(grupoItemMensalidadeTrabalho);
	}
	
	public void persistAll(List<GrupoItemMensalidadeTrabalho> gruposItemMensalidadeTrabalho) {
		for (GrupoItemMensalidadeTrabalho gimt : gruposItemMensalidadeTrabalho) {
			this.persist(gimt);
		}
	}

	public GrupoItemMensalidadeTrabalho merge(GrupoItemMensalidadeTrabalho grupoItemMensalidadeTrabalho) {
		return manager.merge(grupoItemMensalidadeTrabalho);		
	}
	
	/**
	 * Executa um {@link #merge(GrupoItemMensalidadeTrabalho)} de todos os {@code GrupoItemMensalidadeTrabalho} passados como parâmetro.
	 * 
	 * @param gruposItemMensalidadeTrabalho os {@code GrupoItemMensalidadeTrabalho}s a terem merge.
	 * @return um {@link Map} contendo como chave a entidade passada como parâmetro e como valor a versão
	 * 	gerenciada desta entidade, conforme retornado pelo {@code merge}.
	 * 
	 */
	public Map<GrupoItemMensalidadeTrabalho, GrupoItemMensalidadeTrabalho> mergeAll(List<GrupoItemMensalidadeTrabalho> 
		gruposItemMensalidadeTrabalho) {
		Map<GrupoItemMensalidadeTrabalho, GrupoItemMensalidadeTrabalho> retorno = new HashMap<>(gruposItemMensalidadeTrabalho.size());
		
		for (GrupoItemMensalidadeTrabalho gimt : gruposItemMensalidadeTrabalho) {
			retorno.put(gimt, this.merge(gimt));
		}
		
		return retorno;
		
	}

	public MensalidadeTrabalho findMensalidadeTrabalho(GrupoItemMensalidadeTrabalho grupoItemMensalidadeTrabalho) {
		TypedQuery<MensalidadeTrabalho> query = manager
				.createQuery(" select gimt.mensalidadeTrabalho "
						+ " from GrupoItemMensalidadeTrabalho gimt "
						+ " where gimt = :grupoItemMensalidadeTrabalho"
						, MensalidadeTrabalho.class);
		query.setParameter("grupoItemMensalidadeTrabalho", grupoItemMensalidadeTrabalho);
		
		return query.getSingleResult();
	}
	
}
