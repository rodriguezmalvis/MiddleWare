package br.org.cac.integrador.dao;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.StoredProcedureQuery;

import br.org.cac.integrador.modelo.Reembolso;
import br.org.cac.integrador.modelo.infraestrutura.ReembolsoTrabalho;

public class ReembolsoDAO {

	@Inject
	private EntityManager manager;
	
	public ReembolsoDAO() {
	
	}
	
	/**
	 * @param reembolsoTrabalho
	 * @return
	 */
	public List<Reembolso> findByReembolsoTrabalho(ReembolsoTrabalho reembolsoTrabalho) {		
		StoredProcedureQuery listaReembolsosProcedure = manager.createNamedStoredProcedureQuery("listaReembolsos")
				.setParameter("ano_apresentacao", reembolsoTrabalho.getAtendimentoId().getAnoApresentacao())
				.setParameter("id_representacao", reembolsoTrabalho.getAtendimentoId().getIdRepresentacao())
				.setParameter("id_processo", reembolsoTrabalho.getAtendimentoId().getIdProcesso())
				.setParameter("d_sub_processo", reembolsoTrabalho.getAtendimentoId().getdSubProcesso())
				.setParameter("d_natureza", reembolsoTrabalho.getAtendimentoId().getdNatureza())
				.setParameter("id_sequencial_natureza", reembolsoTrabalho.getAtendimentoId().getIdSequencialNatureza())
				.setParameter("id_atendimento", reembolsoTrabalho.getAtendimentoId().getIdAtendimento());
		
		@SuppressWarnings("unchecked")
		List<Reembolso> reembolsos = listaReembolsosProcedure.getResultList();
		
		return reembolsos;
	}
}
