package com.quantumsoft.hrms.entity;



import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "dei_profiles")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class DeiProfile {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false, unique = true)
    private Employee employee;

    @Column(name = "gender_identity")
    private String genderIdentity;

    @Column
    private String ethnicity;

    @Column(name = "disability_status")
    private String disabilityStatus;

    @Column(name = "disclosure_consent")
    private Boolean disclosureConsent;

    @ElementCollection
    @CollectionTable(name = "dei_badges", joinColumns = @JoinColumn(name = "dei_profile_id"))
    @Column(name = "badge")
    private Set<String> badgesEarned;
}
