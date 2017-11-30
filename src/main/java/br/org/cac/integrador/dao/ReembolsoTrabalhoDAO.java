package br.org.cac.integrador.dao;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import br.org.cac.integrador.modelo.infraestrutura.Processamento;
import br.org.cac.integrador.modelo.infraestrutura.ReembolsoTrabalho;

@Transactional
public class ReembolsoTrabalhoDAO {

	@Inject
	private EntityManager manager;

	public ReembolsoTrabalhoDAO() {

	}

	public ReembolsoTrabalho findById(int id) {
		ReembolsoTrabalho retorno = manager.find(ReembolsoTrabalho.class, id);
		manager.getTransaction().commit();
		return retorno;
	}

	public List<ReembolsoTrabalho> findByProcessamento(Processamento processamento) {

		TypedQuery<ReembolsoTrabalho> query = manager
				.createQuery("select e from ReembolsoTrabalho e join fetch e.statusProcessamento "
						+ " where e.processamento = :processamento", ReembolsoTrabalho.class);
		query.setParameter("processamento", processamento);
		List<ReembolsoTrabalho> retorno = query.getResultList();

		return retorno;
	}

	public void persist(ReembolsoTrabalho reembolsoTrabalho) {
		manager.persist(reembolsoTrabalho);
	}

	public void merge(ReembolsoTrabalho reembolsoTrabalho) {
		manager.merge(reembolsoTrabalho);
	}
}
