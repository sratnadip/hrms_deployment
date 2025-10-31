package com.quantumsoft.hrms.controller;

import com.quantumsoft.hrms.entity.Announcement;
import com.quantumsoft.hrms.enums.Visibility;
import com.quantumsoft.hrms.servicei.AnnouncementService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/announcements")
@CrossOrigin("*")
@RequiredArgsConstructor
public class AnnouncementController {

    private final AnnouncementService service;
    private final NotificationSocketController socketController;

    @PostMapping
    public Announcement create(@Valid @RequestBody Announcement announcement) {
        // ✅ Validate visibleToValue if visibility is not ALL
        if (announcement.getVisibility() != Visibility.ALL) {
            if (announcement.getVisibleToValue() == null || announcement.getVisibleToValue().isBlank()) {
                throw new IllegalArgumentException("visibleToValue is required when visibility is " + announcement.getVisibility());
            }

            // Validate UUID format if visibility is EMPLOYEE
            if (announcement.getVisibility() == Visibility.EMPLOYEE) {
                try {
                    UUID.fromString(announcement.getVisibleToValue());
                } catch (IllegalArgumentException ex) {
                    throw new IllegalArgumentException("Invalid UUID format for visibleToValue (EMPLOYEE)");
                }
            }

        }

        Announcement created = service.createAnnouncement(announcement);

        // ✅ Send WebSocket Notification
        String title = "📢 New Announcement";
        String message = created.getTitle() + ": " + created.getMessage();

        switch (created.getVisibility()) {
            case ALL:
                // Optional: broadcast to all
                break;

            case EMPLOYEE:
                socketController.sendToUser(UUID.fromString(created.getVisibleToValue()), title, message);
                break;

            case DEPARTMENT:
                // Optional: notify department users
                break;

            case ROLE:
                // Optional: notify role-based users
                break;
        }

        return created;
    }

    @GetMapping("/visible")
    public List<Announcement> getVisibleAnnouncements(
            @RequestParam String role,
            @RequestParam String department,
            @RequestParam UUID empId
    ) {
        return service.getAnnouncementsFor(role, department, empId);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id) {
        service.deleteAnnouncement(id);
    }
}
