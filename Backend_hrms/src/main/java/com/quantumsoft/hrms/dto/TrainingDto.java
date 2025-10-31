package com.quantumsoft.hrms.dto;

import com.quantumsoft.hrms.entity.Employee;
import com.quantumsoft.hrms.enums.Role;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class TrainingDto {

    private UUID trainingId;
    private String title;
    private String description;
    @Enumerated(EnumType.STRING)
    private List<Role> mandatoryFor;
    private List<UUID> employeeIds;
    private LocalDate completionDeadline;
}
