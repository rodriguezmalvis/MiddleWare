package br.org.cac.integrador.util;

import java.util.Calendar;
import java.util.Date;

/**
 * Classe de utilidade, que contém algumas constantes e métodos para gerar dados constantes.
 * 
 * @author JCJ
 * @since 2017-07-22
 */
public class Constantes {
	public static final Integer PRESTADOR_PADRAO = 62779;
	
	public static final Character TIPO_GUIA_MENSALIDADE_COPARTICIPACAO_PADRAO = 'A';
	
	public static final String CNES_NAO_INFORMADO = "9999999";
	
	public static final Integer PROCEDIMENTO_PADRAO = 50010000;

	public static Date getDataRealizacaoPadrao() {
		Calendar calendar = Calendar.getInstance();
		
		calendar.clear();
		calendar.set(2017, 0, 31);
		
		return calendar.getTime();
	}
}
