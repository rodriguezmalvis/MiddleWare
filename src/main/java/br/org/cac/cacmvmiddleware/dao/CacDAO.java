package br.org.cac.cacmvmiddleware.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.Transactional;

import br.org.cac.cacmvmiddleware.modelo.AtLogMvCac;
import br.org.cac.cacmvmiddleware.modelo.BeneficiarioAssistencial;
import br.org.cac.cacmvmiddleware.modelo.BeneficiarioAssistencialTodos;
import br.org.cac.cacmvmiddleware.modelo.BeneficiarioOcupacional;
import br.org.cac.cacmvmiddleware.modelo.MvCacDeParaBeneficiarioAssistencial;
import br.org.cac.cacmvmiddleware.modelo.MvCacDeParaPrestador;
import br.org.cac.cacmvmiddleware.modelo.Prestador;

@Transactional
public class CacDAO {

	@Inject
	EntityManager manager;

	@SuppressWarnings("unchecked")
	public List<BeneficiarioAssistencial> getBeneficiariosAssistencial() {
		List<BeneficiarioAssistencial> beneficiarios = new ArrayList<BeneficiarioAssistencial>();
		beneficiarios = manager.createQuery("SELECT b FROM BeneficiarioAssistencial b where b.ordem_endereco = 1  and b.cep is not null and b.enviado = 0").getResultList();

		return beneficiarios;
	}

	@SuppressWarnings("unchecked")
	public List<BeneficiarioOcupacional> getBeneficiariosOcupacional() {
		List<BeneficiarioOcupacional> beneficiarios = new ArrayList<BeneficiarioOcupacional>();
		beneficiarios = manager.createQuery("SELECT b FROM BeneficiarioOcupacional b where b.ordem_endereco = 1")
				.getResultList();
		return beneficiarios;
	}

	@SuppressWarnings("unchecked")
	public List<Prestador> getPrestadores() {
		List<Prestador> prestadores = new ArrayList<Prestador>();
		prestadores = manager
				.createQuery("SELECT p FROM Prestador p where p.cdPrestador is null and p.cpf_cgc is not null")
				.getResultList();
		return prestadores;
	}

	@SuppressWarnings("unchecked")
	public List<BeneficiarioAssistencial> getBeneficiarioAssistencialAlteracao() {
		List<BeneficiarioAssistencial> beneficiarios = new ArrayList<BeneficiarioAssistencial>();
		beneficiarios = manager.createQuery("SELECT b FROM BeneficiarioAssistencial b where b.cdMatricula is not null").getResultList();
		return beneficiarios;
	}
	
	@SuppressWarnings("unchecked")
	public List<Prestador> getPrestadoresAlteracao() {
		List<Prestador> prestadores = new ArrayList<Prestador>();
		prestadores = manager
				.createQuery("SELECT distinct p FROM Prestador p join fetch p.endereco join fetch p.especialidade "
						+ "where p.cdPrestador is not null")
				.getResultList();
		return prestadores;
	}

	@SuppressWarnings("unchecked")
	public List<MvCacDeParaBeneficiarioAssistencial> getDeParaBeneficiarios() {
		List<MvCacDeParaBeneficiarioAssistencial> listDeParaBeneficiarios = new ArrayList<MvCacDeParaBeneficiarioAssistencial>();
		listDeParaBeneficiarios = manager.createQuery("SELECT b FROM MvCacDeParaBeneficiario b").getResultList();
		return listDeParaBeneficiarios;
	}

	@SuppressWarnings("unchecked")
	public List<MvCacDeParaPrestador> getDeParaPrestadores() {
		List<MvCacDeParaPrestador> listDeParaPrestadores = new ArrayList<MvCacDeParaPrestador>();
		listDeParaPrestadores = manager.createQuery("SELECT b FROM MvCacDeParaPrestador b").getResultList();
		return listDeParaPrestadores;
	}
	
	@SuppressWarnings("unchecked")
	public List<AtLogMvCac> getOperacoesPendentes() {
		List<AtLogMvCac> listOperacoesPendentes = new ArrayList<AtLogMvCac>();
		listOperacoesPendentes = manager.createQuery("SELECT b FROM AtLogMvCac b").getResultList();
		return listOperacoesPendentes;
	}

	public Prestador getPrestador(Integer id_prestador) {
		Prestador prestador = new Prestador();
		Query query = manager.createQuery("SELECT p FROM Prestador p where p.id_prestador = :id_prestador");
		query.setParameter("id_prestador", id_prestador);
		prestador = (Prestador) query.getSingleResult();	
		return prestador;
	}
	
	public BeneficiarioAssistencial getBeneficiarioAssistencial(String id_pessoa) {
		BeneficiarioAssistencial beneficiario = new BeneficiarioAssistencial();  
		Query query = manager.createQuery("SELECT b FROM BeneficiarioAssistencial b where b.cd_pessoa = :id_pessoa");
		query.setParameter("id_pessoa", id_pessoa);		
		beneficiario = (BeneficiarioAssistencial)query.getSingleResult();
		return beneficiario;
	}
	
	public BeneficiarioAssistencialTodos getBeneficiarioAssistencialTodos(String id_pessoa) {
		BeneficiarioAssistencialTodos beneficiario = new BeneficiarioAssistencialTodos();  
		Query query = manager.createQuery("SELECT b FROM BeneficiarioAssistencialTodos b where b.cd_pessoa = :id_pessoa");
		query.setParameter("id_pessoa", id_pessoa);	
		query.setMaxResults(1);
		beneficiario = (BeneficiarioAssistencialTodos)query.getSingleResult();
		return beneficiario;
	}	
	
	public BeneficiarioOcupacional getBeneficiarioOcupacional(String id_pessoa) {
		BeneficiarioOcupacional beneficiario = new BeneficiarioOcupacional();  
		Query query = manager.createQuery("SELECT b FROM BeneficiarioOcupacional b where b.cd_pessoa = :id_pessoa");
		query.setParameter("id_pessoa", id_pessoa);		
		beneficiario = (BeneficiarioOcupacional)query.getSingleResult();
		return beneficiario;
	}
	
	public void removeAtLogMvCac(AtLogMvCac logMvCac) {

		manager.remove(manager.find(AtLogMvCac.class, logMvCac.getId_log_mv_cac()));

	}
	
	public void atualizaAtLogMvCac(AtLogMvCac logMvCac) {

		AtLogMvCac log = new AtLogMvCac();
		log = manager.find(AtLogMvCac.class, logMvCac.getId_log_mv_cac());
		
		log.setResposta(logMvCac.getResposta());
		log.setTimestamp(new Date());
		
		manager.merge(log);

	}
}
