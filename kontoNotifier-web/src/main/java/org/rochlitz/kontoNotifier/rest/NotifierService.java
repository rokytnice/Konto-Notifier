package org.rochlitz.kontoNotifier.rest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;

import org.rochlitz.kontoNotfier.persistence.AllDAO;
import org.rochlitz.kontoNotfier.persistence.FilterDTO;
import org.rochlitz.kontoNotfier.persistence.KontoDTO;
import org.rochlitz.kontoNotfier.persistence.NotifierDTO;
import org.rochlitz.kontoNotfier.persistence.UserDTO;
import org.rochlitz.kontoNotifier.security.Authentication;

//   http://your_domain:port/display-name/url-pattern/path_from_rest_class 
//   http://localhost:8080/kontoNotifier-web/rest/konto
@Path("/notifier")
public class NotifierService {

	@Inject
	AllDAO kDAO;

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addFilterANDNotfier(FilterDTO filter,
			@Context HttpServletRequest request) {

		Response.ResponseBuilder builder = Response.ok();
		Response result = builder.build();

		try {

			Authentication authServ = new Authentication();
			UserDTO user = authServ.getUserFromSession(request);
//			authServ.setSessionLoginStatus(user, result);

			KontoDTO kto = (KontoDTO) kDAO.find(filter.getKontoSelection(),
					new KontoDTO());

			NotifierDTO not = new NotifierDTO();
			not.setKonto(kto);
			not.setFilter(filter);
			not.setUser(user);

			kDAO.persist(filter);
			kDAO.persist(not);
			// Create an "ok" response
			builder = Response.ok().entity(filter);
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
	public List<NotifierDTO> getAll(@Context HttpServletRequest request) {

		Authentication authServ = new Authentication();
		UserDTO user = authServ.getUserFromSession(request);
		List<NotifierDTO> list = null;
		try {
			list = kDAO.getNotifierOfUser(user); 
													// session
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return list;
	}

}
