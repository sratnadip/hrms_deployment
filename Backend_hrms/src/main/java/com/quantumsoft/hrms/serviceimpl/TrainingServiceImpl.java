package com.quantumsoft.hrms.serviceimpl;

import com.quantumsoft.hrms.dto.TrainingDto;
import com.quantumsoft.hrms.entity.Employee;
import com.quantumsoft.hrms.entity.Training;
import com.quantumsoft.hrms.exception.ResourceNotFoundException;
import com.quantumsoft.hrms.repository.EmployeeRepository;
import com.quantumsoft.hrms.repository.TrainingRepository;
import com.quantumsoft.hrms.servicei.TrainingServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class TrainingServiceImpl implements TrainingServiceI {

    @Autowired
    private TrainingRepository trainingRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Override
    public String saveTrainingData(TrainingDto trainingDto) {
        List<Employee> listOfEmployees = trainingDto.getEmployeeIds().stream()
                .map(id -> employeeRepository.findById(id).orElseThrow(() ->
                        new ResourceNotFoundException("Employee record with ID " + id +
                                " not found in database."))).collect(Collectors.toList());

        Training training = new Training();

        training.setTitle(trainingDto.getTitle());
        training.setDescription(trainingDto.getDescription());
        training.setAssignedTo(listOfEmployees);
        training.setMandatoryFor(trainingDto.getMandatoryFor());
        training.setCompletionDeadline(trainingDto.getCompletionDeadline());

        trainingRepository.save(training);

        Map<String, String> notification = new HashMap<>();
        notification.put("message", "Training is assigned to " + training.getAssignedTo());

        simpMessagingTemplate.convertAndSend( "/topic/notifications", notification);

        return "Training data saved successfully...!";
    }

    @Override
    public TrainingDto fetchTrainingData(UUID trainingId) {
        Training training = trainingRepository.findById(trainingId).orElseThrow(() -> new ResourceNotFoundException("Training reord with id " + trainingId + " is not found in database"));

        TrainingDto trainingDto = new TrainingDto();
        trainingDto.setTrainingId(training.getTrainingId());
        trainingDto.setTitle(training.getTitle());
        trainingDto.setDescription(training.getDescription());
        trainingDto.setMandatoryFor(training.getMandatoryFor());
        trainingDto.setCompletionDeadline(training.getCompletionDeadline());

        List<UUID> listOfEmployeeIds = training.getAssignedTo().stream().map(Employee::getEmpId).collect(Collectors.toList());

        trainingDto.setEmployeeIds(listOfEmployeeIds);
        return trainingDto;
    }

    @Override
    public String updateTrainingData(TrainingDto trainingDto, UUID trainingId) {
        Training training = trainingRepository.findById(trainingId).orElseThrow(() -> new ResourceNotFoundException("Training record with id " + trainingId + " is not found in database"));

        List<Employee> listOfEmployees = trainingDto.getEmployeeIds().stream()
                .map(id -> employeeRepository.findById(id).orElseThrow(() ->
                        new ResourceNotFoundException("Employee record with ID " + id +
                                " not found in database."))).collect(Collectors.toList());

        training.setTitle(trainingDto.getTitle());
        training.setDescription(trainingDto.getDescription());
        training.setMandatoryFor(trainingDto.getMandatoryFor());
        training.setAssignedTo(listOfEmployees);
        training.setCompletionDeadline(trainingDto.getCompletionDeadline());

        trainingRepository.save(training);

        return "Training record with id " + trainingId + " is updated successfully.";
    }

    @Override
    public List<TrainingDto> fetchAllTrainingData() {
        List<Training> trainings = trainingRepository.findAll();

        List<TrainingDto> list = new ArrayList<>();
        for(Training t : trainings){
            TrainingDto trainingDto = new TrainingDto();
            trainingDto.setTrainingId(t.getTrainingId());
            trainingDto.setTitle(t.getTitle());
            trainingDto.setDescription(t.getDescription());
            trainingDto.setMandatoryFor(t.getMandatoryFor());
            trainingDto.setCompletionDeadline(t.getCompletionDeadline());

            list.add(trainingDto);
        }
        return list;
    }
}
