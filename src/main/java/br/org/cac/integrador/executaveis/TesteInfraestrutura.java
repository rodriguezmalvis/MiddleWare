package br.org.cac.integrador.executaveis;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.org.cac.integrador.dao.DeParaAjusteSubProcessoDAO;
import br.org.cac.integrador.dao.DeParaMensalidadeCobrancaDAO;
import br.org.cac.integrador.dao.DeParaMensalidadeDAO;
import br.org.cac.integrador.dao.DeParaSubProcessoDAO;
import br.org.cac.integrador.dao.GrupoItemMensalidadeTrabalhoDAO;
import br.org.cac.integrador.dao.ItemMensalidadeTrabalhoDAO;
import br.org.cac.integrador.dao.MensalidadeTrabalhoDAO;
import br.org.cac.integrador.dao.ProcessamentoDAO;
import br.org.cac.integrador.modelo.infraestrutura.DeParaAjusteSubProcesso;
import br.org.cac.integrador.modelo.infraestrutura.DeParaSubProcesso;
import br.org.cac.integrador.modelo.infraestrutura.MotivoAjuste;

//@WebServlet("/testeInfraestrutura")
public class TesteInfraestrutura extends HttpServlet{
	
	@Inject
	private DeParaMensalidadeCobrancaDAO dpmcDao;
	
	@Inject
	private DeParaMensalidadeDAO dpmDao;
	
	@Inject
	private MensalidadeTrabalhoDAO mtDao;
	
	@Inject
	private ItemMensalidadeTrabalhoDAO imtDao;
	
	@Inject
	private GrupoItemMensalidadeTrabalhoDAO gimtDao;
	
	@Inject
	private ProcessamentoDAO pDao;
	
	@Inject
	private DeParaSubProcessoDAO dpspDao;
	
	@Inject
	private DeParaAjusteSubProcessoDAO dpaspDao;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6496474067454216920L;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		//EntityManager manager = JPAutil.getEntitymanager();
		PrintWriter pw = resp.getWriter();
		
		/*Processamento processamento = new Processamento();
		ProcessamentoDAO pdao = new ProcessamentoDAO();

		manager.getTransaction().begin();

		processamento = pdao.findById(1); //manager.find(Processamento.class, 1);
		
		processamento.setDthrFimProcessamento(Calendar.getInstance().getTime());
		
		//pdao.merge(processamento);
		
		Query queryLista = manager.createQuery("select s from StatusProcessamento s");
		
		@SuppressWarnings("unchecked")
		List<StatusProcessamento> todosStatus = queryLista.getResultList();
		
		CriteriaBuilder cb = manager.getCriteriaBuilder();
		CriteriaQuery<LoteTrabalho> queryTrabalho = cb.createQuery(LoteTrabalho.class);
		Root<LoteTrabalho> root = queryTrabalho.from(LoteTrabalho.class);
		queryTrabalho.select(root);
		queryTrabalho.where(
					cb.equal(root.get("processamento"), processamento)
					);
		
		Query resultadoLoteTrabalho = manager.createQuery(queryTrabalho);
		
		
		@SuppressWarnings("unchecked")
		List<LoteTrabalho> lotesTrabalho = resultadoLoteTrabalho.getResultList();
		
		TipoProcessamento tp = new TipoProcessamentoDAO().findByCodProcessamento("CI");
		
		Processamento p2 = new Processamento();
		p2.setTipoProcessamento(tp);
		p2.setDthrInicioProcessamento(Calendar.getInstance().getTime());
		//pdao.persist(p2);
		

		// TODO: ver se as transações estão realmente sendo concluídas
		manager.close();
		

		
		/*for (StatusProcessamento sp : todosStatus){
			pw.println(sp.getCodStatus() + "-" + sp.getStatusFinal());
		}
		
		for (LoteTrabalho lt : lotesTrabalho) {
			pw.println(lt.getSubProcesso());
			for (ErroProcessamento ep : lt.getErrosProcessamento()){
				pw.println(ep.getMensagem());
			}
		}*/
		
