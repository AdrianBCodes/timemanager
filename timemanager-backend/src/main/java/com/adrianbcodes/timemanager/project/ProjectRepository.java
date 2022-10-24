package com.adrianbcodes.timemanager.project;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ProjectRepository {
    List<Project> getAllProjects();

    Page<Project> getAllProjectsByNameContainsIgnoreCaseAndClient_NameContainsIgnoreCaseAndOwner_NameContainsIgnoreCaseAndOwner_SurnameContainsIgnoreCase(String name, String clientName, String projectManagerName, String projectManagerSurname, Pageable pageable);

    Optional<Project> getProjectById(Long id);
    Project saveProject(Project project);
    void deleteProject(Project project);
}
