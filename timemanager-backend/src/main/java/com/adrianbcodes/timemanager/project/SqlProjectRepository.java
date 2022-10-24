package com.adrianbcodes.timemanager.project;

import com.adrianbcodes.timemanager.common.StatusEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface SqlProjectRepository extends ProjectRepository, JpaRepository<Project, Long> {
    Page<Project> findByNameContainsIgnoreCaseAndClient_NameContainsIgnoreCaseAndOwner_NameContainsIgnoreCaseAndOwner_SurnameContainsIgnoreCaseAndStatus(String name, String clientName, String projectManagerName, String projectManagerSurname, StatusEnum statusEnum, Pageable pageable);

    @Override
    default List<Project> getAllProjects() {
        return this.findAll();
    }

    @Override
    default Page<Project> getAllProjectsByNameContainsIgnoreCaseAndClient_NameContainsIgnoreCaseAndOwner_NameContainsIgnoreCaseAndOwner_SurnameContainsIgnoreCase(String name, String clientName, String projectManagerName, String projectManagerSurname, Pageable pageable){
        return this.findByNameContainsIgnoreCaseAndClient_NameContainsIgnoreCaseAndOwner_NameContainsIgnoreCaseAndOwner_SurnameContainsIgnoreCaseAndStatus(name,clientName,projectManagerName,projectManagerSurname, StatusEnum.ACTIVE,pageable);
    }
    @Override
    default Optional<Project> getProjectById(Long id) {
        return this.findById(id);
    }

    @Override
    default Project saveProject(Project project) {
        return this.save(project);
    }

    @Override
    default void deleteProject(Project project){
        project.setDeletedAt(new Date());
        this.save(project);
    }
}
