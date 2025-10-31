package com.quantumsoft.hrms.entity;

import com.quantumsoft.hrms.enums.SurveyType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "survey_feedback")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SurveyFeedback {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String title;

    @Enumerated(EnumType.STRING)
    private SurveyType type;

    @ElementCollection
    private List<String> targetRoles;

    @Column(columnDefinition = "TEXT")
    private String questionsJson;

    private LocalDateTime createdAt;

    private LocalDateTime deadline;

    private boolean closed = false;

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
        if (this.deadline == null) {
            this.deadline = createdAt.plusDays(7);
        }
    }
}