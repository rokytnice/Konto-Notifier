package org.rochlitz.kontoNotifier.rest;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.PersistenceException;
import javax.security.sasl.AuthenticationException;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.rochlitz.kontoNotfier.persistence.AllDAO;
import org.rochlitz.kontoNotfier.persistence.UserDTO;
import org.rochlitz.kontoNotifier.security.Authentication;

//   http://your_domain:port/display-name/url-pattern/path_from_rest_class 
//   http://localhost:8080/kontoNotifier-web/rest/konto
@Path("/user")
@Stateless
public class UserService {

	@Inject
	AllDAO kDAO;

	@Inject
	Authentication authServ;

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response registerUser(UserDTO user, @Context HttpServletRequest request) {

		Authentication authServ = new Authentication();
		List<UserDTO> users;
		try {
			users = kDAO.getUserByMail( user.getEmail() );
			if(users.size() >= 1){//user exist - error
				return Response.status(Response.Status.BAD_REQUEST).entity("Diese E-Mail adresse wurde bereits registriert.").type(MediaType.TEXT_PLAIN).build();			
			}else{
				user.setActive(true);//TODO mit email bestätigung muss das geändert werden
				user = (UserDTO) kDAO.persist( user );
				authServ.setUserToSession(request, user);//TODO bestätigungs email!!! und dann zu session hinzufügen
			}
		} catch (PersistenceException | AuthenticationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//TODO		return Response.ok().entity("Bitte die E-Mail bestätigen damit der Account aktiviert werden kann.");
		return Response.ok(user).build();
	}


}
