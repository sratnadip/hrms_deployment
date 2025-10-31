package com.quantumsoft.hrms.serviceimpl;
import com.quantumsoft.hrms.entity.DeiProfile;
import com.quantumsoft.hrms.entity.Employee;
import com.quantumsoft.hrms.repository.DeiProfileRepository;
import com.quantumsoft.hrms.repository.EmployeeRepository;
import com.quantumsoft.hrms.servicei.DeiService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeiServiceImpl implements DeiService {

    private final DeiProfileRepository deiRepo;
    private final EmployeeRepository empRepo;

    @Override
    public DeiProfile getDeiProfile(UUID empId) {
        return deiRepo.findByEmployeeEmpId(empId)
                .orElseThrow(() -> new EntityNotFoundException("DEI profile not found"));
    }

    @Override
    public DeiProfile upsertDeiProfile(DeiProfile profile) {
        Employee emp = empRepo.findById(profile.getEmployee().getEmpId())
                .orElseThrow(() -> new EntityNotFoundException("Employee not found"));
        profile.setEmployee(emp);
        return deiRepo.save(profile);
    }
}
