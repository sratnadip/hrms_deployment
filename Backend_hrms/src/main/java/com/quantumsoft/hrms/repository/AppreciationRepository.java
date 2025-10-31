package com.quantumsoft.hrms.repository;

import com.quantumsoft.hrms.entity.Appreciation;
import com.quantumsoft.hrms.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface AppreciationRepository extends JpaRepository<Appreciation, UUID> {
    List<Appreciation> findByToEmployee(Employee toEmployee);
    List<Appreciation> findByFromEmployee(Employee fromEmployee);
}