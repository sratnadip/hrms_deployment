package com.quantumsoft.hrms.serviceimpl;

import com.quantumsoft.hrms.dto.EmployeeBenefitDto;
import com.quantumsoft.hrms.entity.Benefit;
import com.quantumsoft.hrms.entity.Employee;
import com.quantumsoft.hrms.entity.EmployeeBenefit;
import com.quantumsoft.hrms.enums.Status;
import com.quantumsoft.hrms.exception.ResourceNotFoundException;
import com.quantumsoft.hrms.repository.BenefitRepository;
import com.quantumsoft.hrms.repository.EmployeeBenefitRepository;
import com.quantumsoft.hrms.repository.EmployeeRepository;
import com.quantumsoft.hrms.servicei.EmployeeBenefitService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmployeeBenefitServiceImpl implements EmployeeBenefitService {

    @Autowired
    private EmployeeBenefitRepository repository;
    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private BenefitRepository benefitRepository;

//    @Override
//    public EmployeeBenefit assignBenefit(EmployeeBenefit benefit) {

    @Override
    public EmployeeBenefit assignBenefit(EmployeeBenefitDto employeeBenefitDto) {

       Employee employee=employeeRepository.findById(employeeBenefitDto.getEmpId())
               .orElseThrow(()->new ResourceNotFoundException("Employee not in dataBase"));
        System.out.println("empId "+employee.getEmpId());

        Benefit benefit = benefitRepository.findById(employeeBenefitDto.getBenefitId())
                .orElseThrow(() -> new ResourceNotFoundException("Benefit not in dataBase"));

//        var oldBenefits =repository.findByEmployee_EmpIdAndBenefit_BenefitIdAndStatus(
//                employeeBenefitDto.getEmpId(), employeeBenefitDto.getBenefitId(), String.valueOf(Status.ACTIVE));
//
//        for (EmployeeBenefit eb : oldBenefits) {
//            eb.setEmployee(employee);
//            eb.setEffectiveTo(LocalDate.now());
//            eb.setStatus(Status.DEACTIVE);
//            eb.setUpdatedAt(LocalDateTime.now());
//            repository.save(eb);
//        }

        if(repository.existsByEmployee_empIdAndStatus(employeeBenefitDto.getEmpId(), Status.ACTIVE)){
            EmployeeBenefit employeeBenefit = repository.findByEmployee_empIdAndStatus(employeeBenefitDto.getEmpId(), Status.ACTIVE);

            employeeBenefit.setEffectiveTo(LocalDate.now());
            employeeBenefit.setStatus(Status.DEACTIVE);
            repository.save(employeeBenefit);
        }

        EmployeeBenefit newEmployeeBenefit = new EmployeeBenefit();

        newEmployeeBenefit.setEmployee(employee);
        newEmployeeBenefit.setBenefit(benefit);
        newEmployeeBenefit.setStatus(Status.ACTIVE);
        newEmployeeBenefit.setEffectiveFrom(LocalDate.now());
        newEmployeeBenefit.setAmount(employeeBenefitDto.getAmount());
        newEmployeeBenefit.setNotes(employeeBenefitDto.getNotes());
        newEmployeeBenefit.setEffectiveTo(null);

        return repository.save(newEmployeeBenefit);
    }

    ////        EmployeeRepository.findById(benefit.getEmployee().getEmpId())
////                .orElseThrow(()->new ResourceNotFoundException("Employee not in dataBase"));
////
////
//
//  var oldBenefits =repository.findByEmployee_EmpIdAndBenefit_BenefitIdAndStatus(
//          benefit.getEmployee().getEmpId(), benefit.getBenefit().getBenefitId(), String.valueOf(Status.ACTIVE));
//        EmployeeRepository.findById(benefit.getEmployee().getEmpId())
//                .orElseThrow(()->new ResourceNotFoundException("Employee not in dataBase"));
//
//
//        for (EmployeeBenefit eb : oldBenefits) {
//            eb.setEffectiveTo(LocalDate.now());
//            eb.setStatus(Status.INACTIVE);
//            eb.setUpdatedAt(LocalDateTime.now());
//            repository.save(eb);
//        }
//
//        benefit.setStatus(Status.ACTIVE);
//        benefit.setEffectiveFrom(LocalDate.now());
//        benefit.setCreatedAt(LocalDateTime.now());
//        return repository.save(benefit);
//    }

    @Override
    public List<EmployeeBenefit> getBenefitsForEmployee(UUID empId) {
        return repository.findByEmployee_EmpId(empId);
    }

    @Override
    public EmployeeBenefit updateBenefit(Long id, EmployeeBenefit updated) {
        EmployeeBenefit existing = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("EmployeeBenefit record not found in database"));
        updated.setEmployee(existing.getEmployee());
        return repository.save(updated);
    }

    @Override
    public void softDeleteBenefit(Long id) {
        EmployeeBenefit existing = repository.findById(id).orElseThrow();
        existing.setStatus(Status.DEACTIVE);
        existing.setEffectiveTo(LocalDate.now());
        existing.setUpdatedAt(LocalDateTime.now());
        repository.save(existing);
    }
}

