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
import br.org.cac.cacmvmiddleware.dao.MvCacDeParaBeneficiarioAssistencialDAO;
import br.org.cac.cacmvmiddleware.dao.MvCacDeParaBeneficiarioOcupacionalDAO;
import br.org.cac.cacmvmiddleware.modelo.MvCacDeParaBeneficiarioAssistencial;
import br.org.cac.cacmvmiddleware.modelo.MvCacDeParaBeneficiarioOcupacional;
import br.org.cac.cacmvmiddleware.modelo.retorno.Beneficiario;
import br.org.cac.cacmvmiddleware.modelo.retorno.RetornoBeneficiario;
import br.org.cac.cacmvmiddleware.services.BeneficiarioWebServices;


/**
 * Servlet implementation class CarregaPrestadores
 */
@WebServlet("/CarregaContratos")
public class CarregaContratos extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	@Inject
	LogMiddlewareDAO dao;
	
	@Inject
	MvCacDeParaBeneficiarioAssistencialDAO deParaAssistencialDAO;
	
	@Inject
	MvCacDeParaBeneficiarioOcupacionalDAO deParaOcupacionalDAO;
	
	@Inject
	BeneficiarioWebServices beneficiarioWS;
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CarregaContratos() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		
		Integer q = Integer.valueOf(request.getParameter("q"));
		String tipo = request.getParameter("tipo");
		Integer c = 0;
		
		
		if (tipo.equalsIgnoreCase("A")){
			List<MvCacDeParaBeneficiarioAssistencial> deParaBeneficiariosAssistencial = deParaAssistencialDAO.getTodosMvCacDeParaBeneficiariosAssistencial();
	
			for (MvCacDeParaBeneficiarioAssistencial dePara : deParaBeneficiariosAssistencial) {
				c++;
				
				ObjectMapper mapper = new ObjectMapper();
				
				Response responseBeneficiario = beneficiarioWS.getBeneficiario(dePara.getCod_pessoa());
				RetornoBeneficiario retBeneficiario =  mapper.readValue(responseBeneficiario.getEntity().toString(), RetornoBeneficiario.class);
				
				
				if (retBeneficiario.getStatus().equalsIgnoreCase("200")) {
	
					if (retBeneficiario.getBeneficiario() != null){
						
						Beneficiario b = retBeneficiario.getBeneficiario();
						
						MvCacDeParaBeneficiarioAssistencial deParaBeneficiarioAssistencial = new MvCacDeParaBeneficiarioAssistencial();
						deParaBeneficiarioAssistencial.setId_pessoa(dePara.getId_pessoa());
						deParaBeneficiarioAssistencial.setCd_contrato(b.getCdContrato());
						deParaBeneficiarioAssistencial.setCod_pessoa(dePara.getCod_pessoa());
						deParaBeneficiarioAssistencial.setTimestamp(new Date());
						deParaAssistencialDAO.atualizaMvCacDeParaBeneficiario(deParaBeneficiarioAssistencial);
						
						System.out.println("Gravei o Cd_Contrato: "+b.getCdContrato()+" para a Cd Matricula "+dePara.getCod_pessoa()+", Id Pessoa:"+dePara.getId_pessoa());
						response.getWriter().println("Gravei o Cd_Contrato: "+b.getCdContrato()+" para a Cd Matricula "+dePara.getCod_pessoa()+", Id Pessoa:"+dePara.getId_pessoa());
							
					}
					
		        }else{
		        	System.out.println(responseBeneficiario.getEntity().toString());
		        	
		        }		        	
		        
				if (q > 0){
					if (c >= q){
						break;
					}
				}
			}
		}else if (tipo.equalsIgnoreCase("O")){
			List<MvCacDeParaBeneficiarioOcupacional> deParaBeneficiariosOcupacional = deParaOcupacionalDAO.getTodosMvCacDeParaBeneficiariosOcupacional();
			
			for (MvCacDeParaBeneficiarioOcupacional dePara : deParaBeneficiariosOcupacional) {
				c++;
				
				ObjectMapper mapper = new ObjectMapper();
				
				Response responseBeneficiario = beneficiarioWS.getBeneficiario(dePara.getCod_pessoa());
				RetornoBeneficiario retBeneficiario =  mapper.readValue(responseBeneficiario.getEntity().toString(), RetornoBeneficiario.class);
				
				
				if (retBeneficiario.getStatus().equalsIgnoreCase("200")) {
	
					if (retBeneficiario.getBeneficiario() != null){
						
						Beneficiario b = retBeneficiario.getBeneficiario();
						
						MvCacDeParaBeneficiarioOcupacional deParaBeneficiarioOcupacional = new MvCacDeParaBeneficiarioOcupacional();
						deParaBeneficiarioOcupacional.setId(dePara.getId());
						deParaBeneficiarioOcupacional.setId_pessoa(dePara.getId_pessoa());
						deParaBeneficiarioOcupacional.setCd_contrato(b.getCdContrato());
						deParaBeneficiarioOcupacional.setCod_pessoa(dePara.getCod_pessoa());
						deParaBeneficiarioOcupacional.setTimestamp(new Date());
						
						deParaOcupacionalDAO.alteraMvCacDeParaBeneficiario(deParaBeneficiarioOcupacional);
						
						System.out.println("Gravei o Cd_Contrato: "+b.getCdContrato()+" para a Cd Matricula "+dePara.getCod_pessoa()+", Id Pessoa:"+dePara.getId_pessoa());
						response.getWriter().println("Gravei o Cd_Contrato: "+b.getCdContrato()+" para a Cd Matricula "+dePara.getCod_pessoa()+", Id Pessoa:"+dePara.getId_pessoa());
							
					}
					
		        }else{
		        	System.out.println(responseBeneficiario.getEntity().toString());
		        	
		        }		        	
		        
				if (q > 0){
					if (c >= q){
						break;
					}
				}
			}
			
		}else{
			
			response.getWriter().println("Tipo de Plano diferente de A e O");
			
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
