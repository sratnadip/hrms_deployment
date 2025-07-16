package com.quantumsoft.hrms.controller;

import com.quantumsoft.hrms.entity.SurveyFeedback;

import com.quantumsoft.hrms.servicei.SurveyFeedbackService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/surveys")
@RequiredArgsConstructor
//@CrossOrigin("*")
public class SurveyFeedbackController {

    private final SurveyFeedbackService service;

    @PreAuthorize("hasAnyRole('HR', 'ADMIN')")
    @PostMapping
    public SurveyFeedback create(@RequestBody SurveyFeedback feedback) {
        return service.create(feedback);
    }

    @PreAuthorize("hasAnyRole('EMPLOYEE', 'HR', 'ADMIN')")
    @GetMapping
    public List<SurveyFeedback> list() {
        return service.getAll();
    }

    @PreAuthorize("hasAnyRole('EMPLOYEE', 'HR', 'ADMIN')")
    @GetMapping("/{id}")
    public SurveyFeedback get(@PathVariable UUID id) {
        return service.getById(id);
    }
}
