/**
 * 
 */
package br.org.cac.integrador.dao;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import br.org.cac.integrador.modelo.infraestrutura.StatusEnvioGrupoItemMensalidade;

/**
 * @author JCJ
 * @since 2017-07-15
 */
public class StatusEnvioGrupoItemMensalidadeDAO {
	@Inject
	private EntityManager manager;

	public StatusEnvioGrupoItemMensalidadeDAO() {

	}

	public StatusEnvioGrupoItemMensalidade findById(int id) {
		StatusEnvioGrupoItemMensalidade retorno = manager.find(StatusEnvioGrupoItemMensalidade.class, id);
		return retorno;
	}

	public StatusEnvioGrupoItemMensalidade findByCodStatus(String codStatus) {
		CriteriaBuilder cb = manager.getCriteriaBuilder();
		CriteriaQuery<StatusEnvioGrupoItemMensalidade> query = cb.createQuery(StatusEnvioGrupoItemMensalidade.class);
		Root<StatusEnvioGrupoItemMensalidade> root = query.from(StatusEnvioGrupoItemMensalidade.class);
		query.select(root);
		query.where(cb.equal(root.get("codStatus"), codStatus));

		StatusEnvioGrupoItemMensalidade retorno = manager.createQuery(query).getSingleResult();

		return retorno;
	}

	public List<StatusEnvioGrupoItemMensalidade> findAll() {
		CriteriaBuilder cb = manager.getCriteriaBuilder();
		CriteriaQuery<StatusEnvioGrupoItemMensalidade> query = cb.createQuery(StatusEnvioGrupoItemMensalidade.class);
		Root<StatusEnvioGrupoItemMensalidade> root = query.from(StatusEnvioGrupoItemMensalidade.class);
		query.select(root);

		List<StatusEnvioGrupoItemMensalidade> retorno = manager.createQuery(query).getResultList();

		return retorno;
	}
}
