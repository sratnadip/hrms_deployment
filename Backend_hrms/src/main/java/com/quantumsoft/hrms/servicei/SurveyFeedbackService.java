package com.quantumsoft.hrms.servicei;


import com.quantumsoft.hrms.entity.SurveyFeedback;

import java.util.List;
import java.util.UUID;

public interface SurveyFeedbackService {
    SurveyFeedback create(SurveyFeedback feedback);
    List<SurveyFeedback> getAll();
    SurveyFeedback getById(UUID id);
    void checkAndCloseExpiredSurveys();
}

