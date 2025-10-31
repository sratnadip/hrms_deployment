package com.quantumsoft.hrms.servicei;


import com.quantumsoft.hrms.entity.Document;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface DocumentService {
    Document uploadDocument(UUID empId, String type, MultipartFile file, String uploaderUsername);

    List<Document> getDocumentsByEmployee(UUID empId);
    void deleteDocument(UUID id);
    Document verifyDocument(UUID id, UUID userId, String remarks);
    List<Document> getExpiringDocuments();
}