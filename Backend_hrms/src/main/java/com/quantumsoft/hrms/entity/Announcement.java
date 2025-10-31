package com.quantumsoft.hrms.entity;
import com.quantumsoft.hrms.enums.Visibility;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "announcements")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Announcement {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotBlank(message = "Title is required")
    @Size(max = 150, message = "Title too long")
    private String title;

    @NotBlank(message = "Message is required")
    @Size(max = 2000)
    private String message;

//    @Column(nullable = false)
//    private UUID postedBy; // FK to User or Employee

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Visibility visibility; // ALL, DEPARTMENT, ROLE, EMPLOYEE

    private String visibleToValue; // Dept name / Role / Employee UUID string

    private LocalDate createdAt;

    private LocalDate expiresOn;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDate.now();
    }
}
