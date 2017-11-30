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

import com.fasterxml.jackson.databind.ObjectMapper;

import br.org.cac.cacmvmiddleware.dao.LogMiddlewareDAO;
import br.org.cac.cacmvmiddleware.dao.MvCacDeParaPrestadorDAO;
import br.org.cac.cacmvmiddleware.dao.MvCacDeParaPrestadorEnderecoDAO;
import br.org.cac.cacmvmiddleware.dao.MvCacDeParaPrestadorEspecialidadeDAO;
import br.org.cac.cacmvmiddleware.modelo.MvCacDeParaPrestador;
import br.org.cac.cacmvmiddleware.modelo.MvCacDeParaPrestadorEnderecos;
import br.org.cac.cacmvmiddleware.modelo.MvCacDeParaPrestadorEspecialidades;
import br.org.cac.cacmvmiddleware.modelo.retorno.Endereco;
import br.org.cac.cacmvmiddleware.modelo.retorno.Especialidade;
import br.org.cac.cacmvmiddleware.modelo.retorno.RetornoPrestador;
import br.org.cac.cacmvmiddleware.services.PrestadorWebServices;

/**
 * Servlet implementation class CarregaPrestadores
 */
@WebServlet("/CarregaEnderecoEspecialidade")
public class CarregaEnderecoEspecialidade extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	@Inject
	LogMiddlewareDAO dao;
	
	@Inject
	MvCacDeParaPrestadorDAO deParaDAO;
	
	@Inject
	MvCacDeParaPrestadorEnderecoDAO deParaEnderecosDAO;
	
	@Inject
	MvCacDeParaPrestadorEspecialidadeDAO deParaEspecialidadeDAO;
	
	@Inject
	PrestadorWebServices prestadorWS;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CarregaEnderecoEspecialidade() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		Integer q = Integer.valueOf(request.getParameter("q"));
		Integer c = 0;
		
		List<MvCacDeParaPrestador> deParaPrestadores = deParaDAO.getTodosMvCacDeParaPrestador();

		for (MvCacDeParaPrestador dePara : deParaPrestadores) {
			c++;
			
			ObjectMapper mapper = new ObjectMapper();
			
			Response responsePrestador = prestadorWS.getprestador(dePara.getCod_prestador());
			RetornoPrestador retPrestador =  mapper.readValue(responsePrestador.getEntity().toString(), RetornoPrestador.class);
			
			
			if (retPrestador.getStatus().equalsIgnoreCase("200")) {

				if (retPrestador.getPrestador() != null){
					
					
					if (retPrestador.getPrestadorEndereco() != null){
						for (Endereco e : retPrestador.getPrestadorEndereco()){
							if (deParaEnderecosDAO.getMvCacDeParaPrestadorPorIdPrestador(dePara.getId_prestador(), e.getId()) == null){
								System.out.println("Gravei o DexPara do Endereco com o Prestador: "+dePara.getId_prestador()+", ID Endereco Prestador: null, Prestador SoulMV: "+dePara.getCod_prestador()+", Endereco Prestador SOUL: "+e.getId());
								response.getWriter().println("Gravei o DexPara do Endereco com o Prestador: "+dePara.getId_prestador()+", ID Endereco Prestador: null, Prestador SoulMV: "+dePara.getCod_prestador()+", Endereco Prestador SOUL: "+e.getId());
								
								MvCacDeParaPrestadorEnderecos deParaEnderecos = new MvCacDeParaPrestadorEnderecos();
								deParaEnderecos.setId_prestador(dePara.getId_prestador());
								deParaEnderecos.setId_endereco_prestador(null);
								deParaEnderecos.setCod_prestador_endereco(e.getId());
								deParaEnderecos.setTimestamp(new Date());
								
								deParaEnderecosDAO.incluiDePara(deParaEnderecos);
							
							}else{
								System.out.println("Endereco já cadastrado no DexPara");
								response.getWriter().println("Endereco já cadastrado no DexPara");
							}	
						}
					}else{
						System.out.println("Prestador sem informação de endereço: "+dePara.getCod_prestador());
						response.getWriter().println("Prestador sem informação de endereço: "+dePara.getCod_prestador());
					}
					
					if (retPrestador.getPrestadorEspecialidade() != null){
						for (Especialidade esp : retPrestador.getPrestadorEspecialidade()){
							if (deParaEspecialidadeDAO.getMvCacDeParaPrestadorPorIdPrestador(dePara.getId_prestador(), esp.getId().getEspecialidade()) == null){
								System.out.println("Gravei o DexPara da Especialidade com o Prestador: "+dePara.getId_prestador()+", ID Especialidade: null, Cod Especialidade: "+esp.getId().getEspecialidade()+", Prestador SoulMV: "+dePara.getCod_prestador());
								response.getWriter().println("Gravei o DexPara da Especialidade com o Prestador: "+dePara.getId_prestador()+", ID Especialidade: null,  Cod Especialidade: "+esp.getId().getEspecialidade()+", Prestador SoulMV: "+dePara.getCod_prestador());
								
								MvCacDeParaPrestadorEspecialidades deParaEspecialidades = new MvCacDeParaPrestadorEspecialidades();
								
								deParaEspecialidades.setCd_especialidade(esp.getId().getEspecialidade());
								deParaEspecialidades.setId_prestador(dePara.getId_prestador());
								deParaEspecialidades.setCod_prestador(dePara.getCod_prestador());
								deParaEspecialidades.setTimestamp(new Date());
								
								deParaEspecialidadeDAO.incluiDePara(deParaEspecialidades);
							
							}else{
								System.out.println("Especialidade já cadastrada no DexPara");
								response.getWriter().println("Especialidade já cadastrada no DexPara");
							}	
						}
					}else{
						System.out.println("Prestador sem informação de especialidades: "+dePara.getCod_prestador());
						response.getWriter().println("Prestador sem informação de especialidades: "+dePara.getCod_prestador());
					}

						
				}
				
				
	        }else{
	        	System.out.println(responsePrestador.getEntity().toString());
	        	
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
