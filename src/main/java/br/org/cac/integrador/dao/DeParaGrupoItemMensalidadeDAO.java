package br.org.cac.integrador.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import br.org.cac.integrador.modelo.infraestrutura.DeParaGrupoItemMensalidade;
import br.org.cac.integrador.modelo.infraestrutura.DeParaMensalidade;

@Transactional
public class DeParaGrupoItemMensalidadeDAO {

	@Inject
	private EntityManager manager;

	public DeParaGrupoItemMensalidadeDAO() {
		// Construtor padrão
	}
	
	public List<DeParaGrupoItemMensalidade> listByDeParaMensalidade(DeParaMensalidade deParaMensalidade){
		if (null == deParaMensalidade) {
			throw new IllegalArgumentException("O parâmetro deParaMensalidade não pode ser null");
		}
		
		if (null == deParaMensalidade.getIdDeParaMensalidade()) {
			throw new NullPointerException("O parâmetro deParaMensalidade possui seu idDeParaMensalidade null");
		}
		
		TypedQuery<DeParaGrupoItemMensalidade> query = manager.createQuery(" select distinct dpgim "
				+ " from DeParaGrupoItemMensalidade dpgim "
				+ " join fetch dpgim.deParaMensalidade "
				+ " join fetch dpgim.statusEnvioGrupoItemMensalidade "
				+ " where dpgim.deParaMensalidade = :deParaMensalidade", 
				DeParaGrupoItemMensalidade.class );
		query.setParameter("deParaMensalidade", deParaMensalidade);
		
		return query.getResultList();
	}
	
	public List<DeParaGrupoItemMensalidade> listByStatusConsistencia(boolean geraInconsistencia){
		TypedQuery<DeParaGrupoItemMensalidade> query = manager.createQuery(" select dpgim "
				+ " from DeParaGrupoItemMensalidade dpgim "
				+ " join fetch dpgim.deParaMensalidade dpm "
				+ " join fetch dpm.processamento pp "
				+ " join fetch dpgim.statusEnvioGrupoItemMensalidade segim"
				+ " where segim.fGeraInconsistencia = :geraInconsistencia ", 				
				DeParaGrupoItemMensalidade.class);
		query.setParameter("geraInconsistencia", geraInconsistencia);
		
		return query.getResultList();
	}
	
	public void persist(DeParaGrupoItemMensalidade deParaGrupoItemMensalidade) {
		manager.persist(deParaGrupoItemMensalidade);
	}
	
	public DeParaGrupoItemMensalidade merge(DeParaGrupoItemMensalidade deParaGrupoItemMensalidade) {
		return manager.merge(deParaGrupoItemMensalidade);
	}
	
	public void persistAll(List<DeParaGrupoItemMensalidade> deParaGruposItemMensalidade) {
		for (DeParaGrupoItemMensalidade dpgim : deParaGruposItemMensalidade) {
			this.persist(dpgim);
		}
	}
	
	/**
	 * Executa um {@link #merge(DeParaGrupoItemMensalidade)} de todos os {@code DeParaGrupoItemMensalidade} passados como parâmetro.
	 * 
	 * @param deParaGruposItemMensalidade os {@code DeParaGrupoItemMensalidade}s a terem merge.
	 * @return um {@link Map} contendo como chave a entidade passada como parâmetro e como valor a versão
	 * 	gerenciada desta entidade, conforme retornado pelo {@code merge}.
	 * 
	 */
	public Map<DeParaGrupoItemMensalidade, DeParaGrupoItemMensalidade> mergeAll(List<DeParaGrupoItemMensalidade> 
		deParaGruposItemMensalidade) {
		Map<DeParaGrupoItemMensalidade, DeParaGrupoItemMensalidade> retorno = new HashMap<>(deParaGruposItemMensalidade.size());
		
		for (DeParaGrupoItemMensalidade gimt : deParaGruposItemMensalidade) {
			retorno.put(gimt, this.merge(gimt));
		}
		
		return retorno;
		
	}
	
}
