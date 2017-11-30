package br.org.cac.integrador.executaveis;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.StoredProcedureQuery;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.org.cac.cacmvmiddleware.jpautil.JPAutil;
import br.org.cac.integrador.jacksonutil.ObjectMapperFactory;
import br.org.cac.integrador.modelo.ItemReembolso;
import br.org.cac.integrador.modelo.Reembolso;

@WebServlet("/testeStoredProcedure")
public class TesteStoredProcedure extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3652760829003808027L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		PrintWriter pw = resp.getWriter();
		ObjectMapper mapper = ObjectMapperFactory.getDefaultConfiguredObjectMapper(true);
		
		/* SubProcessoId subProcesso = new SubProcessoId(2016, 1, 107, "1", "O", 1); // Guias mistas (Consulta e SADT)
		//SubProcessoId subProcesso = new SubProcessoId(2016, 8, 14, "1", "O", 1); // Trat ser
		ProcessadorContasMedicas processador = new ProcessadorContasMedicas();
		try {
			LoteContaMedica lote = processador.processaSubProcesso(subProcesso, 1).get(0);

			
			pw.println(mapper.writeValueAsString(lote));
			pw.println("Tudo certo!");
		} catch (ProcessadorException e) {
			pw.println("Ocorreu um erro ao tentar gerar um lote baseado no subprocesso " + subProcesso + 
					". Mensagem de erro: ");
			e.printStackTrace(pw);
		} */
		
		EntityManager manager = JPAutil.getEntitymanager();
		
		StoredProcedureQuery listaReembolsosProcedure = manager.createNamedStoredProcedureQuery("listaReembolsos")
				.setParameter("ano_apresentacao", 2016)
				.setParameter("id_representacao", 20)
				.setParameter("id_processo", 4)
				.setParameter("d_sub_processo", "1")
				.setParameter("d_natureza", "O")
				.setParameter("id_sequencial_natureza", 1);
		
		@SuppressWarnings("unchecked")
		List<Reembolso> lista = listaReembolsosProcedure.getResultList();
		
		//pw.println("Lista gerada! Número de itens: " + lista.size());
		
		Reembolso primeiro = lista.get(0);
		
		StoredProcedureQuery listaItensReembolsoProcedure = manager.createNamedStoredProcedureQuery("listaItensReembolso")
				.setParameter("ano_apresentacao", primeiro.getAtendimentoId().getAnoApresentacao())
				.setParameter("id_representacao", primeiro.getAtendimentoId().getIdRepresentacao())
				.setParameter("id_processo", primeiro.getAtendimentoId().getIdProcesso())
				.setParameter("d_sub_processo", primeiro.getAtendimentoId().getdSubProcesso())
				.setParameter("d_natureza", primeiro.getAtendimentoId().getdNatureza())
				.setParameter("id_sequencial_natureza", primeiro.getAtendimentoId().getIdSequencialNatureza())
				.setParameter("id_atendimento", primeiro.getAtendimentoId().getIdAtendimento())
				.setParameter("cd_reembolso", primeiro.getCdReembolso());
		
		@SuppressWarnings("unchecked")
		List<ItemReembolso> itens = listaItensReembolsoProcedure.getResultList();
		
		//pw.println("Outra lista gerada! Número de itens: " + itens.size());
		
		primeiro.setItensReembolso(itens);
		
		pw.println(mapper.writeValueAsString(primeiro));

	}

}
