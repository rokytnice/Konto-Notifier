import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.ejb.embeddable.EJBContainer;

import org.junit.Before;
import org.junit.Test;


public class TestOverplus {
	
	   @Before  
	    public void setUp(){  
//	        EJBContainer container = EJBContainer.createEJBContainer();  
	    }  

	@Test
	public void test() {
//		fail("Not yet implemented");
		Calendar today = new GregorianCalendar();
		Calendar calStart = new GregorianCalendar();
		Calendar calEnd = new GregorianCalendar();
//		calStart.set(Calendar.DAY_OF_MONTH, user.getSettingsAomatStartDay() );
		
	 
		int monthOfStartDate = (today.get(Calendar.MONTH)-1);
			
		calStart.set(Calendar.MONTH, monthOfStartDate );
		
		System.out.println( "§§§§§ "+ monthOfStartDate );
		
		System.out.println("today: " + today.getTime() );
		System.out.println( "calStart: " + calStart.getTime() );
		System.out.println( "calEnd: "+ calEnd.getTime()  );
	}

}
