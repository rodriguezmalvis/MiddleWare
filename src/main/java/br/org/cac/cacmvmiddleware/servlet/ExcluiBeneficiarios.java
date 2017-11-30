   package br.org.cac.cacmvmiddleware.servlet;

import java.io.IOException;
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
import br.org.cac.cacmvmiddleware.dao.MvCacDeParaBeneficiarioAssistencialDAO;
import br.org.cac.cacmvmiddleware.modelo.MvCacDeParaBeneficiarioAssistencial;
import br.org.cac.cacmvmiddleware.modelo.Retorno;
import br.org.cac.cacmvmiddleware.services.BeneficiarioWebServices;


/**
 * Servlet implementation class CarregaBeneficiarios
 */
@WebServlet("/ExcluiBeneficiarios")
public class ExcluiBeneficiarios extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	@Inject
	CacDAO cac;
	
	@Inject
	LogMiddlewareDAO dao;
	
	@Inject
	MvCacDeParaBeneficiarioAssistencialDAO deParaAssistencialDAO;
	
	@Inject
	BeneficiarioWebServices beneficiarioWS;
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ExcluiBeneficiarios() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		Integer q = Integer.valueOf(request.getParameter("q"));
		Integer c = 0;
		
		List<MvCacDeParaBeneficiarioAssistencial> listDeParaBeneficiarios = cac.getDeParaBeneficiarios();
		
		
		for (MvCacDeParaBeneficiarioAssistencial b : listDeParaBeneficiarios) {
		  c++;	
		    
		  Response resposta = beneficiarioWS.delete(b.getCod_pessoa());
	
		  Gson gson = new Gson();
		  Retorno ret = gson.fromJson(resposta.getEntity().toString(), Retorno.class);
		
		  if (ret.getStatus().equalsIgnoreCase("200")) {
	
			deParaAssistencialDAO.removeMvCacDeParaBeneficiario(b);
			
			System.out.println("Id pessoa: "+b.getCod_pessoa()+", Usuario MV: "+ret.getEntidadeId());
			response.getWriter().println("EXCLUÍDO: Id pessoa: "+b.getId_pessoa()+", Usuario MV: "+ret.getEntidadeId());
			
	      }else{
	    	System.out.println(resposta.getEntity().toString());
	    	response.getWriter().println("Retono da MV:" +resposta.getEntity().toString());
	    	response.getWriter().println("==============================================");
	    	response.getWriter().println("Cod_Pessoa: "+b.getCod_pessoa());
	    	response.getWriter().println("==============================================");
	      }
			   
		  
		  if (q > 0){
			if (c >= q){
				break;
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
