package org.rochlitz.kontoNotifier;

import java.util.concurrent.Callable;

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
			t.getAuszug();
			
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