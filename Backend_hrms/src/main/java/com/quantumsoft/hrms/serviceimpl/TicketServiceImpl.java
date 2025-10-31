package com.quantumsoft.hrms.serviceimpl;

import com.quantumsoft.hrms.entity.Ticket;
import com.quantumsoft.hrms.entity.*;
import com.quantumsoft.hrms.enums.Role;
import com.quantumsoft.hrms.enums.TicketStatus;
import com.quantumsoft.hrms.repository.EmployeeRepository;
import com.quantumsoft.hrms.repository.TicketRepository;
import com.quantumsoft.hrms.servicei.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;
    private final EmployeeRepository employeeRepository;

    @Override
    public Ticket createTicket(Ticket ticket) {
        ticket.setStatus(TicketStatus.OPEN);
        ticket.setCreatedAt(LocalDateTime.now());
        return ticketRepository.save(ticket);
    }

    @Override
    public List<Ticket> getTicketsByEmployee(Long employeeId) {
        return List.of();
    }

    @Override
    public List<Ticket> getTicketsByEmployee(UUID empId) {
        Employee employee = employeeRepository.findById(empId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
        return ticketRepository.findByEmployee(employee);
    }

    @Override
    public List<Ticket> getTicketsAssignedTo(UUID userId) {
        Employee hr = employeeRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("HR not found"));

        if (hr.getUser() == null || hr.getUser().getRole() != Role.HR) {
            throw new RuntimeException("This employee is not an HR");
        }

        return ticketRepository.findByAssignedTo(hr);
    }



    @Override
    public List<Ticket> getAllTickets() {
        return ticketRepository.findAll();
    }

    @Override
    public Ticket updateTicketStatus(Long ticketId, String status) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));
        ticket.setStatus(TicketStatus.valueOf(status.toUpperCase()));
        if (ticket.getStatus() == TicketStatus.CLOSED) {
            ticket.setResolvedAt(LocalDateTime.now());
        }
        return ticketRepository.save(ticket);
    }
}
