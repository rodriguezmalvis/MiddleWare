package br.org.cac.integrador.dao;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import br.org.cac.integrador.modelo.infraestrutura.Processamento;

@Transactional
public class ProcessamentoDAO {

	@Inject
	private EntityManager manager;

	public ProcessamentoDAO() {

	}

	public Processamento findById(int id) {
		Processamento retorno = manager.find(Processamento.class, id);
		return retorno;
	}

	public void persist(Processamento processamento) {
		manager.persist(processamento);
	}

	public void merge(Processamento processamento) {
		manager.merge(processamento);
	}
}
