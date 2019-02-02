package ie.dq.unit.motorbike.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import ie.dq.motorbike.controller.MotorbikeController;
import ie.dq.motorbike.domain.Motorbike;
import ie.dq.motorbike.service.MotorbikeService;
import ie.dq.util.MotorbikeTestData;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.LinkedList;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class MotorbikeControllerTest {

    private MockMvc mockMvc;

    @Mock
    private MotorbikeService motorbikeService;

    @InjectMocks
    private MotorbikeController motorbikeController;

    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        this.objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders.standaloneSetup(motorbikeController).build();
    }

    @Test
    public void testGetMotorbikes() throws Exception {
        when(motorbikeService.getMotorbikes()).thenReturn(MotorbikeTestData.motorbikePage());

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/motorbikes").accept(MediaType.APPLICATION_JSON_UTF8_VALUE);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(content().string(objectMapper.writeValueAsString(MotorbikeTestData.motorbikePage())))
                .andReturn();

        verify(motorbikeService).getMotorbikes();
    }

    @Test
    public void testGetMotorbikesNoContent() throws Exception {
        when(motorbikeService.getMotorbikes()).thenReturn(new PageImpl<Motorbike>(new LinkedList()));

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/motorbikes").accept(MediaType.APPLICATION_JSON_UTF8_VALUE);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isNoContent())
                .andExpect(content().string(""))
                .andReturn();

        verify(motorbikeService).getMotorbikes();
    }

    @Test
    public void testCreateMotorbike() throws Exception {
        when(motorbikeService.createMotorbike(MotorbikeTestData.newMotorbike())).thenReturn(MotorbikeTestData.createdMotorbike());

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/motorbikes")
                .content(objectMapper.writeValueAsString(MotorbikeTestData.newMotorbike()))
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .accept(MediaType.APPLICATION_JSON_UTF8_VALUE);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(content().string(objectMapper.writeValueAsString(MotorbikeTestData.createdMotorbike())))
                .andReturn();

        verify(motorbikeService).createMotorbike(MotorbikeTestData.newMotorbike());
    }

    @Test
    public void testCreateMotorbikeConflict() throws Exception {
        when(motorbikeService.createMotorbike(MotorbikeTestData.newMotorbike())).thenReturn(new Motorbike());

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/motorbikes")
                .content(objectMapper.writeValueAsString(MotorbikeTestData.newMotorbike()))
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .accept(MediaType.APPLICATION_JSON_UTF8_VALUE);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isConflict())
                .andReturn();

        verify(motorbikeService).createMotorbike(MotorbikeTestData.newMotorbike());
    }

    @Test
    public void testCreateMotorbikeBadRequest() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/motorbikes")
                .content("BAD_REQUEST")
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .accept(MediaType.APPLICATION_JSON_UTF8_VALUE);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest())
                .andReturn();

        verify(motorbikeService, never()).createMotorbike(MotorbikeTestData.newMotorbike());
    }

}