		/*Processamento processamento = new Processamento();
		processamento.setIdProcessamento(43);*/
		
		
		/*List<MensalidadeTrabalho> lista = mtDao.findByProcessamento(processamento);
		
		for (MensalidadeTrabalho mt : lista){
			pw.println(mt.getIdMensalidadeTrabalho());
			
		}*/		
		
		/*MensalidadeTrabalho mt = mtDao.findById(63593);
		
		List<GrupoItemMensalidadeTrabalho> grupos = gimtDao.findByMensalidadeTrabalho(mt);
		
		/*List<ItemMensalidadeTrabalho> cobrancas = imtDao.findByMensalidadeTrabalho(mt, false);
		List<ItemMensalidadeTrabalho> devolucoes = imtDao.findByMensalidadeTrabalho(mt, true);
		
		for (ItemMensalidadeTrabalho imt : cobrancas){
			pw.println(imt.toString());			
		}
		
		for (ItemMensalidadeTrabalho imt : devolucoes){
			pw.println(imt.toString());			
		}	
		
		
		pw.println(mt.toString());*/
		
		/*Calendar calendar = Calendar.getInstance();
			
		DeParaMensalidade dpm = new DeParaMensalidade();
		
		calendar.clear();
		calendar.set(2017, 5, 25);		
		dpm.setDtEmissao(calendar.getTime());
		
		calendar.clear();
		calendar.set(2017, 6, 15);
		dpm.setDtVencimento(calendar.getTime());
		
		calendar.clear();
		calendar.set(2017, 5, 1);
		dpm.setDtReferenciaEfetiva(calendar.getTime());
		
		dpm.setFormaPagamento(FormaPagamento.FOLHA);
		
		dpm.setIdTitular(144707);
		
		dpm.setProcessamento(pDao.findById(134));
		
		DeParaMensalidadeCobranca dpmc = new DeParaMensalidadeCobranca();
		
		dpmc.setCdMensContratoInterno(-1);
		dpmc.setCdMensContratoEfetivo(-1);
		dpmc.setDeParaMensalidade(dpm);
		
		dpm.setDeParaMensalidadeCobranca(dpmc);
		
		dpmDao.persist(dpm);
		
		dpm.setDeParaMensalidadeDevolucao(new ArrayList<>());
		
		DeParaMensalidadeDevolucao dpmd = new DeParaMensalidadeDevolucao();
		dpmd.setCdDevolucaoInterno(-1);
		dpmd.setCdDevolucaoEfetivo(-1);
		dpmd.setDeParaMensalidade(dpm);
		
		dpm.getDeParaMensalidadeDevolucao().add(dpmd);
		
		dpmd.setDeParaItensMensalidadeDevolucao(new ArrayList<>());
		
		DeParaItemMensalidadeDevolucao dpimd = new DeParaItemMensalidadeDevolucao();
		
		dpimd.setIdComando(1);
		dpimd.setDeParaMensalidadeDevolucao(dpmd);
		
		dpmd.getDeParaItensMensalidadeDevolucao().add(dpimd);
		
		dpmDao.merge(dpm);
		
		pw.println(dpmc);		
		*/
		
		DeParaSubProcesso deParaSubProcesso = dpspDao.findById(5685);
		
		List<DeParaAjusteSubProcesso> ajustes = new ArrayList<>();
		
		ajustes.add(new DeParaAjusteSubProcesso(1, 2, "valor_apresentado", "50", "60", MotivoAjuste.VALOR_APRESENTADO_MENOR_QUE_PAGO));
		ajustes.add(new DeParaAjusteSubProcesso(2, null, "valor_final", "40", "22", MotivoAjuste.VALOR_APRESENTADO_MENOR_QUE_PAGO));
		ajustes.add(new DeParaAjusteSubProcesso(null, null, "valor_legal", "bola", "chute", MotivoAjuste.VALOR_APRESENTADO_MENOR_QUE_PAGO));
		
		ajustes.forEach(e -> e.setDeParaSubProcesso(deParaSubProcesso));
		
		dpaspDao.mergeAll(ajustes);
		
		pw.println(deParaSubProcesso);
		
		

		//return mvCacDeParaBeneficiario;
	}

}
