package org.rochlitz.kontoNotfier.persistence;

//import java.util.logging.Logger;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;


@Stateless
public class AllDAO {

	@PersistenceContext
	private EntityManager em;

	public IDTO persist(IDTO dto) throws PersistenceException {
		IDTO result = em.merge(dto);
		em.flush();
		return result;
	}

	// TODO genercis?
	public List getAll(IDTO dto) throws PersistenceException {
		String dtoName = dto.getClass().getSimpleName();
		List<IDTO> result = em.createQuery("SELECT e FROM " + dtoName + " e")
				.getResultList();
		return result;
	}

	public IDTO find(long id, IDTO dto) {
		IDTO res = em.find(dto.getClass(), id);
		return res;
	}

	public List<UserDTO> getUserByMail(String email) throws PersistenceException {
		Query q = em.createQuery("SELECT e FROM UserDTO e WHERE e.email = :email");
		q.setParameter("email", email);
		@SuppressWarnings("unchecked")
		List<UserDTO> result =  q.getResultList();
		return result;
	}

	public List<FilterDTO> getFilterOfUser(KontoDTO konto) throws PersistenceException {
		Query q = em.createQuery("SELECT e FROM FilterDTO e WHERE konto.id = :id");
		q.setParameter("id", konto.getId());
		@SuppressWarnings("unchecked")
		List<FilterDTO> result =  q.getResultList();
		return result;
	}

	
	//TODO delete filter
	public List<KontoDTO> getKontenOfUser(UserDTO user) throws PersistenceException {
		Query q = em.createQuery("SELECT e FROM KontoDTO e WHERE e.user.id = :userid");
		q.setParameter("userid", user.getId());
		@SuppressWarnings("unchecked")
		List<KontoDTO> result =  q.getResultList();
		return result;
	}

	//TODO delete also file
	public void deleteKontoWithFilter(long kontoid) {
		Query q = em.createQuery("SELECT e FROM FilterDTO e WHERE e.konto.id = :id");
		q.setParameter("id", kontoid);
		List<FilterDTO> result =  q.getResultList();
		
		if(result.size()>0){
			em.remove(result.get(0));
		}
		KontoDTO konto = em.getReference(KontoDTO.class, kontoid);
		 //TODO validate is konto from current user??
		em.remove(konto);
		em.flush();
	}
	
	public void deleteKonto(Long kontoId) {
		KontoDTO filter = em.getReference(KontoDTO.class, kontoId);
		em.remove(filter);
		em.flush();
	}	

	public void deleteFilter(Long pk) {
		FilterDTO filter = em.getReference(FilterDTO.class, pk);//
		 //TODO validate is konto from current user??
		em.remove(filter);
		em.flush();
	}	
	
//	SELECT e, MAX(e.createdDate) FROM KontoauszugDTO e WHERE e.user.id = :userid"
	
	public KontoauszugDTO getLatestKontauszugOfUser(UserDTO user) throws PersistenceException {
		Query q = em.createQuery( "SELECT k FROM KontoauszugDTO k WHERE k.createdDate = (SELECT MAX(e.createdDate) FROM KontoauszugDTO e WHERE e.user.id = :userid)" );
		q.setParameter("userid", user.getId());
		KontoauszugDTO result =  (KontoauszugDTO)  q.getSingleResult();
		return result;
	}
	
}
