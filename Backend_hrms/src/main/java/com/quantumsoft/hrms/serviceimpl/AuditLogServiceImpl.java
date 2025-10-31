package com.quantumsoft.hrms.serviceimpl;

import com.quantumsoft.hrms.entity.AuditLog;
import com.quantumsoft.hrms.enums.Action;
import com.quantumsoft.hrms.enums.Module;
import com.quantumsoft.hrms.exception.ResourceNotFoundException;
import com.quantumsoft.hrms.repository.AuditLogRepository;
import com.quantumsoft.hrms.servicei.AuditLogServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class AuditLogServiceImpl implements AuditLogServiceI {

    @Autowired
    private AuditLogRepository repository;

    @Override
    public void logInfo(UUID userId, Action action, Module module) {

        AuditLog auditLog = new AuditLog();

        auditLog.setUserId(userId);
        auditLog.setAction(action);
        auditLog.setModule(module);
        auditLog.setTime(LocalDateTime.now());

        repository.save(auditLog);
    }

    @Override
    public List<AuditLog> getAuditLog(UUID userId) {
        return repository.findByUserId(userId).orElseThrow(() -> new ResourceNotFoundException("AuditLog with given userId not found in database"));
    }

//    @Override
//    public List<AuditLog> getAuditLogs() {
//        return repository.findAll();
//    }
}
