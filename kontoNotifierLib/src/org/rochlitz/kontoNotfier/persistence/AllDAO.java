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

//TODO activate findbug pmd, checkstyle ..
//TODO logging
//TODO bean validation
//TODO js validation
//TODO http port 90 auf 443 umleiten 
//TODO oauth
//TODO - contents - wie dialoge- über rest service?
//TODO jeder änderung des notifier durch eine nutzer - per mail bestatigen
// ejb eliminates the need for manual transaction demarcation

@Stateless
public class AllDAO {

	@PersistenceContext
	private EntityManager em;

	public void persist(IDTO dto) throws Exception {
		em.merge(dto);
		em.flush();
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

}
