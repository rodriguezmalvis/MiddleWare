package br.org.cac.integrador.executaveis;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.org.cac.integrador.jacksonutil.ObjectMapperFactory;
import br.org.cac.integrador.modelo.ItemMensalidadeUsuario;
import br.org.cac.integrador.modelo.MensalidadeContrato;

//@WebServlet("/testeJson")
public class TesteJson extends HttpServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4252831914304803622L;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		PrintWriter pw = resp.getWriter();
		
		ObjectMapper mapper = ObjectMapperFactory.getDefaultConfiguredObjectMapper(true);
		
		/*PeriodoReferencia per = new PeriodoReferencia();
		
		Calendar calendar = Calendar.getInstance();
		calendar.clear();
		
		calendar.set(2017, 0, 1);
		Date dtReferencia = calendar.getTime();
		Date dtInicial = calendar.getTime();
		
		calendar.set(2017, 0, 31);
		Date dtFinal = calendar.getTime();
		
		per.setDtReferencia(dtReferencia);
		per.setDtInicial(dtInicial);
		per.setDtFinal(dtFinal);
		
		pw.println(mapper.writeValueAsString(per));
		
		String json = "{\"dtReferencia\" : \"01/01/2017\",\"dtInicial\" : \"01/01/2017\",\"dtFinal\" : \"31/01/2017\"}";
		
		PeriodoReferencia novo = mapper.readValue(json, PeriodoReferencia.class);*/
		
		MensalidadeContrato novo = new MensalidadeContrato();
		Calendar calendar = Calendar.getInstance();
		calendar.clear();
		
		novo.setCdContrato(24558);
		novo.setNrParcela(1);
		novo.setCdMensContratoInterno(13);
		novo.setCdMultiEmpresa(4);
		novo.setDsObservacao("[MV SAÚDE - NOSSO NÚMERO IMPORTADO: 263814602]");
		
		calendar.set(2011, Calendar.NOVEMBER, 20);
		novo.setDtEmissao(calendar.getTime());
		
		calendar.set(2011, Calendar.DECEMBER, 20);
		novo.setDtVencimento(calendar.getTime());
		
		calendar.set(2011, Calendar.DECEMBER, 10);
		novo.setDtVencimentoOriginal(calendar.getTime());
		
		novo.setNrAno(2017);
		novo.setNrMes(9);
		novo.setTpQuitacao("A");
		novo.setVlJurosMora(0.33);
		novo.setVlMensalidade(146.48);
		novo.setVlPago(146.48);
		novo.setVlPercentualMulta(2.0);
		novo.setItensMensalidadeUsuario(new ArrayList<>());
		
		ItemMensalidadeUsuario itemMensalidade = new ItemMensalidadeUsuario();
		itemMensalidade.setCdMatricula(49660152);
		itemMensalidade.setCdMensContratoInterno(13);
		itemMensalidade.setCdLctoMensalidade(604);
		itemMensalidade.setVlLancamento(140.1);
		
		novo.getItensMensalidadeUsuario().add(itemMensalidade);
		
		
		pw.println(mapper.writeValueAsString(novo));
		
		
	}

}
