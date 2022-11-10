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

public interface SqlProjectRepository extends ProjectRepository, JpaRepository<Project, Long>, QuerydslPredicateExecutor {

    Page<Project> findAll(Predicate predicate, Pageable pageable);
    @Query("""
            select p from Project p
            where upper(p.name) like upper(concat('%', ?1, '%')) and p.client.id in ?2 and p.owner.id in ?3 and p.status = ?4""")
    Page<Project> findByNameContainsIgnoreCaseAndClient_IdInAndOwner_IdInAndStatus(String name, Collection<Long> clientsIds, Collection<Long> ownersIds, StatusEnum status, Pageable pageable);

    @Override
    default List<Project> getAllProjects() {
        return this.findAll();
    }

    @Override
    default Page<Project> getAllProjectsByNameContainsIgnoreCaseAndClient_IdInAndOwner_IdInAndStatus(String name, List<Long> clientsIds, List<Long> ownersIds, Pageable pageable){
        return this.findByNameContainsIgnoreCaseAndClient_IdInAndOwner_IdInAndStatus(name, clientsIds, ownersIds, StatusEnum.ACTIVE, pageable);
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
    default Page<Project> getAllProjectsPaged(Predicate predicate, Pageable pageable){
        return this.findAll(predicate, pageable);
    }

    @Override
    default void deleteProject(Project project){
        project.setDeletedAt(new Date());
        this.save(project);
    }
}
