package br.org.cac.integrador.dao;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import br.org.cac.integrador.modelo.infraestrutura.DeParaReembolso;
import br.org.cac.integrador.modelo.infraestrutura.Processamento;

@Transactional
public class DeParaReembolsoDAO {

	@Inject
	private EntityManager manager;

	public DeParaReembolsoDAO() {

	}

	public DeParaReembolso findById(int id) {
		DeParaReembolso retorno = manager.find(DeParaReembolso.class, id);
		return retorno;
	}

	public List<DeParaReembolso> findByProcessamento(Processamento processamento) {
		// TODO: Implementar findByProcessamento em DeParaReembolsoDAO
		throw new UnsupportedOperationException("Método ainda não implementado");
	}

	public void persist(DeParaReembolso deParaReembolso) {
		manager.persist(deParaReembolso);
	}

	public void merge(DeParaReembolso deParaReembolso) {
		manager.merge(deParaReembolso);
	}
}
