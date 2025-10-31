package com.quantumsoft.hrms.entity;
import com.quantumsoft.hrms.enums.AppreciationCategory;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "appreciations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Appreciation {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_employee_id", nullable = false)
    private Employee fromEmployee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_employee_id", nullable = false)
    private Employee toEmployee;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AppreciationCategory category;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String message;

    @Column(nullable = false)
    private boolean isAnonymous;

    private LocalDate date;
}
