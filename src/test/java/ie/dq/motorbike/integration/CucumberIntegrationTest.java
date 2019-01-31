package ie.dq.motorbike.integration;

import ie.dq.motorbike.category.Integration;
import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@Category(Integration.class)
@CucumberOptions(features = {"src/test/resources/features"}, plugin = {"pretty", "html:build/cucumber"})
public class CucumberIntegrationTest {
}
