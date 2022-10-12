package com.adrianbcodes.timemanager.project;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface SqlProjectRepository extends ProjectRepository, JpaRepository<Project, Long> {


    @Override
    default List<Project> getAllProjects() {
        return this.findAll();
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
