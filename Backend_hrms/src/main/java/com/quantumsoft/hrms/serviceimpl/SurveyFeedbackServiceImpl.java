package com.quantumsoft.hrms.serviceimpl;

import com.quantumsoft.hrms.entity.SurveyFeedback;
import com.quantumsoft.hrms.repository.SurveyFeedbackRepository;

import com.quantumsoft.hrms.servicei.SurveyFeedbackService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SurveyFeedbackServiceImpl implements SurveyFeedbackService {

    private final SurveyFeedbackRepository repository;

    @Override
    public SurveyFeedback create(SurveyFeedback feedback) {
        return repository.save(feedback);
    }

    @Override
    public List<SurveyFeedback> getAll() {
        return repository.findAll();
    }

    @Override
    public SurveyFeedback getById(UUID id) {
        return repository.findById(id).orElseThrow(() -> new RuntimeException("Survey not found"));
    }

    @Scheduled(cron = "0 0 * * * *")
    public void checkAndCloseExpiredSurveys() {
        List<SurveyFeedback> openSurveys = repository.findAll()
                .stream()
                .filter(s -> !s.isClosed() && s.getDeadline().isBefore(LocalDateTime.now()))
                .toList();

        for (SurveyFeedback survey : openSurveys) {
            survey.setClosed(true);
            repository.save(survey);
        }
    }
}