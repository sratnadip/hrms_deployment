package com.quantumsoft.hrms.repository;

import com.quantumsoft.hrms.entity.Announcement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AnnouncementRepository extends JpaRepository<Announcement, UUID> {
}
