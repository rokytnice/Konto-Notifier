package org.rochlitz.kontoNotifier.rest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.rochlitz.kontoNotfier.persistence.AllDAO;
import org.rochlitz.kontoNotfier.persistence.FilterDTO;
import org.rochlitz.kontoNotfier.persistence.KontoDTO;
import org.rochlitz.kontoNotfier.persistence.NotifierDTO;
import org.rochlitz.kontoNotfier.persistence.UserDTO;


//   http://your_domain:port/display-name/url-pattern/path_from_rest_class 
//   http://localhost:8080/kontoNotifier-web/rest/konto
@Path("/notifier")
public class NotifierService {
	
	@Inject
	AllDAO kDAO;
	
	@POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
	public Response addFilterANDNotfier(FilterDTO filter) {

		Response.ResponseBuilder builder = null;
		
		try{
			
			kDAO.persist(filter);
			
			UserDTO user = (UserDTO) kDAO.getAll(new UserDTO()).iterator().next() ;//TODO FAKED:  get user from sessiob
			KontoDTO kto = (KontoDTO) kDAO.find(filter.getKontoSelection(),new KontoDTO());
			
			NotifierDTO not = new NotifierDTO();
			not.setKonto(kto);
			not.setFilter(filter);
			not.setUser(user);

			kDAO.persist(not);
			   // Create an "ok" response
	        builder = Response.ok().entity(filter);
    } catch (ConstraintViolationException ce) {
        // Handle bean validation issues
//        builder = createViolationResponse(ce.getConstraintViolations());
    } catch (ValidationException e) {
        // Handle the unique constrain violation
        Map<String, String> responseObj = new HashMap<String, String>();
        responseObj.put("email", "Email taken");
//        builder = Response.status(Response.Status.CONFLICT).entity(responseObj);
    } catch (Exception e) {
        // Handle generic exceptions
        Map<String, String> responseObj = new HashMap<String, String>();
        responseObj.put("error", e.getMessage());
//        builder = Response.status(Response.Status.BAD_REQUEST).entity(responseObj);
    }
		return builder.build();
	}
	
	
	 @GET
	    @Produces(MediaType.APPLICATION_JSON)
	    public List getAll() {
	      
		 List<NotifierDTO> list=null; 
		 try {
			list = kDAO.getAll(  new NotifierDTO() ); //TODO nur für user aus session
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		 return list;
	    }

}
