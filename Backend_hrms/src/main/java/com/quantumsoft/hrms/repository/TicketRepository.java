package com.quantumsoft.hrms.repository;



import com.quantumsoft.hrms.entity.Ticket;
import com.quantumsoft.hrms.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    List<Ticket> findByEmployee(Employee employee);
    List<Ticket> findByAssignedTo(Employee employee);
}
