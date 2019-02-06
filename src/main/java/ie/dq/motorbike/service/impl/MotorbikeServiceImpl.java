package ie.dq.motorbike.service.impl;

import ie.dq.motorbike.domain.Motorbike;
import ie.dq.motorbike.repository.MotorbikeRepository;
import ie.dq.motorbike.service.MotorbikeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class MotorbikeServiceImpl implements MotorbikeService {

    private static final Logger logger = LoggerFactory.getLogger(MotorbikeServiceImpl.class);

    @Autowired
    private MotorbikeRepository motorbikeRepository;

    @Override
    public Motorbike createMotorbike(Motorbike motorbike) {
        logger.info("createMotorbike {} {}", motorbike.getMake(), motorbike.getModel());
        if(motorbikeRepository.existsMotorbikeByMakeAndModelAndEngine(motorbike.getMake(), motorbike.getModel(), motorbike.getEngine())){
            logger.info("Motorbike {} {} {} already exists", motorbike.getMake(), motorbike.getModel(), motorbike.getEngine());
            return new Motorbike();
        }else{
            logger.info("Saving motorbike {} {} {}", motorbike.getMake(), motorbike.getModel(), motorbike.getEngine());
            return motorbikeRepository.save(motorbike);
        }
    }

    @Override
    public Page<Motorbike> getMotorbikes() {
        logger.info("getMotorbikes()");
        return motorbikeRepository.findAll(PageRequest.of(0, 1000, Sort.by(Sort.Direction.ASC, "make")));
    }
}
