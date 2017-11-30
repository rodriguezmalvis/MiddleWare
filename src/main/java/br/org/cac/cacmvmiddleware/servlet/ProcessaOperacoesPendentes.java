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
import br.org.cac.cacmvmiddleware.dao.MvCacDeParaBeneficiarioAssistencialDAO;
import br.org.cac.cacmvmiddleware.dao.SoulMvDAO;
import br.org.cac.cacmvmiddleware.modelo.AtLogMvCac;
import br.org.cac.cacmvmiddleware.modelo.BeneficiarioAssistencialTodos;
import br.org.cac.cacmvmiddleware.modelo.CepLocalidade;
import br.org.cac.cacmvmiddleware.modelo.CepLogradouro;
import br.org.cac.cacmvmiddleware.modelo.MvCacDeParaBeneficiarioAssistencial;
import br.org.cac.cacmvmiddleware.modelo.Operacoes;
import br.org.cac.cacmvmiddleware.modelo.Retorno;
import br.org.cac.cacmvmiddleware.services.BeneficiarioWebServices;
import br.org.cac.cacmvmiddleware.util.Util;

/**
 * Servlet implementation class ProcessaOperacoesPendentes
 */
@WebServlet("/ProcessaOperacoesPendentes")
public class ProcessaOperacoesPendentes extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	@Inject
	CacDAO cac;
	
	@Inject
	SoulMvDAO soulmv;
	
	@Inject
	MvCacDeParaBeneficiarioAssistencialDAO deParaAssistencialDAO;	
	
	@Inject
	LogMiddlewareDAO dao;
	
	@Inject
	BeneficiarioWebServices beneficiarioWS;	
	
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ProcessaOperacoesPendentes() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		List<AtLogMvCac> listPendentes = cac.getOperacoesPendentes();
		
		for (AtLogMvCac atLogMvCac : listPendentes) {
			Response resposta = null;
			BeneficiarioAssistencialTodos b = null;
			
			
        	if (atLogMvCac.getD_tipo_operacao().equalsIgnoreCase("I")){
				b = cac.getBeneficiarioAssistencialTodos(atLogMvCac.getId_pessoa().toString());
			
				System.out.println(atLogMvCac.getId_pessoa()+"==============================================");
				response.getWriter().println(atLogMvCac.getId_pessoa()+"==============================================");
	        	response.getWriter().println(Util.geraJsonBeneficiario(b,Operacoes.SALVAR));
	        	response.getWriter().println("==============================================");
				
				resposta = beneficiarioWS.incluir(Util.geraJsonBeneficiario(b,Operacoes.SALVAR));
			}else if (atLogMvCac.getD_tipo_operacao().equalsIgnoreCase("A")){
				b = cac.getBeneficiarioAssistencialTodos(atLogMvCac.getId_pessoa().toString());
				
				System.out.println(atLogMvCac.getId_pessoa()+"==============================================");
				response.getWriter().println(atLogMvCac.getId_pessoa()+"==============================================");
	        	response.getWriter().println(Util.geraJsonBeneficiario(b,Operacoes.ALTERAR));
	        	response.getWriter().println("==============================================");
				
				resposta = beneficiarioWS.alterar(Util.geraJsonBeneficiario(b,Operacoes.ALTERAR));
			} 
        	
        	String mensagem = resposta.getEntity().toString().substring(17, resposta.getEntity().toString().length()-13);
			
			Gson gson = new Gson();
			Retorno ret = gson.fromJson(resposta.getEntity().toString(), Retorno.class);
			
			
			if (ret.getStatus().equalsIgnoreCase("200")) {

				System.out.println("Id pessoa: "+b.getCdPessoa()+", Usuario MV: "+ret.getEntidadeId());
				response.getWriter().println("Id pessoa: "+b.getCdPessoa()+", Usuario MV: "+ret.getEntidadeId());
				
				cac.removeAtLogMvCac(atLogMvCac);
				
				if (atLogMvCac.getD_tipo_operacao().equalsIgnoreCase("I")){
					MvCacDeParaBeneficiarioAssistencial deparaben = new MvCacDeParaBeneficiarioAssistencial();
					deparaben.setId_pessoa(Integer.parseInt(b.getCdPessoa()));
					deparaben.setCod_pessoa(Integer.parseInt(ret.getEntidadeId()));
					deparaben.setCd_contrato(ret.getContrato());
					deparaben.setTimestamp(new Date());
					
					deParaAssistencialDAO.incluiDePara(deparaben);
				}	
				
			}else if (ret.getStatus().equalsIgnoreCase("500")) {
				if (resposta.getEntity().toString().indexOf("CEP") > 0){	
					
					Integer cd_localidade = soulmv.getCepLocalidadeByLocalidade(b.getCidade());
					if (cd_localidade != null){
						CepLogradouro cepLogradouro = new CepLogradouro();
						cepLogradouro.setCd_localidade(cd_localidade);
						cepLogradouro.setCd_logradouro(soulmv.getMaxCdLogradouro(cd_localidade));
						cepLogradouro.setDs_logradouro(b.getDescricao());
						cepLogradouro.setDs_complemento(b.getDesc_complemento1());
						cepLogradouro.setNr_cep(Integer.valueOf(b.getCep()));
						cepLogradouro.setCd_uf("RJ");
						cepLogradouro.setCd_bairro_final(null);
						cepLogradouro.setCd_bairro_inicial(null);
						cepLogradouro.setDs_adicional(null);
						cepLogradouro.setTp_logradouro(null);
						
						soulmv.incluiCepLogradouro(cepLogradouro);
						
					}else{
						cd_localidade = soulmv.getCepLocalidadeByCep(b.getCep());
						if (cd_localidade != null){
							
							CepLogradouro cepLogradouro = new CepLogradouro();
							cepLogradouro.setCd_localidade(cd_localidade);
							cepLogradouro.setCd_logradouro(soulmv.getMaxCdLogradouro(cd_localidade));
							cepLogradouro.setDs_logradouro(b.getDescricao());
							cepLogradouro.setDs_complemento(b.getDesc_complemento1());
							cepLogradouro.setNr_cep(Integer.valueOf(b.getCep()));
							cepLogradouro.setCd_uf("RJ");
							cepLogradouro.setCd_bairro_final(null);
							cepLogradouro.setCd_bairro_inicial(null);
							cepLogradouro.setDs_adicional(null);
							cepLogradouro.setTp_logradouro(null);
							soulmv.incluiCepLogradouro(cepLogradouro);
							
						}else{
							//Insiro na CepLocalidade
							CepLocalidade cepLocalidadeInserir = new CepLocalidade();
							Integer cdLocalidadeInserir = soulmv.getMaxCdLocalidade();
							cepLocalidadeInserir.setCd_localidade(cdLocalidadeInserir);
							cepLocalidadeInserir.setCd_localidade_origem(108930);
							cepLocalidadeInserir.setCd_municipio(null);
							cepLocalidadeInserir.setCd_uf("RJ");
							cepLocalidadeInserir.setNm_localidade(b.getDescricao());
							cepLocalidadeInserir.setNr_cep(Integer.valueOf(b.getCep()));
							cepLocalidadeInserir.setTp_atualizacao(null);
							cepLocalidadeInserir.setTp_localidade("M");
							cepLocalidadeInserir.setTp_situacao(null);
							soulmv.incluiCepLocalidade(cepLocalidadeInserir);
							
							//Insiro na CepLogradouro
							CepLogradouro cepLogradouro = new CepLogradouro();
							cepLogradouro.setCd_localidade(cepLocalidadeInserir.getCd_localidade());
							cepLogradouro.setCd_logradouro(soulmv.getMaxCdLogradouro(cdLocalidadeInserir));
							cepLogradouro.setDs_logradouro(b.getDescricao());
							cepLogradouro.setDs_complemento(b.getDesc_complemento1());
							cepLogradouro.setNr_cep(Integer.valueOf(b.getCep()));
							cepLogradouro.setCd_uf("RJ");
							cepLogradouro.setCd_bairro_final(null);
							cepLogradouro.setCd_bairro_inicial(null);
							cepLogradouro.setDs_adicional(null);
							cepLogradouro.setTp_logradouro(null);
							soulmv.incluiCepLogradouro(cepLogradouro);
						}	
						
						//Envio Novamente à MV
						if (atLogMvCac.getD_tipo_operacao().equalsIgnoreCase("I")){
							resposta = beneficiarioWS.incluir(Util.geraJsonBeneficiario(b,Operacoes.SALVAR));
						}else if (atLogMvCac.getD_tipo_operacao().equalsIgnoreCase("A")){
							resposta = beneficiarioWS.alterar(Util.geraJsonBeneficiario(b,Operacoes.ALTERAR));
						} 
						
						gson = new Gson();
						ret = gson.fromJson(resposta.getEntity().toString(), Retorno.class);
						
						
						if (ret.getStatus().equalsIgnoreCase("200")) {

							System.out.println("Id pessoa: "+b.getCdPessoa()+", Usuario MV: "+ret.getEntidadeId());
							response.getWriter().println("Id pessoa: "+b.getCdPessoa()+", Usuario MV: "+ret.getEntidadeId());
							
							cac.removeAtLogMvCac(atLogMvCac);
							
							if (atLogMvCac.getD_tipo_operacao().equalsIgnoreCase("I")){
								MvCacDeParaBeneficiarioAssistencial deparaben = new MvCacDeParaBeneficiarioAssistencial();
								deparaben.setId_pessoa(Integer.parseInt(b.getCdPessoa()));
								deparaben.setCod_pessoa(Integer.parseInt(ret.getEntidadeId()));
								deparaben.setCd_contrato(ret.getContrato());
								deparaben.setTimestamp(new Date());
								
								deParaAssistencialDAO.incluiDePara(deparaben);
							}	
							
						}else {
				        	System.out.println(resposta.getEntity().toString());
				        	response.getWriter().println("Retorno da MV:" +resposta.getEntity().toString());
				        	response.getWriter().println("==============================================");
				        	//response.getWriter().println(Util.geraJsonBeneficiario(b,Operacoes.SALVAR));
				        	//response.getWriter().println("==============================================");
				        	atLogMvCac.setResposta(mensagem);
				        	cac.atualizaAtLogMvCac(atLogMvCac);
			        	}	
					
					}
				}else if (resposta.getEntity().toString().indexOf("CD_MAT_ALTERNATIVA") > 0){

					System.out.println("Id pessoa: "+b.getCdPessoa()+", Usuario MV: "+ret.getEntidadeId());
					response.getWriter().println("Id pessoa: "+b.getCdPessoa()+", Usuario MV: "+ret.getEntidadeId());
					
					cac.removeAtLogMvCac(atLogMvCac);
					
					
			    }else {
		        	System.out.println(resposta.getEntity().toString());
		        	response.getWriter().println("Retorno da MV:" +resposta.getEntity().toString());
		        	response.getWriter().println("==============================================");
		        	//response.getWriter().println(Util.geraJsonBeneficiario(b,Operacoes.SALVAR));
		        	//response.getWriter().println("==============================================");
		        	atLogMvCac.setResposta(mensagem);
		        	cac.atualizaAtLogMvCac(atLogMvCac);
				}	
			}else {
	        	System.out.println(resposta.getEntity().toString());
	        	response.getWriter().println("Retorno da MV:" +resposta.getEntity().toString());
	        	response.getWriter().println("==============================================");
	        	//response.getWriter().println(Util.geraJsonBeneficiario(b,Operacoes.SALVAR));
	        	//response.getWriter().println("==============================================");
	        	atLogMvCac.setResposta(mensagem);
	        	cac.atualizaAtLogMvCac(atLogMvCac);
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
