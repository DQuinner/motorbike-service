package ie.dq.motorbike.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import ie.dq.motorbike.domain.Motorbike;
import ie.dq.motorbike.service.MotorbikeService;
import ie.dq.motorbike.util.MotorbikeTestData;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.LinkedList;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(value = MotorbikeController.class)
public class MotorbikeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MotorbikeService motorbikeService;

    private ObjectMapper objectMapper = new ObjectMapper();

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
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(content().string(objectMapper.writeValueAsString(MotorbikeTestData.newMotorbike())))
                .andReturn();

        verify(motorbikeService).createMotorbike(MotorbikeTestData.newMotorbike());
    }

    @Test
    public void testCreateMotorbikeBadRequest() throws Exception {
        when(motorbikeService.createMotorbike(MotorbikeTestData.newMotorbike())).thenReturn(new Motorbike());

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
