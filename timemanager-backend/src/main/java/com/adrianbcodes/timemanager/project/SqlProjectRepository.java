package com.adrianbcodes.timemanager.project;

import com.adrianbcodes.timemanager.common.StatusEnum;
import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.lang.Nullable;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface SqlProjectRepository extends ProjectRepository, JpaRepository<Project, Long>, QuerydslPredicateExecutor<Project> {

    Page<Project> findAll(Predicate predicate, Pageable pageable);
    List<Project> findAll(Predicate predicate);

    @Override
    default Page<Project> getAllProjectsPaged(Predicate predicate, Pageable pageable){
        return this.findAll(predicate, pageable);
    }
    @Override
    default List<Project> getAllProjects() {
        return this.findAll();
    }
    default List<Project> getAllProjects(Predicate predicate) {
        return this.findAll(predicate);
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
