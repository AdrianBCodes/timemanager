package com.adrianbcodes.timemanager.project;

import com.adrianbcodes.timemanager.common.StatusEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ProjectRepository {
    Page<Project> getAllActiveProjectsPagedAndFiltered(String name, List<Long> clientsIds, List<Long> ownersIds, Pageable pageable);
    List<Project> getAllProjects();

    List<Project> getAllProjectsOfParticipantWithUsername(String username);

    Optional<Project> getProjectById(Long id);

    List<Project> getAllProjectsByNameAndStatus(String name, StatusEnum status);

    Project saveProject(Project project);
    void deleteProject(Project project);


}
