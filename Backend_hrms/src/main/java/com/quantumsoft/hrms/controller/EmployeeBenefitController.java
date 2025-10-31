package com.quantumsoft.hrms.controller;

import com.quantumsoft.hrms.dto.EmployeeBenefitDto;
import com.quantumsoft.hrms.entity.EmployeeBenefit;
import com.quantumsoft.hrms.servicei.EmployeeBenefitService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/employee-benefits")
@RequiredArgsConstructor
public class EmployeeBenefitController {

    private final EmployeeBenefitService service;

    @PreAuthorize("hasRole('HR') or hasRole('ADMIN')")
    @PostMapping
    public EmployeeBenefit assign(@RequestBody EmployeeBenefitDto employeeBenefitDto) {
        return service.assignBenefit(employeeBenefitDto);
    }

    @PreAuthorize("hasRole('HR') or hasRole('EMPLOYEE') or hasRole('ADMIN')")
    @GetMapping("/{empId}")
    public List<EmployeeBenefit> getBenefits(@PathVariable UUID empId) {
        return service.getBenefitsForEmployee(empId);
    }

    @PreAuthorize("hasRole('HR') or hasRole('ADMIN')")
    @PutMapping("/{employeeBenefitId}")
    public EmployeeBenefit update(@PathVariable Long employeeBenefitId, @RequestBody EmployeeBenefit updated) {
        return service.updateBenefit(employeeBenefitId, updated);
    }

    @PreAuthorize("hasRole('HR') or hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public void softDelete(@PathVariable Long id) {
        service.softDeleteBenefit(id);
    }
}

