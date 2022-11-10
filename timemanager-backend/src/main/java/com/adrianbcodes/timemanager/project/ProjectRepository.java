package com.adrianbcodes.timemanager.project;

import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ProjectRepository {
    Page<Project> getAllProjectsPaged(Predicate predicate, Pageable pageable);
    List<Project> getAllProjects();

    Page<Project> getAllProjectsByNameContainsIgnoreCaseAndClient_IdInAndOwner_IdInAndStatus(String name, List<Long> clientsIds, List<Long> ownersIds, Pageable pageable);

    Optional<Project> getProjectById(Long id);
    Project saveProject(Project project);
    void deleteProject(Project project);
}
