package com.quantumsoft.hrms.servicei;

import com.quantumsoft.hrms.dto.TrainingDto;
import com.quantumsoft.hrms.entity.Training;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import java.util.List;
import java.util.UUID;

public interface TrainingServiceI {
    String saveTrainingData(TrainingDto trainingDto);

    TrainingDto fetchTrainingData(UUID trainingId);

    String updateTrainingData(TrainingDto trainingDto, UUID trainingId);

    List<TrainingDto> fetchAllTrainingData();
}
