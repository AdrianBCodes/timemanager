package com.adrianbcodes.timemanager.project;

import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.*;

public class FakeProjectRepository implements ProjectRepository{

    private final Map<Long, Project> projects = new HashMap<>();


    //TODO
    @Override
    public Page<Project> getAllProjectsPaged(Predicate predicate, Pageable pageable) {
        return null;
    }

    @Override
    public List<Project> getAllProjects() {
        return projects.values().stream().toList();
    }


    //TODO
    @Override
    public Page<Project> getAllProjectsByNameContainsIgnoreCaseAndClient_IdInAndOwner_IdInAndStatus(String name, List<Long> clientsIds, List<Long> ownersIds, Pageable pageable) {
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
