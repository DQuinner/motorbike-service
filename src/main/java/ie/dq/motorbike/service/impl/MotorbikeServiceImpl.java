package ie.dq.motorbike.service.impl;

import ie.dq.motorbike.domain.Motorbike;
import ie.dq.motorbike.repository.MotorbikeRepository;
import ie.dq.motorbike.service.MotorbikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class MotorbikeServiceImpl implements MotorbikeService {

    @Autowired
    private MotorbikeRepository motorbikeRepository;

    @Override
    public Motorbike createMotorbike(Motorbike motorbike) {
        if(motorbikeRepository.existsMotorbikeByMakeAndModel(motorbike.getMake(), motorbike.getModel())){
            return new Motorbike();
        }else{
            return motorbikeRepository.save(motorbike);
        }
    }

    @Override
    public Page<Motorbike> getMotorbikes() {
        return motorbikeRepository.findAll(PageRequest.of(0, 1000, Sort.by(Sort.Direction.ASC, "make")));
    }
}
