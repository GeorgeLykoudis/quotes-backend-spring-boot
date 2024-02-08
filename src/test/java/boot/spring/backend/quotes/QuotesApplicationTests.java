package boot.spring.backend.quotes;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles({"mysql", "redis"})
@SpringBootTest
class QuotesApplicationTests {

	@Test
	void contextLoads() {
	}

}
