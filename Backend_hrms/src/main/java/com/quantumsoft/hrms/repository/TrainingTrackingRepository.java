package com.quantumsoft.hrms.repository;

import com.quantumsoft.hrms.entity.TrainingTracking;
import com.quantumsoft.hrms.enums.TrainingCompletionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TrainingTrackingRepository extends JpaRepository<TrainingTracking, UUID> {
    Optional<TrainingTracking> findByEmployee_empId(UUID empId);

    Optional<List<TrainingTracking>> findByStatus(TrainingCompletionStatus trainingCompletionStatus);

    List<TrainingTracking> findAllByEmployee_managerId(UUID managerId);

    Optional<TrainingTracking> findByEmployee_User_username(String username);
}
