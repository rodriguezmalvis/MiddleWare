package br.org.cac.integrador.dao;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import br.org.cac.integrador.modelo.infraestrutura.DeParaItemMensalidade;
import br.org.cac.integrador.modelo.infraestrutura.DeParaMensalidade;

@Transactional
public class DeParaItemMensalidadeDAO {

	@Inject
	private EntityManager manager;

	public DeParaItemMensalidadeDAO() {

	}

	public DeParaItemMensalidade findById(int id) {
		DeParaItemMensalidade retorno = manager.find(DeParaItemMensalidade.class, id);
		return retorno;
	}

	public List<DeParaItemMensalidade> findByDeParaMensalidade(DeParaMensalidade deParaMensalidade) {
		// TODO: Implementar findByDeParaMensalidade em DeParaItemMensalidadeDAO
		throw new UnsupportedOperationException("Método ainda não implementado");
	}

	public void persist(DeParaItemMensalidade deParaItemMensalidade) {
		manager.persist(deParaItemMensalidade);
	}

	public void merge(DeParaItemMensalidade deParaItemMensalidade) {
		manager.merge(deParaItemMensalidade);
	}
}
