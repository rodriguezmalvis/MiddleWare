package br.org.cac.integrador.dao;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import br.org.cac.integrador.modelo.infraestrutura.DeParaSubProcesso;

@Transactional
public class DeParaSubProcessoDAO {

	@Inject
	private EntityManager manager;

	public DeParaSubProcessoDAO() {

	}

	public DeParaSubProcesso findById(int id) {
		DeParaSubProcesso retorno = manager.find(DeParaSubProcesso.class, id);
		return retorno;
	}

	public void persist(DeParaSubProcesso deParaSubProcesso) {
		manager.persist(deParaSubProcesso);
	}

	public void merge(DeParaSubProcesso deParaSubProcesso) {
		manager.merge(deParaSubProcesso);
	}
}
