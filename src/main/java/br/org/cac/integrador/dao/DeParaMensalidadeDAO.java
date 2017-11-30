package br.org.cac.integrador.dao;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import org.jboss.logging.Logger;

import br.org.cac.integrador.modelo.infraestrutura.DeParaMensalidade;
import br.org.cac.integrador.modelo.infraestrutura.Processamento;

@Transactional
public class DeParaMensalidadeDAO {

	@Inject
	private EntityManager manager;
	
	@Inject
	private Logger logger;

	public DeParaMensalidadeDAO() {

	}

	public DeParaMensalidade findById(int id) {
		TypedQuery<DeParaMensalidade> query = manager.createQuery(" select distinct dpm "
				+ " from DeParaMensalidade dpm "
				+ " left join fetch dpm.deParaGruposItensMensalidade "
				+ " left join fetch dpm.deParaGruposItemMensalidade.statusEnvioGrupoItemMensalidade "
				+ " where dpm.idDeParaMensalidade = :id", DeParaMensalidade.class );
		query.setParameter("id", id);
		
		try {
			return query.getSingleResult();
		} catch (NoResultException e) {
			logger.warnf("Nenhum DeParaMensalidade encontrado com id %d. Retornando null.", id);
			return null;
		}
	}

	public List<DeParaMensalidade> findByProcessamento(Processamento processamento) {
		// TODO: Implementar findByProcessamento em DeParaMensalidadeDAO
		throw new UnsupportedOperationException("Método ainda não implementado");
	}

	public void persist(DeParaMensalidade deParaMensalidade) {
		manager.persist(deParaMensalidade);
	}

	public DeParaMensalidade merge(DeParaMensalidade deParaMensalidade) {
		return manager.merge(deParaMensalidade);
	}
}
