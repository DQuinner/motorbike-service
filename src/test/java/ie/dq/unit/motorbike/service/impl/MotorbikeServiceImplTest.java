package ie.dq.unit.motorbike.service.impl;

import ie.dq.motorbike.domain.Motorbike;
import ie.dq.motorbike.repository.MotorbikeRepository;
import ie.dq.motorbike.service.impl.MotorbikeServiceImpl;
import ie.dq.util.MotorbikeTestData;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class MotorbikeServiceImplTest {

    @Mock
    private MotorbikeRepository motorbikeRepository;

    @InjectMocks
    private MotorbikeServiceImpl motorbikeService;

    @Test
    public void testCreateMotorbikeNew(){
        Motorbike motorbike = MotorbikeTestData.newMotorbike();

        when(motorbikeRepository.save(motorbike)).thenReturn(MotorbikeTestData.createdMotorbike());
        when(motorbikeRepository.existsMotorbikeByMakeAndModelAndEngine(motorbike.getMake(), motorbike.getModel(), motorbike.getEngine())).thenReturn(false);

        Motorbike created = motorbikeService.createMotorbike(motorbike);
        assertNotNull(created);
        assertTrue(0l < created.getId());
        assertEquals(motorbike.getMake(), created.getMake());
        assertEquals(motorbike.getModel(), created.getModel());
        assertEquals(motorbike.getType(), created.getType());
        assertEquals(motorbike.getEngine(), created.getEngine());

        verify(motorbikeRepository).existsMotorbikeByMakeAndModelAndEngine(motorbike.getMake(), motorbike.getModel(), motorbike.getEngine());
        verify(motorbikeRepository).save(motorbike);
    }

    @Test
    public void testCreateMotorbikeExisting(){
        Motorbike motorbike = MotorbikeTestData.newMotorbike();

        when(motorbikeRepository.existsMotorbikeByMakeAndModelAndEngine(motorbike.getMake(), motorbike.getModel(), motorbike.getEngine())).thenReturn(true);

        Motorbike created = motorbikeService.createMotorbike(motorbike);
        assertNotNull(created);
        assertEquals(0l, created.getId());
        assertNull(created.getMake());
        assertNull(created.getModel());
        assertNull(created.getType());
        assertEquals(0, created.getEngine());

        verify(motorbikeRepository).existsMotorbikeByMakeAndModelAndEngine(motorbike.getMake(), motorbike.getModel(), motorbike.getEngine());
        verify(motorbikeRepository, never()).save(motorbike);
    }

    @Test
    public void testGetMotorbikes(){
        when(motorbikeRepository.findAll(any(Pageable.class))).thenReturn(MotorbikeTestData.motorbikePage());
        Page<Motorbike> motorbikes = motorbikeService.getMotorbikes();
        assertNotNull(motorbikes);
    }

}
