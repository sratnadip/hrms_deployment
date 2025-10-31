package com.quantumsoft.hrms.dto;

import com.quantumsoft.hrms.enums.AppreciationCategory;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppreciationResponse {
    private UUID id;
    private UUID fromEmpId;
    private String fromEmployeeName;
    private UUID toEmpId;
    private String toEmployeeName;
    private AppreciationCategory category;
    private String message;
    private boolean anonymous;
    private LocalDate date;
}
