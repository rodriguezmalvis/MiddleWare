package br.org.cac.cacmvmiddleware.dao;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.Transactional;

import br.org.cac.cacmvmiddleware.modelo.MvCacDeParaPrestadorEnderecos;

@Transactional
public class MvCacDeParaPrestadorEnderecoDAO {

	@Inject
	EntityManager manager;

	public void incluiDePara(MvCacDeParaPrestadorEnderecos mvCacDeParaPrestadorEnderecos) {

		manager.persist(mvCacDeParaPrestadorEnderecos);

	}

	public MvCacDeParaPrestadorEnderecos getMvCacDeParaPrestadorEndereco(int id) {
		MvCacDeParaPrestadorEnderecos mvCacDeParaPrestadorEnderecos = new MvCacDeParaPrestadorEnderecos();

		mvCacDeParaPrestadorEnderecos = manager.find(MvCacDeParaPrestadorEnderecos.class, id);

		return mvCacDeParaPrestadorEnderecos;
	}

	public MvCacDeParaPrestadorEnderecos getMvCacDeParaPrestadorPorIdPrestador(int id_prestador,
			String cod_endereco_prestador) {
		MvCacDeParaPrestadorEnderecos mvCacDeParaPrestadorEnderecos = new MvCacDeParaPrestadorEnderecos();

		Query query = manager.createQuery(
				"SELECT p FROM MvCacDeParaPrestadorEnderecos p where p.id_prestador = :id_prestador and p.cod_prestador_endereco = :cod_endereco_prestador");
		query.setParameter("id_prestador", id_prestador);
		query.setParameter("cod_endereco_prestador", cod_endereco_prestador);
		try {
			mvCacDeParaPrestadorEnderecos = (MvCacDeParaPrestadorEnderecos) query.getSingleResult();
			return mvCacDeParaPrestadorEnderecos;
		} catch (Exception e) {
			return null;
		}

	}

	@SuppressWarnings("unchecked")
	public List<MvCacDeParaPrestadorEnderecos> getTodosMvCacDeParaPrestador() {
		List<MvCacDeParaPrestadorEnderecos> mvCacDeParaPrestadorEnderecos = new ArrayList<MvCacDeParaPrestadorEnderecos>();

		Query query = manager.createQuery("SELECT p FROM MvCacDeParaPrestador p");
		try {
			mvCacDeParaPrestadorEnderecos = (List<MvCacDeParaPrestadorEnderecos>) query.getResultList();
			return mvCacDeParaPrestadorEnderecos;
		} catch (Exception e) {
			return null;
		}

	}

	public void removeDePara(MvCacDeParaPrestadorEnderecos mvCacDeParaPrestadorEnderecos) {

		manager.remove(manager.find(MvCacDeParaPrestadorEnderecos.class, mvCacDeParaPrestadorEnderecos.getId()));

	}

}
