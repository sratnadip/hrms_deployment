package com.quantumsoft.hrms.controller;

import com.quantumsoft.hrms.dto.AppreciationResponse;
import com.quantumsoft.hrms.entity.Appreciation;
import com.quantumsoft.hrms.servicei.AppreciationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/appreciations")
//@CrossOrigin("*")
@RequiredArgsConstructor
public class AppreciationController {

    private final AppreciationService appreciationService;

    @PreAuthorize("hasAnyRole('EMPLOYEE', 'MANAGER', 'HR', 'ADMIN')")
    @PostMapping
    public Appreciation sendAppreciation(@RequestBody Appreciation appreciation) {
        return appreciationService.sendAppreciation(appreciation);
    }

    @PreAuthorize("hasAnyRole('EMPLOYEE', 'MANAGER', 'HR', 'ADMIN')")
    @GetMapping("/to/{empId}")
    public List<AppreciationResponse> getReceivedAppreciations(@PathVariable UUID empId) {
        return appreciationService.getAppreciationsForEmployee(empId)
                .stream()
                .map(app -> AppreciationResponse.builder()
                        .id(app.getId())
                        .fromEmpId(app.getFromEmployee().getEmpId())
                        .fromEmployeeName(app.isAnonymous() ? "Anonymous" : app.getFromEmployee().getFirstName())
                        .toEmpId(app.getToEmployee().getEmpId())
                        .toEmployeeName(app.getToEmployee().getFirstName())
                        .category(app.getCategory())
                        .message(app.getMessage())
                        .anonymous(app.isAnonymous())
                        .date(app.getDate())
                        .build())
                .toList();
    }


    @PreAuthorize("hasAnyRole('EMPLOYEE', 'MANAGER', 'HR', 'ADMIN')")
    @GetMapping("/from/{empId}")
    public List<AppreciationResponse> getSentAppreciations(@PathVariable UUID empId) {
        return appreciationService.getAppreciationsBySender(empId)
                .stream()
                .map(app -> AppreciationResponse.builder()
                        .id(app.getId())
                        .fromEmpId(app.getFromEmployee().getEmpId())
                        .fromEmployeeName(app.isAnonymous() ? "Anonymous" : app.getFromEmployee().getFirstName())
                        .toEmpId(app.getToEmployee().getEmpId())
                        .toEmployeeName(app.getToEmployee().getFirstName())
                        .category(app.getCategory())
                        .message(app.getMessage())
                        .anonymous(app.isAnonymous())
                        .date(app.getDate())
                        .build())
                .toList();
    }


    @PreAuthorize("hasAnyRole('HR', 'ADMIN')")
    @GetMapping("/all")
    public List<Appreciation> getAllAppreciations() {
        return appreciationService.getAll();
    }
}
