package com.quantumsoft.hrms.entity;

import com.quantumsoft.hrms.enums.ComplianceStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ComplianceRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long recordId;

    @ManyToOne
    @JoinColumn(name = "compliance_id")
    private Compliance compliance;

    private String period;

    @Enumerated(EnumType.STRING)
    private ComplianceStatus status;

    private String filePath;
    private LocalDateTime submittedOn;
    private String remarks;
    private String createdBy;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
