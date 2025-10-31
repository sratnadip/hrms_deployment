package com.quantumsoft.hrms.repository;

import com.quantumsoft.hrms.entity.SurveyFeedback;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SurveyFeedbackRepository extends JpaRepository<SurveyFeedback, UUID> {
}
