package ie.dq.acceptance.motorbike;

import cucumber.api.CucumberOptions;
import net.serenitybdd.cucumber.CucumberWithSerenity;
import org.junit.runner.RunWith;

@RunWith(CucumberWithSerenity.class)
@CucumberOptions(features = {"src/test/resources/features"}, plugin = {"pretty", "html:build/cucumber"})
public class CucumberRunner {
}
