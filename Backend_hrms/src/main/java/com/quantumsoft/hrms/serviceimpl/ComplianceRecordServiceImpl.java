package com.quantumsoft.hrms.serviceimpl;

import com.quantumsoft.hrms.entity.ComplianceRecord;
import com.quantumsoft.hrms.repository.ComplianceRecordRepository;
import com.quantumsoft.hrms.servicei.ComplianceRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ComplianceRecordServiceImpl implements ComplianceRecordService {
    private final ComplianceRecordRepository repo;

    @Override
    public ComplianceRecord submitCompliance(ComplianceRecord record) {
        record.setCreatedAt(LocalDateTime.now());
        record.setUpdatedAt(LocalDateTime.now());
        record.setSubmittedOn(LocalDateTime.now());
        return repo.save(record);
    }

    @Override
    public List<ComplianceRecord> getByPeriod(String period) {
        return repo.findByPeriod(period);
    }

    @Override
    public ComplianceRecord updateRecord(Long id, ComplianceRecord updated) {
        ComplianceRecord existing = repo.findById(id).orElseThrow();
        existing.setRemarks(updated.getRemarks());
        existing.setFilePath(updated.getFilePath());
        existing.setUpdatedAt(LocalDateTime.now());
        return repo.save(existing);
    }
}