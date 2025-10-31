package com.quantumsoft.hrms.controller;

import com.quantumsoft.hrms.dto.EmployeeProjectAssignmentDTO;
import com.quantumsoft.hrms.entity.EmployeeProjectAssignment;
import com.quantumsoft.hrms.servicei.EmployeeProjectAssignmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/employee-projects")
public class EmployeeProjectAssignmentController {

    @Autowired
    private EmployeeProjectAssignmentService service;

    @PreAuthorize("hasRole('MANAGER')")
    @PostMapping
    public ResponseEntity<EmployeeProjectAssignment> assignEmployee(@RequestBody EmployeeProjectAssignmentDTO dto) {
        EmployeeProjectAssignment saved = service.assignEmployee(dto);
        return ResponseEntity.status(201).body(saved);
    }

    @PreAuthorize("hasRole('MANAGER')")
    @PutMapping("/{id}")
    public ResponseEntity<EmployeeProjectAssignment> updateAssignment(@PathVariable UUID id,
                                                                      @RequestBody EmployeeProjectAssignmentDTO dto) {
        return ResponseEntity.ok(service.updateAssignment(id, dto));
    }


    @PreAuthorize("hasRole('MANAGER')")
    @GetMapping("/{id}")
    public ResponseEntity<List<EmployeeProjectAssignment>> getAssignmentsByProjectId(@PathVariable Long id) {
        return new ResponseEntity<List<EmployeeProjectAssignment>>(service.getAssignmentsByProjectById(id), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('MANAGER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.deleteAssignment(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('EMPLOYEE')")
    @GetMapping("/employee/{id}")
    public ResponseEntity<List<EmployeeProjectAssignment>> getAssignmentsByEmployee(@PathVariable String id) {
        UUID uuid = UUID.fromString(id);
        List<EmployeeProjectAssignment> assignments = service.getAssignmentsByEmployee(uuid);
        return ResponseEntity.ok(assignments);
    }
}
