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

@Path("/login")
@Stateless
public class LoginService {

	@Inject
	AllDAO kDAO;

	@Inject
	Authentication authServ;

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response loginUser(UserDTO user, @Context HttpServletRequest request) {

		Authentication authServ = new Authentication();
		List<UserDTO> users;
		try {
			users = kDAO.getUserByMail(user.getEmail());
			if (users.size() >= 1) {// user exist - error
				if ( validatePassword(user, users.get(0)) ) {
					authServ.setUserToSession(request, users.get(0));//TODO bestätigungs email!!! und dann zu session hinzufügen
					// Response.ok().entity("Bitte die E-Mail bestätigen damit der Account aktiviert werden kann.");
					return Response.ok(user).build();
				}
			}
		} catch (PersistenceException | AuthenticationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return Response.status(Response.Status.BAD_REQUEST)
					.entity("Ein Fehler beim anmelden ist aufgetreten.")
					.type(MediaType.TEXT_PLAIN).build();

		}
		// TODO return
		return Response.status(Response.Status.BAD_REQUEST)
				.entity("E-Mail oder Passort ist falsch.")
				.type(MediaType.TEXT_PLAIN).build();
	}

	private boolean validatePassword(UserDTO user, UserDTO userFromDB) {
		if (0 == user.getPassword().compareTo(userFromDB.getPassword())) {
			return true;
		}
		return false;
	}

}
