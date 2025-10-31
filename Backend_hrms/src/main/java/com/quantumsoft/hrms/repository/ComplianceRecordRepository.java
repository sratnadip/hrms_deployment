package com.quantumsoft.hrms.repository;

import com.quantumsoft.hrms.entity.ComplianceRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ComplianceRecordRepository extends JpaRepository<ComplianceRecord, Long> {
    List<ComplianceRecord> findByPeriod(String period);
}
