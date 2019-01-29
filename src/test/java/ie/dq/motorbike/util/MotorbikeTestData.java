package ie.dq.motorbike.util;

import ie.dq.motorbike.domain.Motorbike;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.LinkedList;
import java.util.List;

public class MotorbikeTestData {

    public static Motorbike newMotorbike(){
        Motorbike motorbike = new Motorbike();
        motorbike.setMake("Motorbike Test Make");
        motorbike.setModel("Motorbike Test Model");
        motorbike.setType("Motorbike Test Type");
        return motorbike;
    }

    public static Motorbike createdMotorbike(){
        Motorbike motorbike = newMotorbike();
        motorbike.setId(1l);
        return motorbike;
    }

    public static Page motorbikePage(){
        List<Motorbike> motorbikeList = new LinkedList();
        motorbikeList.add(newMotorbike());
        motorbikeList.add(newMotorbike());
        return new PageImpl(motorbikeList);
    }

}
