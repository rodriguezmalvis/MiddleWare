package br.org.cac.cacmvmiddleware.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import br.org.cac.cacmvmiddleware.modelo.Beneficiario;
import br.org.cac.cacmvmiddleware.modelo.Endereco;
import br.org.cac.cacmvmiddleware.modelo.Especialidade;
import br.org.cac.cacmvmiddleware.modelo.Operacoes;
import br.org.cac.cacmvmiddleware.modelo.Prestador;

public class Util {

	public static String retorno;

	public static String geraJsonBeneficiario(Beneficiario b, Operacoes op) {

		String[] agencia = new String[2];
		String[] conta = new String[2];
		String nomeAgenciaTratado;
		String orgaoExpedidorTratado;
		String nomePai;
		String nomeMae;

		if (b.getNome_pai() != null && b.getNome_pai().length() > 39) {
			nomePai = b.getNome_pai().substring(0, 39).trim();
		} else {
			nomePai = b.getNome_pai();
		}

		if (b.getNome_mae() != null && b.getNome_mae().length() > 19) {
			nomeMae = b.getNome_mae().substring(0, 19).trim();
		} else {
			nomeMae = b.getNome_mae();
		}

		if (b.getId_agencia() != null) {
			agencia = b.getId_agencia().split("-");
		} else {
			agencia[0] = null;
			agencia[1] = null;
		}

		if (b.getConta_corrente() != null) {
			conta = b.getConta_corrente().split("-");
		} else {
			conta[0] = null;
			conta[1] = null;
		}

		if (b.getNome_agencia() != null && b.getNome_agencia().length() > 19) {
			nomeAgenciaTratado = b.getNome_agencia().substring(0, 19);
		} else {
			nomeAgenciaTratado = b.getNome_agencia();
		}

		if (b.getOrgao_identidade() != null && b.getOrgao_identidade().length() > 9) {
			orgaoExpedidorTratado = b.getOrgao_identidade().substring(0, 9);
		} else {
			orgaoExpedidorTratado = b.getOrgao_identidade();
		}

		retorno = "{";

		if (op == Operacoes.ALTERAR) {
			retorno = retorno + " \"cdMatricula\" : \"" + b.getCdMatricula() + "\",";
		}

		retorno = retorno + " \"nmSegurado\" : \"" + b.getNome().trim().replace("\"", "").replace("'", "") + "\","
				+ " \"nmMae\": \"" + nomeMae + "\"," + " \"tpEstadoCivil\": \"" + b.getD_estado_civil() + "\","
				+ " \"tpSexo\": \"" + b.getD_sexo() + "\",";
		if (b.getDt_nascimento() != null) {
			retorno = retorno + " \"dtNascimento\" : \"" + b.getDt_nascimento() + "\",";
		}

		retorno = retorno + " \"dtCadastro\" : \"" + b.getDt_inscricao() + "\"," + " \"cdMotDesligamento \": null,"
				+ " \"nrCns\": \"" + b.getCns() + "\",";

		if (b.getTipo_usuario().equalsIgnoreCase("D")) {
			retorno = retorno + " \"cdMatriculaTem\" : \"" + b.getCarteira_titular().trim() + "\",";
		}
		if (b.getTipo_usuario().equalsIgnoreCase("D")) {
			retorno = retorno + " \"cdParentesco\" : \"" + b.getParentesco().trim() + "\",";
		} else {
			retorno = retorno + " \"cdParentesco\" : null,";
		}

		retorno = retorno + " \"nmPai\" :\"" + nomePai + "\"," + " \"nmConjuge\" : null,";
		if (b.getCep() != null) {
			retorno = retorno + " \"nrCep\":\"" + b.getCep().trim() + "\",";
		} else {
			retorno = retorno + " \"nrCep\":null,";
		}
		if (b.getNumero_telefone_1() != null) {
			retorno = retorno + " \"nrTelefone\" :\"" + b.getNumero_telefone_1().trim().replace("-", "") + "\",";
		} else {
			retorno = retorno + " \"nrTelefone\" :null,";
		}
		if (b.getNumero_identidade() != null) {
			retorno = retorno + " \"nrRg\" : \"" + b.getNumero_identidade().trim() + "\"," + " \"dsOrgaoExpeditor\" :\""
					+ orgaoExpedidorTratado + "\",";
		}
		if (b.getCpf() != null && !b.getCpf().equalsIgnoreCase("")) {
			retorno = retorno + " \"nrCpf\" :\"" + b.getCpf().trim() + "\",";
		} else {
			retorno = retorno + " \"nrCpf\" :null,";
		}

		if (b.getDescricao() != null) {
			retorno = retorno + " \"dsEndereco\" :\"" + b.getDescricao().trim() + "\",";
		} else {
			retorno = retorno + " \"dsEndereco\" :null,";
		}
		if (b.getBairro() != null && !b.getBairro().trim().equalsIgnoreCase("")) {
			retorno = retorno + " \"dsBairro\" :\"" + b.getBairro().trim() + "\",";
		} else {
			retorno = retorno + " \"dsBairro\" :\"SEM BAIRRO\",";
		}

		retorno = retorno + " \"snAtivo\" :\"" + b.getSn_ativo() + "\"," + " \"cdPlano\" :\"" + b.getId_plano() + "\",";

		if (b.getCidade() != null) {
			retorno = retorno + " \"nmCidade\" :\"" + b.getCidade().trim() + "\",";
		} else {
			retorno = retorno + " \"nmCidade\" :null,";
		}
		if (b.getId_sigla_estado() != null) {
			retorno = retorno + " \"nmUf\" :\"" + b.getId_sigla_estado().trim() + "\",";
		} else {
			retorno = retorno + " \"nmUf\" :null,";
		}

		retorno = retorno + " \"tpUsuario\" :\"" + b.getTipo_usuario() + "\",";

		if (b.getId_banco() != null) {
			retorno = retorno + " \"cdBanco\" :\"" + b.getId_banco().trim() + "\",";
		} else {
			retorno = retorno + " \"cdBanco\" : 999,";
		}
		if (b.getId_agencia() != null) {
			retorno = retorno + " \"nmAgencia\" :\"" + nomeAgenciaTratado + "\",";
		} else {
			retorno = retorno + " \"nmAgencia\" :null,";
		}
		if (b.getId_agencia() != null) {
			retorno = retorno + " \"nrAgencia\" :\"" + agencia[0].trim() + "\",";
		} else {
			retorno = retorno + " \"nrAgencia\" :9999,";
		}
		try {
			if (agencia[1] != null) {
				retorno = retorno + " \"DvAgencia\" :\"" + agencia[1].trim() + "\",";
			} else {
				retorno = retorno + " \"DvAgencia\" : null,";
			}
		} catch (Exception e) {
			retorno = retorno + " \"DvAgencia\" : null,";
		}
		if (b.getConta_corrente() != null) {
			retorno = retorno + " \"nrConta\" :\"" + conta[0].trim() + "\",";
		} else {
			retorno = retorno + " \"nrConta\" : 9999,";
		}
		try {
			if (conta[1] != null) {
				retorno = retorno + " \"DvContaCorrente\" :\"" + conta[1].trim() + "\",";
			} else {
				retorno = retorno + " \"DvContaCorrente\" : null,";
			}
		} catch (Exception e) {
			retorno = retorno + " \"DvContaCorrente\" : null,";
		}

		retorno = retorno + " \"salario\" :null," + " \"cdMatAlternativa\" :\"" + b.getCarteira().trim() + "\","
				+ " \"dtAdmissao\" :null," + " \"cdCidade\" :\"261160\",";

		if (b.getPorta() != null) {
			retorno = retorno + " \"nrEndereco\" :\"" + b.getPorta().trim() + "\",";
		} else {
			retorno = retorno + " \"nrEndereco\" :null,";
		}
		if (b.getDesc_complemento1() != null) {
			retorno = retorno + " \"dsComplemento\" :\"" + b.getDesc_complemento1().trim() + "\",";
		} else {
			retorno = retorno + " \"dsComplemento\" :null,";
		}

		retorno = retorno + " \"dtCadastroSistema\" :\"" + b.getDt_inscricao() + "\"," + " \"dsEmail\" : null,"
				+ " \"dtUltimoReajuste\" : null," + " \"dtProximoReajuste\": null," + " \"cdMultiEmpresa\": 1,"
				+ " \"cdCco\" :\"" + b.getCco() + "\"," + " \"nrDivCco\" :\"" + b.getDv_cco() + "\","
				+ " \"cdMatriculaAns\" : null," + " \"snGerarBoletoProprio\" :\"" + b.getGeraBoletoProprio() + "\","
				+ " \"dsSenha\": \"de4b4a681901d9f074c4487409e27da2\"," + " \"dtDesligamentoProgramada\": null,"
				+ " \"nmNaturalidade\": null," + " \"cdNacionalidade\": null,";

		if (b.getId_natureza() != null && !b.getId_natureza().trim().equalsIgnoreCase("")) {
			retorno = retorno + " \"tpLogradouro\" :\""
					+ b.getId_natureza().trim().toUpperCase().replace("Ã", "A") + "\",";
		} else {
			retorno = retorno + " \"tpLogradouro\" :\"RUA\",";
		}

		retorno = retorno + " \"nrDdd\" : null,";

		if (b.getCelular_1() != null && !b.getCelular_1().equalsIgnoreCase("")) {
			retorno = retorno + " \"nrCelular\" :\"" + b.getCelular_1().trim().replace("-", "") + "\",";
		} else {
			retorno = retorno + " \"nrCelular\" :null,";
		}

		retorno = retorno + " \"tpFormaPagamento\" :\"" + b.getForma_pagamento() + "\"," + " \"nrDiaVencimento\" :\""
				+ b.getDia_vencimento() + "\",";

		if (b.getNumero_comando_titular() != null) {
			retorno = retorno + " \"nrComando\" :\"" + b.getNumero_comando_titular() + "\",";
		} else {
			retorno = retorno + " \"nrComando\" : null,";
		}

		if (b.getId_empresa() != null) {
			retorno = retorno + " \"cdEmpresaParceira\" :\"" + b.getId_empresa() + "\"}";
		} else {
			retorno = retorno + " \"cdEmpresaParceira\" : null }";
		}

		// System.out.println(retorno);

		return retorno;
	}

