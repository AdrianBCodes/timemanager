package com.adrianbcodes.timemanager.project;

import com.adrianbcodes.timemanager.common.StatusEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.*;

public class FakeProjectRepository implements ProjectRepository{

    private final Map<Long, Project> projects = new HashMap<>();


    @Override
    public Page<Project> getAllActiveProjectsPagedAndFiltered(String name, List<Long> clientsIds, List<Long> ownersIds, Pageable pageable) {
        List<Project> filteredProjects = projects.values()
                .stream()
                .filter(project ->
                    project.getStatus().equals(StatusEnum.ACTIVE) &&
                    project.getName().contains(name) &&
                    clientsIds.contains(project.getClient().getId()) &&
                    ownersIds.contains(project.getOwner().getId())
                ).toList();
        return new PageImpl<>(filteredProjects, pageable, filteredProjects.size());
    }

    @Override
    public List<Project> getAllProjects() {
        return projects.values().stream().toList();
    }


    //TODO
    @Override
    public List<Project> getAllProjectsOfParticipantWithUsername(String username) {
        List<Project> filteredProjects = projects.values()
                .stream()
                .filter(project ->
                        project.getParticipants()
                                .stream().anyMatch(user ->
                                        user.getUsername().equals(username)))
                .toList();
        return filteredProjects;
    }

    @Override
    public Optional<Project> getProjectById(Long id) {
        return Optional.ofNullable(projects.get(id));
    }

    @Override
    public List<Project> getAllProjectsByNameAndStatus(String name, StatusEnum status) {
        return projects.values()
                .stream()
                .filter(project ->
                        project.getName().equals(name) && project.getStatus().equals(StatusEnum.ACTIVE))
                .toList();
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
