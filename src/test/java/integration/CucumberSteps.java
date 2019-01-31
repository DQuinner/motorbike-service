package integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import ie.dq.motorbike.domain.Motorbike;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.*;

import static junit.framework.TestCase.assertNotNull;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

public class CucumberSteps extends CucumberRoot {

    private ResponseEntity responseEntity;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Given("no motorbikes exist in the database")
    public void no_motorbikes_exist_in_the_database() throws Throwable {
        responseEntity = restTemplate.getForEntity("/motorbikes", String.class);
        assertNull(responseEntity.getBody());
        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
    }

    @Given("motorbike exists in the database of {string} {string}")
    public void motorbike_exists_in_the_database_of(String make, String model) throws Throwable {
        responseEntity = restTemplate.getForEntity("/motorbikes", String.class);
        assertNotNull(responseEntity.getBody());
        JSONObject jsonMotorbike = getJSONMotorbike(make, model);
        assertEquals(make, jsonMotorbike.getString("make"));
        assertEquals(model, jsonMotorbike.getString("model"));
    }

    @When("the client calls GET {string}")
    public void the_client_issues_GET_url(String url) throws Throwable {
        responseEntity = restTemplate.getForEntity(url, String.class);
        assertNotNull(responseEntity);
    }

    @When("the client calls POST /motorbikes with motorbike of {string} {string} {string}")
    public void the_client_calls_POST_motorbike_of(String make, String model, String type) throws Throwable {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        Motorbike motorbike = new Motorbike();
        motorbike.setMake(make);
        motorbike.setModel(model);
        motorbike.setType(type);
        responseEntity = restTemplate.postForEntity("/motorbikes", new HttpEntity<>(motorbike, headers), String.class);
        assertNotNull(responseEntity);
    }

    @Then("the client receives response status code of {int}")
    public void the_client_receives_status_code_of(int statusCode) throws Throwable {
        HttpStatus currentStatusCode = responseEntity.getStatusCode();
        assertThat("status code is incorrect : " +responseEntity.getBody(), currentStatusCode.value(), is(statusCode));
    }

    @Then("the response body is empty")
    public void the_response_body_is_empty() throws Throwable {
        assertNull(responseEntity.getBody());
    }

    @Then("the response body contains created motorbike of {string} {string} {string}")
    public void the_response_body_contains_created_motorbike_of(String make, String model, String type) throws Throwable {
        String responseBody = (String) responseEntity.getBody();
        assertNotNull(responseBody);
        JSONObject createdMotorbike = new JSONObject(responseBody);
        assertTrue(0l<createdMotorbike.getLong("id"));
        assertEquals(make, createdMotorbike.getString("make"));
        assertEquals(model, createdMotorbike.getString("model"));
        assertEquals(type, createdMotorbike.getString("type"));
    }

    @Then("the response body contains array with created motorbike of {string} {string} {string}")
    public void the_response_body_contains_array_with_created_motorbike_of(String make, String model, String type) throws Throwable {
        String responseBody = (String) responseEntity.getBody();
        assertNotNull(responseBody);
        JSONObject createdMotorbike = getJSONMotorbike(make, model);
        assertTrue(0l<createdMotorbike.getLong("id"));
        assertEquals(make, createdMotorbike.getString("make"));
        assertEquals(model, createdMotorbike.getString("model"));
        assertEquals(type, createdMotorbike.getString("type"));
    }

    private JSONObject getJSONMotorbike(String make, String model) throws JSONException {
        String responseBody = (String) responseEntity.getBody();
        JSONArray jsonMotorbikes = new JSONObject(responseBody).getJSONArray("content");
        for(int i = 0; i<jsonMotorbikes.length(); i++){
            JSONObject jsonMotorbike = (JSONObject) jsonMotorbikes.get(i);
            if(make.equalsIgnoreCase(jsonMotorbike.getString("make")) && model.equalsIgnoreCase(jsonMotorbike.getString("model"))){
                return  jsonMotorbike;
            }
        }
        return new JSONObject();
    }
}
