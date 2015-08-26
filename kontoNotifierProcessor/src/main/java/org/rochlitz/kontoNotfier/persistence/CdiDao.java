package org.rochlitz.kontoNotfier.persistence;

//import java.util.logging.Logger;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

//TODO activate findbug pmd, checkstyle ..
//TODO logging
//TODO bean validation
//TODO js validation
//TODO SSL
//TODO oauth
//TODO - contents - wie dialoge- ��ber rest service?

// ejb eliminates the need for manual transaction demarcation
@Named("CdiDao")
@ApplicationScoped
public class CdiDao {

	@PersistenceContext
	private EntityManager em;

	// TODO test hbci data / connection
	public void persist(IDTO dto) throws PersistenceException {
		em.persist(dto);
		em.flush();
	}

	// TODO genercis?
	public List getAll(IDTO dto) throws PersistenceException {

		String dtoName = dto.getClass().getSimpleName();

		List<IDTO> result = em.createQuery("SELECT e FROM " + dtoName + " e")
				.getResultList();
		return result;

	}

	public List<Long> getAllIDs(IDTO dto) throws PersistenceException {

		String dtoName = dto.getClass().getSimpleName();

		List<Long> result = em
				.createQuery("SELECT e.id FROM " + dtoName + " e")
				.getResultList();

		return result;

	}

	public IDTO find(long id, IDTO dto) {
		IDTO res = em.find(dto.getClass(), id);
		return res;
	}

	public List<FilterDTO> getFilterOfUser(KontoDTO konto) throws PersistenceException {
		Query q = em
				.createQuery("SELECT e FROM FilterDTO e WHERE konto.id = :id");
		q.setParameter("id", konto.getId());
		@SuppressWarnings("unchecked")
		List<FilterDTO> result = q.getResultList();
		return result;
	}

	public List<KontoDTO> getKontenOfUser(UserDTO user) throws PersistenceException {
		Query q = em
				.createQuery("SELECT e FROM KontoDTO e WHERE e.user.id = :userid");
		q.setParameter("userid", user.getId());
		@SuppressWarnings("unchecked")
		List<KontoDTO> result = q.getResultList();
		return result;
	}

}
