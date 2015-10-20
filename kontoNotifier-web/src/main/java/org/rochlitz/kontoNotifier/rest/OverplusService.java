package org.rochlitz.kontoNotifier.rest;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.security.sasl.AuthenticationException;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.kapott.hbci.GV_Result.GVRKUms;
import org.kapott.hbci.manager.HBCIUtils;
import org.rochlitz.hbci.tests.web.KontoAuszugThreaded;
import org.rochlitz.hbci.tests.web.MyCallback;
import org.rochlitz.kontoNotfier.persistence.AllDAO;
import org.rochlitz.kontoNotfier.persistence.KontoDTO;
import org.rochlitz.kontoNotfier.persistence.UserDTO;
import org.rochlitz.kontoNotifier.security.Authentication;

//   http://your_domain:port/display-name/url-pattern/path_from_rest_class 
//   http://localhost:8080/kontoNotifier-web/rest/konto
@Path("/overplus")
@Stateless
public class OverplusService {


	@Inject
	AllDAO kDAO;

	@Inject
	Authentication authServ;


	/*
	 * @param user
	 * 
	 * @param request
	 * 
	 * @return
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getOverplus(@Context HttpServletRequest request) {

		List<Overplus> lOverplus = new ArrayList<Overplus>();
		UserDTO user = null;
		Overplus overplusMergedAllKontos = null;
		try {
		
			user = authServ.getUserFromSession(request);

			Iterator<KontoDTO> kontIter = kDAO.getKontenOfUser(user).iterator();
			while (kontIter.hasNext()) {
				KontoDTO konto = kontIter.next();
				MyCallback myCallback = new MyCallback(konto, user);
				KontoAuszugThreaded kontoAuzugThread = new KontoAuszugThreaded(
						myCallback);
				Calendar calStart = new GregorianCalendar();
				Calendar calEnd = new GregorianCalendar();
				calStart.set(Calendar.DAY_OF_MONTH, 1);
				GVRKUms umsaetze = kontoAuzugThread.getAuszug(calStart, calEnd);
				Overplus overplus = getOverplusData(umsaetze);
				lOverplus.add(overplus);
			}
			
			overplusMergedAllKontos = mergeOverplus(lOverplus);
			
			HBCIUtils.doneThread();// clean up data structure - need to be done

		} catch (AuthenticationException ae) {
			// Handle the unique constrain violation
			Map<String, String> responseObj = new HashMap<String, String>();
			responseObj.put("email", "Email taken");
			// builder =
			// Response.status(Response.Status.CONFLICT).entity(responseObj);
			return Response.status(Response.Status.UNAUTHORIZED).build();
		} catch (Exception e) {
			// Handle generic exceptions
			Map<String, String> responseObj = new HashMap<String, String>();
			responseObj.put("error", e.getMessage());
			// builder =
			// Response.status(Response.Status.BAD_REQUEST).entity(responseObj);
			return Response.serverError().build();
		}

		return Response.ok(overplusMergedAllKontos).build();
	}

	private Overplus mergeOverplus(List<Overplus> lOverplus) {
		Iterator<Overplus> iter = lOverplus.iterator();
		Overplus result = new Overplus();
		while( iter.hasNext() ){
			Overplus o = iter.next();
			result.merge(o);
		}
		return result;
	}

	private Overplus getOverplusData(GVRKUms umsaetze) {
		Calendar calendar = Calendar.getInstance();
		int maxTageMonat = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		int vergangeneTageMonat = calendar.get(Calendar.DAY_OF_MONTH);
		Double einnahmen = new Double(0) ;
		Double ausgaben = new Double(0) ;

		List dpd = umsaetze.getDataPerDay();

		Iterator<GVRKUms.BTag> iter = dpd.iterator();

		while (iter.hasNext()) { // every Umsatz will checked against
			GVRKUms.BTag d = iter.next();
			String umsatzBTag = d.toString();
			Iterator<GVRKUms.UmsLine> iterLines = d.lines.iterator();
			while (iterLines.hasNext()) {
				GVRKUms.UmsLine line = iterLines.next();
				String usage = line.usage.toString().toLowerCase();
				Double dbetrag = line.value.getDoubleValue();
				
				if ( 0 > dbetrag) { //ausgabe
					
					ausgaben = ausgaben + dbetrag;
				}
				if (  0 < dbetrag) { //einnahme
					einnahmen = einnahmen +  dbetrag;
				}
				System.out.println(" §§§§§§§§§§§§§§§§§§§§§§§§§§§§  betrag: " + dbetrag   +  " §§§§§  einnahmen: "+einnahmen   + " §§§§§  ausgaben: "+ausgaben  + "  §§§§§ maxTageMonat: "+maxTageMonat  + "  §§§§§ vergangeneTageMonat:"+vergangeneTageMonat);

			}
		}

		int iEinnahmen = (int) einnahmen.doubleValue();
		int iAusgaben = (int) ausgaben.doubleValue();
		

		Overplus result = new Overplus(maxTageMonat, einnahmen,  iAusgaben, vergangeneTageMonat );
		
		
		return result;
	}

}
