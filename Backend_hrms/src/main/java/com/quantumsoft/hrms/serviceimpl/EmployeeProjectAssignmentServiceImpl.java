package com.quantumsoft.hrms.serviceimpl;

import com.quantumsoft.hrms.dto.EmployeeProjectAssignmentDTO;
import com.quantumsoft.hrms.entity.Employee;
import com.quantumsoft.hrms.entity.EmployeeProjectAssignment;
import com.quantumsoft.hrms.entity.Project;
import com.quantumsoft.hrms.enums.AssignmentStatus;
import com.quantumsoft.hrms.exception.ResourceNotFoundException;
import com.quantumsoft.hrms.repository.EmployeeProjectAssignmentRepository;
import com.quantumsoft.hrms.repository.EmployeeRepository;
import com.quantumsoft.hrms.repository.ProjectRepository;
import com.quantumsoft.hrms.servicei.EmployeeProjectAssignmentService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class EmployeeProjectAssignmentServiceImpl implements EmployeeProjectAssignmentService {

    @Autowired
    private EmployeeProjectAssignmentRepository assignmentRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Override
    public EmployeeProjectAssignment assignEmployee(EmployeeProjectAssignmentDTO dto) {
        if (dto.getEmployeeId() == null) {
            throw new IllegalArgumentException("Employee ID must not be null");
        }
        if (dto.getProjectId() == null) {
            throw new IllegalArgumentException("Project ID must not be null");
        }

        Employee employee = employeeRepository.findById(dto.getEmployeeId())
                .orElseThrow(() -> new EntityNotFoundException("Employee not found"));

        Project project = projectRepository.findById(dto.getProjectId())
                .orElseThrow(() -> new EntityNotFoundException("Project not found"));

        EmployeeProjectAssignment assignment = new EmployeeProjectAssignment();
        assignment.setEmployee(employee);
        assignment.setProject(project);
        assignment.setRole(dto.getRole());
        assignment.setAssignedDate(dto.getAssignedDate() != null ? dto.getAssignedDate() : LocalDate.now());
        assignment.setReleaseDate(dto.getReleaseDate());
        assignment.setStatus(dto.getStatus() != null ? dto.getStatus() : AssignmentStatus.ACTIVE);

        return assignmentRepository.save(assignment);
    }

    @Override
    public EmployeeProjectAssignment updateAssignment(UUID id, EmployeeProjectAssignmentDTO dto) {
        EmployeeProjectAssignment existing = assignmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Assignment not found"));

        if (dto.getRole() != null) {
            existing.setRole(dto.getRole());
        }

        if (dto.getReleaseDate() != null) {
            existing.setReleaseDate(dto.getReleaseDate());
            if (!dto.getReleaseDate().isAfter(LocalDate.now())) {
                existing.setStatus(AssignmentStatus.RELEASED);
            }
        }
        return assignmentRepository.save(existing);
    }

    @Override
    public List<EmployeeProjectAssignment> getAssignmentsByEmployee(UUID employeeId) {
        return assignmentRepository.findByEmployee_EmpId(employeeId);
    }

    @Override
    public List<EmployeeProjectAssignment> getAssignmentsByProjectById(Long projectId) {
        return assignmentRepository.findByProject_ProjectId(projectId);
    }

    @Override
    public void deleteAssignment(UUID id) {
        EmployeeProjectAssignment assignment = assignmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Assignment not found with ID: " + id));
        assignmentRepository.delete(assignment);
    }
}
