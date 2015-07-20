import org.junit.Test;
import org.rochlitz.kontoNotfier.persistence.KontoDTO;


public class TestDTO {

	@Test
	public void test() {
		
		KontoDTO k = new KontoDTO("ktonr=1&blz=1&account=2&password=1");
		assert(true);
		
	}

}
