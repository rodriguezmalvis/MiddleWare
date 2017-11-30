package br.org.cac.cacmvmiddleware.dao;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.Transactional;

import br.org.cac.cacmvmiddleware.modelo.MvCacDeParaPrestadorEspecialidades;

@Transactional
public class MvCacDeParaPrestadorEspecialidadeDAO {

	@Inject
	EntityManager manager;

	public void incluiDePara(MvCacDeParaPrestadorEspecialidades mvCacDeParaPrestadorEspecialidades) {

		manager.persist(mvCacDeParaPrestadorEspecialidades);

	}

	public MvCacDeParaPrestadorEspecialidades getMvCacDeParaPrestadorEndereco(int id) {
		MvCacDeParaPrestadorEspecialidades mvCacDeParaPrestadorEspecialidades = new MvCacDeParaPrestadorEspecialidades();

		mvCacDeParaPrestadorEspecialidades = manager.find(MvCacDeParaPrestadorEspecialidades.class, id);

		return mvCacDeParaPrestadorEspecialidades;
	}

	public MvCacDeParaPrestadorEspecialidades getMvCacDeParaPrestadorPorIdPrestador(int id_prestador,
			String id_especialidade) {
		MvCacDeParaPrestadorEspecialidades mvCacDeParaPrestadorEspecialidades = new MvCacDeParaPrestadorEspecialidades();

		Query query = manager.createQuery(
				"SELECT p FROM MvCacDeParaPrestadorEspecialidades p where p.id_prestador = :id_prestador and p.cd_especialidade = :id_especialidade");
		query.setParameter("id_prestador", id_prestador);
		query.setParameter("id_especialidade", id_especialidade);
		try {
			mvCacDeParaPrestadorEspecialidades = (MvCacDeParaPrestadorEspecialidades) query.getSingleResult();
			return mvCacDeParaPrestadorEspecialidades;
		} catch (Exception e) {
			return null;
		}

	}

	@SuppressWarnings("unchecked")
	public List<MvCacDeParaPrestadorEspecialidades> getTodosMvCacDeParaPrestador() {
		List<MvCacDeParaPrestadorEspecialidades> mvCacDeParaPrestadorEspecialidades = new ArrayList<MvCacDeParaPrestadorEspecialidades>();

		Query query = manager.createQuery("SELECT p FROM MvCacDeParaPrestador p");
		try {
			mvCacDeParaPrestadorEspecialidades = (List<MvCacDeParaPrestadorEspecialidades>) query.getResultList();
			return mvCacDeParaPrestadorEspecialidades;
		} catch (Exception e) {
			return null;
		}

	}

	public void removeDePara(MvCacDeParaPrestadorEspecialidades mvCacDeParaPrestadorEnderecos) {

		manager.remove(manager.find(MvCacDeParaPrestadorEspecialidades.class, mvCacDeParaPrestadorEnderecos.getId()));

	}

}
