package br.org.cac.cacmvmiddleware.servlet;

import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Response;
import com.google.gson.Gson;
import br.org.cac.cacmvmiddleware.dao.CacDAO;
import br.org.cac.cacmvmiddleware.modelo.BeneficiarioAssistencial;
import br.org.cac.cacmvmiddleware.modelo.Operacoes;
import br.org.cac.cacmvmiddleware.modelo.Retorno;
import br.org.cac.cacmvmiddleware.services.BeneficiarioWebServices;
import br.org.cac.cacmvmiddleware.util.Util;

/**
 * Servlet implementation class CarregaPrestadores
 */
@WebServlet("/AlteraBeneficiariosAssistencial")
public class AlteraBeneficiariosAssistencial extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AlteraBeneficiariosAssistencial() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		CacDAO cac = new CacDAO();
		
		if (request.getParameter("idPessoa") != null){
			
			String idPessoa = request.getParameter("idPessoa");
			
			BeneficiarioAssistencial b = cac.getBeneficiarioAssistencial(idPessoa);
			
			if (b != null){
				if ((b.getDia_vencimento() == null) && (b.getForma_pagamento().equalsIgnoreCase("F") || b.getForma_pagamento().equalsIgnoreCase("U"))){
					b.setDia_vencimento("5");
				}	
				
			  	BeneficiarioWebServices beneficiarioWS = new BeneficiarioWebServices();
				
				Response resposta = beneficiarioWS.alterar(Util.geraJsonBeneficiario(b,Operacoes.ALTERAR));
	
				Gson gson = new Gson();
				Retorno ret = gson.fromJson(resposta.getEntity().toString(), Retorno.class);
				
				if (ret.getStatus().equalsIgnoreCase("200")) {
	
					System.out.println("Id Pessoa: "+b.getCdPessoa()+", Usuario MV: "+ret.getEntidadeId());
					response.getWriter().println("Id Pessoa: "+b.getCdPessoa()+", Usuario MV: "+ret.getEntidadeId());
					
		        }else{
		        	System.out.println(resposta.getEntity().toString());
		        	response.getWriter().println("Retono da MV:" +resposta.getEntity().toString());
		        	response.getWriter().println("==============================================");
		        	response.getWriter().println(Util.geraJsonBeneficiario(b,Operacoes.ALTERAR));
		        	response.getWriter().println("==============================================");
		        }		        	
			}
		}else{
			Integer q = Integer.valueOf(request.getParameter("q"));
			Integer c = 0;
			
			
			List<BeneficiarioAssistencial> beneficiarios = cac.getBeneficiarioAssistencialAlteracao();
	
			for (BeneficiarioAssistencial b : beneficiarios) {
				c++;
				
				if ((b.getDia_vencimento() == null) && (b.getForma_pagamento().equalsIgnoreCase("F") || b.getForma_pagamento().equalsIgnoreCase("U"))){
					b.setDia_vencimento("5");
				}
				
				BeneficiarioWebServices beneficiarioWS = new BeneficiarioWebServices();
				
				Response resposta = beneficiarioWS.alterar(Util.geraJsonBeneficiario(b,Operacoes.ALTERAR));
	
				Gson gson = new Gson();
				Retorno ret = gson.fromJson(resposta.getEntity().toString(), Retorno.class);
				
				if (ret.getStatus().equalsIgnoreCase("200")) {
	
					System.out.println("Id Pessoa: "+b.getCdPessoa()+", Usuario MV: "+ret.getEntidadeId());
					response.getWriter().println("Id Pessoa: "+b.getCdPessoa()+", Usuario MV: "+ret.getEntidadeId());
					
		        }else{
		        	System.out.println(resposta.getEntity().toString());
		        	response.getWriter().println("Retono da MV:" +resposta.getEntity().toString());
		        	response.getWriter().println("==============================================");
		        	response.getWriter().println(Util.geraJsonBeneficiario(b,Operacoes.ALTERAR));
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
