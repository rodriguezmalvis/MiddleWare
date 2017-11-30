package br.org.cac.integrador.dao;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.StoredProcedureQuery;

import br.org.cac.integrador.modelo.ItemReembolso;
import br.org.cac.integrador.modelo.infraestrutura.ReembolsoTrabalho;

public class ItemReembolsoDAO {

	@Inject
	private EntityManager manager;
	
	public ItemReembolsoDAO() {
	
	}
	
	/**
	 * @param reembolsoTrabalho
	 * @param reembolso
	 * @return
	 */
	public List<ItemReembolso> findByReembolsoTrabalho(ReembolsoTrabalho reembolsoTrabalho, Integer cdReembolso) {
		// Processa itens de reembolso
		StoredProcedureQuery listaItensReembolsoProcedure = manager.createNamedStoredProcedureQuery("listaItensReembolso")
				.setParameter("ano_apresentacao", reembolsoTrabalho.getAtendimentoId().getAnoApresentacao())
				.setParameter("id_representacao", reembolsoTrabalho.getAtendimentoId().getIdRepresentacao())
				.setParameter("id_processo", reembolsoTrabalho.getAtendimentoId().getIdProcesso())
				.setParameter("d_sub_processo", reembolsoTrabalho.getAtendimentoId().getdSubProcesso())
				.setParameter("d_natureza", reembolsoTrabalho.getAtendimentoId().getdNatureza())
				.setParameter("id_sequencial_natureza", reembolsoTrabalho.getAtendimentoId().getIdSequencialNatureza())
				.setParameter("id_atendimento", reembolsoTrabalho.getAtendimentoId().getIdAtendimento())
				.setParameter("cd_reembolso", cdReembolso );
		
		@SuppressWarnings("unchecked")
		List<ItemReembolso> itensReembolsoTrabalho = listaItensReembolsoProcedure.getResultList();
		return itensReembolsoTrabalho;
	}	
}
