package com.quantumsoft.hrms.controller;

import com.quantumsoft.hrms.entity.Compliance;
import com.quantumsoft.hrms.servicei.ComplianceService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/compliances")
@RequiredArgsConstructor
public class ComplianceController {
    private final ComplianceService service;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public Compliance create(@RequestBody Compliance compliance) {
        return service.saveCompliance(compliance);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public List<Compliance> getAll() {
        return service.getAll();
    }
}
