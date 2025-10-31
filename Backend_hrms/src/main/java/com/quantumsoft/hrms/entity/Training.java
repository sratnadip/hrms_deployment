package com.quantumsoft.hrms.entity;

import com.quantumsoft.hrms.enums.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Mutability;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Entity
public class Training {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID trainingId;
    private String title;
    private String description;
    @Enumerated(EnumType.STRING)
    private List<Role> mandatoryFor;
    @ManyToMany // Initially I tried with OneToMany but I was not able to assign new training to the same employee so that's why I am using ManyToMany now.
    private List<Employee> assignedTo;
    private LocalDate completionDeadline;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    public void setCreatedAt(){
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    public void setUpdatedAt(){
        this.updatedAt = LocalDateTime.now();
    }
}
