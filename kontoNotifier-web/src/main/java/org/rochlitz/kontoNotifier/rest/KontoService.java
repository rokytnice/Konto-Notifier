package org.rochlitz.kontoNotifier.rest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.security.sasl.AuthenticationException;
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

import org.kapott.hbci.manager.HBCIUtils;
import org.rochlitz.hbci.tests.web.KontoAuszugThreaded;
import org.rochlitz.hbci.tests.web.MyCallback;
import org.rochlitz.kontoNotfier.persistence.AllDAO;
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

	@Inject
	Authentication authServ;

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addKonto(KontoDTO konto, @Context HttpServletRequest request) {

		Response.ResponseBuilder builder = Response.ok();
		Response result = builder.build();
		UserDTO user = null;
		try {

			user = authServ.getUserFromSession(request);

//			List<KontoDTO> konten = kDAO.getKontenOfUser(user);
			konto.setUser(user);
			
//			List<KontoDTO> kontenOfUser = user.getKonten();
//			kontenOfUser.add(konto);
			kDAO.persist(konto);
			
			MyCallback mc = new MyCallback(konto,user);
			KontoAuszugThreaded t = new KontoAuszugThreaded(mc);
			t.getAuszug();// TODO nach erfolgreichem anlegen passport speichern
							// konto in db

			// Create an "ok" response
			builder = Response.ok().entity(konto);
			result = builder.build();
		} catch (ConstraintViolationException ce) {
			// Handle bean validation issues
			// builder = createViolationResponse(ce.getConstraintViolations());
			builder = Response.serverError();
		} catch (ValidationException e) {
			// Handle the unique constrain violation
			Map<String, String> responseObj = new HashMap<String, String>();
			responseObj.put("email", "Email taken");
			// builder =
			// Response.status(Response.Status.CONFLICT).entity(responseObj);
			builder = Response.serverError();
			
		} catch (AuthenticationException ae) {
				// Handle the unique constrain violation
				Map<String, String> responseObj = new HashMap<String, String>();
				responseObj.put("email", "Email taken");
				// builder =
				// Response.status(Response.Status.CONFLICT).entity(responseObj);
				return  Response.status(Response.Status.UNAUTHORIZED).build();
		} catch (Exception e) {
			// Handle generic exceptions
			Map<String, String> responseObj = new HashMap<String, String>();
			responseObj.put("error", e.getMessage());
			// builder =
			// Response.status(Response.Status.BAD_REQUEST).entity(responseObj);
			builder = Response.serverError();
		}
		HBCIUtils.doneThread();// clean up data structure - need to be done for
								// new baking connection
		return result;
	}

	@GET
	@Path("/{id:[0-9][0-9]*}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getKonto(@PathParam("id") long id,
			@Context HttpServletRequest request) throws AuthenticationException {

		Response.ResponseBuilder builder = null;
		authServ.getUserFromSession(request);
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
			builder = Response.serverError();
		} catch (ValidationException e) {
			// Handle the unique constrain violation
			Map<String, String> responseObj = new HashMap<String, String>();
			responseObj.put("email", "Email taken");
			// builder =
			// Response.status(Response.Status.CONFLICT).entity(responseObj);
			builder = Response.serverError();
		} catch (Exception e) {
			// Handle generic exceptions
			Map<String, String> responseObj = new HashMap<String, String>();
			responseObj.put("error", e.getMessage());
			// builder =
			// Response.status(Response.Status.BAD_REQUEST).entity(responseObj);
			builder = Response.serverError();
		}
		return builder.build();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAll(@Context HttpServletRequest request)
			throws AuthenticationException {
		UserDTO user = authServ.getUserFromSession(request);
		List<KontoDTO> res=null;
		try {
			res = kDAO.getKontenOfUser(user);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return Response.serverError().build();
		}
		
		 GenericEntity<List<KontoDTO>> list = new GenericEntity<List<KontoDTO>>(res) {
	        };
	        
	       Response result = Response.ok(list).build();
	    		 
		return result;
	}

	@GET
	@Path("/deletekonto/{id}")
	public Response deleteKonto(@PathParam("id") String id,
			@Context HttpServletRequest request) throws AuthenticationException {
		authServ.getUserFromSession(request);
		try {
//			kDAO.deleteKontoWithFilter(Long.parseLong(id));
			kDAO.deleteKonto(Long.parseLong(id));
			//TODO DELETE PASSPORTFILE !!!!!!!!!!!!!!
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Response.serverError().build();
		}
		return Response.ok().build();
	}

}
