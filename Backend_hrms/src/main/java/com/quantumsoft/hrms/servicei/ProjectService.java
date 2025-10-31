package com.quantumsoft.hrms.servicei;

import com.quantumsoft.hrms.dto.ProjectDto;
import com.quantumsoft.hrms.entity.Project;

import java.util.List;

public interface ProjectService {
    Project createProject(ProjectDto projectDto);
    //Project updateProject(Long id, Project updatedProject);
   Project updateProject(Long id, ProjectDto updatedProject);
    Project getProjectById(Long id);
    List<Project> getAllProjects();
    void softDeleteProject(Long id);

    ProjectDto displayRequiredProjectData(Long id);
}
