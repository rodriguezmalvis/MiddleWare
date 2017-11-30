package br.org.cac.integrador.dao;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import br.org.cac.integrador.modelo.infraestrutura.LoteTrabalho;
import br.org.cac.integrador.modelo.infraestrutura.Processamento;

@Transactional
public class LoteTrabalhoDAO {

	@Inject
	private EntityManager manager;

	public LoteTrabalhoDAO() {

	}

	public LoteTrabalho findById(int id) {
		LoteTrabalho retorno = manager.find(LoteTrabalho.class, id);
		return retorno;
	}

	public List<LoteTrabalho> findByProcessamento(Processamento processamento) {

		CriteriaBuilder cb = manager.getCriteriaBuilder();
		CriteriaQuery<LoteTrabalho> query = cb.createQuery(LoteTrabalho.class);
		Root<LoteTrabalho> root = query.from(LoteTrabalho.class);
		query.select(root);
		query.where(cb.equal(root.get("processamento"), processamento));

		List<LoteTrabalho> retorno = manager.createQuery(query).getResultList();

		return retorno;
	}

	public void persist(LoteTrabalho loteTrabalho) {
		manager.persist(loteTrabalho);
	}

	public void merge(LoteTrabalho loteTrabalho) {
		manager.merge(loteTrabalho);
	}
}
