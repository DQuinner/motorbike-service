package ie.dq.acceptance.motorbike;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import ie.dq.motorbike.domain.Motorbike;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

import static junit.framework.TestCase.assertNotNull;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

@ActiveProfiles("ACCEPTANCE_TEST")
@ContextConfiguration(classes = AcceptanceTestConfig.class)
@TestPropertySource(locations="classpath:application-test.properties")
public class CucumberSteps {

    @Autowired
    protected TestRestTemplate restTemplate;

    @Value("${acceptance.test.host}")
    private String testHost;

    @Value("${acceptance.test.port}")
    private String testPort;

    private ResponseEntity responseEntity;

    @Given("^no motorbikes exist in the database$")
    public void no_motorbikes_exist_in_the_database() throws Throwable {
        responseEntity = restTemplate.getForEntity(testEndpoint()+"/motorbikes", String.class);
        assertNull(responseEntity.getBody());
        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
    }

    @Given("^motorbike exists in the database of \"([^\"]+)\" \"([^\"]+)\" \"([^\"]+)\"$")
    public void motorbike_exists_in_the_database_of(String make, String model, int engine) throws Throwable {
        responseEntity = restTemplate.getForEntity(testEndpoint()+"/motorbikes", String.class);
        assertNotNull(responseEntity.getBody());
        JSONObject jsonMotorbike = getJSONMotorbikeResponseBody(make, model, engine);
        assertEquals(make, jsonMotorbike.getString("make"));
        assertEquals(model, jsonMotorbike.getString("model"));
        assertEquals(engine, jsonMotorbike.getInt("engine"));
    }

    @Given("^no motorbike exists in the database of \"([^\"]+)\" \"([^\"]+)\" \"([^\"]+)\"$")
    public void no_motorbike_exists_in_the_database_of(String make, String model, int engine) throws Throwable {
        responseEntity = restTemplate.getForEntity(testEndpoint()+"/motorbikes", String.class);
        if(responseEntity.getBody()!=null){
            JSONObject createdMotorbike = getJSONMotorbikeResponseBody(make, model, engine);
            assertEquals(0, createdMotorbike.length());
        }else {
            assertNull(responseEntity.getBody());
        }
    }

    @When("^the client calls GET (.+)$")
    public void the_client_issues_GET_url(String url) throws Throwable {
        responseEntity = restTemplate.getForEntity(testEndpoint()+url, String.class);
        assertNotNull(responseEntity);
    }

    @When("^the client calls POST /motorbikes with motorbike of \"([^\"]+)\" \"([^\"]+)\" \"([^\"]+)\" \"([^\"]+)\"$")
    public void the_client_calls_POST_motorbike_of(String make, String model, String type, int engine) throws Throwable {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        responseEntity = restTemplate.postForEntity(testEndpoint()+"/motorbikes", new HttpEntity<>(motorbike(make,model,type,engine), headers), String.class);
        assertNotNull(responseEntity);
    }

    @Then("^the client receives response status code of (.+)$")
    public void the_client_receives_status_code_of(int statusCode) throws Throwable {
        HttpStatus currentStatusCode = responseEntity.getStatusCode();
        assertThat("status code is incorrect : " +responseEntity.getBody(), currentStatusCode.value(), is(statusCode));
    }

    @Then("^the health status is UP$")
    public void the_health_status_is_up() throws Throwable {
        assertNotNull(responseEntity.getBody());
        assertTrue(String.valueOf(responseEntity.getBody()).contains("\"status\" : \"UP\""));
    }

    @Then("^the response body is empty$")
    public void the_response_body_is_empty() throws Throwable {
        assertNull(responseEntity.getBody());
    }

    @Then("^the response body contains created motorbike of \"([^\"]+)\" \"([^\"]+)\" \"([^\"]+)\" \"([^\"]+)\"$")
    public void the_response_body_contains_created_motorbike_of(String make, String model, String type, int engine) throws Throwable {
        assertNotNull(responseEntity.getBody());
        JSONObject createdMotorbike = new JSONObject(String.valueOf(responseEntity.getBody()));
        assertTrue(0l<createdMotorbike.getLong("id"));
        assertEquals(make, createdMotorbike.getString("make"));
        assertEquals(model, createdMotorbike.getString("model"));
        assertEquals(type, createdMotorbike.getString("type"));
        assertEquals(engine, createdMotorbike.getInt("engine"));
    }

    @Then("^the response body contains array with created motorbike of \"([^\"]+)\" \"([^\"]+)\" \"([^\"]+)\" \"([^\"]+)\"$")
    public void the_response_body_contains_array_with_created_motorbike_of(String make, String model, String type, int engine) throws Throwable {
        assertNotNull(responseEntity.getBody());
        JSONObject createdMotorbike = getJSONMotorbikeResponseBody(make, model, engine);
        assertTrue(0l<createdMotorbike.getLong("id"));
        assertEquals(make, createdMotorbike.getString("make"));
        assertEquals(model, createdMotorbike.getString("model"));
        assertEquals(type, createdMotorbike.getString("type"));
        assertEquals(engine, createdMotorbike.getInt("engine"));
    }

    private String testEndpoint(){
        return "http://"+testHost+":"+testPort;
    }

    private JSONObject getJSONMotorbikeResponseBody(String make, String model, int engine) throws JSONException {
        JSONArray jsonMotorbikes = new JSONObject(String.valueOf(responseEntity.getBody())).getJSONArray("content");
        for(int i = 0; i<jsonMotorbikes.length(); i++){
            JSONObject jsonMotorbike = jsonMotorbikes.getJSONObject(i);
            if(make.equalsIgnoreCase(jsonMotorbike.getString("make"))
                    && model.equalsIgnoreCase(jsonMotorbike.getString("model")) && engine==jsonMotorbike.getInt("engine")){
                return jsonMotorbike;
            }
        }
        return new JSONObject();
    }

    private Motorbike motorbike(String make, String model, String type, int engine){
        Motorbike motorbike = new Motorbike();
        motorbike.setMake(make);
        motorbike.setModel(model);
        motorbike.setType(type);
        motorbike.setEngine(engine);
        return motorbike;
    }
}
