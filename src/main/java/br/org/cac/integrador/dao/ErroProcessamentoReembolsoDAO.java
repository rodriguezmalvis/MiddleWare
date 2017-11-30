package br.org.cac.integrador.dao;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import br.org.cac.integrador.modelo.infraestrutura.ErroProcessamentoReembolso;

@Transactional
public class ErroProcessamentoReembolsoDAO {

	@Inject
	private EntityManager manager;

	public ErroProcessamentoReembolsoDAO() {

	}

	public ErroProcessamentoReembolso findById(int id) {
		ErroProcessamentoReembolso retorno = manager.find(ErroProcessamentoReembolso.class, id);
		return retorno;
	}

	// TODO: Ver a necessidade de um findByReembolsoTrabalho

	public void persist(ErroProcessamentoReembolso erroProcessamento) {
		manager.persist(erroProcessamento);
	}
}
