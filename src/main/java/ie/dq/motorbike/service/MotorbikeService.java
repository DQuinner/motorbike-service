package ie.dq.motorbike.service;

import ie.dq.motorbike.domain.Motorbike;
import org.springframework.data.domain.Page;

public interface MotorbikeService {

    Motorbike createMotorbike(Motorbike motorbike);

    Page<Motorbike> getMotorbikes();

}
