package com.quantumsoft.hrms.serviceimpl;

import com.quantumsoft.hrms.dto.TrainingTrackingDto;
import com.quantumsoft.hrms.entity.AuditLog;
import com.quantumsoft.hrms.entity.Employee;
import com.quantumsoft.hrms.entity.Training;
import com.quantumsoft.hrms.entity.TrainingTracking;
import com.quantumsoft.hrms.enums.Action;
import com.quantumsoft.hrms.enums.Module;
import com.quantumsoft.hrms.enums.TrainingCompletionStatus;
import com.quantumsoft.hrms.exception.ResourceNotFoundException;
import com.quantumsoft.hrms.repository.AuditLogRepository;
import com.quantumsoft.hrms.repository.TrainingRepository;
import com.quantumsoft.hrms.repository.TrainingTrackingRepository;
import com.quantumsoft.hrms.servicei.AuditLogServiceI;
import com.quantumsoft.hrms.servicei.TrainingTrackingServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class TrainingTrackingServiceImpl implements TrainingTrackingServiceI {

    @Autowired
    private TrainingTrackingRepository trainingTrackingRepository;

    @Autowired
    private TrainingRepository trainingRepository;

    @Autowired
    private AuditLogServiceI auditLogService;

    // EMPLOYEE, MANAGER click on the start training button and then training will start by creating the
    // training tracking object

    @Override
    public TrainingTrackingDto startTraining(String username, UUID trainingId) {
        Training training = trainingRepository.findById(trainingId).orElseThrow(() -> new ResourceNotFoundException("Training record with id " + trainingId + " not found in the database"));

        Employee employee = training.getAssignedTo().stream().filter(e -> e.getUser().getUsername()
                                    .equals(username)).findFirst().orElseThrow(() ->
                                    new ResourceNotFoundException("Given training is not assigned to you."));

        TrainingTracking trainingTracking = new TrainingTracking();

        trainingTracking.setEmployee(employee);
        trainingTracking.setTraining(training);
        trainingTracking.setStartDate(LocalDate.now());
        trainingTracking.setCompletionDate(null);
        trainingTracking.setStatus(TrainingCompletionStatus.IN_PROGRESS);

        TrainingTracking savedTrainingTracking = trainingTrackingRepository.save(trainingTracking);

        auditLogService.logInfo(employee.getUser().getUserId(), Action.START_TRAINING, Module.TRAINING);

        TrainingTrackingDto trainingTrackingDto = new TrainingTrackingDto();

        trainingTrackingDto.setTrainingTrackingId(savedTrainingTracking.getTrainingTrackingId());
        trainingTrackingDto.setEmployeeId(savedTrainingTracking.getEmployee().getEmpId());
        trainingTrackingDto.setTrainingId(savedTrainingTracking.getTraining().getTrainingId());
        trainingTrackingDto.setTitle(savedTrainingTracking.getTraining().getTitle());
        trainingTrackingDto.setDescription(savedTrainingTracking.getTraining().getDescription());
        trainingTrackingDto.setCompletionDeadline(savedTrainingTracking.getTraining().getCompletionDeadline());
        trainingTrackingDto.setStartDate(savedTrainingTracking.getStartDate());
        trainingTrackingDto.setCompletionDate(null);
        trainingTrackingDto.setStartDate(savedTrainingTracking.getStartDate());
        trainingTrackingDto.setStatus(savedTrainingTracking.getStatus());

        return trainingTrackingDto;
    }

    @Override
    public TrainingTrackingDto fetchTrainingTrackingByEmpId(UUID empId) {
        TrainingTracking trainingTracking = trainingTrackingRepository.findByEmployee_empId(empId).orElseThrow(() -> new ResourceNotFoundException("No training is assigned to Employee with id " + empId));

        TrainingTrackingDto trainingTrackingDto = new TrainingTrackingDto();

        trainingTrackingDto.setTrainingTrackingId(trainingTracking.getTrainingTrackingId());
        trainingTrackingDto.setEmployeeId(trainingTracking.getEmployee().getEmpId());
        trainingTrackingDto.setTrainingId(trainingTracking.getTraining().getTrainingId());
        trainingTrackingDto.setTitle(trainingTracking.getTraining().getTitle());
        trainingTrackingDto.setDescription(trainingTracking.getTraining().getDescription());
        trainingTrackingDto.setCompletionDeadline(trainingTracking.getTraining().getCompletionDeadline());
        trainingTrackingDto.setStartDate(trainingTracking.getStartDate());
        trainingTrackingDto.setCompletionDate(trainingTracking.getTraining().getCompletionDeadline());
        trainingTrackingDto.setStartDate(trainingTracking.getStartDate());
        trainingTrackingDto.setStatus(trainingTracking.getStatus());

        return trainingTrackingDto;
    }

    @Override
    public List<TrainingTrackingDto> fetchAllTrainingTrackingByStatus(String status) {

        List<TrainingTrackingDto> trainingTrackingDtos = new ArrayList<>();

        if(status.equals("IN_PROGRESS")){
            List<TrainingTracking> inProgressTrainingTrackings = trainingTrackingRepository.findByStatus(TrainingCompletionStatus.IN_PROGRESS).orElseThrow(() -> new ResourceNotFoundException("Np training record with given status " + status + " is found"));

            for(TrainingTracking trainingTracking : inProgressTrainingTrackings){

                TrainingTrackingDto trainingTrackingDto = new TrainingTrackingDto();

                trainingTrackingDto.setTrainingTrackingId(trainingTracking.getTrainingTrackingId());
                trainingTrackingDto.setEmployeeId(trainingTracking.getEmployee().getEmpId());
                trainingTrackingDto.setTrainingId(trainingTracking.getTraining().getTrainingId());
                trainingTrackingDto.setTitle(trainingTracking.getTraining().getTitle());
                trainingTrackingDto.setDescription(trainingTracking.getTraining().getDescription());
                trainingTrackingDto.setCompletionDeadline(trainingTracking.getTraining().getCompletionDeadline());
                trainingTrackingDto.setStartDate(trainingTracking.getStartDate());
                trainingTrackingDto.setCompletionDate(trainingTracking.getTraining().getCompletionDeadline());
                trainingTrackingDto.setStartDate(trainingTracking.getStartDate());
                trainingTrackingDto.setStatus(trainingTracking.getStatus());

                trainingTrackingDtos.add(trainingTrackingDto);
            }
        }
        else if(status.equals("COMPLETED")){
            List<TrainingTracking> completedTrainingTrackings = trainingTrackingRepository.findByStatus(TrainingCompletionStatus.COMPLETED).orElseThrow(() -> new ResourceNotFoundException("Np training record with given status " + status + " is found"));

            for(TrainingTracking trainingTracking : completedTrainingTrackings) {

                TrainingTrackingDto trainingTrackingDto = new TrainingTrackingDto();

                trainingTrackingDto.setTrainingTrackingId(trainingTracking.getTrainingTrackingId());
                trainingTrackingDto.setEmployeeId(trainingTracking.getEmployee().getEmpId());
                trainingTrackingDto.setTrainingId(trainingTracking.getTraining().getTrainingId());
                trainingTrackingDto.setTitle(trainingTracking.getTraining().getTitle());
                trainingTrackingDto.setDescription(trainingTracking.getTraining().getDescription());
                trainingTrackingDto.setCompletionDeadline(trainingTracking.getTraining().getCompletionDeadline());
                trainingTrackingDto.setStartDate(trainingTracking.getStartDate());
                trainingTrackingDto.setCompletionDate(trainingTracking.getTraining().getCompletionDeadline());
                trainingTrackingDto.setStartDate(trainingTracking.getStartDate());
                trainingTrackingDto.setStatus(trainingTracking.getStatus());

                trainingTrackingDtos.add(trainingTrackingDto);
            }
        }
        return trainingTrackingDtos;
    }

    @Override
    public List<TrainingTrackingDto> fetchAllTrainingTrackingByManagerId(UUID managerId) {
        List<TrainingTracking> trainingTrackings = trainingTrackingRepository.findAllByEmployee_managerId(managerId);

        List<TrainingTrackingDto> trainingTrackingDtos = new ArrayList<>();

        for(TrainingTracking trainingTracking : trainingTrackings) {

            TrainingTrackingDto trainingTrackingDto = new TrainingTrackingDto();

            trainingTrackingDto.setTrainingTrackingId(trainingTracking.getTrainingTrackingId());
            trainingTrackingDto.setEmployeeId(trainingTracking.getEmployee().getEmpId());
            trainingTrackingDto.setTrainingId(trainingTracking.getTraining().getTrainingId());
            trainingTrackingDto.setTitle(trainingTracking.getTraining().getTitle());
            trainingTrackingDto.setDescription(trainingTracking.getTraining().getDescription());
            trainingTrackingDto.setCompletionDeadline(trainingTracking.getTraining().getCompletionDeadline());
            trainingTrackingDto.setStartDate(trainingTracking.getStartDate());
            trainingTrackingDto.setCompletionDate(trainingTracking.getTraining().getCompletionDeadline());
            trainingTrackingDto.setStartDate(trainingTracking.getStartDate());
            trainingTrackingDto.setStatus(trainingTracking.getStatus());

            trainingTrackingDtos.add(trainingTrackingDto);
        }
        return trainingTrackingDtos;
    }

    @Override
    public String updateTrainingStatus(String username) {
        TrainingTracking trainingTracking = trainingTrackingRepository.findByEmployee_User_username(username).orElseThrow(() -> new ResourceNotFoundException("Training tracking record not found"));

        if(LocalDate.now().isBefore(trainingTracking.getTraining().getCompletionDeadline()) || LocalDate.now().isEqual(trainingTracking.getTraining().getCompletionDeadline())){
            trainingTracking.setStatus(TrainingCompletionStatus.COMPLETED);
            trainingTracking.setCompletionDate(LocalDate.now());
            trainingTrackingRepository.save(trainingTracking);
            auditLogService.logInfo(trainingTracking.getEmployee().getUser().getUserId(), Action.TRAINING_COMPLETION, Module.TRAINING);

            return "Training is completed";
        }else
            return "You can't complete the training. You have crossed the deadline to complete the training.";
    }
}
