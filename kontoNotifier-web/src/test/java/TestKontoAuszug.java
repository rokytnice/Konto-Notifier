import java.util.Calendar;
import java.util.GregorianCalendar;

import org.junit.Test;
import org.rochlitz.hbci.tests.web.KontoAuszugThreaded;
import org.rochlitz.hbci.tests.web.MyCallback;
import org.rochlitz.kontoNotfier.persistence.KontoDTO;
import org.rochlitz.kontoNotfier.persistence.UserDTO;


public class TestKontoAuszug {

	@Test
	public void test() {
		KontoDTO konto = new KontoDTO("ktonr=4900585&blz=20041133&account=30746930&password=741896");
		UserDTO user = new UserDTO();
		user.setEmail("andre.rochlitz@gmailcom");
		MyCallback mc = new MyCallback(konto,user);
		KontoAuszugThreaded t = new KontoAuszugThreaded(mc);
		 Calendar calStart = new GregorianCalendar();
	        Calendar calEnd = new GregorianCalendar();
	        calStart.add(Calendar.DAY_OF_MONTH, KontoAuszugThreaded.DAY_OFFSET);
//	        calEnd.add(Calendar.DAY_OF_MONTH, DAY_OFFSET);
		t.getAuszug(calStart, calEnd);
		assert(true);
	}

}
