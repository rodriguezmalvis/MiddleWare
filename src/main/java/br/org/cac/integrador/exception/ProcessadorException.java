package br.org.cac.integrador.exception;

import java.util.Optional;

import javax.persistence.PersistenceException;

import br.org.cac.integrador.util.ThrowableUtil;

/**
 * Classe base de exceções que podem ser lançadas pelos processadores do integrador
 * @author JCJ
 * @version 1.0
 * @since 2017-03-17
 *
 */
public class ProcessadorException extends Exception {
	
	private String customMessage = null;

	/**
	 * Número serial da classe.
	 */
	private static final long serialVersionUID = -8436450622895889578L;

	/**
	 * Construtor padrão, sem mensagem ou causa definida.
	 * 
	 * @see Exception#Exception()
	 */
	public ProcessadorException() {
		super();
	}

	/**
	 * Constrói um novo ProcessadorException, com a mensagem e o motivo informados.
	 * 
	 * @param message Mensagem relacionada a esta exceção.
	 * @param cause Motivo da exceção.
	 * @see Exception#Exception(String, Throwable)
	 */
	public ProcessadorException(String message, Throwable cause) {
		super(message, cause);
		
		definePersistenceExceptionCustomMessage(cause);
	}

	/**
	 * Constrói um novo ProcessadorException, com a mensagem informada.
	 * 
	 * @param message Mensagem relacionada a esta exceção.
	 * @see Exception#Exception(String)
	 */
	public ProcessadorException(String message) {
		super(message);
	}
	
	/**
	 * Constrói um novo ProcessadorException, com a mensagem com formato e
	 * os parâmetros preenchidos.
	 * @param message Uma mensagem com formato, contendo parâmetros para 
	 * 	formatação
	 * @param args Os valores que servirão de parâmetros para a mensagem
	 * @see String#format(String, Object...)
	 * @since 2017-06-22 
	 */
	public ProcessadorException(String message, Object... args){
		this(String.format(message, args));
	}

	/**
	 * Constrói um novo ProcessadorException, com o motivo informado.
	 * 
	 * @param cause Motivo da exceção.
	 * @see Exception#Exception(Throwable)
	 */
	public ProcessadorException(Throwable cause) {
		super(cause);
		
		definePersistenceExceptionCustomMessage(cause);
	}

	/** 
	 * Tenta definir uma mensagem personalizada de erro quando este {@code ProcessadorException}
	 * é construído com um {@link PersistenceException} como parâmetro.
	 * 
	 * @param cause Uma causa qualquer para esta exceção.
	 * @see ThrowableUtil#getRootCause(Throwable)
	 * @author JCJ
	 * @version 1.5
	 * @since 2017-06-09
	 */
	private void definePersistenceExceptionCustomMessage(Throwable cause) {
		if ( (null != cause) && (cause instanceof PersistenceException) ){
			this.customMessage = "O seguinte erro originado no banco de dados foi detectado: " + ThrowableUtil.getRootMessage(cause);
		}
	}
	
	@Override
	public String getMessage(){
        return Optional.ofNullable(customMessage).orElse(super.getMessage());
	}
	
	

}
