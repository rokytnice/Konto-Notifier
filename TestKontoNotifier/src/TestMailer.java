import org.junit.Before;
import org.junit.Test;
import org.rochlitz.kontoNotfier.message.EMailer;


public class TestMailer {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void test() {
		EMailer mail = new EMailer();
		mail.mail("test $$$$$$$$$$$$$$$$$$$$$ ###################### ","andre.rochlitz@gmail.com");
	}

}
