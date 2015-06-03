 package org.rochlitz.kontoNotfier.persistence;

//import java.util.logging.Logger;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;


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
public class CdiDao{


	@PersistenceContext
    private EntityManager em;


	//TODO test hbci data /  connection 
    public void persist(IDTO dto) throws Exception {
        em.persist(dto);
    }
    
    
//TODO genercis?
    public List getAll(IDTO dto) throws Exception {
    
		String dtoName = dto.getClass().getSimpleName();
    	
    	List<IDTO> result = em.createQuery("SELECT e FROM "+dtoName+" e").getResultList();
    	return  result;
    
    }
    
    
    public List<Long>  getAllIDs(IDTO dto) throws Exception {
        
		String dtoName = dto.getClass().getSimpleName();
    	
    	List<Long> result = em.createQuery("SELECT e.id FROM "+dtoName+" e").getResultList();
    	
    	return  result;
    
    }
    
 
    
    public IDTO find(long id, IDTO dto) {
         IDTO res = em.find(dto.getClass(), id);
         return res;
      }
    
}
