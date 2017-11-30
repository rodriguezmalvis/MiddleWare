package br.org.cac.integrador.jacksonutil;

import java.text.SimpleDateFormat;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class ObjectMapperFactory {

	/**
	 * Cria um {@link ObjectMapper} com as configurações padrão.
	 * @return Um {@code ObjectMapper} com as configurações padrão.
	 */
	public static ObjectMapper getObjectMapper(){	
		return new ObjectMapper();
	}
	
	/**
	 * Cria um {@link ObjectMapper} com alguns parâmetros já pré-definidos.
	 * Esses parâmetros são:
	 * 
	 * <ul>
	 * <li> {@link SerializationFeature#INDENT_OUTPUT}: para geração de JSON formatado.
	 * Ativo se {@code prettyPrint} for {@code true};</li>
	 * <li> {@link JsonGenerator.Feature#WRITE_NUMBERS_AS_STRINGS}: para que os 
	 * números sejam gerados como strings.</li>
	 * <li> {@link JsonInclude.Include#NON_NULL}: para que os valores {@code null} 
	 * não sejam incluídos no JSON.
	 * <li> {@link ObjectMapper#setDateFormat(java.text.DateFormat)} configurado com 
	 * o formato {@code dd/MM/yyyy}.</li>
	 * @param prettyPrint indica se os JSONs gerados devem ser formatados ou não.
	 * @return um {@code ObjectMapper} com as configurações definidas acima.
	 * @see ObjectMapper#enable(SerializationFeature, SerializationFeature...)
	 * @see ObjectMapper#enable(JsonGenerator.Feature...)
	 */
	public static ObjectMapper getDefaultConfiguredObjectMapper(boolean prettyPrint){
		ObjectMapper mapper = new ObjectMapper();
		
		if (prettyPrint){
			mapper.enable(SerializationFeature.INDENT_OUTPUT);
		}
		// TODO: Não funciona no contexto do WildFly 10.0: mapper.registerModule(new JavaTimeModule());
		mapper.enable(JsonGenerator.Feature.WRITE_NUMBERS_AS_STRINGS);
		mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		mapper.setDateFormat(new SimpleDateFormat("dd/MM/yyyy"));
		
		return mapper;
	}
}
