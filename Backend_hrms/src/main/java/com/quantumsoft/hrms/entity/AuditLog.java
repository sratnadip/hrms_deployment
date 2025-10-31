package com.quantumsoft.hrms.entity;

import com.quantumsoft.hrms.enums.Action;
import com.quantumsoft.hrms.enums.Module;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Data
@Entity
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID auditLogId;
    private UUID userId;
    @Enumerated(EnumType.STRING)
    private Action action;
    @Enumerated(EnumType.STRING)
    private Module module;
    private LocalDateTime time;
}
