package br.org.cac.cacmvmiddleware.util;

import org.apache.commons.mail.SimpleEmail;

public class Mail {

	public void enviarEmail(String textoEmail) throws Exception {

		System.out.println("======== Enviando Email - " + textoEmail + " ========");
		SimpleEmail email = new SimpleEmail();
		email.setHostName("smtp.cac.org.br");
		email.setAuthentication("tiss@cac.org.br", "Nuro3194");
		email.setSmtpPort(587);
		email.setFrom("tiss@cac.org.br", "CacMvMiddleware");
		email.addTo("sistemas@cac.org.br");
		email.addCc("suporte@cac.org.br");
		email.addCc("detin@cac.org.br");
		email.addCc("suporte.cac@maino.com.br");
		email.setSubject("CACMVMiddleware - SoulMV Fora do Ar");
		email.setMsg("Prezados,\n" + textoEmail);
		email.setSocketConnectionTimeout(10000);
		email.setSocketTimeout(10000);
		email.setCharset("UTF-8");
		email.send();
		System.out.println("======== Email Enviado ========");
	}
}
