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
import javax.persistence.Query;


@Stateless
public class AllDAO {

	@PersistenceContext
	private EntityManager em;

	public IDTO persist(IDTO dto) throws Exception {
		IDTO result = em.merge(dto);
		em.flush();
		return result;
	}

	// TODO genercis?
	public List getAll(IDTO dto) throws Exception {
		String dtoName = dto.getClass().getSimpleName();
		List<IDTO> result = em.createQuery("SELECT e FROM " + dtoName + " e")
				.getResultList();
		return result;
	}

	public IDTO find(long id, IDTO dto) {
		IDTO res = em.find(dto.getClass(), id);
		return res;
	}

	public List<UserDTO> getUserByMail(String email) throws Exception {
		Query q = em.createQuery("SELECT e FROM UserDTO e WHERE e.email = :email");
		q.setParameter("email", email);
		@SuppressWarnings("unchecked")
		List<UserDTO> result =  q.getResultList();
		return result;
	}

	public List<NotifierDTO> getNotifierOfUser(UserDTO user) throws Exception {
		Query q = em.createQuery("SELECT e FROM NotifierDTO e WHERE e.user.id = :userid");
		q.setParameter("userid", user.getId());
		@SuppressWarnings("unchecked")
		List<NotifierDTO> result =  q.getResultList();
		return result;
	}

	public List<KontoDTO> getKontenOfUser(UserDTO user) throws Exception {
		Query q = em.createQuery("SELECT e FROM KontoDTO e WHERE e.user.id = :userid");
		q.setParameter("userid", user.getId());
		@SuppressWarnings("unchecked")
		List<KontoDTO> result =  q.getResultList();
		return result;
	}

	
}
