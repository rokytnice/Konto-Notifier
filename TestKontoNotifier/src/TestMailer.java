import org.junit.Before;
import org.junit.Test;
import org.rochlitz.kontoNotfier.message.EMailer;
import org.rochlitz.kontoNotfier.persistence.FilterDTO;
import org.rochlitz.kontoNotfier.persistence.NotifierDTO;
import org.rochlitz.kontoNotfier.persistence.UserDTO;


public class TestMailer {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void test() {
		EMailer mail = new EMailer();
		NotifierDTO not = new NotifierDTO();
		UserDTO user = new UserDTO();
		user.setEmail("andre.rochlitz@gmail.com");
		FilterDTO filter = new FilterDTO();
		filter.setSearch("test suche ");
		not.setFilter(filter);
		not.setUser(user);
		mail.mail("junit usage ", not);
	}

}
