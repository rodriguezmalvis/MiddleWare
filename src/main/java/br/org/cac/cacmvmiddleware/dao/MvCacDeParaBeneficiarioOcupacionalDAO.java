package br.org.cac.cacmvmiddleware.dao;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.Transactional;

import br.org.cac.cacmvmiddleware.modelo.MvCacDeParaBeneficiarioOcupacional;

@Transactional
public class MvCacDeParaBeneficiarioOcupacionalDAO {

	@Inject
	EntityManager manager;

	public void incluiDePara(MvCacDeParaBeneficiarioOcupacional deParaBeneficiario) {

		manager.persist(deParaBeneficiario);

	}

	public MvCacDeParaBeneficiarioOcupacional getMvCacDeParaBeneficiario(int id) {
		MvCacDeParaBeneficiarioOcupacional mvCacDeParaBeneficiario = new MvCacDeParaBeneficiarioOcupacional();

		mvCacDeParaBeneficiario = manager.find(MvCacDeParaBeneficiarioOcupacional.class, id);

		return mvCacDeParaBeneficiario;
	}

	public MvCacDeParaBeneficiarioOcupacional getMvCacDeParaBeneficiarioPorCodPessoa(int cod_pessoa) {
		MvCacDeParaBeneficiarioOcupacional mvCacDeParaBeneficiario = new MvCacDeParaBeneficiarioOcupacional();
		Query query = manager
				.createQuery("SELECT b FROM MvCacDeParaBeneficiarioOcupacional b where b.cod_pessoa = :cod_pessoa");
		query.setParameter("cod_pessoa", cod_pessoa);
		try {
			mvCacDeParaBeneficiario = (MvCacDeParaBeneficiarioOcupacional) query.getSingleResult();
			return mvCacDeParaBeneficiario;
		} catch (Exception e) {
			return null;
		}
	}

	public void removeMvCacDeParaBeneficiario(MvCacDeParaBeneficiarioOcupacional deParaBeneficiario) {

		manager.remove(manager.find(MvCacDeParaBeneficiarioOcupacional.class, deParaBeneficiario.getId()));

	}

	public void alteraMvCacDeParaBeneficiario(MvCacDeParaBeneficiarioOcupacional deParaBeneficiario) {

		MvCacDeParaBeneficiarioOcupacional b = getMvCacDeParaBeneficiarioPorCodPessoa(
				deParaBeneficiario.getCod_pessoa());

		b.setCd_contrato(deParaBeneficiario.getCd_contrato());

		manager.merge(b);

	}

	@SuppressWarnings("unchecked")
	public List<MvCacDeParaBeneficiarioOcupacional> getTodosMvCacDeParaBeneficiariosOcupacional() {
		List<MvCacDeParaBeneficiarioOcupacional> mvCacDeParaBeneficiariosOcupacional = new ArrayList<MvCacDeParaBeneficiarioOcupacional>();

		Query query = manager.createQuery("SELECT p FROM MvCacDeParaBeneficiarioOcupacional p");
		try {
			mvCacDeParaBeneficiariosOcupacional = (List<MvCacDeParaBeneficiarioOcupacional>) query.getResultList();
			return mvCacDeParaBeneficiariosOcupacional;
		} catch (Exception e) {
			return null;
		}

	}

}
