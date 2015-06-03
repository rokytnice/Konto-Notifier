package org.rochlitz.kontoNotifier.rest;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

/**
 * A class extending {@link Application} and annotated with @ApplicationPath is
 * the Java EE 6 "no XML" approach to activating JAX-RS.
 * 
 * <p>
 * Resources are served relative to the servlet path specified in the
 * {@link ApplicationPath} annotation.
 * </p>
 */
@ApplicationPath("/rest")
public class RestApplication extends Application {
	/* class body intentionally left blank */

	private Set<Object> singletons = new HashSet<Object>();
	private Set<Class<?>> classes = new HashSet<Class<?>>();

	public RestApplication() {
//		singletons.add( new KontoRestService() );
		classes.add(KontoService.class);
		classes.add(NotifierService.class);
	}

	@Override
	public Set<Class<?>> getClasses() {
		return classes;
	}

	@Override
	public Set<Object> getSingletons() {
		return singletons;
	}

}
