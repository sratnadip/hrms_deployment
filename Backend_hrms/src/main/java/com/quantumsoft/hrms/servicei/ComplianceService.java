package com.quantumsoft.hrms.servicei;

import com.quantumsoft.hrms.entity.Compliance;

import java.util.List;

public interface ComplianceService {
    Compliance saveCompliance(Compliance compliance);
    List<Compliance> getAll();
}
