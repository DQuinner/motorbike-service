package ie.dq.motorbike.service.impl;

import ie.dq.motorbike.domain.Motorbike;
import ie.dq.motorbike.repository.MotorbikeRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.LinkedList;
import java.util.List;

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
        Motorbike motorbike = newMotorbike();

        when(motorbikeRepository.save(motorbike)).thenReturn(createdMotorbike());
        when(motorbikeRepository.existsMotorbikeByMakeAndModel(motorbike.getMake(), motorbike.getModel())).thenReturn(false);

        Motorbike created = motorbikeService.createMotorbike(motorbike);
        assertNotNull(created);
        assertTrue(0l < created.getId());
        assertEquals(motorbike.getMake(), created.getMake());
        assertEquals(motorbike.getModel(), created.getModel());
        assertEquals(motorbike.getType(), created.getType());

        verify(motorbikeRepository).existsMotorbikeByMakeAndModel(motorbike.getMake(), motorbike.getModel());
        verify(motorbikeRepository).save(motorbike);
    }

    @Test
    public void testCreateMotorbikeExisting(){
        Motorbike motorbike = newMotorbike();

        when(motorbikeRepository.existsMotorbikeByMakeAndModel(motorbike.getMake(), motorbike.getModel())).thenReturn(true);

        Motorbike created = motorbikeService.createMotorbike(motorbike);
        assertNotNull(created);
        assertEquals(0l, created.getId());
        assertNull(created.getMake());
        assertNull(created.getModel());
        assertNull(created.getType());

        verify(motorbikeRepository).existsMotorbikeByMakeAndModel(motorbike.getMake(), motorbike.getModel());
        verify(motorbikeRepository, never()).save(motorbike);
    }

    @Test
    public void testGetMotorbikes(){
        when(motorbikeRepository.findAll(any(Pageable.class))).thenReturn(motorbikePage());
        Page<Motorbike> motorbikes = motorbikeService.getMotorbikes();
        assertNotNull(motorbikes);
    }

    private Motorbike newMotorbike(){
        Motorbike motorbike = new Motorbike();
        motorbike.setMake("Motorbike Test Make");
        motorbike.setModel("Motorbike Test Model");
        motorbike.setType("Motorbike Test Type");
        return motorbike;
    }

    private Motorbike createdMotorbike(){
        Motorbike motorbike = newMotorbike();
        motorbike.setId(1l);
        return motorbike;
    }

    private Page motorbikePage(){
        List<Motorbike> motorbikeList = new LinkedList();
        motorbikeList.add(newMotorbike());
        motorbikeList.add(newMotorbike());
        return new PageImpl(motorbikeList);
    }

}
