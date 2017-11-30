package br.org.cac.cacmvmiddleware.servlet;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;

import br.org.cac.cacmvmiddleware.dao.CacDAO;
import br.org.cac.cacmvmiddleware.dao.LogMiddlewareDAO;
import br.org.cac.cacmvmiddleware.dao.MvCacDeParaBeneficiarioOcupacionalDAO;
import br.org.cac.cacmvmiddleware.modelo.BeneficiarioOcupacional;
import br.org.cac.cacmvmiddleware.modelo.MvCacDeParaBeneficiarioOcupacional;
import br.org.cac.cacmvmiddleware.modelo.Operacoes;
import br.org.cac.cacmvmiddleware.modelo.Retorno;
import br.org.cac.cacmvmiddleware.services.BeneficiarioWebServices;
import br.org.cac.cacmvmiddleware.util.Util;

/**
 * Servlet implementation class CarregaBeneficiarios
 */
@WebServlet("/CarregaBeneficiariosOcupacional")
public class CarregaBeneficiariosOcupacional extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	@Inject
	CacDAO cac;
	
	@Inject
	LogMiddlewareDAO dao;
	
	@Inject
	MvCacDeParaBeneficiarioOcupacionalDAO deParaOcupacionalDAO;
	
	@Inject
	BeneficiarioWebServices beneficiarioWS;
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CarregaBeneficiariosOcupacional() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		if (request.getParameter("idPessoa") != null){
			
			String idPessoa = request.getParameter("idPessoa");
			
			BeneficiarioOcupacional b = cac.getBeneficiarioOcupacional(idPessoa);
			
			if (b != null){
				
				
				Response resposta = beneficiarioWS.incluir(Util.geraJsonBeneficiario(b,Operacoes.SALVAR));

				Gson gson = new Gson();
				Retorno ret = gson.fromJson(resposta.getEntity().toString(), Retorno.class);
				
				if (ret.getStatus().equalsIgnoreCase("200")) {

					System.out.println("Id pessoa: "+b.getCdPessoa()+", Usuario MV: "+ret.getEntidadeId());
					response.getWriter().println("Id pessoa: "+b.getCdPessoa()+", Usuario MV: "+ret.getEntidadeId());
					
					MvCacDeParaBeneficiarioOcupacional deparaben = new MvCacDeParaBeneficiarioOcupacional();
					deparaben.setId_pessoa(Integer.parseInt(b.getCdPessoa()));
					deparaben.setCod_pessoa(Integer.parseInt(ret.getEntidadeId()));
					deparaben.setCd_contrato(ret.getContrato());
					deparaben.setTimestamp(new Date());
					
					deParaOcupacionalDAO.incluiDePara(deparaben);
					
				}else {
			        	System.out.println(resposta.getEntity().toString());
			        	response.getWriter().println("Retono da MV:" +resposta.getEntity().toString());
			        	response.getWriter().println("==============================================");
			        	response.getWriter().println(Util.geraJsonBeneficiario(b,Operacoes.SALVAR));
			        	response.getWriter().println("==============================================");
		        }	
				
			}	
		}else{
		
		
			Integer q = Integer.valueOf(request.getParameter("q"));
			Integer c = 0;
			
			List<BeneficiarioOcupacional> beneficiarios = cac.getBeneficiariosOcupacional();
	
			for (BeneficiarioOcupacional b : beneficiarios) {
			    c++;	
				Response resposta = beneficiarioWS.incluir(Util.geraJsonBeneficiario(b,Operacoes.SALVAR));
		
				Gson gson = new Gson();
				Retorno ret = gson.fromJson(resposta.getEntity().toString(), Retorno.class);
				
				if (ret.getStatus().equalsIgnoreCase("200")) {
		
					System.out.println("Id pessoa: "+b.getCdPessoa()+", Usuario MV: "+ret.getEntidadeId());
					response.getWriter().println("Id pessoa: "+b.getCdPessoa()+", Usuario MV: "+ret.getEntidadeId());
					
					MvCacDeParaBeneficiarioOcupacional deparaben = new MvCacDeParaBeneficiarioOcupacional();
					deparaben.setId_pessoa(Integer.parseInt(b.getCdPessoa()));
					deparaben.setCod_pessoa(Integer.parseInt(ret.getEntidadeId()));
					deparaben.setCd_contrato(ret.getContrato());
					deparaben.setTimestamp(new Date());
					
					deParaOcupacionalDAO.incluiDePara(deparaben);
					
		        }else{
		        	System.out.println(resposta.getEntity().toString());
		        	response.getWriter().println("Retono da MV:" +resposta.getEntity().toString());
		        	response.getWriter().println("==============================================");
		        	response.getWriter().println(Util.geraJsonBeneficiario(b,Operacoes.SALVAR));
		        	response.getWriter().println("==============================================");
		        	
		        }
			  
				if (q > 0){
					if (c >= q){
						break;
					}
				}
				
			}
			
		}
		
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
