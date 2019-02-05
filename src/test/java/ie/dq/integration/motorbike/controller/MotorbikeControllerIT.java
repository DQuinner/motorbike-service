package ie.dq.integration.motorbike.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import ie.dq.motorbike.MotorbikeApp;
import ie.dq.motorbike.repository.MotorbikeRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ie.dq.util.MotorbikeTestData;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MotorbikeApp.class, webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@ActiveProfiles("INTEGRATION_TEST")
public class MotorbikeControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MotorbikeRepository motorbikeRepository;

    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        this.objectMapper = new ObjectMapper();
        motorbikeRepository.deleteAll();
    }

    @Test
    public void testGetMotorbikes() throws Exception {
        createMotorbike();

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/motorbikes").accept(MediaType.APPLICATION_JSON_UTF8_VALUE);

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].make", is(MotorbikeTestData.newMotorbike().getMake())))
                .andExpect(jsonPath("$.content[0].model", is(MotorbikeTestData.newMotorbike().getModel())))
                .andExpect(jsonPath("$.content[0].type", is(MotorbikeTestData.newMotorbike().getType())))
                .andExpect(jsonPath("$.totalElements", is(1)))
                .andExpect(jsonPath("$.totalPages", is(1)))
                .andExpect(jsonPath("$.last", is(true)))
                .andExpect(jsonPath("$.number", is(0)))
                .andExpect(jsonPath("$.first", is(true)))
                .andExpect(jsonPath("$.numberOfElements", is(1)))
                .andExpect(jsonPath("$.sort.empty", is(false)))
                .andExpect(jsonPath("$.sort.sorted", is(true)))
                .andExpect(jsonPath("$.sort.unsorted", is(false)))
                .andReturn();
    }

    @Test
    public void testGetMotorbikesNoContent() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/motorbikes").accept(MediaType.APPLICATION_JSON_UTF8_VALUE);

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isNoContent())
                .andExpect(content().string(""))
                .andReturn();
    }

    @Test
    public void testCreateMotorbike() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/motorbikes")
                .content(objectMapper.writeValueAsString(MotorbikeTestData.newMotorbike()))
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .accept(MediaType.APPLICATION_JSON_UTF8_VALUE);

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.make", is(MotorbikeTestData.newMotorbike().getMake())))
                .andExpect(jsonPath("$.model", is(MotorbikeTestData.newMotorbike().getModel())))
                .andExpect(jsonPath("$.type", is(MotorbikeTestData.newMotorbike().getType())))
                .andReturn();
    }

    @Test
    public void testCreateMotorbikeConflict() throws Exception {
        createMotorbike();

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/motorbikes")
                .content(objectMapper.writeValueAsString(MotorbikeTestData.newMotorbike()))
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .accept(MediaType.APPLICATION_JSON_UTF8_VALUE);

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isConflict())
                .andReturn();
    }

    @Test
    public void testCreateMotorbikeBadRequest() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/motorbikes")
                .content("BAD_REQUEST")
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .accept(MediaType.APPLICATION_JSON_UTF8_VALUE);

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    private void createMotorbike(){
        motorbikeRepository.save(MotorbikeTestData.newMotorbike());
    }

}
