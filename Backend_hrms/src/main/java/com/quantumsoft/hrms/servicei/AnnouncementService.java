package com.quantumsoft.hrms.servicei;

import com.quantumsoft.hrms.entity.Announcement;

import java.util.List;
import java.util.UUID;

public interface AnnouncementService {
    Announcement createAnnouncement(Announcement announcement);

    List<Announcement> getAnnouncementsFor(String role, String department, UUID employeeId);

    void deleteAnnouncement(UUID id);
}
