package br.org.cac.integrador.dao;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import br.org.cac.integrador.modelo.infraestrutura.DeParaItemMensalidadeDevolucao;
import br.org.cac.integrador.modelo.infraestrutura.DeParaMensalidadeDevolucao;

@Transactional
public class DeParaItemMensalidadeDevolucaoDAO {

	@Inject
	private EntityManager manager;

	public DeParaItemMensalidadeDevolucaoDAO() {

	}

	public DeParaItemMensalidadeDevolucao findById(int id) {
		DeParaItemMensalidadeDevolucao retorno = manager.find(DeParaItemMensalidadeDevolucao.class, id);
		return retorno;
	}

	public List<DeParaItemMensalidadeDevolucao> findByDeParaMensalidadeDevolucao(DeParaMensalidadeDevolucao deParaMensalidadeDevolucao) {
		// TODO: Implementar findByDeParaMensalidadeDevolucao em DeParaItemMensalidadeDevolucaoDAO
		throw new UnsupportedOperationException("Método ainda não implementado");
	}

	public void persist(DeParaItemMensalidadeDevolucao deParaItemMensalidadeDevolucao) {
		manager.persist(deParaItemMensalidadeDevolucao);
	}

	public DeParaItemMensalidadeDevolucao merge(DeParaItemMensalidadeDevolucao deParaItemMensalidadeDevolucao) {
		return manager.merge(deParaItemMensalidadeDevolucao);
	}
}
