package ie.dq.motorbike.controller;

import ie.dq.motorbike.domain.Motorbike;
import ie.dq.motorbike.service.MotorbikeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MotorbikeController {

    private static final Logger logger = LoggerFactory.getLogger(MotorbikeController.class);

    private static final long ZERO = 0l;

    @Autowired
    private MotorbikeService motorbikeService;

    @GetMapping("/motorbikes")
    public ResponseEntity<Page<Motorbike>> getMotorbikes(){
        logger.info("getMotorbikes()");
        Page<Motorbike> motorbikes = motorbikeService.getMotorbikes();
        if(motorbikes.hasContent()){
            logger.info("Found {} motorbikes", motorbikes.getTotalElements());
            return new ResponseEntity(motorbikes, HttpStatus.OK);
        }else{
            logger.info("No motorbikes found");
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
    }

    @PostMapping("/motorbikes")
    public ResponseEntity<Motorbike> createMotorbike(@RequestBody Motorbike motorbike){
        logger.info("createMotorbike {} {}",motorbike.getMake(), motorbike.getModel());
        Motorbike result = motorbikeService.createMotorbike(motorbike);
        if(result.getId()>ZERO){
            logger.info("Motorbike {} {} created with id {}",result.getMake(), result.getModel(), result.getId());
            return new ResponseEntity(result, HttpStatus.CREATED);
        }else{
            logger.error("Motorbike {} {} already exists", motorbike.getMake(), motorbike.getModel());
            return new ResponseEntity(HttpStatus.CONFLICT);
        }
    }

}
