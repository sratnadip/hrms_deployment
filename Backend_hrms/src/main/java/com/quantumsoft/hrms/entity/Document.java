package com.quantumsoft.hrms.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.quantumsoft.hrms.enums.DocumentStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "documents")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Document {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Employee employee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uploaded_by", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private User uploadedBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "verified_by")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private User verifiedBy;


    @NotBlank(message = "Document type is required")
    private String type;

    @NotBlank(message = "File name is required")
    private String fileName;

    private String fileType;
    private Long fileSize;

    @NotBlank(message = "URL is required")
    private String url;

    private Integer version = 1;
    private LocalDateTime expiryDate;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Status is required")
    private DocumentStatus documentStatus = DocumentStatus.UPLOADED;

    @Column(length = 1000)
    private String remarks;

    private boolean verified = false;
    private LocalDateTime verifiedAt;

    @Column(nullable = false, updatable = false)
    private LocalDateTime uploadedAt;

    @PrePersist
    public void prePersist() {
        this.uploadedAt = LocalDateTime.now();
    }
}

