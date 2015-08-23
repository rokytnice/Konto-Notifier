package org.rochlitz.kontoNotifier.rest;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.rochlitz.kontoNotfier.persistence.AllDAO;
import org.rochlitz.kontoNotfier.persistence.TokenDTO;
import org.rochlitz.kontoNotfier.persistence.UserDTO;
import org.rochlitz.kontoNotifier.security.Authentication;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;

//   http://your_domain:port/display-name/url-pattern/path_from_rest_class 
//   http://localhost:8080/kontoNotifier-web/rest/konto
@Path("/setToken")
@Stateless
public class LoginService<JsonFactory> {

	private static final String APPS_DOMAIN_NAME = "kontoagent.ddns.net";
	private static final String MY_GOOGLE_API_CLIENT = "906809457981-emoo2f0vobh740d25v9mb2o9f57142ci.apps.googleusercontent.com";
	@Inject
	AllDAO kDAO;
	
	@Inject
	Authentication authServ;

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response catchtoken(TokenDTO id_token, @Context HttpServletRequest request ) {

		UserDTO user=null;
		Payload payload;
		String subject;
		
		try {
			JsonFactory mJFactory = (JsonFactory) new GsonFactory();
			
			NetHttpTransport transport = new NetHttpTransport();
			GoogleIdTokenVerifier mVerifier = new GoogleIdTokenVerifier(
					transport, (com.google.api.client.json.JsonFactory) mJFactory);
			GoogleIdToken idToken = mVerifier.verify(id_token.getId_token());
			if (idToken != null) {
				payload = idToken.getPayload();
				
				subject = payload.getSubject();
				String hostedDomain__ = payload.getHostedDomain();
				boolean clientIds = Arrays.asList(MY_GOOGLE_API_CLIENT).contains(payload.getAuthorizedParty());// If multiple clients access the backend server:
				//TODO needs to be true ??  - https://developers.google.com/identity/sign-in/android/backend-auth

//				iss: always accounts.google.com
//				aud: the client ID of the web component of the project
//				azp: the client ID of the Android app component of project
//				email: the email that identifies the user requesting the token
//				boolean hostedDomain = payload.getHostedDomain().equals(APPS_DOMAIN_NAME);
//				Validate the cryptographic signature. Because the token takes the form of a JSON web token or JWT and there are libraries to validate JWTs available in most popular programming languages, this is straightforward and efficient.
//				Ensure that the value of the aud field is identical to its own client ID.
				if ( clientIds) {
					System.out.println("User ID: " + payload.getSubject());
				} else {
					System.out.println("Invalid ID token.");
				}
				
				Authentication authServ = new Authentication();
				List<UserDTO> users = kDAO.getUserByMail( payload.getEmail() );
				if(users.size() >= 1){//user exist
					user = users.get(0);
					authServ.setUserToSession(request, user);
				}else{
					user = new UserDTO( payload.getEmail() );
					user = (UserDTO) kDAO.persist( user );
					authServ.setUserToSession(request, user);
				}
				
			} else {
				System.out.println("Invalid ID token.");
			}

			
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
		return Response.ok(user).build();
	}

}
