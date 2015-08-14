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
import org.rochlitz.kontoNotfier.persistence.FilterDTO;
import org.rochlitz.kontoNotfier.persistence.KontoDTO;
import org.rochlitz.kontoNotfier.persistence.UserDTO;

@Named("NotfierCallableTask")
public class NotfierCallableTask implements Callable<Boolean> {

	private FilterDTO filter;
	private UserDTO user;
	private KontoDTO konto;

	public NotfierCallableTask(FilterDTO not, UserDTO user, KontoDTO konto) {
		this.filter = not;
		this.user = user;
		this.konto = konto;
	}
 

	@Override
	public Boolean call() {
		try {
			// logger.info("Sleeping...");
			// Thread.sleep(5000);
			// logger.info("Finished	sleeping!");
			
			MyCallback mc = new MyCallback( konto, user);
			
			System.out.println(" +++++++++++++++++++  callable task user " + user.getEmail()   + " -filter: " +filter.getId() + "  konto : "  + konto.getKtonr()  );
			
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

					if( filter.getSearch() != null ){
						if( 0 <=  usage.indexOf(filter.getSearch().toLowerCase()) ){ //-1 not found
							//TODO found message
							message.append(line+"\n\n");
						}
					}
					if( filter.getMinValue() != null ){
						if( filter.getMinValue() > line.value.getLongValue() ){ //-1 not found
							//TODO found message
							message.append(line+"\n\n");
						}
					}
					if( filter.getMaxValue()  != null  ){
						if(  filter.getMaxValue() < line.value.getLongValue() ){ //-1 not found
							//TODO found message
							message.append(line+"\n\n");
						}
					}
				}
				
				
				EMailer.mail(message.toString(), filter, user);
				
				boolean compaerRes = d.end.timestamp.before(new Date() ); //is day before today
				str.isEmpty();
			}
			
			
			System.out.println("process task for Not id " + filter.getId()
					+ "   filter - " + filter.getSearch());
			
			
			HBCIUtils.doneThread();//clean up data structure - need to be done for new baking connection
			// logger.info("process task for Not id " + not.getId());
			// TODO hbci execute
			// TODO JMS , email
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			HBCIUtils.doneThread();
		}
		return new Boolean(true); // sucess
	}
}