package br.org.cac.integrador.modelo;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class FormaPagamentoConverter implements AttributeConverter<FormaPagamento, String>{

	@Override
	public String convertToDatabaseColumn(FormaPagamento attribute) {
		if (attribute == null){
			return null;
		}
		
		return attribute.getValor();
	}

	@Override
	public FormaPagamento convertToEntityAttribute(String dbData) {
		if (dbData == null){
			return null;
		}
		
		return FormaPagamento.fromValue(dbData);
	}
	

}
