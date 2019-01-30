package integration;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.springframework.http.*;

import static junit.framework.TestCase.assertNotNull;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

public class CucumberSteps extends CucumberRoot {

    private ResponseEntity responseEntity;

    @Given("no motorbikes exist in the database")
    public void no_motorbikes_exist() throws Throwable {
        responseEntity = restTemplate.getForEntity("/motorbikes", String.class);
        assertNull(responseEntity.getBody());
        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
    }

    //2DO Improve this logic to check for specific bike
    @Given("motorbike with make {string} and model {string} exists in the database")
    public void motorbike_exist(String make, String model) throws Throwable {
        responseEntity = restTemplate.getForEntity("/motorbikes", String.class);
        assertNotNull(responseEntity.getBody());
    }

    @When("the client calls GET {string}")
    public void the_client_issues_GET_url(String url) throws Throwable {
        responseEntity = restTemplate.getForEntity(url, String.class);
        assertNotNull(responseEntity);
    }

    @When("the client calls POST {string} with body {string}")
    public void the_client_issues_POST_url_with_body(String url, String body) throws Throwable {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        responseEntity = restTemplate.postForEntity(url, new HttpEntity<>(body, headers), String.class);
        assertNotNull(responseEntity);
    }

    @Then("the client receives response status code of {int}")
    public void the_client_receives_status_code_of(int statusCode) throws Throwable {
        HttpStatus currentStatusCode = responseEntity.getStatusCode();
        assertThat("status code is incorrect : " +responseEntity.getBody(), currentStatusCode.value(), is(statusCode));
    }
}
