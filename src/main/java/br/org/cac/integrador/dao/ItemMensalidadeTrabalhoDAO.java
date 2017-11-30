package br.org.cac.integrador.dao;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import br.org.cac.integrador.modelo.infraestrutura.GrupoItemMensalidadeTrabalho;
import br.org.cac.integrador.modelo.infraestrutura.ItemMensalidadeTrabalho;
import br.org.cac.integrador.modelo.infraestrutura.MensalidadeTrabalho;

@Transactional
public class ItemMensalidadeTrabalhoDAO {

	@Inject
	private EntityManager manager;

	public ItemMensalidadeTrabalhoDAO() {

	}

	public ItemMensalidadeTrabalho findById(int id) {
		ItemMensalidadeTrabalho retorno = manager.find(ItemMensalidadeTrabalho.class, id);
		return retorno;
	}

	public List<ItemMensalidadeTrabalho> findByMensalidadeTrabalho(MensalidadeTrabalho mensalidadeTrabalho) {

		TypedQuery<ItemMensalidadeTrabalho> query = manager.createQuery(
				"select i from ItemMensalidadeTrabalho i" + " where i.mensalidadeTrabalho = :mensalidadeTrabalho",
				ItemMensalidadeTrabalho.class);
		query.setParameter("mensalidadeTrabalho", mensalidadeTrabalho);

		List<ItemMensalidadeTrabalho> retorno = query.getResultList();

		return retorno;
	}
	
	public List<ItemMensalidadeTrabalho> findByMensalidadeTrabalho(MensalidadeTrabalho mensalidadeTrabalho, boolean fProvento){
		Query query = manager.createNativeQuery(
				" select imt.id_item_mensalidade_trabalho, imt.id_mensalidade_trabalho, imt.id_comando " + 
				" from ICM_ITEM_MENSALIDADE_TRABALHO imt inner join COMANDO cc " + 
				"         on imt.id_comando = cc.id_comando " + 
				"     inner join RUBRICA_CAC rc " + 
				"         on cc.id_rubrica_cac = rc.id_rubrica_cac " + 
				" where imt.id_mensalidade_trabalho = :id_mensalidade_trabalho " +
				" and rc.f_provento = :f_provento ", ItemMensalidadeTrabalho.class);
		query.setParameter("id_mensalidade_trabalho", mensalidadeTrabalho);
		query.setParameter("f_provento", fProvento);
		
		@SuppressWarnings("unchecked")
		List<ItemMensalidadeTrabalho> retorno = query.getResultList();
		return retorno;
	}

	public void persist(ItemMensalidadeTrabalho itemMensalidadeTrabalho) {
		manager.persist(itemMensalidadeTrabalho);
	}

	public void merge(ItemMensalidadeTrabalho itemMensalidadeTrabalho) {
		manager.merge(itemMensalidadeTrabalho);
	}

	@SuppressWarnings("unchecked")
	public List<ItemMensalidadeTrabalho> findByGrupoItemMensalidadeTrabalho(GrupoItemMensalidadeTrabalho grupoItemMensalidadeTrabalho) 
		throws PersistenceException {
		Query query = manager.createQuery(" select imt from ItemMensalidadeTrabalho imt "
				+ " where imt.grupoItemMensalidadeTrabalho = :grupoItemMensalidadeTrabalho ", ItemMensalidadeTrabalho.class);
		query.setParameter("grupoItemMensalidadeTrabalho", grupoItemMensalidadeTrabalho);
		
		return query.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<ItemMensalidadeTrabalho> listByIdComando(MensalidadeTrabalho mensalidadeTrabalho, List<Integer> idsComando){
		Query query = manager.createQuery(" select imt from ItemMensalidadeTrabalho imt "
				+ " where imt.idComando in :idsComando "
				+ " and imt.mensalidadeTrabalho = :mensalidadeTrabalho", ItemMensalidadeTrabalho.class);
		query.setParameter("mensalidadeTrabalho", mensalidadeTrabalho);
		query.setParameter("idsComando", idsComando);
		
		return query.getResultList();
	}
}
