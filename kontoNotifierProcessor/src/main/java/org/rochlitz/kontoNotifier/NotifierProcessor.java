package org.rochlitz.kontoNotifier;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import javax.annotation.Resource;
import javax.enterprise.concurrent.ManagedExecutorService;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.rochlitz.kontoNotfier.persistence.CdiDao;
import org.rochlitz.kontoNotfier.persistence.NotifierDTO;

/**
 * @author aroc
 *
 *         reads nots from db and put them to queue and waits until queue has
 *         mor space or time is for next sceduling
 * @param <NotfierCallableTask>
 * 
 */

//@Singleton
@Named("NotifierProcessor")
@ApplicationScoped
public class NotifierProcessor {

	@Resource
	private ManagedExecutorService executorService;

	// @Inject
	// private Logger logger;

	@Inject
	private CdiDao dao;
	
	public void processing() {
		try {

			@SuppressWarnings("unchecked")
			List<NotifierDTO> nots = dao.getAll(new NotifierDTO());
			Collection<NotfierCallableTask> notTasks = new ArrayList<NotfierCallableTask>();

			Iterator<NotifierDTO> iter = nots.iterator();
			while(iter.hasNext()){
				notTasks.add( new NotfierCallableTask(  iter.next() )  );
			}
			
			notTasks.addAll( notTasks);

		  executorService.invokeAll(  notTasks);


		} catch (Exception e) {
			// logger.log(Level.SEVERE, e.getMessage(), e);
			e.printStackTrace();
			
			//TODO config : queue-length="1000" at <managed-executor-service
		}
	}

	private List<Integer> get(Future<List<Integer>> future) {
		try {
			return future.get();
		} catch (InterruptedException | ExecutionException e) {
			// logger.log(Level.SEVERE, e.getMessage(), e);
			return new ArrayList<>();
		}
	}

}
