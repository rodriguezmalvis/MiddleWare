package br.org.cac.integrador.dao;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import br.org.cac.integrador.modelo.infraestrutura.TipoProcessamento;

@Transactional
public class TipoProcessamentoDAO {

	@Inject
	private EntityManager manager;

	public TipoProcessamentoDAO() {

	}

	public TipoProcessamento findById(int id) {
		TipoProcessamento retorno = manager.find(TipoProcessamento.class, id);
		return retorno;
	}

	public TipoProcessamento findByCodProcessamento(String codProcessamento) {

		CriteriaBuilder cb = manager.getCriteriaBuilder();
		CriteriaQuery<TipoProcessamento> query = cb.createQuery(TipoProcessamento.class);
		Root<TipoProcessamento> root = query.from(TipoProcessamento.class);
		query.select(root);
		query.where(cb.equal(root.get("codProcessamento"), codProcessamento));

		TipoProcessamento retorno = manager.createQuery(query).getSingleResult();

		return retorno;
	}
}
