package br.org.cac.cacmvmiddleware.dao;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.Transactional;

import br.org.cac.cacmvmiddleware.modelo.MvCacDeParaBeneficiarioAssistencial;

@Transactional
public class MvCacDeParaBeneficiarioAssistencialDAO {

	@Inject
	EntityManager manager;

	public void incluiDePara(MvCacDeParaBeneficiarioAssistencial deParaBeneficiario) {

		manager.persist(deParaBeneficiario);

	}

	public MvCacDeParaBeneficiarioAssistencial getMvCacDeParaBeneficiario(int id) {
		MvCacDeParaBeneficiarioAssistencial mvCacDeParaBeneficiario = new MvCacDeParaBeneficiarioAssistencial();

		mvCacDeParaBeneficiario = manager.find(MvCacDeParaBeneficiarioAssistencial.class, id);

		return mvCacDeParaBeneficiario;
	}

	public MvCacDeParaBeneficiarioAssistencial getMvCacDeParaBeneficiarioPorCodPessoa(int cod_pessoa) {
		MvCacDeParaBeneficiarioAssistencial mvCacDeParaBeneficiario = new MvCacDeParaBeneficiarioAssistencial();
		Query query = manager
				.createQuery("SELECT b FROM MvCacDeParaBeneficiarioAssistencial b where b.cod_pessoa = :cod_pessoa");
		query.setParameter("cod_pessoa", cod_pessoa);
		try {
			mvCacDeParaBeneficiario = (MvCacDeParaBeneficiarioAssistencial) query.getSingleResult();
			return mvCacDeParaBeneficiario;
		} catch (Exception e) {
			return null;
		}
	}

	public void removeMvCacDeParaBeneficiario(MvCacDeParaBeneficiarioAssistencial deParaBeneficiario) {

		manager.remove(manager.find(MvCacDeParaBeneficiarioAssistencial.class, deParaBeneficiario.getId()));

	}

	public void atualizaMvCacDeParaBeneficiario(MvCacDeParaBeneficiarioAssistencial deParaBeneficiario) {

		MvCacDeParaBeneficiarioAssistencial b = getMvCacDeParaBeneficiarioPorCodPessoa(
				deParaBeneficiario.getCod_pessoa());

		b.setCd_contrato(deParaBeneficiario.getCd_contrato());

		manager.merge(b);

	}

	@SuppressWarnings("unchecked")
	public List<MvCacDeParaBeneficiarioAssistencial> getTodosMvCacDeParaBeneficiariosAssistencial() {
		List<MvCacDeParaBeneficiarioAssistencial> mvCacDeParaBeneficiariosAssistencial = new ArrayList<MvCacDeParaBeneficiarioAssistencial>();

		Query query = manager.createQuery("SELECT p FROM MvCacDeParaBeneficiarioAssistencial p");
		try {
			mvCacDeParaBeneficiariosAssistencial = (List<MvCacDeParaBeneficiarioAssistencial>) query.getResultList();
			return mvCacDeParaBeneficiariosAssistencial;
		} catch (Exception e) {
			return null;
		}

	}

}
