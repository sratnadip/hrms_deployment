package com.quantumsoft.hrms.entity;
import com.quantumsoft.hrms.enums.TicketStatus;
import com.quantumsoft.hrms.enums.TicketType;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tickets")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TicketType type; // QUERY, GRIEVANCE, POSH, PAYROLL

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TicketStatus status; // OPEN, IN_PROGRESS, CLOSED, ESCALATED

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_to")
    private Employee assignedTo; // HR/Admin assigned

    private LocalDateTime createdAt;
    private LocalDateTime resolvedAt;
}
