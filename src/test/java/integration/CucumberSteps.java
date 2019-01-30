package integration;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import ie.dq.motorbike.domain.Motorbike;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import unit.ie.dq.motorbike.util.MotorbikeTestData;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class CucumberSteps extends CucumberRoot {

    private ResponseEntity response;

    @When("^the client calls GET /health$")
    public void the_client_issues_GET_health() throws Throwable {
        response = restTemplate.getForEntity("/actuator/health", String.class);
    }

    @When("^the client calls GET /motorbikes$")
    public void the_client_issues_GET_motorbikes() throws Throwable {
        response = restTemplate.getForEntity("/motorbikes", String.class);
    }

    @When("^the client calls POST /motorbikes$")
    public void the_client_issues_POST_motorbikes() throws Throwable {
        response = restTemplate.postForEntity("/motorbikes", MotorbikeTestData.newMotorbike(), Motorbike.class);
    }

    @Then("^the client receives response status code of (\\d+)$")
    public void the_client_receives_status_code_of(int statusCode) throws Throwable {
        HttpStatus currentStatusCode = response.getStatusCode();

        assertThat("status code is incorrect : " +response.getBody(), currentStatusCode.value(), is(statusCode));
    }
}
