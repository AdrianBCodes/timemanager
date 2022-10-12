package com.adrianbcodes.timemanager.project;

import java.util.*;

public class FakeProjectRepository implements ProjectRepository{

    private final Map<Long, Project> projects = new HashMap<>();
    @Override
    public List<Project> getAllProjects() {
        return projects.values().stream().toList();
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
