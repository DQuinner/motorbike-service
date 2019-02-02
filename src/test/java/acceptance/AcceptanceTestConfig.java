package acceptance;

import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AcceptanceTestConfig {

    @Bean
    public TestRestTemplate restTemplate(){
        return new TestRestTemplate();
    }

}
