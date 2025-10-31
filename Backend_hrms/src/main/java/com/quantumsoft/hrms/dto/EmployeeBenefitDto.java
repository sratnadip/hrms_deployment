package com.quantumsoft.hrms.dto;

import com.quantumsoft.hrms.enums.Status;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
public class EmployeeBenefitDto {
    private UUID empId;
    private Long benefitId;
    private BigDecimal amount;
    private String notes;
}
