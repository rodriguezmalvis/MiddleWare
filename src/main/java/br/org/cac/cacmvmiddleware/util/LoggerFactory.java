package br.org.cac.cacmvmiddleware.util;

import java.util.regex.Pattern;

import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;

import org.jboss.logging.Logger;

/**
 * Classe {@code factory} para a injeção de {@link Logger}s em outras classes.
 * 
 * @author JCJ
 * @version 1.2
 * @since 1.2, 2017-04-24
 */
public class LoggerFactory {
	
	private final String REGEX = "(([a-zA-Z0-9])[a-zA-Z0-9]+\\.)";
	private final String REPLACEMENT = "$2\\.";
	
	/**
	 * Cria um {@link Logger}, baseado no ponto de injeção do campo 
	 * correspondente ({@link InjectionPoint}).
	 *  
	 * @param injectionPoint referência para o ponto de injeção do campo
	 * @return um {@code Logger} configurado com o nome da classe que está injetando
	 * o campo
	 * @see Logger#getLogger(Class)
	 * @see javax.inject.Inject
	 */
	@Produces
	public Logger createLogger(InjectionPoint injectionPoint) {
		return Logger.getLogger(injectionPoint.getMember().getDeclaringClass());
	}
	
	/**
	 * Cria um {@link Logger}, baseado no ponto de injeção do campo 
	 * correspondente ({@link InjectionPoint}), com o nome do pacote
	 * da classe em questão abreviado (apenas a primeira letra de cada
	 * trecho do pacote).
	 *  
	 * @param injectionPoint referência para o ponto de injeção do campo
	 * @return um {@code Logger} configurado com o nome da classe que está injetando
	 * o campo, contendo o nome do pacote da classe em questão abreviado.
	 * @see Logger#getLogger(Class)
	 * @see javax.inject.Inject
	 */	
	public Logger createShortenedLogger(InjectionPoint injectionPoint) {	
		return Logger.getLogger(Pattern
				.compile(REGEX)
				.matcher(injectionPoint
						.getMember()
						.getDeclaringClass()
						.getCanonicalName())
				.replaceAll(REPLACEMENT));
	}

}
