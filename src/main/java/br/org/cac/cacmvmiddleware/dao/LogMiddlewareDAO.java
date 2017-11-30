package br.org.cac.cacmvmiddleware.dao;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.Transactional;

import br.org.cac.cacmvmiddleware.modelo.LogMiddleware;
import br.org.cac.cacmvmiddleware.modelo.LogWebservice;

@Transactional
public class LogMiddlewareDAO {

	@Inject
	EntityManager manager;

	public void incluiLog(LogMiddleware log) {
		manager.persist(log);
	}

	public LogWebservice getWebservice(int id) {
		LogWebservice logWebservice;
		logWebservice = manager.find(LogWebservice.class, id);
		return logWebservice;
	}

	public LogMiddleware getUltimoLog404() {
		LogMiddleware logMiddleware = new LogMiddleware();
		Query query = manager.createQuery("select log from LogMiddleware log where log.statusCode = 404 order by log.id desc");
		try {
			logMiddleware = (LogMiddleware) query.setMaxResults(1).getSingleResult();;
			return logMiddleware;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
