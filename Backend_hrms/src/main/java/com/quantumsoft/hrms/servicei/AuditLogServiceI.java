package com.quantumsoft.hrms.servicei;

import com.quantumsoft.hrms.entity.AuditLog;
import com.quantumsoft.hrms.enums.Action;
import com.quantumsoft.hrms.enums.Module;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface AuditLogServiceI {
   public void logInfo(UUID userId, Action action, Module module);

   public List<AuditLog> getAuditLog(UUID userId);

   //public List<AuditLog> getAuditLogs();
}
