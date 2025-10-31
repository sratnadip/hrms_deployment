package com.quantumsoft.hrms.servicei;

import com.quantumsoft.hrms.entity.ComplianceRecord;

import java.util.List;

public interface ComplianceRecordService {
    ComplianceRecord submitCompliance(ComplianceRecord record);
    List<ComplianceRecord> getByPeriod(String period);
    ComplianceRecord updateRecord(Long id, ComplianceRecord updated);
}
