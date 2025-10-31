package com.quantumsoft.hrms.controller;

import com.quantumsoft.hrms.dto.TrainingDto;
import com.quantumsoft.hrms.servicei.TrainingServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "/api/training")
public class TrainingController {

    @Autowired
    private TrainingServiceI trainingService;

    @PreAuthorize("hasRole('HR')")
    @PostMapping(consumes = "application/json", produces = "plain/text")
    public ResponseEntity<String> saveTrainingData(@RequestBody TrainingDto trainingDto){
        return new ResponseEntity<String>(trainingService.saveTrainingData(trainingDto), HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('HR')")
    @GetMapping(value = "/{trainingId}", produces = "application/json")
    public ResponseEntity<TrainingDto> fetchTrainingData(@PathVariable UUID trainingId){
        return new ResponseEntity<TrainingDto>(trainingService.fetchTrainingData(trainingId), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('HR')")
    @PutMapping(value = "/{trainingId}", consumes = "application/json", produces = "application/json")
    public ResponseEntity<String> updateTrainingData(@RequestBody TrainingDto trainingDto, @PathVariable UUID trainingId){
        System.out.println(trainingDto.getTitle());
        System.out.println(trainingId);
        return new ResponseEntity<String>(trainingService.updateTrainingData(trainingDto, trainingId), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('HR')")
    @GetMapping(produces = "application/json")
    public ResponseEntity<List<TrainingDto>> fetchAllTrainingData(){
        return new ResponseEntity<List<TrainingDto>>(trainingService.fetchAllTrainingData(), HttpStatus.OK);
    }

}
