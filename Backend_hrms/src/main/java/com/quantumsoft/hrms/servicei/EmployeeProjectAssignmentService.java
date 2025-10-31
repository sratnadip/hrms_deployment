package com.quantumsoft.hrms.servicei;

import com.quantumsoft.hrms.dto.EmployeeProjectAssignmentDTO;
import com.quantumsoft.hrms.entity.EmployeeProjectAssignment;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EmployeeProjectAssignmentService {

    EmployeeProjectAssignment assignEmployee(EmployeeProjectAssignmentDTO dto);

    EmployeeProjectAssignment updateAssignment(UUID id, EmployeeProjectAssignmentDTO dto);

    List<EmployeeProjectAssignment> getAssignmentsByEmployee(UUID employeeId);

    List<EmployeeProjectAssignment> getAssignmentsByProjectById(Long projectId);

    void deleteAssignment(UUID id);
}
