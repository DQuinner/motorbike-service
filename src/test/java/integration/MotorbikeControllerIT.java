package integration;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static junit.framework.TestCase.assertTrue;

@RunWith(SpringRunner.class)
@ActiveProfiles("INTEGRATION_TEST")
public class MotorbikeControllerIT {

    @Test
    public void sampleIntegrationTest(){
        assertTrue(true);
    }

}
