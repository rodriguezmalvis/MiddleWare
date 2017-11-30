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
import br.org.cac.cacmvmiddleware.dao.MvCacDeParaPrestadorDAO;
import br.org.cac.cacmvmiddleware.modelo.MvCacDeParaPrestador;
import br.org.cac.cacmvmiddleware.modelo.Operacoes;
import br.org.cac.cacmvmiddleware.modelo.Prestador;
import br.org.cac.cacmvmiddleware.modelo.Retorno;
import br.org.cac.cacmvmiddleware.services.PrestadorWebServices;
import br.org.cac.cacmvmiddleware.util.Util;

/**
 * Servlet implementation class CarregaPrestadores
 */
@WebServlet("/CarregaPrestadores")
public class CarregaPrestadores extends HttpServlet {
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
    public CarregaPrestadores() {
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
				
				Response resposta = prestadorWS.incluir(Util.geraJsonPrestador(p,Operacoes.SALVAR));

				Gson gson = new Gson();
				Retorno ret = gson.fromJson(resposta.getEntity().toString(), Retorno.class);
				
				if (ret.getStatus().equalsIgnoreCase("200")) {

					System.out.println("Id Prestador: "+p.getId_prestador()+", Usuario MV: "+ret.getEntidadeId());
					response.getWriter().println("Id Prestador: "+p.getId_prestador()+", Usuario MV: "+ret.getEntidadeId());
					
					MvCacDeParaPrestador deparaprest = new MvCacDeParaPrestador();
					deparaprest.setId_prestador(p.getId_prestador());
					deparaprest.setCod_prestador(Integer.parseInt(ret.getEntidadeId()));
					deparaprest.setTimestamp(new Date());

					mvCacDeParaPrestadorDAO.incluiDePara(deparaprest);
					
				}else if (ret.getStatus().equalsIgnoreCase("500")) {
					if (resposta.getEntity().toString().indexOf("CD_INTERNO") > 0){	
							Integer pos = resposta.getEntity().toString().indexOf("=>");
							Integer pos2 = resposta.getEntity().toString().indexOf("-",pos);
							String entidadeId = resposta.getEntity().toString().substring(pos+3, pos2);
							
							if (mvCacDeParaPrestadorDAO.getMvCacDeParaPrestadorPorIdPrestador(p.getId_prestador()) == null){
								MvCacDeParaPrestador deparaprest = new MvCacDeParaPrestador();
								deparaprest.setId_prestador(p.getId_prestador());
								deparaprest.setCod_prestador(Integer.parseInt(entidadeId.trim()));
								deparaprest.setTimestamp(new Date());
								
								mvCacDeParaPrestadorDAO.incluiDePara(deparaprest);
								
								System.out.println(resposta.getEntity().toString());
					        	response.getWriter().println("==============================================");
					        	response.getWriter().println("Entidade ID encontrada e gravada no DexPara:" +entidadeId.trim());
					        	response.getWriter().println("==============================================");
							}else{
								response.getWriter().println("==============================================");
					        	response.getWriter().println("Entidade ID encontrada já existe no DexPara:" +entidadeId.trim());
					        	response.getWriter().println("==============================================");
							}	
							
					}else {
			        	System.out.println(resposta.getEntity().toString());
			        	response.getWriter().println("Retono da MV:" +resposta.getEntity().toString());
			        	response.getWriter().println("==============================================");
			        	response.getWriter().println(Util.geraJsonPrestador(p,Operacoes.SALVAR));
			        	response.getWriter().println("==============================================");
		        	}	
				}
			}	
		}else{
		
			Integer q = Integer.valueOf(request.getParameter("q"));
			Integer c = 0;
			
			List<Prestador> prestadores = cac.getPrestadores();
	
			for (Prestador p : prestadores) {
				c++;
				
				Response resposta = prestadorWS.incluir(Util.geraJsonPrestador(p,Operacoes.SALVAR));
	
				Gson gson = new Gson();
				Retorno ret = gson.fromJson(resposta.getEntity().toString(), Retorno.class);
				
				if (ret.getStatus().equalsIgnoreCase("200")) {
	
					System.out.println("Id Prestador: "+p.getId_prestador()+", Usuario MV: "+ret.getEntidadeId());
					response.getWriter().println("Id Prestador: "+p.getId_prestador()+", Usuario MV: "+ret.getEntidadeId());
					
					MvCacDeParaPrestador deparaprest = new MvCacDeParaPrestador();
					deparaprest.setId_prestador(p.getId_prestador());
					deparaprest.setCod_prestador(Integer.parseInt(ret.getEntidadeId()));
					deparaprest.setTimestamp(new Date());

					mvCacDeParaPrestadorDAO.incluiDePara(deparaprest);
					
				}else if (ret.getStatus().equalsIgnoreCase("500")) {
					if (resposta.getEntity().toString().indexOf("CD_INTERNO") > 0){	
							Integer pos = resposta.getEntity().toString().indexOf("=>");
							Integer pos2 = resposta.getEntity().toString().indexOf("-",pos);
							String entidadeId = resposta.getEntity().toString().substring(pos+3, pos2);
							
							if (mvCacDeParaPrestadorDAO.getMvCacDeParaPrestadorPorIdPrestador(p.getId_prestador()) == null){
								MvCacDeParaPrestador deparaprest = new MvCacDeParaPrestador();
								deparaprest.setId_prestador(p.getId_prestador());
								deparaprest.setCod_prestador(Integer.parseInt(entidadeId.trim()));
								deparaprest.setTimestamp(new Date());
								
								mvCacDeParaPrestadorDAO.incluiDePara(deparaprest);
								
								System.out.println(resposta.getEntity().toString());
					        	response.getWriter().println("==============================================");
					        	response.getWriter().println("Entidade ID encontrada e gravada no DexPara:" +entidadeId.trim());
					        	response.getWriter().println("==============================================");
							}else{
								response.getWriter().println("==============================================");
					        	response.getWriter().println("Entidade ID encontrada já existe no DexPara:" +entidadeId.trim());
					        	response.getWriter().println("==============================================");
							}		
					}else{
			        	System.out.println(resposta.getEntity().toString());
			        	response.getWriter().println("Retono da MV:" +resposta.getEntity().toString());
			        	response.getWriter().println("==============================================");
			        	response.getWriter().println(Util.geraJsonPrestador(p,Operacoes.SALVAR));
			        	response.getWriter().println("==============================================");
			        }
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
