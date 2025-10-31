package com.quantumsoft.hrms.controller;

import com.quantumsoft.hrms.dto.TrainingTrackingDto;
import com.quantumsoft.hrms.entity.Training;
import com.quantumsoft.hrms.entity.TrainingTracking;
import com.quantumsoft.hrms.servicei.TrainingServiceI;
import com.quantumsoft.hrms.servicei.TrainingTrackingServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "/api/trainingTracking")
public class TrainingTrackingController {

    @Autowired
    private TrainingTrackingServiceI trainingTrackingService;

    // When employee start training then training status get automatically change to IN_PROGRESS

    @PreAuthorize("hasRole('EMPLOYEE')")
    @PostMapping(value = "/{trainingId}", produces = "application/json")
    public ResponseEntity<TrainingTrackingDto> startTraining(@PathVariable UUID trainingId){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return new ResponseEntity<TrainingTrackingDto>(trainingTrackingService.startTraining(username, trainingId), HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('HR') or hasRole('EMPLOYEE')")
    @GetMapping(value = "/{empId}", produces = "application/json")
    public ResponseEntity<TrainingTrackingDto> fetchTrainingTrackingByEmpId(@PathVariable UUID empId){
        return new ResponseEntity<TrainingTrackingDto>(trainingTrackingService.fetchTrainingTrackingByEmpId(empId), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('HR') or hasRole('MANAGER')")
    @GetMapping(produces = "application/json")
    public ResponseEntity<List<TrainingTrackingDto>> fetchAllTrainingTrackingByStatus(@RequestParam String status){
        return new ResponseEntity<List<TrainingTrackingDto>>(trainingTrackingService.fetchAllTrainingTrackingByStatus(status), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('MANAGER')")
    @GetMapping(value = "/manager", produces = "application/json")
    public ResponseEntity<List<TrainingTrackingDto>> fetchAllTrainingTrackingByManagerId(@RequestParam UUID managerId){
        return new ResponseEntity<List<TrainingTrackingDto>>(trainingTrackingService.fetchAllTrainingTrackingByManagerId(managerId), HttpStatus.OK);
    }

    // API that changes training status from In_PROGRESS to COMPLETED

    @PreAuthorize("hasRole('EMPLOYEE')")
    @PatchMapping(produces = "application/json")
    public ResponseEntity<String> updateTrainingStatus(){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return new ResponseEntity<String>(trainingTrackingService.updateTrainingStatus(username), HttpStatus.OK);
    }

}
