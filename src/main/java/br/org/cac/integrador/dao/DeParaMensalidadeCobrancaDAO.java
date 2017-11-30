package br.org.cac.integrador.dao;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import org.jboss.logging.Logger;

import br.org.cac.integrador.modelo.infraestrutura.DeParaMensalidade;
import br.org.cac.integrador.modelo.infraestrutura.DeParaMensalidadeCobranca;

@Transactional
public class DeParaMensalidadeCobrancaDAO {

	@Inject
	private EntityManager manager;
	
	@Inject
	private Logger logger;
	
	public DeParaMensalidadeCobrancaDAO(){
		
	}
		
	public DeParaMensalidadeCobranca findByDeParaMensalidade(DeParaMensalidade deParaMensalidade){
		if (deParaMensalidade == null){
			throw new IllegalArgumentException("deParaMensalidade não pode ser null");
		}
		
		return this.findById(deParaMensalidade.getIdDeParaMensalidade());
		
		/*DeParaMensalidadeCobranca retorno = manager.find(DeParaMensalidadeCobranca.class, id.getIdDeParaMensalidade());
		return retorno;*/
	}
	
	public DeParaMensalidadeCobranca findById(Integer id){
		if (id == null){
			throw new IllegalArgumentException("id não pode ser null");
		}
		
		/* TODO: Investigar o motivo de não ser possível usar deParaMensalidade como parâmetro na query abaixo. */
		TypedQuery<DeParaMensalidadeCobranca> query = manager
				.createQuery("select mc from DeParaMensalidadeCobranca mc join fetch mc.deParaMensalidade "
						+ "where mc.idDeParaMensalidadeCobranca = :idDeParaMensalidadeCobranca", DeParaMensalidadeCobranca.class);
		query.setParameter("idDeParaMensalidadeCobranca", id);
		try {
			DeParaMensalidadeCobranca retorno = query.getSingleResult();
			return retorno;
		} catch (NoResultException e) {
			String mensagem = String.format("Nenhum DeParaMensalidadeCobranca localizado com o id informado: %d",
					id);
			logger.error(mensagem);
			throw new PersistenceException(mensagem, e);
		}		
	}
	
	public void persist(DeParaMensalidadeCobranca deParaMensalidadeCobranca){
		manager.persist(deParaMensalidadeCobranca);
	}
	
	public DeParaMensalidadeCobranca merge(DeParaMensalidadeCobranca deParaMensalidadeCobranca){
		return manager.merge(deParaMensalidadeCobranca);
	}
}
