package br.org.cac.cacmvmiddleware.dao;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.Transactional;

import br.org.cac.cacmvmiddleware.annotations.SoulMV;
import br.org.cac.cacmvmiddleware.modelo.CepLocalidade;
import br.org.cac.cacmvmiddleware.modelo.CepLogradouro;


@Transactional
public class SoulMvDAO {

	@Inject
	@SoulMV
	EntityManager manager;

	
	public Integer getCepLocalidadeByLocalidade(String nm_localidade) {
		Integer cepLocalidade;  
		Query query = manager.createQuery("SELECT Max(b.cd_localidade) FROM CepLocalidade b WHERE cd_uf = 'RJ' AND nm_localidade like :localidade ");
		query.setParameter("localidade", "%"+nm_localidade+"%");		
		cepLocalidade = (Integer)query.getSingleResult();
		return cepLocalidade;
	}
	
	public Integer getCepLocalidadeByCep(String nr_cep) {
		Integer cepLocalidade;  
		Query query = manager.createQuery("SELECT Max(b.cd_localidade) FROM CepLocalidade b WHERE cd_uf = 'RJ' AND nr_cep like :cep ");
		query.setParameter("cep", "%"+nr_cep+"%");		
		cepLocalidade = (Integer)query.getSingleResult();
		return cepLocalidade;
	}
	
	public Integer getMaxCdLogradouro(Integer cd_localidade){
		Query query = manager.createQuery("SELECT Max(cd_logradouro) FROM CepLogradouro WHERE cd_uf = 'RJ' AND cd_localidade = :cd_localidade");
		query.setParameter("cd_localidade", cd_localidade);		
		Integer max_id = (Integer)query.getSingleResult();
		if (max_id != null) {
			max_id = max_id+1;
		}else{
			query = manager.createQuery("SELECT Max(cd_logradouro) FROM CepLogradouro WHERE cd_uf = 'RJ'");
			max_id = (Integer)query.getSingleResult()+1;
		}
		return max_id;	
	}
	
	public Integer getMaxCdLocalidade(){
		Query query = manager.createQuery("SELECT Max(cd_logradouro) FROM CepLogradouro WHERE cd_uf = 'RJ' ");
		Integer max_id = (Integer)query.getSingleResult();
		return max_id+1;
	}

	
	public void incluiCepLogradouro(CepLogradouro log) {
		manager.persist(log);
	}
	
	public void incluiCepLocalidade(CepLocalidade log) {
		manager.persist(log);
	}
	
}
