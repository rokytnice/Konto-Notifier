package org.rochlitz.kontoNotifier.rest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.rochlitz.kontoNotfier.persistence.AllDAO;
import org.rochlitz.kontoNotfier.persistence.IDTO;
import org.rochlitz.kontoNotfier.persistence.KontoDTO;
import org.rochlitz.kontoNotfier.persistence.UserDTO;
import org.rochlitz.kontoNotifier.security.Authentication;

//   http://your_domain:port/display-name/url-pattern/path_from_rest_class 
//   http://localhost:8080/kontoNotifier-web/rest/konto
@Path("/konto")
@Stateless
public class KontoService {

	@Inject
	AllDAO kDAO;
	private List<IDTO> list;
	
	@Inject
	Authentication authServ;

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addKonto(KontoDTO konto, @Context HttpServletRequest request ) {

		Response.ResponseBuilder builder = Response.ok();
		Response result = builder.build();
		
		try {

			Authentication authServ = new Authentication();
			UserDTO user = authServ.getUserFromSession(request);

			if(user == null){
				//TODO exception
				
			}else{
				konto.setUser(user);
				kDAO.persist(konto);
			}
			
			// Create an "ok" response
			builder = Response.ok().entity(konto);
			result = builder.build();
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
	@Path("/{id:[0-9][0-9]*}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getKonto(@PathParam("id") long id) {

		Response.ResponseBuilder builder = null;

		try {
			KontoDTO konto = (KontoDTO) kDAO.find(id, new KontoDTO());

			if (konto == null) {
				return Response.status(Response.Status.NOT_FOUND).build();
			}

			// Create an "ok" response
			builder = Response.ok().entity(konto);
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
		return builder.build();
	}
	
	
	 @GET
	    @Produces(MediaType.APPLICATION_JSON)
	    public List getAll(@Context HttpServletRequest request ) {
			Authentication authServ = new Authentication();
			UserDTO user = authServ.getUserFromSession(request);
			List<KontoDTO> list = null;
			try {
				list = kDAO.getKontenOfUser(user); 
														// session
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return list;
	    }

}
