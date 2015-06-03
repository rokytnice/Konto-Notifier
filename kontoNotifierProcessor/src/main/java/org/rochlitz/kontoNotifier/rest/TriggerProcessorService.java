package org.rochlitz.kontoNotifier.rest;

import java.util.GregorianCalendar;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.rochlitz.kontoNotifier.NotifierProcessor;

//   http://your_domain:port/display-name/url-pattern/path_from_rest_class 
//   http://localhost:8080/kontoNotifier-web/rest/konto
@Path("/process")
public class TriggerProcessorService {

	@Inject
	NotifierProcessor notCon;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String getAll() {
		System.out.println(" Timer: run  " + new GregorianCalendar().toString());
		notCon.processing();
		return "ok";
	}

}
