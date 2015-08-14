import org.junit.Before;
import org.junit.Test;
import org.rochlitz.kontoNotfier.message.EMailer;
import org.rochlitz.kontoNotfier.persistence.FilterDTO;
import org.rochlitz.kontoNotfier.persistence.KontoDTO;
import org.rochlitz.kontoNotfier.persistence.UserDTO;


public class TestMailer {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void test() {
		EMailer mail = new EMailer();
		UserDTO user = new UserDTO();
		KontoDTO konto = new KontoDTO();
//		konto.setUser(user);
		user.setEmail("andre.rochlitz@gmail.com");
		FilterDTO filter = new FilterDTO();
//		filter.setKonto(konto);
		filter.setSearch("test suche ");
		mail.mail("junit usage ", filter,user);
	}

}
