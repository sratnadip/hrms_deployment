package com.quantumsoft.hrms.entity;

import com.quantumsoft.hrms.enums.ComplianceType;
import com.quantumsoft.hrms.enums.Frequency;
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
public class Compliance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long complianceId;

    private String name;
    private String description;

    @Enumerated(EnumType.STRING)
    private ComplianceType type;

    @Enumerated(EnumType.STRING)
    private Frequency frequency;

    private String dueDate;
    private String penalty;
    private Boolean documentRequired;
    private Boolean isActive;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
