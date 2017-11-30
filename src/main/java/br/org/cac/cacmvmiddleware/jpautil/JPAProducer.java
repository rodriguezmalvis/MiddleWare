package br.org.cac.cacmvmiddleware.jpautil;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Default;
import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import br.org.cac.cacmvmiddleware.annotations.SoulMV;

@ApplicationScoped
public class JPAProducer {
	
	@PersistenceContext(unitName = "LogUnit")
	private EntityManager em;
	
	@PersistenceContext(unitName = "SoulMV")
	private EntityManager soulMVem;
 
    @RequestScoped
    @Produces
    @Default
    public EntityManager createEntityManager() {
        return em;
    }
    
    @RequestScoped
    @Produces
    @SoulMV
    public EntityManager createSoulMvEntityManager() {
        return soulMVem;
    }    

}
