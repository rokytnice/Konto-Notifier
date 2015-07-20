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
		konto.setUser(user);
		MyCallback mc = new MyCallback(konto);
		KontoAuszugThreaded t = new KontoAuszugThreaded(mc);
		t.getAuszug();
		assert(true);
	}

}
