package ie.dq.motorbike.integration;

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
        assertNotNull(responseEntity.getBody());
        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
    }

    @Given("motorbike exists in the database of {string} {string}")
    public void motorbike_exists_in_the_database_of(String make, String model) throws Throwable {
        responseEntity = restTemplate.getForEntity("/motorbikes", String.class);
        assertNotNull(responseEntity.getBody());
        JSONObject jsonMotorbike = getJSONMotorbikeResponseBody(make, model);
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
        responseEntity = restTemplate.postForEntity("/motorbikes", new HttpEntity<>(motorbike(make,model,type), headers), String.class);
        assertNotNull(responseEntity);
    }

    @Then("the client receives response status code of {int}")
    public void the_client_receives_status_code_of(int statusCode) throws Throwable {
        HttpStatus currentStatusCode = responseEntity.getStatusCode();
        assertThat("status code is incorrect : " +responseEntity.getBody(), currentStatusCode.value(), is(statusCode));
    }

    @Then("the health status is UP")
    public void the_health_status_is_up() throws Throwable {
        assertNotNull(responseEntity.getBody());
        assertTrue(String.valueOf(responseEntity.getBody()).contains("\"status\" : \"UP\""));
    }

    @Then("the response body is empty")
    public void the_response_body_is_empty() throws Throwable {
        assertNull(responseEntity.getBody());
    }

    @Then("the response body contains created motorbike of {string} {string} {string}")
    public void the_response_body_contains_created_motorbike_of(String make, String model, String type) throws Throwable {
        assertNotNull(responseEntity.getBody());
        JSONObject createdMotorbike = new JSONObject(String.valueOf(responseEntity.getBody()));
        assertTrue(0l<createdMotorbike.getLong("id"));
        assertEquals(make, createdMotorbike.getString("make"));
        assertEquals(model, createdMotorbike.getString("model"));
        assertEquals(type, createdMotorbike.getString("type"));
    }

    @Then("the response body contains array with created motorbike of {string} {string} {string}")
    public void the_response_body_contains_array_with_created_motorbike_of(String make, String model, String type) throws Throwable {
        assertNotNull(responseEntity.getBody());
        JSONObject createdMotorbike = getJSONMotorbikeResponseBody(make, model);
        assertTrue(0l<createdMotorbike.getLong("id"));
        assertEquals(make, createdMotorbike.getString("make"));
        assertEquals(model, createdMotorbike.getString("model"));
        assertEquals(type, createdMotorbike.getString("type"));
    }

    private JSONObject getJSONMotorbikeResponseBody(String make, String model) throws JSONException {
        JSONArray jsonMotorbikes = new JSONObject(String.valueOf(responseEntity.getBody())).getJSONArray("content");
        for(int i = 0; i<jsonMotorbikes.length(); i++){
            JSONObject jsonMotorbike = jsonMotorbikes.getJSONObject(i);
            if(make.equalsIgnoreCase(jsonMotorbike.getString("make")) && model.equalsIgnoreCase(jsonMotorbike.getString("model"))){
                return jsonMotorbike;
            }
        }
        return new JSONObject();
    }

    private Motorbike motorbike(String make, String model, String type){
        Motorbike motorbike = new Motorbike();
        motorbike.setMake(make);
        motorbike.setModel(model);
        motorbike.setType(type);
        return motorbike;
    }
}
