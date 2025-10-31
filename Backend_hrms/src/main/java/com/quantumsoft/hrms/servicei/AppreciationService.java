package com.quantumsoft.hrms.servicei;

import com.quantumsoft.hrms.entity.Appreciation;

import java.util.List;
import java.util.UUID;

public interface AppreciationService {
    Appreciation sendAppreciation(Appreciation appreciation);
    List<Appreciation> getAppreciationsForEmployee(UUID employeeId);
    List<Appreciation> getAppreciationsBySender(UUID employeeId);
    List<Appreciation> getAll();
}