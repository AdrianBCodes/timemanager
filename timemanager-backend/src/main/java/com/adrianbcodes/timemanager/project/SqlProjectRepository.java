package com.adrianbcodes.timemanager.project;

import com.adrianbcodes.timemanager.common.StatusEnum;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface SqlProjectRepository extends ProjectRepository, JpaRepository<Project, Long>, QuerydslPredicateExecutor<Project> {

    @NonNull
    Page<Project> findAll(@NonNull Predicate predicate, @NonNull Pageable pageable);
    @NonNull
    List<Project> findAll(@NonNull Predicate predicate);
    List<Project> findAllByNameAndStatus(String name, StatusEnum status);

    @Override
    default Page<Project> getAllActiveProjectsPagedAndFiltered(String name, List<Long> clientsIds, List<Long> ownersIds, Pageable pageable){
        QProject project = QProject.project;

        BooleanBuilder builder = new BooleanBuilder();

        builder.and(project.status.eq(StatusEnum.ACTIVE));

        if(!name.isEmpty()){
            builder.and(project.name.containsIgnoreCase(name));
        }
        if(!clientsIds.isEmpty()){
            builder.and(project.client.id.in(clientsIds));
        }
        if(!ownersIds.isEmpty()){
            builder.and(project.owner.id.in(ownersIds));
        }

        return this.findAll(builder, pageable);
    }
    @Override
    default List<Project> getAllProjects() {
        return this.findAll();
    }
    default List<Project> getAllProjectsOfParticipantWithUsername(String username) {
        QProject project = QProject.project;

        BooleanBuilder builder = new BooleanBuilder();

        builder.and(project.status.eq(StatusEnum.ACTIVE)).and(project.participants.any().username.eq(username));
        return this.findAll(builder);
    }
    @Override
    default Optional<Project> getProjectById(Long id) {
        return this.findById(id);
    }

    @Override
    default List<Project> getAllProjectsByNameAndStatus(String name, StatusEnum status){
        return this.findAllByNameAndStatus(name, status);
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
