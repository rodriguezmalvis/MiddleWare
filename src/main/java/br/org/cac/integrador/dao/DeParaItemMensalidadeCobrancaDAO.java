package br.org.cac.integrador.dao;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import br.org.cac.integrador.modelo.infraestrutura.DeParaItemMensalidadeCobranca;
import br.org.cac.integrador.modelo.infraestrutura.DeParaMensalidadeCobranca;

@Transactional
public class DeParaItemMensalidadeCobrancaDAO {

	@Inject
	private EntityManager manager;

	public DeParaItemMensalidadeCobrancaDAO() {

	}

	public DeParaItemMensalidadeCobranca findById(int id) {
		DeParaItemMensalidadeCobranca retorno = manager.find(DeParaItemMensalidadeCobranca.class, id);
		return retorno;
	}

	public List<DeParaItemMensalidadeCobranca> findByDeParaMensalidadeCobranca(DeParaMensalidadeCobranca deParaMensalidadeCobranca) {
		// TODO: Implementar findByDeParaMensalidadeCobranca em DeParaItemMensalidadeCobrancaDAO
		throw new UnsupportedOperationException("Método ainda não implementado");
	}

	public void persist(DeParaItemMensalidadeCobranca deParaItemMensalidadeCobranca) {
		manager.persist(deParaItemMensalidadeCobranca);
	}

	public DeParaItemMensalidadeCobranca merge(DeParaItemMensalidadeCobranca deParaItemMensalidadeCobranca) {
		return manager.merge(deParaItemMensalidadeCobranca);
	}
}
