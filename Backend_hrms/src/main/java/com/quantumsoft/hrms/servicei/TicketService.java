package com.quantumsoft.hrms.servicei;


import com.quantumsoft.hrms.entity.Ticket;

import java.util.List;
import java.util.UUID;

public interface TicketService {
    Ticket createTicket(Ticket ticket);
    List<Ticket> getTicketsByEmployee(Long empId);

    List<Ticket> getTicketsByEmployee(UUID empId);

   // List<Ticket> getTicketsAssignedTo(Long hrId);

    List<Ticket> getTicketsAssignedTo(UUID userId);

    List<Ticket> getAllTickets();
    Ticket updateTicketStatus(Long ticketId, String status);
}
