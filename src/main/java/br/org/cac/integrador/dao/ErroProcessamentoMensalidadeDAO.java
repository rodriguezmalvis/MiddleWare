package br.org.cac.integrador.dao;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import br.org.cac.integrador.modelo.infraestrutura.ErroProcessamentoMensalidade;

@Transactional
public class ErroProcessamentoMensalidadeDAO {

	@Inject
	private EntityManager manager;

	public ErroProcessamentoMensalidadeDAO() {

	}

	public ErroProcessamentoMensalidade findById(int id) {
		ErroProcessamentoMensalidade retorno = manager.find(ErroProcessamentoMensalidade.class, id);
		return retorno;
	}

	// TODO: Ver a necessidade de um findByMensalidadeTrabalho

	public void persist(ErroProcessamentoMensalidade erroProcessamento) {
		manager.persist(erroProcessamento);
	}
}
