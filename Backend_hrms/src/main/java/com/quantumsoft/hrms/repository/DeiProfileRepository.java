package com.quantumsoft.hrms.repository;
import com.quantumsoft.hrms.entity.DeiProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface DeiProfileRepository extends JpaRepository<DeiProfile, UUID> {
    Optional<DeiProfile> findByEmployeeEmpId(UUID empId);
}
