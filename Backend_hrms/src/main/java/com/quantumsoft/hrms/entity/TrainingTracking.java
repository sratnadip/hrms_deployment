package com.quantumsoft.hrms.entity;

import com.quantumsoft.hrms.enums.TrainingCompletionStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Entity
public class TrainingTracking {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID trainingTrackingId;
    @OneToOne
    @JoinColumn(name = "empId")
    private Employee employee;
    @OneToOne
    @JoinColumn(name = "trainingId")
    private Training training;
    private LocalDate startDate;
    private LocalDate completionDate;
    @Enumerated(EnumType.STRING)
    private TrainingCompletionStatus status;
    @Min(value = 1)
    @Max(value = 10)
    private Integer score;
    private String feedback;

}
