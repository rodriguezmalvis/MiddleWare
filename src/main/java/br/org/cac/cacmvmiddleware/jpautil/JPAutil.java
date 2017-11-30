package br.org.cac.cacmvmiddleware.jpautil;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class JPAutil {

	private static EntityManagerFactory emf = null;
	
	public static EntityManager getEntitymanager(){
		
		if(emf == null){
			emf = Persistence.createEntityManagerFactory("LogUnit");
		}
		
		return emf.createEntityManager();

	}
	
	/**
	 * Método de facilidade para {@code closeEntityManager(EntityManager, boolean)}, com
	 * o parâmetro {@code rollback} definido como {@code false}.
	 * @param manager o {@link EntityManager} a ser fechado.
	 * @see #closeEntityManager(EntityManager, boolean)
	 * @author JCJ
	 * @version 1.1
	 * @since 1.1, 2017-03-29
	 * 
	 * 
	 */
	public static void closeEntityManager(EntityManager manager){
		closeEntityManager(manager, false);
	}
	
	/**
	 * Tenta fechar um {@link EntityManager}, opcionalmente tentando fazer rollback de
	 * uma possível transação ativa dele. A tentativa de fechamento sempre ocorrerá, mesmo
	 * que a tentativa de rollback, se solicitada, falhe.
	 * 
	 * @param manager o {@code EntityManager} a ser fechado.
	 * @param rollback {@code boolean} que indica se deve tentar fazer rollback de
	 * transação antes de fechar. Caso {@code manager} não esteja em transação, este
	 * parâmetro não tem efeito.
	 * @author JCJ
	 * @version 1.1
	 * @since 1.1, 2017-03-29
	 */
	public static void closeEntityManager(EntityManager manager, boolean rollback){
		if (manager != null){
			try {
				if ((rollback) && (manager.getTransaction().isActive())) {
					manager.getTransaction().rollback();
				} 
			} finally {
				manager.close();
			}
			
		}
	}
}
