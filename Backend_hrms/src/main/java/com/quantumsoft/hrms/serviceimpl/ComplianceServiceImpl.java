package com.quantumsoft.hrms.serviceimpl;

import com.quantumsoft.hrms.entity.Compliance;
import com.quantumsoft.hrms.repository.ComplianceRepository;
import com.quantumsoft.hrms.servicei.ComplianceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ComplianceServiceImpl implements ComplianceService {
    private final ComplianceRepository repo;

    @Override
    public Compliance saveCompliance(Compliance compliance) {
        compliance.setCreatedAt(LocalDateTime.now());
        compliance.setUpdatedAt(LocalDateTime.now());
        return repo.save(compliance);
    }

    @Override
    public List<Compliance> getAll() {
        return repo.findAll();
    }}
