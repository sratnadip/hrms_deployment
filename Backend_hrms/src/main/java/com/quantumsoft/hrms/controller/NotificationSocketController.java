package com.quantumsoft.hrms.controller;



import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.UUID;

@Slf4j
@Controller
public class NotificationSocketController {

    private final SimpMessagingTemplate messagingTemplate;

    public NotificationSocketController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    /**
     * Sends a notification message to a specific employee/user via WebSocket.
     * Destination: /topic/notifications/{userId}
     */
    public void sendToUser(UUID userId, String title, String message) {
        NotificationMessage notification = new NotificationMessage(title, message);
        String destination = "/topic/notifications/" + userId;
        log.info("Sending notification to {}: {}", destination, notification);
        messagingTemplate.convertAndSend(destination, notification);
    }

    /**
     * Inner class for the notification payload structure.
     */
    public record NotificationMessage(String title, String message) {}
}
