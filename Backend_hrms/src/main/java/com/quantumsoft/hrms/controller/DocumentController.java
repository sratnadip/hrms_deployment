package com.quantumsoft.hrms.controller;

import com.quantumsoft.hrms.entity.Document;
import com.quantumsoft.hrms.servicei.DocumentService;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

//@CrossOrigin("*")
@RestController
@RequestMapping("/api/documents")
@RequiredArgsConstructor
@Validated
public class DocumentController {

    private final DocumentService documentService;
    //hr or admin only upload docs
     @PostMapping("/upload/{empId}")
     public ResponseEntity<Document> upload(
        @PathVariable UUID empId,
        @RequestParam String type,
        @RequestParam MultipartFile file,
        Principal principal) {

    // Correct method call using principal.getName() as uploaderUsername
    return ResponseEntity.ok(
            documentService.uploadDocument(empId, type, file, principal.getName())
    );
}


    @GetMapping("/employee/{empId}")
    public ResponseEntity<List<Document>> list(@PathVariable UUID empId) {
        return ResponseEntity.ok(documentService.getDocumentsByEmployee(empId));
    }

    @PostMapping("/verify/{id}/{userId}")
    public ResponseEntity<Document> verify(
            @PathVariable UUID id,
            @PathVariable UUID userId,
            @RequestParam String remarks) {
        return ResponseEntity.ok(documentService.verifyDocument(id, userId, remarks));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        documentService.deleteDocument(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/expiring")
    public ResponseEntity<List<Document>> expiring() {
        return ResponseEntity.ok(documentService.getExpiringDocuments());
    }
}

