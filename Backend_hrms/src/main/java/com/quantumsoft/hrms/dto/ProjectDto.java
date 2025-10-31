package com.quantumsoft.hrms.dto;

import com.quantumsoft.hrms.enums.ProjectStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class ProjectDto {

    private UUID managerId;
    private String projectName;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    @Enumerated(EnumType.STRING)
    private ProjectStatus status;
}
