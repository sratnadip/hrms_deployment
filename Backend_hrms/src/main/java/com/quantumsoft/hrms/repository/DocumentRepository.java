package com.quantumsoft.hrms.repository;


import com.quantumsoft.hrms.entity.Document;
import com.quantumsoft.hrms.enums.DocumentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface DocumentRepository extends JpaRepository<Document, UUID> {
    List<Document> findByEmployeeEmpId(UUID empId);
    List<Document> findByDocumentStatus(DocumentStatus status);
}