package br.org.cac.integrador.dao;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import br.org.cac.integrador.modelo.infraestrutura.DeParaMensalidade;
import br.org.cac.integrador.modelo.infraestrutura.DeParaMensalidadeDevolucao;

@Transactional
public class DeParaMensalidadeDevolucaoDAO {

	@Inject
	private EntityManager manager;
	
	public DeParaMensalidadeDevolucaoDAO(){
		
	}
	
	public DeParaMensalidadeDevolucao findById(Integer id){
		DeParaMensalidadeDevolucao retorno = manager.find(DeParaMensalidadeDevolucao.class, id);
		return retorno;
	}	
		
	public List<DeParaMensalidadeDevolucao> findByDeParaMensalidade(DeParaMensalidade deParaMensalidade){
		// TODO: Implementar findByDeParaMensalidade em DeParaItemMensalidadeDAO
		throw new UnsupportedOperationException("Método ainda não implementado");
	}

	public void persist(DeParaMensalidadeDevolucao deParaMensalidadeDevolucao){
		manager.persist(deParaMensalidadeDevolucao);
	}
	
	public DeParaMensalidadeDevolucao merge(DeParaMensalidadeDevolucao deParaMensalidadeDevolucao){
		return manager.merge(deParaMensalidadeDevolucao);
	}
}
