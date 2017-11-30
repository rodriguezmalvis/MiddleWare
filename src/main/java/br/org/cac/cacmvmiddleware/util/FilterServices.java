package br.org.cac.cacmvmiddleware.util;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.core.Context;
import javax.ws.rs.ext.Provider;

import org.jboss.logging.Logger;
import org.jboss.logging.Logger.Level;

@PreMatching
@Provider
public class FilterServices implements ContainerRequestFilter {
	
	private static final Logger LOGGER = Logger.getLogger(WebServiceFactory.class.getName());

	@Context
	HttpServletRequest request;
	
	@Override
	public void filter(ContainerRequestContext container) throws IOException {
		LOGGER.log(Level.INFO,"-------------- RECEBENDO REQUISIÇÃO -----------------------");
		LOGGER.log(Level.INFO,"URL: " + container.getUriInfo().getPath());
		LOGGER.log(Level.INFO,"Method: " + container.getMethod());
		LOGGER.log(Level.INFO,"IP: "+request.getRemoteAddr());
		LOGGER.log(Level.INFO,"Headers: "+container.getHeaders());
		LOGGER.log(Level.INFO,"-------------- CONTINUANDO REQUISIÇÃO -----------------------");
	}
}
