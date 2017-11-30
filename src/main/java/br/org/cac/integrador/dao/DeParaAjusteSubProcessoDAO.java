/**
 * 
 */
package br.org.cac.integrador.dao;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import br.org.cac.integrador.modelo.infraestrutura.DeParaAjusteSubProcesso;

/**
 * DAO para as operações da classe {@link DeParaAjusteSubProcesso}.
 * @author JCJ
 * @since 2017-07-09
 */
@Transactional
public class DeParaAjusteSubProcessoDAO {
	
	@Inject
	private EntityManager manager;

	public DeParaAjusteSubProcessoDAO() {

	}

	public DeParaAjusteSubProcesso findById(int id) {
		return manager.find(DeParaAjusteSubProcesso.class, id);
	}

	public void persist(DeParaAjusteSubProcesso deParaAjusteSubProcesso) {
		manager.persist(deParaAjusteSubProcesso);
	}
	
	public void persistAll(List<DeParaAjusteSubProcesso> deParaAjustesSubProcesso) {
		for (DeParaAjusteSubProcesso dpasp : deParaAjustesSubProcesso) {
			this.persist(dpasp);
		}
	}

	public DeParaAjusteSubProcesso merge(DeParaAjusteSubProcesso deParaAjusteSubProcesso) {
		return manager.merge(deParaAjusteSubProcesso);
	}
	
	public void mergeAll(List<DeParaAjusteSubProcesso> deParaAjustesSubProcesso) {
		// TODO: Alterar para retornar map?
		for (DeParaAjusteSubProcesso dpasp : deParaAjustesSubProcesso) {
			this.merge(dpasp);
		}
	}
}
