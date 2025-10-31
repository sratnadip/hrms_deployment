package com.quantumsoft.hrms.serviceimpl;

import com.quantumsoft.hrms.entity.Appreciation;
import com.quantumsoft.hrms.entity.Employee;
import com.quantumsoft.hrms.repository.AppreciationRepository;
import com.quantumsoft.hrms.repository.EmployeeRepository;
import com.quantumsoft.hrms.servicei.AppreciationService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AppreciationServiceImpl implements AppreciationService {

    private final AppreciationRepository appreciationRepository;
    private final EmployeeRepository employeeRepository;

    @Override
    public Appreciation sendAppreciation(Appreciation appreciation) {
        appreciation.setDate(LocalDate.now());
        return appreciationRepository.save(appreciation);
    }

    @Override
    public List<Appreciation> getAppreciationsForEmployee(UUID employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found"));
        return appreciationRepository.findByToEmployee(employee);
    }

    @Override
    public List<Appreciation> getAppreciationsBySender(UUID employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found"));
        return appreciationRepository.findByFromEmployee(employee);
    }

    @Override
    public List<Appreciation> getAll() {
        return appreciationRepository.findAll();
    }
}