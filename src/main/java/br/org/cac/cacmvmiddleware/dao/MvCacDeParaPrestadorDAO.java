package br.org.cac.cacmvmiddleware.dao;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.Transactional;

import br.org.cac.cacmvmiddleware.modelo.MvCacDeParaPrestador;

@Transactional
public class MvCacDeParaPrestadorDAO {

	@Inject
	EntityManager manager;

	public void incluiDePara(MvCacDeParaPrestador deParaPrestador) {

		manager.persist(deParaPrestador);

	}

	public MvCacDeParaPrestador getMvCacDeParaPrestador(int id) {
		MvCacDeParaPrestador mvCacDeParaPrestador = new MvCacDeParaPrestador();

		mvCacDeParaPrestador = manager.find(MvCacDeParaPrestador.class, id);

		return mvCacDeParaPrestador;
	}

	public MvCacDeParaPrestador getMvCacDeParaPrestadorPorIdPrestador(int id_prestador) {
		MvCacDeParaPrestador mvCacDeParaPrestador = new MvCacDeParaPrestador();

		Query query = manager.createQuery("SELECT p FROM MvCacDeParaPrestador p where p.id_prestador = :id_prestador");
		query.setParameter("id_prestador", id_prestador);
		try {
			mvCacDeParaPrestador = (MvCacDeParaPrestador) query.getSingleResult();
			return mvCacDeParaPrestador;
		} catch (Exception e) {
			return null;
		}

	}

	@SuppressWarnings("unchecked")
	public List<MvCacDeParaPrestador> getTodosMvCacDeParaPrestador() {
		List<MvCacDeParaPrestador> mvCacDeParaPrestador = new ArrayList<MvCacDeParaPrestador>();

		Query query = manager.createQuery("SELECT p FROM MvCacDeParaPrestador p");
		try {
			mvCacDeParaPrestador = (List<MvCacDeParaPrestador>) query.getResultList();
			return mvCacDeParaPrestador;
		} catch (Exception e) {
			return null;
		}

	}

	public void removeDePara(MvCacDeParaPrestador deParaPrestador) {

		manager.remove(manager.find(MvCacDeParaPrestador.class, deParaPrestador.getId()));

	}

}
