package org.rochlitz.kontoNotifier;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;

import javax.inject.Named;

import org.kapott.hbci.GV_Result.GVRKUms;
import org.kapott.hbci.manager.HBCIUtils;
import org.rochlitz.hbci.tests.web.MyCallback;
import org.rochlitz.hbci.tests.web.KontoAuszugThreaded;
import org.rochlitz.kontoNotfier.message.EMailer;
import org.rochlitz.kontoNotfier.persistence.NotifierDTO;

@Named("NotfierCallableTask")
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
			
			HBCIUtils.done();
			MyCallback mc = new MyCallback(not.getKonto());
			
			System.out.println(" +++++++++++++++++++  callable task user " + not.getUser().getEmail()  + " - " +not.getUser().getId() + "  konto : "  + not.getKonto().getKtonr()  );
			
			KontoAuszugThreaded t = new KontoAuszugThreaded(mc);
			GVRKUms result = t.getAuszug();
			List dpd = result.getDataPerDay();
			
			Iterator<GVRKUms.BTag> iter = dpd.iterator();
			
			StringBuffer message = new StringBuffer();
			
			while(iter.hasNext()){
				GVRKUms.BTag d = iter.next();
				String str = d.toString();
				Iterator<GVRKUms.UmsLine> iterLines = d.lines.iterator();
				while(iterLines.hasNext()){
					GVRKUms.UmsLine line = iterLines.next();
					String usage = line.usage.toString().toLowerCase();

					if( not.getFilter().getSearch() != null ){
						if( 0 <=  usage.indexOf(not.getFilter().getSearch().toLowerCase()) ){ //-1 not found
							//TODO found message
							message.append(line+"\n\n");
						}
					}
					if( not.getFilter().getMinValue() != null ){
						if( not.getFilter().getMinValue() > line.value.getLongValue() ){ //-1 not found
							//TODO found message
							message.append(line+"\n\n");
						}
					}
					if( not.getFilter().getMaxValue()  != null  ){
						if(  not.getFilter().getMaxValue() < line.value.getLongValue() ){ //-1 not found
							//TODO found message
							message.append(line+"\n\n");
						}
					}
				}
				
				
				EMailer.mail(message.toString(), not);
				
				boolean compaerRes = d.end.timestamp.before(new Date() ); //is day before today
				str.isEmpty();
			}
			
			
			System.out.println("process task for Not id " + not.getId()
					+ "   filter - " + not.getFilter().getSearch());
			
			
			HBCIUtils.done();//clean up data structure - need to be done for new baking connection
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