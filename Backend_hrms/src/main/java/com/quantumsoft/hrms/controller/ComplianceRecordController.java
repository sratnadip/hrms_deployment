package com.quantumsoft.hrms.controller;

import com.quantumsoft.hrms.entity.ComplianceRecord;
import com.quantumsoft.hrms.servicei.ComplianceRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/compliance-records")
@RequiredArgsConstructor
public class ComplianceRecordController {
    private final ComplianceRecordService service;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ComplianceRecord submit(@RequestBody ComplianceRecord record) {
        return service.submitCompliance(record);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{period}")
    public List<ComplianceRecord> getByMonth(@PathVariable String period) {
        return service.getByPeriod(period);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ComplianceRecord update(@PathVariable Long id, @RequestBody ComplianceRecord updated) {
        return service.updateRecord(id, updated);
    }
}
