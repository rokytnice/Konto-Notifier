/*
 * JBoss, Home of Professional Open Source
 * Copyright 2014, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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

	public List<KontoDTO> getKontenOfUser(UserDTO user) throws PersistenceException {
		Query q = em.createQuery("SELECT e FROM KontoDTO e WHERE e.user.id = :userid");
		q.setParameter("userid", user.getId());
		@SuppressWarnings("unchecked")
		List<KontoDTO> result =  q.getResultList();
		return result;
	}

	//TODO delete also file
	public void deleteNotfifier(long kontoid) {
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

	public void deleteFilter(Integer filterId) {

		FilterDTO filter = em.getReference(FilterDTO.class, filterId);
		 //TODO validate is konto from current user??
		em.remove(filter);
		em.flush();
	}	
	
}