	public static String geraJsonPrestador(Prestador p, Operacoes op) {

		String[] agencia = new String[2];
		String[] conta = new String[2];
		int contador = 0;
		int quantidade = 0;

		if (p.getId_agencia() != null) {
			agencia = p.getId_agencia().trim().split("-");
		} else {
			agencia[0] = null;
			agencia[1] = null;
		}

		if (p.getNumero() != null) {
			conta = p.getNumero().trim().split("-");
		} else {
			conta[0] = null;
			conta[1] = null;
		}

		retorno = "{";

		if (op == Operacoes.ALTERAR) {
			retorno = retorno + " \"cdPrestador\" : \"" + p.getCdPrestador() + "\",";
		}

		retorno = retorno + " \"nmPrestador\" : \"" + p.getNome().trim().replace("\"", "").replace("'", "") + "\","
				+ " \"nmGuerra\" : \"" + p.getNome().trim().replace("\"", "").replace("'", "") + "\","
				+ " \"tpSituacao\" : \"" + p.getSituacao() + "\"," + " \"cdInterno\" :  \"" + p.getId_prestador()
				+ "\",";
		if (p.getNome_fantasia() != null) {
			retorno = retorno + " \"nmFantasia\" : \"" + p.getNome_fantasia().trim().replace("\"", "").replace("'", "")
					+ "\",";
		} else {
			retorno = retorno + " \"nmFantasia\" : null,";
		}
		retorno = retorno + " \"cdGrupoPrestador\" : 1," + " \"cdTipPrestador\" : \"" + p.getTipo_prestador() + "\","
				+ " \"tpPrestador\" : \"" + p.getD_tipo_pessoa() + "\"," + " \"nrCpfCgc\" : \"" + p.getCpf_cgc()
				+ "\",";

		if (p.getD_tipo_pessoa().equalsIgnoreCase("F")) {
			retorno = retorno + " \"ufConselho\" : \"RJ\"," + " \"dsCodConselho\" : \"" + p.getRegistro_conselho()
					+ "\"," + " \"cdConselhoProfissional\" : 6,";
			if (p.getInss() != null)
				retorno = retorno + " \"nrInscricaoinss\" : \"" + p.getInss() + "\",";
		}

		if (p.getCnes() != null) {
			retorno = retorno + " \"cdCnes\" : \"" + p.getCnes() + "\",";
		} else {
			retorno = retorno + " \"cdCnes\":\"9999999\",";
		}
		retorno = retorno + " \"tpCredenciamento\" : \"" + p.getD_tipo_prestador() + "\"," + " \"snExecutor\" : \"S\",";
		if (p.getDt_nascimento() != null) {
			retorno = retorno + " \"dtNascimento\" : \"" + p.getDt_nascimento() + "\",";
		} else {
			retorno = retorno + " \"dtNascimento\" : null,";
		}

		retorno = retorno + " \"cdMultiEmpresa\": 1,";

		if (p.getId_banco() != null) {
			retorno = retorno + " \"cdBanco\" :\"" + p.getId_banco() + "\",";
		} else {
			retorno = retorno + " \"cdBanco\" : 999,";
		}

		if (p.getId_agencia() != null) {
			retorno = retorno + " \"cdAgencia\" :\"" + agencia[0] + "\",";
		} else {
			retorno = retorno + " \"cdAgencia\" :9999,";
		}

		try {
			if (agencia[1] != null) {
				retorno = retorno + " \"dvAgencia\" :\"" + agencia[1].trim() + "\",";
			}
		} catch (Exception e) {
			// não faço nada
		}

		if (p.getNumero() != null) {
			retorno = retorno + " \"nrConta\" :\"" + conta[0] + "\",";
		} else {
			retorno = retorno + " \"nrConta\" : 9999,";
		}

		try {
			if (conta[1] != null) {
				retorno = retorno + " \"dvContaCorrente\" :\"" + conta[1].trim() + "\",";
			} else {
				retorno = retorno + " \"dvContaCorrente\" : null,";
			}
		} catch (Exception e) {
			// nao faço nada
		}

		retorno = retorno + " \"dtCadastro\" : \"" + p.getDt_cadastramento() + "\"";

		if (p.getEndereco() != null) {
			quantidade = p.getEndereco().size();
			if (quantidade > 0) {
				contador = 0;
				retorno = retorno + ",\"enderecos\":[";
				for (Endereco e : p.getEndereco()) {
					retorno = retorno + "{\"cdInterno\":\"" + p.getId_prestador() + "\"," + " \"cdTipoEndereco\":\""
							+ e.getTipo_endereco() + "\"," + " \"cdMunicipio\":\"" + e.getId_cidade() + "\","
							+ " \"cdUf\":\"" + e.getId_sigla_uf() + "\"," + " \"dsMunicipio\":\"" + e.getCidade().trim()
							+ "\"," + " \"nrCep\":\"" + e.getCep().trim() + "\"," + " \"dsEndereco\":\""
							+ e.getEndereco().trim().replace("\\", "/") + "\",";
					if (e.getPorta() != null && !e.getPorta().trim().equalsIgnoreCase("")) {
						retorno = retorno + " \"nrEndereco\":\"" + e.getPorta().trim().replace("-", "") + "\",";
					} else {
						retorno = retorno + " \"nrEndereco\":\"0\",";
					}

					retorno = retorno + " \"dsBairro\":\"" + e.getBairro().trim() + "\",";

					if (e.getCnes() != null) {
						retorno = retorno + " \"cdCnes\":\"" + e.getCnes() + "\",";
					} else {
						retorno = retorno + " \"cdCnes\":\"9999999\",";
					}
					retorno = retorno + " \"snPrincipal\":\"" + e.getPrincipal() + "\"}";
					contador++;
					if (contador < quantidade) {
						retorno = retorno + ",";
					}
				}
				retorno = retorno + "]";
			}
		}

		if (p.getEspecialidade() != null) {
			quantidade = p.getEspecialidade().size();
			if (quantidade > 0) {
				contador = 0;
				retorno = retorno + ",\"especialidades\":[";
				for (Especialidade esp : p.getEspecialidade()) {
					retorno = retorno + "{\"cdInterno\":\"" + p.getId_prestador() + "\"," + " \"cdEspecialidade\":\""
							+ esp.getId_especialidade() + "\"," + " \"snPrincipal\":\"" + esp.getPrincipal() + "\"}";
					contador++;
					if (contador < quantidade) {
						retorno = retorno + ",";
					}

				}
				retorno = retorno + "]";
			}
		}
		retorno = retorno + "}";

		// System.out.println(retorno);

		return retorno;

	}

