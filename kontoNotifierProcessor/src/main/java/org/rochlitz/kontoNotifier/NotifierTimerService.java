package org.rochlitz.kontoNotifier;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

@ApplicationScoped
@Named("NotifierTimerService")
public class NotifierTimerService {

//	@Inject
//	NotifierProcessor notCon;
//
//	@Schedule(second="*/10", minute="*",hour="*", persistent=false)
//	@PostConstruct
//    public void doWork(){
//        System.out.println(" Timer: run  " + new GregorianCalendar().toString()  );
//        notCon.processing();
//    }

}
