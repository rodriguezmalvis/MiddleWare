package br.org.cac.cacmvmiddleware.modelo;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "VWCARGAINICIALMVBENEFICIARIOSOCUPACIONAL")
@Table(name = "VW_CARGA_INICIAL_MV_BENEFICIARIOS_OCUPACIONAL")
@Entity
public class BeneficiarioOcupacional implements Beneficiario{
	
	
	@Id
	private String cd_pessoa;
	private String nome;
	private String nome_mae;
	private String d_estado_civil;
	private String d_sexo;
	private String dt_nascimento;
	private String dt_inscricao;
	private String carteira_titular;
	private String parentesco;
	private String nome_pai;
	private String cep;
	private String numero_telefone_1;
	private String numero_identidade;
	private String orgao_identidade;
	private String cpf;
	private String descricao;
	private String bairro;
	private String id_plano;
	private String cidade;
	private String id_sigla_estado;
	private String tipo_usuario;
	private String id_banco;
	private String nome_agencia;
	private String id_agencia;
	private String conta_corrente;
	private String carteira;
	private String id_cidade;
	private String porta;
	private String desc_complemento1;
	private String cco;
	private String dv_cco;
	private String geraBoletoProprio;
	private String id_natureza;
	private String celular_1;
	private String forma_pagamento;
	private String dia_vencimento;
    private String cns;
    private String ordem_endereco;
    private String numero_comando_titular;
    private String id_empresa;
    private String sn_ativo;
    private String cdMatricula;
    private String enviado;
    private String id;
    
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getNome_mae() {
		return nome_mae;
	}
	public void setNome_mae(String nome_mae) {
		this.nome_mae = nome_mae;
	}
	public String getD_estado_civil() {
		return d_estado_civil;
	}
	public void setD_estado_civil(String d_estado_civil) {
		this.d_estado_civil = d_estado_civil;
	}
	public String getD_sexo() {
		return d_sexo;
	}
	public void setD_sexo(String d_sexo) {
		this.d_sexo = d_sexo;
	}
	public String getDt_nascimento() {
		return dt_nascimento;
	}
	public void setDt_nascimento(String dt_nascimento) {
		this.dt_nascimento = dt_nascimento;
	}
	public String getDt_inscricao() {
		return dt_inscricao;
	}
	public void setDt_inscricao(String dt_inscricao) {
		this.dt_inscricao = dt_inscricao;
	}
	public String getCarteira_titular() {
		return carteira_titular;
	}
	public void setCarteira_titular(String carteira_titular) {
		this.carteira_titular = carteira_titular;
	}
	public String getParentesco() {
		return parentesco;
	}
	public void setParentesco(String parentesco) {
		this.parentesco = parentesco;
	}
	public String getNome_pai() {
		return nome_pai;
	}
	public void setNome_pai(String nome_pai) {
		this.nome_pai = nome_pai;
	}
	public String getCep() {
		return cep;
	}
	public void setCep(String cep) {
		this.cep = cep;
	}
	public String getNumero_telefone_1() {
		return numero_telefone_1;
	}
	public void setNumero_telefone_1(String numero_telefone_1) {
		this.numero_telefone_1 = numero_telefone_1;
	}
	public String getNumero_identidade() {
		return numero_identidade;
	}
	public void setNumero_identidade(String numero_identidade) {
		this.numero_identidade = numero_identidade;
	}
	public String getOrgao_identidade() {
		return orgao_identidade;
	}
	public void setOrgao_identidade(String orgao_identidade) {
		this.orgao_identidade = orgao_identidade;
	}
	public String getCpf() {
		return cpf;
	}
	public void setCpf(String cpf) {
		this.cpf = cpf;
	}
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	public String getBairro() {
		return bairro;
	}
	public void setBairro(String bairro) {
		this.bairro = bairro;
	}
	public String getId_plano() {
		return id_plano;
	}
	public void setId_plano(String id_plano) {
		this.id_plano = id_plano;
	}
	public String getCidade() {
		return cidade;
	}
	public void setCidade(String cidade) {
		this.cidade = cidade;
	}
	public String getId_sigla_estado() {
		return id_sigla_estado;
	}
	public void setId_sigla_estado(String id_sigla_estado) {
		this.id_sigla_estado = id_sigla_estado;
	}
	public String getTipo_usuario() {
		return tipo_usuario;
	}
	public void setTipo_usuario(String tipo_usuario) {
		this.tipo_usuario = tipo_usuario;
	}
	public String getId_banco() {
		return id_banco;
	}
	public void setId_banco(String id_banco) {
		this.id_banco = id_banco;
	}
	public String getNome_agencia() {
		return nome_agencia;
	}
	public void setNome_agencia(String nome_agencia) {
		this.nome_agencia = nome_agencia;
	}
	public String getId_agencia() {
		return id_agencia;
	}
	public void setId_agencia(String id_agencia) {
		this.id_agencia = id_agencia;
	}
	public String getConta_corrente() {
		return conta_corrente;
	}
	public void setConta_corrente(String conta_corrente) {
		this.conta_corrente = conta_corrente;
	}
	public String getCarteira() {
		return carteira;
	}
	public void setCarteira(String carteira) {
		this.carteira = carteira;
	}
	public String getId_cidade() {
		return id_cidade;
	}
	public void setId_cidade(String id_cidade) {
		this.id_cidade = id_cidade;
	}
	public String getPorta() {
		return porta;
	}
	public void setPorta(String porta) {
		this.porta = porta;
	}
	public String getDesc_complemento1() {
		return desc_complemento1;
	}
	public void setDesc_complemento1(String desc_complemento1) {
		this.desc_complemento1 = desc_complemento1;
	}
	public String getCco() {
		return cco;
	}
	public void setCco(String cco) {
		this.cco = cco;
	}
	public String getDv_cco() {
		return dv_cco;
	}
	public void setDv_cco(String dv_cco) {
		this.dv_cco = dv_cco;
	}
	public String getGeraBoletoProprio() {
		return geraBoletoProprio;
	}
	public void setGeraBoletoProprio(String geraBoletoProprio) {
		this.geraBoletoProprio = geraBoletoProprio;
	}
	public String getId_natureza() {
		return id_natureza;
	}
	public void setId_natureza(String id_natureza) {
		this.id_natureza = id_natureza;
	}
	public String getCdPessoa() {
		return cd_pessoa;
	}
	public void setCdPessoa(String cd_pessoa) {
		this.cd_pessoa = cd_pessoa;
	}
	public String getCelular_1() {
		return celular_1;
	}
	public void setCelular_1(String celular_1) {
		this.celular_1 = celular_1;
	}
	public String getForma_pagamento() {
		return forma_pagamento;
	}
	public void setForma_pagamento(String forma_pagamento) {
		this.forma_pagamento = forma_pagamento;
	}
	public String getDia_vencimento() {
		return dia_vencimento;
	}
	public void setDia_vencimento(String dia_vencimento) {
		this.dia_vencimento = dia_vencimento;
	}
	public String getCns() {
		return cns;
	}
	public void setCns(String cns) {
		this.cns = cns;
	}
	public String getOrdem_endereco() {
		return ordem_endereco;
	}
	public void setOrdem_endereco(String ordem_endereco) {
		this.ordem_endereco = ordem_endereco;
	}
	
	public String getNumero_comando_titular() {
		return numero_comando_titular;
	}
	public void setNumero_comando_titular(String numero_comando_titular) {
		this.numero_comando_titular = numero_comando_titular;
	}
	public String getId_empresa() {
		return id_empresa;
	}
	public void setId_empresa(String id_empresa) {
		this.id_empresa = id_empresa;
	}
	public BeneficiarioOcupacional() {
		super();
	}
	@Override
	public String getSn_ativo() {
		return sn_ativo;
	}
	@Override
	public void setSn_ativo(String sn_ativo) {
		this.sn_ativo = sn_ativo;
		
	}
	@Override
	public String getCdMatricula() {
		return cdMatricula;
	}
	@Override
	public void setCdMatricula(String cdMatricula) {
		this.cdMatricula = cdMatricula;
	}	
	@Override
	public String getEnviado() {
		return enviado;
	}
	@Override
	public void setEnviado(String enviado) {
		this.enviado = enviado;
		
	}
	@Override
	public String getId() {
		return id;
	}
	@Override
	public void setId(String id) {
		this.id = id;
		
	}


}
