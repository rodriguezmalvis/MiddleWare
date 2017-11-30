package br.org.cac.cacmvmiddleware.servlet;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import javax.ws.rs.core.Response;
import com.google.gson.Gson;
import br.org.cac.cacmvmiddleware.dao.CacDAO;
import br.org.cac.cacmvmiddleware.dao.LogMiddlewareDAO;
import br.org.cac.cacmvmiddleware.modelo.Operacoes;
import br.org.cac.cacmvmiddleware.modelo.Prestador;
import br.org.cac.cacmvmiddleware.modelo.Retorno;
import br.org.cac.cacmvmiddleware.services.PrestadorWebServices;
import br.org.cac.cacmvmiddleware.util.Util;

/**
 * Servlet implementation class CarregaPrestadores
 */

@WebServlet("/AlteraPrestadores")
public class AlteraPrestadores extends HttpServlet {
	private static final long serialVersionUID = 1L;
      
	@Inject
	CacDAO cac;
	
	@Inject
	LogMiddlewareDAO dao;
	
	@Inject
	PrestadorWebServices prestadorWS;
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AlteraPrestadores() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		if (request.getParameter("idPrestador") != null){
			
			Integer idPrestador = Integer.valueOf(request.getParameter("idPrestador"));
			
			Prestador p = cac.getPrestador(idPrestador);
			
			if (p != null){
				
				Response resposta = prestadorWS.alterar(Util.geraJsonPrestador(p,Operacoes.ALTERAR));
	
				Gson gson = new Gson();
				Retorno ret = gson.fromJson(resposta.getEntity().toString(), Retorno.class);
				
				if (ret.getStatus().equalsIgnoreCase("200")) {
	
					System.out.println("Id Prestador: "+p.getId_prestador()+", Usuario MV: "+ret.getEntidadeId());
					response.getWriter().println("Id Prestador: "+p.getId_prestador()+", Usuario MV: "+ret.getEntidadeId());
					
		        }else{
		        	System.out.println(resposta.getEntity().toString());
		        	response.getWriter().println("Retono da MV:" +resposta.getEntity().toString());
		        	response.getWriter().println("==============================================");
		        	response.getWriter().println(Util.geraJsonPrestador(p,Operacoes.ALTERAR));
		        	response.getWriter().println("==============================================");
		        }		        	
			}
		}else{
			Integer q = Integer.valueOf(request.getParameter("q"));
			Integer c = 0;
			
			
			List<Prestador> prestadores = cac.getPrestadoresAlteracao();
	
			for (Prestador p : prestadores) {
				c++;
				
				Response resposta = prestadorWS.alterar(Util.geraJsonPrestador(p,Operacoes.ALTERAR));
	
				Gson gson = new Gson();
				Retorno ret = gson.fromJson(resposta.getEntity().toString(), Retorno.class);
				
				if (ret.getStatus().equalsIgnoreCase("200")) {
	
					System.out.println("Id Prestador: "+p.getId_prestador()+", Usuario MV: "+ret.getEntidadeId());
					response.getWriter().println("Id Prestador: "+p.getId_prestador()+", Usuario MV: "+ret.getEntidadeId());
					
		        }else{
		        	System.out.println(resposta.getEntity().toString());
		        	response.getWriter().println("Retono da MV:" +resposta.getEntity().toString());
		        	response.getWriter().println("==============================================");
		        	response.getWriter().println(Util.geraJsonPrestador(p,Operacoes.ALTERAR));
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
