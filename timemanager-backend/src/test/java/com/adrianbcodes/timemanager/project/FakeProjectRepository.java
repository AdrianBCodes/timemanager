package com.adrianbcodes.timemanager.project;

import com.adrianbcodes.timemanager.common.StatusEnum;
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
    public List<Project> getAllProjects(Predicate predicate) {
        return null;
    }

    @Override
    public Optional<Project> getProjectById(Long id) {
        return Optional.ofNullable(projects.get(id));
    }

    @Override
    public List<Project> getAllProjectsByNameAndStatus(String name, StatusEnum status) {
        return projects.values().stream().filter(project -> project.getName().equals(name) && project.getStatus().equals(StatusEnum.ACTIVE)).toList();
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
