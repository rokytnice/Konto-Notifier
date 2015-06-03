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


//TODO activate findbug pmd, checkstyle ..
//TODO logging
//TODO bean validation
//TODO js validation
//TODO SSL
//TODO oauth
//TODO - contents - wie dialoge- ??ber rest service?

// ejb eliminates the need for manual transaction demarcation
@Stateless
public class AllDAO{


	@PersistenceContext
    private EntityManager em;


    public void persist(IDTO dto) throws Exception {
        em.persist(dto);
    }
    
    
//TODO genercis?
    public List getAll(IDTO dto) throws Exception {
    
		String dtoName = dto.getClass().getSimpleName();
    	
    	List<IDTO> result = em.createQuery("SELECT e FROM "+dtoName+" e").getResultList();
    	return  result;
    
    }
    
    public IDTO find(long id, IDTO dto) {
         IDTO res = em.find(dto.getClass(), id);
         return res;
      }
    
}
