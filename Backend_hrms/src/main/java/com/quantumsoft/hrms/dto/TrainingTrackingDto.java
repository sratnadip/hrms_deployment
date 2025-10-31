package com.quantumsoft.hrms.dto;

import com.quantumsoft.hrms.enums.TrainingCompletionStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TrainingTrackingDto {

    private UUID trainingTrackingId;
    private UUID employeeId;
    private UUID trainingId;
    private String title;
    private String description;
    private LocalDate completionDeadline;
    private LocalDate startDate;
    private LocalDate completionDate;
    @Enumerated(EnumType.STRING)
    private TrainingCompletionStatus status;
    @Min(value = 1)
    @Max(value = 10)
    private Integer score;
    private String feedback;
}
