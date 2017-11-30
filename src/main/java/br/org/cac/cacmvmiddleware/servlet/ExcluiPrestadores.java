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
import br.org.cac.cacmvmiddleware.dao.MvCacDeParaPrestadorDAO;
import br.org.cac.cacmvmiddleware.modelo.MvCacDeParaPrestador;
import br.org.cac.cacmvmiddleware.modelo.Prestador;
import br.org.cac.cacmvmiddleware.modelo.Retorno;
import br.org.cac.cacmvmiddleware.services.PrestadorWebServices;
import br.org.cac.cacmvmiddleware.util.Util;


/**
 * Servlet implementation class CarregaBeneficiarios
 */
@WebServlet("/ExcluiPrestadores")
public class ExcluiPrestadores extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	@Inject
	CacDAO cac;
	
	@Inject
	LogMiddlewareDAO dao;
	
	@Inject
	MvCacDeParaPrestadorDAO mvCacDeParaPrestadorDAO;
	
	@Inject
	PrestadorWebServices prestadorWS;
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ExcluiPrestadores() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		Integer q = Integer.valueOf(request.getParameter("q"));
		Integer c = 0;
		
		List<MvCacDeParaPrestador> listDeParaPrestador = cac.getDeParaPrestadores();
		
		
		for (MvCacDeParaPrestador p : listDeParaPrestador) {
		  c++;	

		  Prestador prest = cac.getPrestador(p.getId_prestador());
		  
		  if ((prest.getSituacao().equalsIgnoreCase("I")) && (prest.getDtStatus() != null)){ 
		  
			  Response resposta = prestadorWS.desativar(Util.geraJsonCancelamentoPrestador(prest));
		
			  Gson gson = new Gson();
			  Retorno ret = gson.fromJson(resposta.getEntity().toString(), Retorno.class);
			
			  if (ret.getStatus().equalsIgnoreCase("200")) {
				
				mvCacDeParaPrestadorDAO.removeDePara(p);
				
				System.out.println("Id pessoa: "+p.getCod_prestador()+", Usuario MV: "+ret.getEntidadeId());
				response.getWriter().println("DESATIVA: Id Prestador: "+p.getId_prestador()+", Prestador MV: "+ret.getEntidadeId());
				
		      }else{
		    	System.out.println(resposta.getEntity().toString());
		    	response.getWriter().println("Retono da MV:" +resposta.getEntity().toString());
		    	response.getWriter().println("==============================================");
		    	response.getWriter().println("Cod_Prestador: "+p.getCod_prestador());
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
