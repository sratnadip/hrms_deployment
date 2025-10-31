package com.quantumsoft.hrms.repository;

import com.quantumsoft.hrms.entity.SurveyResponse;
import com.quantumsoft.hrms.entity.Employee;
import com.quantumsoft.hrms.entity.SurveyFeedback;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface SurveyResponseRepository extends JpaRepository<SurveyResponse, UUID> {
    boolean existsBySurveyAndEmployee(SurveyFeedback survey, Employee employee);
    List<SurveyResponse> findBySurvey(SurveyFeedback survey);
}

