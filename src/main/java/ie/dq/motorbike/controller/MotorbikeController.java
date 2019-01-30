package ie.dq.motorbike.controller;

import ie.dq.motorbike.domain.Motorbike;
import ie.dq.motorbike.service.MotorbikeService;
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

    private static final long ZERO = 0l;

    @Autowired
    private MotorbikeService motorbikeService;

    @GetMapping("/motorbikes")
    public ResponseEntity<Page<Motorbike>> getMotorbikes(){
        Page<Motorbike> motorbikes = motorbikeService.getMotorbikes();
        if(motorbikes.hasContent()){
            return new ResponseEntity(motorbikes, HttpStatus.OK);
        }else{
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
    }

    @PostMapping("/motorbikes")
    public ResponseEntity<Motorbike> createMotorbike(@RequestBody Motorbike motorbike){
        Motorbike result = motorbikeService.createMotorbike(motorbike);
        if(result.getId()>ZERO){
            return new ResponseEntity(result, HttpStatus.CREATED);
        }else{
            return new ResponseEntity(motorbike, HttpStatus.CONFLICT);
        }
    }

}
