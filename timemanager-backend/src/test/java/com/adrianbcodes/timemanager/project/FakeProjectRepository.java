package com.adrianbcodes.timemanager.project;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.*;

public class FakeProjectRepository implements ProjectRepository{

    private final Map<Long, Project> projects = new HashMap<>();
    @Override
    public List<Project> getAllProjects() {
        return projects.values().stream().toList();
    }

    //TODO
    @Override
    public Page<Project> getAllProjectsByNameContainsIgnoreCaseAndClient_NameContainsIgnoreCaseAndOwner_NameContainsIgnoreCaseAndOwner_SurnameContainsIgnoreCase(String name, String clientName, String projectManagerName, String projectManagerSurname, Pageable pageable) {
        return null;
    }

    @Override
    public Optional<Project> getProjectById(Long id) {
        return Optional.ofNullable(projects.get(id));
    }

    @Override
    public Project saveProject(Project project) {
        // TODO Generate ID
        projects.put(project.getId(), project);
        return projects.get(project.getId());
    }

    @Override
    public void deleteProject(Project project) {
        project.setDeletedAt(new Date());
        projects.put(project.getId(), project);
    }
}
