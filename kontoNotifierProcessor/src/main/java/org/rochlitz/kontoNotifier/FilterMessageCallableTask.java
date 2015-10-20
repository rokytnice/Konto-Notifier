package org.rochlitz.kontoNotifier;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;

import javax.inject.Inject;
import javax.inject.Named;

import org.kapott.hbci.GV_Result.GVRKUms;
import org.kapott.hbci.manager.HBCIUtils;
import org.rochlitz.hbci.tests.web.GVBase;
import org.rochlitz.hbci.tests.web.KontoAuszugThreaded;
import org.rochlitz.hbci.tests.web.MyCallback;
import org.rochlitz.kontoNoitfier.persistence.CdiDao;
import org.rochlitz.kontoNotfier.persistence.FilterDTO;
import org.rochlitz.kontoNotfier.persistence.KontoDTO;
import org.rochlitz.kontoNotfier.persistence.KontoauszugDTO;
import org.rochlitz.kontoNotfier.persistence.UserDTO;

@Named("NotfierCallableTask")
public class FilterMessageCallableTask implements Callable<KontoauszugDTO> {

	private List<FilterDTO> filters;
	private UserDTO user;
	private KontoDTO konto;
	@Inject
	private CdiDao dao;

	public FilterMessageCallableTask(List<FilterDTO> filters, UserDTO user,
			KontoDTO konto) {
		this.filters = filters;
		this.user = user;
		this.konto = konto;
	}

	@Override
	public KontoauszugDTO call() {
		KontoauszugDTO result = null;

		try {
			// logger.info("Sleeping...");
			// Thread.sleep(5000);
			// logger.info("Finished	sleeping!");

			MyCallback myCallback = new MyCallback(konto, user);

			KontoAuszugThreaded kontoAuzugThread = new KontoAuszugThreaded(
					myCallback);
			 Calendar calStart = new GregorianCalendar();
		        Calendar calEnd = new GregorianCalendar();
		        calStart.add(Calendar.DAY_OF_MONTH, KontoAuszugThreaded.DAY_OFFSET);
			GVRKUms umsaetze = kontoAuzugThread.getAuszug(calStart, calEnd);
			List dpd = umsaetze.getDataPerDay();
			StringBuffer finalMessage = new StringBuffer();
			Iterator<FilterDTO> iter2 = filters.iterator();

			while (iter2.hasNext()) { // for all filter of all kontos of all
										// user, compare all kontoauszüge with
										// all filters
				FilterDTO filter = iter2.next();
				System.out.println(" +++++++++++++++++++  callable task user "
						+ user.getEmail() + " -filter: " + filter.getId()
						+ "  konto: " + konto.getKtonr());

				Iterator<GVRKUms.BTag> iter = dpd.iterator();

				StringBuffer messageForFilter = new StringBuffer();
				boolean messageFilterHeadAdded = false;

				while (iter.hasNext()) { // every Umsatz will checked against
					GVRKUms.BTag d = iter.next();
					String umsatzBTag = d.toString();
					Iterator<GVRKUms.UmsLine> iterLines = d.lines.iterator();
					while (iterLines.hasNext()) {
						GVRKUms.UmsLine line = iterLines.next();
						String usage = line.usage.toString().toLowerCase();
						StringBuffer message = new StringBuffer();

						if (filter.getSearch() != null) {
							if (0 <= usage.indexOf(filter.getSearch()
									.toLowerCase())) { // -1 not found
								// TODO found message
								message.append(line + "\n\n");
							}
						}
						if (filter.getMinValue() != null) {
							if (filter.getMinValue() > line.value
									.getLongValue()) { // -1 not found
								// TODO found message
								message.append(line + "\n\n");
							}
						}
						if (filter.getMaxValue() != null) {
							if (filter.getMaxValue() < line.value
									.getLongValue()) { // -1 not found
								// TODO found message
								message.append(line + "\n\n");
							}
						}

						if (message.length() > 0) {
							if (!messageFilterHeadAdded) {
								String filterSubject = " \n\n <b>Suchtext: "
										+ filter.getSearch()
										+ "\n Minimaler Betrag:  "
										+ filter.getMinValue()
										+ "\n Maximaler Betrag:  "
										+ filter.getMaxValue() + "\n\n  </b>";
								messageForFilter.append(filterSubject);

								messageFilterHeadAdded = true;
							}
							messageForFilter.append(message);
						}
					}
				}
				if (messageForFilter.length() > 0) {
					finalMessage.append("<hr>"+messageForFilter);
				}
			}

			if (finalMessage.length() > 0) {
				// refactor to utility

				String subject = "Zeitraum von "
						+ calStart.getTime() + " bis " + calEnd.getTime() + " ";
				// EMailer.mail(finalMessage.toString(), user, subject);//send
				// mail
				String htmlizedMessage = finalMessage.toString().replaceAll("\n", "<br>").replace("null", "");
				result = new KontoauszugDTO(htmlizedMessage, user,
						subject);
			}

			HBCIUtils.doneThread();// clean up data structure - need to be done
									// for new baking connection
			// logger.info("process task for Not id " + not.getId());
			// TODO hbci execute
			// TODO JMS , email

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			HBCIUtils.doneThread();
		}

		return result;
	}
}