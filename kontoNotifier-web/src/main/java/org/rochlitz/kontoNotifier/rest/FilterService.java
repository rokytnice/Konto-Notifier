package org.rochlitz.kontoNotifier.rest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.naming.AuthenticationException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.rochlitz.kontoNotfier.persistence.AllDAO;
import org.rochlitz.kontoNotfier.persistence.FilterDTO;
import org.rochlitz.kontoNotfier.persistence.KontoDTO;
import org.rochlitz.kontoNotfier.persistence.UserDTO;
import org.rochlitz.kontoNotifier.security.Authentication;

//   http://your_domain:port/display-name/url-pattern/path_from_rest_class 
//   http://localhost:8080/kontoNotifier-web/rest/konto
@Path("/notifier")
@Stateless
public class FilterService {

	@Inject
	AllDAO kDAO;
	@Inject
	Authentication authServ;

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addFilterANDNotfier(FilterDTO filter,
			@Context HttpServletRequest request) {

		Response result = null;

		try {

			UserDTO user = authServ.getUserFromSession(request);
			KontoDTO konto = (KontoDTO) kDAO.find(filter.getKontoSelection(),
					new KontoDTO());
			filter.setKonto(konto);
			kDAO.persist(filter);
			
			// Create an "ok" response
			result = Response.ok(filter).build();
		} catch (ConstraintViolationException ce) {
			// Handle bean validation issues
			// builder = createViolationResponse(ce.getConstraintViolations());
		} catch (ValidationException e) {
			// Handle the unique constrain violation
			Map<String, String> responseObj = new HashMap<String, String>();
			responseObj.put("email", "Email taken");
			// builder =
			// Response.status(Response.Status.CONFLICT).entity(responseObj);
		} catch (Exception e) {
			// Handle generic exceptions
			Map<String, String> responseObj = new HashMap<String, String>();
			responseObj.put("error", e.getMessage());
			// builder =
			// Response.status(Response.Status.BAD_REQUEST).entity(responseObj);
		}
		 return result;
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAll(@Context HttpServletRequest request) throws AuthenticationException, javax.security.sasl.AuthenticationException {

		UserDTO user = authServ.getUserFromSession(request);
		List<FilterDTO> filters = new ArrayList<FilterDTO>();
		try {
			Iterator<KontoDTO> iter = kDAO.getKontenOfUser(user).iterator();
			while(iter.hasNext()){
				KontoDTO konto = iter.next();
				filters.addAll( kDAO.getFilterOfUser(konto) ); 
			}
													// session
		} catch (Exception e) {
			// TODO db exception
			e.printStackTrace();
		}
		 GenericEntity<List<FilterDTO>> list = new GenericEntity<List<FilterDTO>>(filters) {
	     };
	        
	       Response result = Response.ok(list).build();
		return result;
	}
	
    @GET
    @Path("/deletefilter/{id}")
    public Response deleteFilter(@PathParam("id") String id ,@Context HttpServletRequest request ) throws AuthenticationException, javax.security.sasl.AuthenticationException {
    	authServ.getUserFromSession(request);
    	Response.ResponseBuilder builder = Response.ok();
		Response result = builder.build();
		try {
			kDAO.deleteFilter(Long.parseLong(id) ); 
			builder = Response.ok();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			builder = Response.serverError();
		}
		return result;
    }

}
