package com.quantumsoft.hrms.serviceimpl;


import com.quantumsoft.hrms.entity.SurveyResponse;
import com.quantumsoft.hrms.repository.EmployeeRepository;
import com.quantumsoft.hrms.repository.SurveyFeedbackRepository;
import com.quantumsoft.hrms.repository.SurveyResponseRepository;

import com.quantumsoft.hrms.servicei.SurveyResponseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;



@Service
@RequiredArgsConstructor
public class SurveyResponseServiceImpl implements SurveyResponseService {

    private final SurveyFeedbackRepository feedbackRepo;
    private final SurveyResponseRepository responseRepo;
    private final EmployeeRepository employeeRepo;

    @Override
    public SurveyResponse submit(SurveyResponse response) {
        if (responseRepo.existsBySurveyAndEmployee(response.getSurvey(), response.getEmployee())) {
            throw new RuntimeException("You have already submitted this survey.");
        }
        return responseRepo.save(response);
    }
}

