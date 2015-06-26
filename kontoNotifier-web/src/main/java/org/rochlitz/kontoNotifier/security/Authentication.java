package org.rochlitz.kontoNotifier.security;

import java.util.Map;

import javax.ejb.Stateless;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;

import org.rochlitz.kontoNotfier.persistence.UserDTO;

//TODO HTTP Digest Authentication.
//TODO SSL certifacte 
//TODO password check
//TODO 
@Stateless
public class Authentication {

	
	private static final String userSessionParamName = "user";

//	public void setSessionLoginStatus(UserDTO user, Response result) {
//		if (user == null) {
//			Map<String, NewCookie> cookies = result.getCookies();
//			cookies.put("loggedIn", new NewCookie("logedIn", "false"));
//		} else {
//			Map<String, NewCookie> cookies = result.getCookies();
//			cookies.put("loggedIn", new NewCookie("logedIn", "true"));
//		}
//		return;
//	}

	public void setUserToSession(HttpServletRequest request, UserDTO user) {

		System.out.println("read from session " + request.getSession().getId() );
		 request.getSession().setAttribute(userSessionParamName, user);
		 System.out.println("save user to session - " + user.getEmail() +"  -   "  +  request.getSession().getId() );
	}

	public UserDTO getUserFromSession(HttpServletRequest request) {
		System.out.println("read from session " + request.getSession().getId() );
		UserDTO user = (UserDTO) request.getSession().getAttribute(userSessionParamName);
		if(user!=null){
			System.out.println("get user from session - " + user.getEmail()  +"  -   "  +  request.getSession().getId() );
		}else {
			System.out.println("No user found in session");
		}
		return user;

	}
 
}
