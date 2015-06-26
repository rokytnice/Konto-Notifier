package org.rochlitz.kontoNotifier.security;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Response;

import org.rochlitz.kontoNotfier.persistence.UserDTO;

@WebFilter("/dis/*")
public class RequestFilter implements Filter {
	private static final String CALLBACK_METHOD = "callback";

	@Override
	public void init(FilterConfig config) throws ServletException {
		// Nothing needed
	}

	// @Inject
	// private Logger log;
	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {

		if (!(request instanceof HttpServletRequest)) {
			throw new ServletException(
					"Only HttpServletRequest requests are supported");
		}

		final HttpServletRequest httpRequest = (HttpServletRequest) request;
		final HttpServletResponse httpResponse = (HttpServletResponse) response;
		UserDTO u = (UserDTO) httpRequest.getSession().getAttribute("user");
		

		Cookie[] cookies = httpRequest.getCookies();
		String sessionId = httpRequest.getRequestedSessionId();
		String cpath = httpRequest.getContextPath();
		String method = httpRequest.getMethod();
		String pi = httpRequest.getPathInfo();
		String locadre = httpRequest.getLocalAddr();
		String l = httpRequest.getLocalName();
		String url = cpath + "/" + pi;

		Enumeration<String> hn = httpRequest.getHeaderNames();
		
		Response.ResponseBuilder builder = null;
		builder = Response.ok();
		Response result = builder.build();
		
		// extract the callback method from the request query parameters
		String callback = getCallbackMethod(httpRequest);

	}

	private String getCallbackMethod(HttpServletRequest httpRequest) {
		return httpRequest.getParameter(CALLBACK_METHOD);
	}

	private boolean isJSONPRequest(String callbackMethod) {
		// A simple check to see if the query parameter has been set.
		return (callbackMethod != null && callbackMethod.length() > 0);
	}

	@Override
	public void destroy() {
		// Nothing to do
	}
}