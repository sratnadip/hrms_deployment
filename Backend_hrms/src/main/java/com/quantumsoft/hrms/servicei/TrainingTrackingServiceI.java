package com.quantumsoft.hrms.servicei;

import com.quantumsoft.hrms.dto.TrainingTrackingDto;
import com.quantumsoft.hrms.entity.TrainingTracking;

import java.util.List;
import java.util.UUID;

public interface TrainingTrackingServiceI {
    TrainingTrackingDto startTraining(String username, UUID trainingId);

    TrainingTrackingDto fetchTrainingTrackingByEmpId(UUID empId);

    List<TrainingTrackingDto> fetchAllTrainingTrackingByStatus(String status);

    List<TrainingTrackingDto> fetchAllTrainingTrackingByManagerId(UUID managerId);

    String updateTrainingStatus(String username);
}
