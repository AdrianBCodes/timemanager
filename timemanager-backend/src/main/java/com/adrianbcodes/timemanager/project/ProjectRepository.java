package com.adrianbcodes.timemanager.project;

import com.adrianbcodes.timemanager.common.StatusEnum;
import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ProjectRepository {
    Page<Project> getAllProjectsPaged(Predicate predicate, Pageable pageable);
    List<Project> getAllProjects();

    List<Project> getAllProjects(Predicate predicate);

    Optional<Project> getProjectById(Long id);

    List<Project> getAllProjectsByNameAndStatus(String name, StatusEnum status);

    Project saveProject(Project project);
    void deleteProject(Project project);


}
