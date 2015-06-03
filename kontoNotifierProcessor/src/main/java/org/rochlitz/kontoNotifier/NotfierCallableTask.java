package org.rochlitz.kontoNotifier;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;

import org.kapott.hbci.GV_Result.GVRKUms;
import org.rochlitz.hbci.tests.web.GVBase;
import org.rochlitz.hbci.tests.web.MyCallback;
import org.rochlitz.hbci.tests.web.TestKontoAuszugThreaded;
import org.rochlitz.kontoNotfier.persistence.NotifierDTO;

public class NotfierCallableTask implements Callable<Boolean> {

	private NotifierDTO not;

	public NotfierCallableTask(NotifierDTO not) {
		this.not = not;
	}

	@Override
	public Boolean call() {
		try {
			// logger.info("Sleeping...");
			// Thread.sleep(5000);
			// logger.info("Finished	sleeping!");

			MyCallback mc = new MyCallback(not);
			TestKontoAuszugThreaded t = new TestKontoAuszugThreaded(mc);
			GVRKUms result = t.getAuszug();
			List dpd = result.getDataPerDay();
			
			Iterator<GVRKUms.BTag> iter = dpd.iterator();
			
			while(iter.hasNext()){
				GVRKUms.BTag d = iter.next();
				String str = d.toString();
				Iterator<GVRKUms.UmsLine> iterLines = d.lines.iterator();
				while(iterLines.hasNext()){
					GVRKUms.UmsLine line = iterLines.next();
					String usage = line.usage.toString().toLowerCase();
					if( 0 <=  usage.indexOf(not.getFilter().getSearch().toLowerCase()) ){ //-1 not found
						//TODO found message
						
						System.out.println(" *********************** FOUND  *******************");
						System.out.println(" " + usage);
						System.out.println(" *********************** FOUND  *******************");
					}
				}
				
				boolean compaerRes = d.end.timestamp.before(new Date() ); //is day before today
				str.isEmpty();
			}
			
			
			System.out.println("process task for Not id " + not.getId()
					+ "   filter - " + not.getFilter().getSearch());
			
			// logger.info("process task for Not id " + not.getId());
			// TODO hbci execute
			// TODO JMS , email
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new Boolean(true); // sucess
	}
}