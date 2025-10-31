package com.quantumsoft.hrms.serviceimpl;

import com.quantumsoft.hrms.controller.NotificationSocketController;
import com.quantumsoft.hrms.entity.Announcement;
import com.quantumsoft.hrms.entity.Employee;
import com.quantumsoft.hrms.entity.User;
import com.quantumsoft.hrms.enums.Role;
import com.quantumsoft.hrms.enums.Visibility;
import com.quantumsoft.hrms.repository.AnnouncementRepository;
import com.quantumsoft.hrms.repository.EmployeeRepository;
import com.quantumsoft.hrms.repository.UserRepository;
import com.quantumsoft.hrms.servicei.AnnouncementService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AnnouncementServiceImpl implements AnnouncementService {

    private final AnnouncementRepository repository;
    private final NotificationSocketController socketController;
    private final EmployeeRepository employeeRepository;
    private final UserRepository userRepository;

    @Override
    public Announcement createAnnouncement(Announcement announcement) {
        Announcement saved = repository.save(announcement);

        // Real-time WebSocket Notifications by visibility
        switch (saved.getVisibility()) {
            case ALL -> {
                List<Employee> employees = employeeRepository.findByStatus("ACTIVE");
                employees.forEach(emp ->
                        socketController.sendToUser(emp.getEmpId(), "📢 New Announcement", saved.getTitle())
                );
            }
            case ROLE -> {
                List<User> users = userRepository.findByRole(String.valueOf(Role.valueOf(saved.getVisibleToValue())));
                users.forEach(user ->
                        socketController.sendToUser(user.getUserId(), "📢 Role Announcement", saved.getTitle())
                );
            }
            case DEPARTMENT -> {
                List<Employee> deptEmployees = employeeRepository.findByDepartment_NameIgnoreCase(saved.getVisibleToValue());
                deptEmployees.forEach(emp ->
                        socketController.sendToUser(emp.getEmpId(), "📢 Department Announcement", saved.getTitle())
                );
            }
            case EMPLOYEE -> {
                UUID empId = UUID.fromString(saved.getVisibleToValue());
                socketController.sendToUser(empId, "📢 Private Announcement", saved.getTitle());
            }
        }

        return saved;
    }

    @Override
    public List<Announcement> getAnnouncementsFor(String role, String department, UUID employeeId) {
        List<Announcement> all = repository.findAll();

        return all.stream()
                .filter(a -> a.getExpiresOn() == null || !a.getExpiresOn().isBefore(LocalDate.now()))
                .filter(a ->
                        a.getVisibility() == Visibility.ALL ||
                                (a.getVisibility() == Visibility.ROLE && role.equalsIgnoreCase(a.getVisibleToValue())) ||
                                (a.getVisibility() == Visibility.DEPARTMENT && department.equalsIgnoreCase(a.getVisibleToValue())) ||
                                (a.getVisibility() == Visibility.EMPLOYEE && employeeId.toString().equals(a.getVisibleToValue()))
                )
                .collect(Collectors.toList());
    }

    @Override
    public void deleteAnnouncement(UUID id) {
        repository.deleteById(id);
    }
}