	public static String geraJsonCancelamentoPrestador(Prestador p) {

		retorno = "{" + " \"cdPrestador\" : \"" + p.getCdPrestador() + "\"," + " \"cdMotDesligamento\" : \""
				+ p.getCdMotivoDesligamento() + "\",";

		if (p.getSituacao().equalsIgnoreCase("I")) {
			retorno = retorno + " \"dtDesligamento\" : \"" + p.getDtStatus() + "\",";
		} else {
			retorno = retorno + " \"dtDesligamento\" : null, ";
		}

		/*
		 * if (p.getSituacao().equalsIgnoreCase("A")){ retorno =
		 * retorno+" \"dtReativacao\" : \""+p.getDtStatus() +"\""; }else{
		 */
		retorno = retorno + " \"dtReativacao\" : null ";
		// }

		retorno = retorno + "}";

		// System.out.println(retorno);

		return retorno;
	}

	public static String formataData(Date data) {
		final SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		return sdf.format(data);
	}
	
	/**
	 * Verifica se a diferença entre duas datas recebidas como parâmetro estão dentro ou fora
	 * de um intervalo definido.
	 * As datas não precisam estar em ordem, pois a diferença é calculada pelo valor absoluto.
	 * @param dataInicial a primeira data a ser comparada.
	 * @param dataFinal a segunda data a ser comparada.
	 * @param intervalo o valor intervalo que servirá de base para comparação.
	 * @param unidade um {@link TimeUnit} que corresponde à unidade de tempo que servirá de base 
	 * para a comparação.
	 * @param menor indica se a comparação entre as datas deve indicar se o {@code intervalo} é
	 * menor (se for {@code true}) ou maior (se for {@code false}) do que o intervalo informado.
	 * @return {@code true}, se as datas são menores ou maiores que o intervalo informado, {@code false}
	 * caso contrário.
	 * @throws NullPointerException se {@code dataInicial}, {@code dataFinal} ou {@code unidade}
	 * forem null.
	 */
	public static boolean testIntervaloDatas(Date dataInicial, Date dataFinal, long intervalo, TimeUnit unidade, boolean menor) {
		Objects.requireNonNull(dataInicial);
		Objects.requireNonNull(dataFinal);
		Objects.requireNonNull(unidade);
		
		long duracao = Math.abs(TimeUnit.MILLISECONDS.convert(dataInicial.getTime(), unidade) - 
				TimeUnit.MILLISECONDS.convert(dataFinal.getTime(), unidade));
		
		return (menor ? duracao <= intervalo : duracao >= intervalo);		
	}

	public static boolean isMaior20Minutos(Date dataLog){
		return testIntervaloDatas(new Date(), dataLog, 20, TimeUnit.MINUTES, false);
	}
}
