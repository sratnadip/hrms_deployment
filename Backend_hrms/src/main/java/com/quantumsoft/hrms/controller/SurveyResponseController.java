package com.quantumsoft.hrms.controller;


import com.quantumsoft.hrms.entity.SurveyResponse;
import com.quantumsoft.hrms.servicei.SurveyResponseService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/survey-responses")
@RequiredArgsConstructor
//@CrossOrigin("*")
public class SurveyResponseController {

    private final SurveyResponseService service;

    @PreAuthorize("hasRole('EMPLOYEE')")
    @PostMapping
    public SurveyResponse submit(@RequestBody SurveyResponse response) {
        return service.submit(response);
    }
}

