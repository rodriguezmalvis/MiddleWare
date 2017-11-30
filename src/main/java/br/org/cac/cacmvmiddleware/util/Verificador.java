package br.org.cac.cacmvmiddleware.util;

public class Verificador {

	public void verificaData(String dia, String mes, String ano) throws Exception {

		if (dia.trim().length() < 2 || mes.trim().length() < 2 || ano.trim().length() < 4) {
			throw new IllegalArgumentException("Data Informada Inválida, Formato Correto Deve Ser: dd/mm/aaaa");
		}

		if (dia.trim().length() > 2 || mes.trim().length() > 2 || ano.trim().length() > 4) {
			throw new IllegalArgumentException("Data Informada Inválida, Formato Correto Deve Ser: dd/mm/aaaa");
		}
	}
}
