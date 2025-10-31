package com.quantumsoft.hrms.controller;
import com.quantumsoft.hrms.entity.Ticket;
import com.quantumsoft.hrms.servicei.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/tickets")
@RequiredArgsConstructor
@CrossOrigin("*")
public class TicketController {

    private final TicketService ticketService;

    @PreAuthorize("hasRole('EMPLOYEE')")
    @PostMapping
    public Ticket create(@RequestBody Ticket ticket) {
        return ticketService.createTicket(ticket);
    }

    @PreAuthorize("hasRole('EMPLOYEE')")
    @GetMapping("/employee/{empId}")
    public List<Ticket> getMyTickets(@PathVariable UUID empId) {
        return ticketService.getTicketsByEmployee(empId);
    }

    @PreAuthorize("hasAnyRole('HR', 'ADMIN')")
    @GetMapping("/assigned/{userId}")
    public List<Ticket> getAssignedTickets(@PathVariable UUID userId) {
        return ticketService.getTicketsAssignedTo(userId);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/all")
    public List<Ticket> allTickets() {
        return ticketService.getAllTickets();
    }

    @PreAuthorize("hasAnyRole('HR', 'ADMIN')")
    @PatchMapping("/{ticketId}/status")
    public Ticket updateStatus(@PathVariable Long ticketId, @RequestParam String status) {
        return ticketService.updateTicketStatus(ticketId, status);
    }
}
