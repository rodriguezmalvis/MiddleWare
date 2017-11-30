package br.org.cac.integrador.modelo;

public enum TipoRubricaCac {
	CONTRIBUICAO("C"),
	PARTICIPACAO("P"),
	REMOCAO("R"),
	OUTROS("O");
	
	private String valor;
	
	private TipoRubricaCac(String valor){
		this.valor = valor;
	}

	public String getValor() {
		return valor;
	}	
	
	public static TipoRubricaCac fromValue(String value) {
		switch (value) {
			case "C":
				return CONTRIBUICAO;
			case "P":
				return PARTICIPACAO;
			case "R":
				return REMOCAO;
			case "O":
				return OUTROS;
			default:
				throw new IllegalArgumentException(value + " não é um valor válido para o Enum TipoRubricaCac!");
		}
	}
}
