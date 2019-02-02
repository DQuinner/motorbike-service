package acceptance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

@ActiveProfiles("ACCEPTANCE_TEST")
@ContextConfiguration(classes = AcceptanceTestConfig.class)
public class AcceptanceTest {

    @Autowired
    protected TestRestTemplate restTemplate;

}
