package com.quantumsoft.hrms.controller;

import com.quantumsoft.hrms.dto.ProjectDto;
import com.quantumsoft.hrms.entity.Project;
import com.quantumsoft.hrms.servicei.ProjectService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    private final ProjectService projectService;

    @Autowired
    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    // ✅ POST /api/projects – Create new project
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<Project> createProject(@Valid @RequestBody ProjectDto projectDto) {
        Project created = projectService.createProject(projectDto);
        return ResponseEntity.ok(created);
    }

//     //✅ PUT /api/projects/{id} – Update project details/status
//    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
//    @PutMapping("/{id}")
//    public ResponseEntity<Project> updateProject(@PathVariable Long id,
//                                                 @Valid @RequestBody Project project) {
//        Project updated = projectService.updateProject(id, project);
//        return ResponseEntity.ok(updated);
//    }

    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<Project> updateProject(@PathVariable Long id,
                                                 @Valid @RequestBody ProjectDto projectDto) {
        Project updated = projectService.updateProject(id, projectDto);
        return ResponseEntity.ok(updated);
    }

    // ✅ GET /api/projects – List all projects
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<Project>> getAllProjects() {
        List<Project> projects = projectService.getAllProjects();
        return ResponseEntity.ok(projects);
    }

    // ✅ GET /api/projects/{id} – View specific project details
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<Project> getProjectById(@PathVariable Long id) {
        Project project = projectService.getProjectById(id);
        return ResponseEntity.ok(project);
    }

    // ✅ DELETE /api/projects/{id} – Soft delete project
    @PreAuthorize("hasRole('MANAGER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> softDeleteProject(@PathVariable Long id) {
        projectService.softDeleteProject(id);
        return ResponseEntity.ok("Project with ID " + id + " has been soft deleted.");
    }

    // This api will be used to display only required project data
    // In frontend we will see this much project information
    // This api will be useful for update the project
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
    @GetMapping("/display/{id}")
    public ResponseEntity<ProjectDto> displayRequiredProjectData(@PathVariable Long id) {
        ProjectDto project = projectService.displayRequiredProjectData(id);
        return ResponseEntity.ok(project);
    }

}
