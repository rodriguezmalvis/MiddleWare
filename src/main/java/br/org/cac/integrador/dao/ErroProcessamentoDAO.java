package br.org.cac.integrador.dao;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import br.org.cac.integrador.modelo.infraestrutura.ErroProcessamento;

@Transactional
public class ErroProcessamentoDAO {

	@Inject
	private EntityManager manager;

	public ErroProcessamentoDAO() {

	}

	public ErroProcessamento findById(int id) {
		ErroProcessamento retorno = manager.find(ErroProcessamento.class, id);
		return retorno;
	}

	// TODO: Ver a necessidade de um findByLoteTrabalho

	public void persist(ErroProcessamento erroProcessamento) {
		manager.persist(erroProcessamento);
	}
}
