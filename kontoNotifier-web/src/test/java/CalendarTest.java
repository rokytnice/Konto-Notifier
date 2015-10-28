import java.util.Calendar;
import java.util.GregorianCalendar;

import org.junit.Test;
import org.rochlitz.kontoNotfier.persistence.UserDTO;


public class CalendarTest {

	@Test
	public void test() {
//		fail("Not yet implemented");
		UserDTO user = new UserDTO();
		
		Calendar today = new GregorianCalendar();
		Calendar calStart = new GregorianCalendar();
		Calendar calEnd = new GregorianCalendar();
		
		System.out.println("today: " + today  );
		System.out.println( "calStart: " + calStart  );
		System.out.println( "calEnd: "+ calEnd  );
		
	}

}
