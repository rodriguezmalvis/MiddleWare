/**
 * 
 */
package br.org.cac.integrador.modelo.infraestrutura;

/**
 * @author JCJ
 * @since 2017-07-09
 */
public enum MotivoAjuste {
	
	VALOR_APRESENTADO_MENOR_QUE_PAGO("AP");

	
	private String valor;

	private MotivoAjuste(String valor) {
		this.valor = valor;
	}

	public String getValor() {
		return valor;
	}

	public static MotivoAjuste fromValue(String value) {
		switch (value) {
			case "AP":
				return VALOR_APRESENTADO_MENOR_QUE_PAGO;
			default:
				throw new IllegalArgumentException(value + " não é um valor válido para o Enum MotivoAjuste!");
		}
	}
}
