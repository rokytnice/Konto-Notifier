package org.rochlitz.kontoNotifier.security;

import javax.ejb.Stateless;
import javax.security.sasl.AuthenticationException;
import javax.servlet.http.HttpServletRequest;

import org.rochlitz.kontoNotfier.persistence.UserDTO;

//TODO HTTP Digest Authentication.
//TODO SSL certifacte 
//TODO password check
//TODO 
@Stateless
public class Authentication {

	
	private static final String userSessionParamName = "user";


	public void setUserToSession(HttpServletRequest request, UserDTO user) {

		System.out.println("read from session " + request.getSession().getId() );
		 request.getSession().setAttribute(userSessionParamName, user);
		 System.out.println("save user to session - " + user.getEmail() +"  -   "  +  request.getSession().getId() );
	}

	public UserDTO getUserFromSession(HttpServletRequest request) throws AuthenticationException {
		System.out.println("read from session " + request.getSession().getId() );
		UserDTO user = (UserDTO) request.getSession().getAttribute(userSessionParamName);
		if(user!=null){
			System.out.println("get user from session - " + user.getEmail()  +"  -   "  +  request.getSession().getId() );
		}else {
			System.out.println("No user found in session");
			throw new AuthenticationException("User not logged in");//TODO redirect to start page
		}
		return user;
	}

}
