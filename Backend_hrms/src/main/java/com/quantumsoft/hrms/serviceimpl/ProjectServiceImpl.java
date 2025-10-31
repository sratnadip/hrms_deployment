package com.quantumsoft.hrms.serviceimpl;

import com.quantumsoft.hrms.dto.ProjectDto;
import com.quantumsoft.hrms.entity.Employee;
import com.quantumsoft.hrms.entity.Project;
import com.quantumsoft.hrms.enums.ProjectStatus;
import com.quantumsoft.hrms.enums.Role;
import com.quantumsoft.hrms.exception.ResourceNotFoundException;
import com.quantumsoft.hrms.repository.EmployeeRepository;
import com.quantumsoft.hrms.repository.ProjectRepository;
import com.quantumsoft.hrms.servicei.ProjectService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final EmployeeRepository employeeRepository;

    @Autowired
    public ProjectServiceImpl(ProjectRepository projectRepository, EmployeeRepository employeeRepository) {
        this.projectRepository = projectRepository;
        this.employeeRepository = employeeRepository;
    }

    @Override
    public Project createProject(ProjectDto projectDto) {
        System.out.println("empId: " + projectDto.getManagerId());
        Employee employee = employeeRepository.findById(projectDto.getManagerId())
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with ID: " + projectDto.getManagerId()));

        if (employee.getUser().getRole() != Role.MANAGER) {
            throw new IllegalArgumentException("Only employees with MANAGER role can be assigned.");
        }

        Project project = new Project();

        project.setManager(employee);
        project.setProjectName(projectDto.getProjectName());
        project.setDescription(projectDto.getDescription());
        project.setStatus(projectDto.getStatus());
        project.setStartDate(projectDto.getStartDate());
        project.setEndDate(projectDto.getEndDate());
        project.setIsDeleted(false);

        return projectRepository.save(project);
    }

    @Override
    public Project updateProject(Long id, ProjectDto projectDto) {
        Project existing = projectRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Project not found with ID " + id));

        // if we want to change the manager of project
        Employee employee = employeeRepository.findById(projectDto.getManagerId())
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with ID: " + projectDto.getManagerId()));

        existing.setManager(employee);
        existing.setProjectName(projectDto.getProjectName());
        existing.setDescription(projectDto.getDescription());
        existing.setStatus(projectDto.getStatus());
        existing.setStartDate(projectDto.getStartDate());
        existing.setEndDate(projectDto.getEndDate());

        return projectRepository.save(existing);
    }


//    @Override
//    public Project updateProject(Long id, Project updatedProject) {
//        Project existing = projectRepository.findById(id)
//                .orElseThrow(() -> new EntityNotFoundException("Project not found with ID " + id));
//        if (updatedProject.getManager() != null) {
//            validateManager(updatedProject.getManager().getEmpId());
//            existing.setManager(updatedProject.getManager()); // manager object is big(contains lot of information)
//        }          // in case if we want to change the manager then it will be problematic.
//
//        existing.setProjectName(updatedProject.getProjectName());
//        existing.setDescription(updatedProject.getDescription());
//        existing.setStartDate(updatedProject.getStartDate());
//        existing.setEndDate(updatedProject.getEndDate());
//
//        if (updatedProject.getStatus() != null) {
//            existing.setStatus(updatedProject.getStatus());
//        }
//
//        existing.setUpdatedAt(LocalDateTime.now());
//
//        return projectRepository.save(existing);
//    }

    @Override
    public Project getProjectById(Long id) {
        return projectRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Project not found with ID " + id));
    }

    @Override
    public List<Project> getAllProjects() {
        return projectRepository.findByIsDeletedFalse();
    }

    @Override
    public void softDeleteProject(Long id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Project not found with ID " + id));
        project.setIsDeleted(true);
        project.setUpdatedAt(LocalDateTime.now());
        projectRepository.save(project);
    }

    @Override
    public ProjectDto displayRequiredProjectData(Long id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Project not found with ID " + id));
        ProjectDto projectDto = new ProjectDto();

        projectDto.setManagerId(project.getManager().getEmpId());
        projectDto.setProjectName(project.getProjectName());
        projectDto.setDescription(project.getDescription());
        projectDto.setStartDate(project.getStartDate());
        projectDto.setEndDate(project.getEndDate());
        projectDto.setStatus(project.getStatus());

        return projectDto;
    }

    private void validateManager(UUID empId) {
        Employee employee = employeeRepository.findById(empId)
                .orElseThrow(() -> new RuntimeException("Manager not found with ID: " + empId));

        if (employee.getUser().getRole() != Role.MANAGER) {
            throw new IllegalArgumentException("Only employees with MANAGER role can be assigned as project managers.");
//            if (!manager.getRole().equals(Role.MANAGER)) {
//                throw new IllegalArgumentException("Only employees with MANAGER role can be assigned.");
//            }

        }
    }


}
