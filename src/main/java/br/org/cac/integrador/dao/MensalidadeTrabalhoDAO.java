package br.org.cac.integrador.dao;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import br.org.cac.integrador.modelo.infraestrutura.MensalidadeTrabalho;
import br.org.cac.integrador.modelo.infraestrutura.Processamento;

@Transactional
public class MensalidadeTrabalhoDAO {

	@Inject
	private EntityManager manager;

	public MensalidadeTrabalhoDAO() {

	}

	public MensalidadeTrabalho findById(int id) {
		MensalidadeTrabalho retorno = manager.find(MensalidadeTrabalho.class, id);
		return retorno;
	}

	public List<MensalidadeTrabalho> findByProcessamento(Processamento processamento) {
		// TODO: Alterar para que statusFinal seja um parâmetro, e implementar
		// um que receba um status específico
		// Essas alterações vão permitir implementar corretamente o continua e o
		// reprocessa.
		TypedQuery<MensalidadeTrabalho> query = manager
				.createQuery("select distinct e from MensalidadeTrabalho e join fetch e.statusProcessamento "
						+ " join fetch e.processamento "
						+ " join fetch e.itensMensalidadeTrabalho i " + " where e.processamento = :processamento"
						+ " and e.statusProcessamento.statusFinal = :statusFinal ", MensalidadeTrabalho.class);
		query.setParameter("processamento", processamento);
		query.setParameter("statusFinal", Boolean.FALSE);

		List<MensalidadeTrabalho> retorno = query.getResultList();

		return retorno;
	}

	public void persist(MensalidadeTrabalho mensalidadeTrabalho) {
		manager.persist(mensalidadeTrabalho);
	}

	public MensalidadeTrabalho merge(MensalidadeTrabalho mensalidadeTrabalho) {
		return manager.merge(mensalidadeTrabalho);
	}
}
