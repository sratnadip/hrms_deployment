package com.quantumsoft.hrms.serviceimpl;

import com.quantumsoft.hrms.entity.*;
import com.quantumsoft.hrms.enums.DocumentStatus;
import com.quantumsoft.hrms.enums.Role;
import com.quantumsoft.hrms.exception.ResourceNotFoundException;
import com.quantumsoft.hrms.repository.*;

import com.quantumsoft.hrms.servicei.DocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DocumentServiceImpl implements DocumentService {

    private final DocumentRepository documentRepository;
    private final EmployeeRepository employeeRepository;
    private final UserRepository userRepository;

    @Value("${hrms.upload.dir}")
    private String uploadDir;

    @Override
    public Document uploadDocument(UUID empId, String type, MultipartFile file, String uploaderUsername) {
        Employee employee = employeeRepository.findById(empId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));

        User uploader = userRepository.findByUsername(uploaderUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Uploader not found"));

        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        File uploadPath = new File(uploadDir);

        if (!uploadPath.exists()) {
            uploadPath.mkdirs();
        }

        File destinationFile = new File(uploadPath, fileName);

        try {
            file.transferTo(destinationFile);
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file", e);
        }

        Document document = Document.builder()
                .employee(employee)
                .uploadedBy(uploader)
                .type(type)
                .fileName(file.getOriginalFilename())
                .fileSize(file.getSize())
                .fileType(file.getContentType())
                .url("/uploads/" + fileName)
                .documentStatus(DocumentStatus.UPLOADED)
                .build();

        return documentRepository.save(document);
    }

    @Override
    public List<Document> getDocumentsByEmployee(UUID empId) {
        return documentRepository.findByEmployeeEmpId(empId);
    }

    @Override
    public void deleteDocument(UUID id) {
        Document doc = documentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Document not found"));

        File file = new File(uploadDir + File.separator + new File(doc.getUrl()).getName());
        if (file.exists()) file.delete();

        documentRepository.delete(doc);
    }

    @Override
    public Document verifyDocument(UUID id, UUID userId, String remarks) {
        Document document = documentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Document not found"));

        User verifier = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (!(verifier.getRole().equals(Role.HR) || verifier.getRole().equals(Role.ADMIN))) {
            throw new AccessDeniedException("Only HR or Admin can verify documents");
        }

        document.setVerified(true);
        document.setVerifiedBy(verifier);
        document.setVerifiedAt(LocalDateTime.now());
        document.setDocumentStatus(DocumentStatus.VERIFIED);
        document.setRemarks(remarks);
        return documentRepository.save(document);
    }

    @Override
    public List<Document> getExpiringDocuments() {
        return documentRepository.findByDocumentStatus(DocumentStatus.EXPIRED);
    }
}
