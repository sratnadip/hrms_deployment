package com.quantumsoft.hrms.repository;

import com.quantumsoft.hrms.entity.EmployeeProjectAssignment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface EmployeeProjectAssignmentRepository extends JpaRepository<EmployeeProjectAssignment, UUID> {

    List<EmployeeProjectAssignment> findByEmployee_EmpId(UUID empId);

    List<EmployeeProjectAssignment> findByProject_ProjectId(Long projectId);
}
