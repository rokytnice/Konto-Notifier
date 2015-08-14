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
import org.rochlitz.kontoNotfier.persistence.FilterDTO;
import org.rochlitz.kontoNotfier.persistence.KontoDTO;
import org.rochlitz.kontoNotfier.persistence.UserDTO;

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
			List<UserDTO> users = dao.getAll(new UserDTO());
			Collection<NotfierCallableTask> notTasks = new ArrayList<NotfierCallableTask>();

			Iterator<UserDTO> iter = users.iterator();
			while(iter.hasNext()){
				UserDTO user = iter.next();
				List<KontoDTO> konten = dao.getKontenOfUser(user);
				Iterator<KontoDTO> iter1 = konten.iterator();
				while(iter1.hasNext()){
					KontoDTO konto = iter1.next();
					
					Iterator<FilterDTO> iter2 = dao.getFilterOfUser(konto).iterator();
//					notTasks.add( new NotfierCallableTask( iter2.next()  )  );
					FilterDTO filter = iter2.next();
					
					
					NotfierCallableTask n = new NotfierCallableTask( filter , user , konto);
					
					n.call();
				}
			}
			
//			notTasks.addAll( notTasks);

			//TODO clean up code  --- beacause disabled threading - ist schon gelöst in HBCICallbackThreaded
//		  executorService.invokeAll(  notTasks);


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
