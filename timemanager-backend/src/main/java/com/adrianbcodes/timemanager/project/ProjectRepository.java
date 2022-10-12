package com.adrianbcodes.timemanager.project;

import java.util.List;
import java.util.Optional;

public interface ProjectRepository {
    List<Project> getAllProjects();
    Optional<Project> getProjectById(Long id);
    Project saveProject(Project project);
    void deleteProject(Project project);
}
