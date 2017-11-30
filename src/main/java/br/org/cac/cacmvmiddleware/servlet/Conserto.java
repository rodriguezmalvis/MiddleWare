package br.org.cac.cacmvmiddleware.servlet;

import java.io.IOException;
import java.util.Date;

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
 * Servlet implementation class Concerto
 */
@WebServlet("/Conserto")
public class Conserto extends HttpServlet {
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
	public Conserto() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());

		Integer[] array = {

				85276, 87208, 85233, 87207, 86381, 85423, 86151, 87274, 77382, 56275, 85482, 85537, 85220, 86057, 85421,
				87318, 86042, 86064, 86326, 85238, 85240, 85596, 85237, 85269, 85963, 86052, 87179, 86166, 85914, 62796,
				87146, 55848, 85539, 86217, 87211, 86778, 86129, 85256, 72760, 85992, 86290, 29527, 59314, 85710, 86168,
				85333, 87045, 85277, 85337, 86161, 85991, 87163, 85916, 58844, 85718, 86149, 86186, 86607, 85711, 87035,
				85248, 85243, 85534, 70162, 24686, 54991, 86228, 86359, 47366, 87144, 86277, 86160, 66171, 85244, 85307,
				87023, 85458, 85223, 85577, 86457, 86977, 60710, 86868, 51637, 85538, 85281, 85815, 42413, 85278, 86131,
				22110, 85232, 86182, 86563, 85255, 87024, 29380, 86603, 85259, 86181, 86063, 57588, 86809, 70053, 85838,
				86415

		};

		for (Integer id : array) {

			try {

				System.out.println("!!!!!! TENTANDO PEGAR O ID: " + id);
				Prestador p = cac.getPrestador(id);
				System.out.println("!!!!!! ENCONTRADO ID: " + id + " PRESTADOR: " + p.getNome());
				if (p != null) {

					Response resposta = prestadorWS.incluir(Util.geraJsonPrestador(p, Operacoes.SALVAR));

					Gson gson = new Gson();
					Retorno ret = gson.fromJson(resposta.getEntity().toString(), Retorno.class);

					if (ret.getStatus().equalsIgnoreCase("200")) {

						System.out.println(
								"Id Prestador: " + p.getId_prestador() + ", Usuario MV: " + ret.getEntidadeId());
						response.getWriter().println(
								"Id Prestador: " + p.getId_prestador() + ", Usuario MV: " + ret.getEntidadeId());

						MvCacDeParaPrestador deparaprest = new MvCacDeParaPrestador();
						deparaprest.setId_prestador(p.getId_prestador());
						deparaprest.setCod_prestador(Integer.parseInt(ret.getEntidadeId()));
						deparaprest.setTimestamp(new Date());

						mvCacDeParaPrestadorDAO.incluiDePara(deparaprest);

					} else if (ret.getStatus().equalsIgnoreCase("500")) {
						if (resposta.getEntity().toString().indexOf("CD_INTERNO") > 0) {
							Integer pos = resposta.getEntity().toString().indexOf("=>");
							Integer pos2 = resposta.getEntity().toString().indexOf("-", pos);
							String entidadeId = resposta.getEntity().toString().substring(pos + 3, pos2);

							if (mvCacDeParaPrestadorDAO
									.getMvCacDeParaPrestadorPorIdPrestador(p.getId_prestador()) == null) {
								MvCacDeParaPrestador deparaprest = new MvCacDeParaPrestador();
								deparaprest.setId_prestador(p.getId_prestador());
								deparaprest.setCod_prestador(Integer.parseInt(entidadeId.trim()));
								deparaprest.setTimestamp(new Date());

								mvCacDeParaPrestadorDAO.incluiDePara(deparaprest);

								System.out.println(resposta.getEntity().toString());
								response.getWriter().println("==============================================");
								response.getWriter().println("Entidade ID encontrada e gravada no DexPara:"
										+ entidadeId.trim() + "Id Prestador Informado para Inclusão: " + id);
								response.getWriter().println("==============================================");
							} else {
								response.getWriter().println("==============================================");
								response.getWriter().println("Entidade ID encontrada já existe no DexPara:"
										+ entidadeId.trim() + "Id Prestador Informado para Inclusão: " + id);
								response.getWriter().println("==============================================");
							}

						} else {
							System.out.println(resposta.getEntity().toString());
							response.getWriter().println("Retono da MV:" + resposta.getEntity().toString());
							response.getWriter().println("==============================================");
							response.getWriter().println(Util.geraJsonPrestador(p, Operacoes.SALVAR));
							response.getWriter().println("==============================================");
						}
					}
				}
				System.out.println("!!!!!! Finalizado ID: " + id + " PRESTADOR: " + p.getNome());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}
}
