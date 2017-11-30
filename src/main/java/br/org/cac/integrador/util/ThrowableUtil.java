package br.org.cac.integrador.util;

/**
 * Classe de utilidade para facilitar determinadas operações com {@link Throwable}s.
 * 
 * @author JCJ
 * @since 1.2, 2017-04-17
 * @version 1.2
 */
public class ThrowableUtil {
	
	/**
	 * Retorna a causa raiz de um {@link Throwable}, navegando por suas possíveis
	 * causas até achar a causa origem. Se não houver uma causa raiz, o próprio {@code Throwable}
	 * é considerado como causa raiz.
	 * 
	 * @param t o {@code Throwable} que se deseja localizar a causa original. Não pode ser {@code null}.
	 * @return o {@code Throwable} que originou {@code t}, ou o próprio {@code t}, caso
	 * ele não exista.
	 * @throws IllegalArgumentException caso {@code t} seja {@code null}.
	 * @see Throwable#getCause()
	 */
	public static Throwable getRootCause(Throwable t) {
		/* Baseado em implementação disponível em http://stackoverflow.com/a/28565320 */
		if (null == t) {
			throw new IllegalArgumentException("t não pode ser null.");
		}
		
	    Throwable cause; 
	    Throwable result = t;

	    while(null != (cause = result.getCause())  && (result != cause) ) {
	        result = cause;
	    }
	    return result;
	}	
	
	/**
	 * Retorna a mensagem da causa raiz de um {@link Throwable}. Caso não haja causa raiz,
	 * é retornada a mensagem do próprio {@code Throwable}.
	 * 
	 * @param t o {@code Throwable} que se deseja localizar a mensagem da causa original.
	 * Não pode ser {@code null}.
	 * @return a mensagem do {@code Throwable} que originou {@code t}, ou a mensagem do próprio
	 * {@code t}, caso ele não exista.
	 * @see #getRootCause(Throwable)
	 * @see Throwable#getMessage()
	 */
	public static String getRootMessage(Throwable t){
		Throwable rootCause = ThrowableUtil.getRootCause(t);
		
		return rootCause.getMessage();
	}
}
