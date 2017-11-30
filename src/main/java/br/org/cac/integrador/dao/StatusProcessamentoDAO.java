package br.org.cac.integrador.dao;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import br.org.cac.integrador.modelo.infraestrutura.StatusProcessamento;

@Transactional
public class StatusProcessamentoDAO {

	@Inject
	private EntityManager manager;

	public StatusProcessamentoDAO() {

	}

	public StatusProcessamento findById(int id) {
		StatusProcessamento retorno = manager.find(StatusProcessamento.class, id);
		return retorno;
	}

	public StatusProcessamento findByCodStatus(String codStatus) {
		CriteriaBuilder cb = manager.getCriteriaBuilder();
		CriteriaQuery<StatusProcessamento> query = cb.createQuery(StatusProcessamento.class);
		Root<StatusProcessamento> root = query.from(StatusProcessamento.class);
		query.select(root);
		query.where(cb.equal(root.get("codStatus"), codStatus));

		StatusProcessamento retorno = manager.createQuery(query).getSingleResult();

		return retorno;
	}

	public List<StatusProcessamento> findAll() {
		CriteriaBuilder cb = manager.getCriteriaBuilder();
		CriteriaQuery<StatusProcessamento> query = cb.createQuery(StatusProcessamento.class);
		Root<StatusProcessamento> root = query.from(StatusProcessamento.class);
		query.select(root);

		List<StatusProcessamento> retorno = manager.createQuery(query).getResultList();

		return retorno;
	}
}
