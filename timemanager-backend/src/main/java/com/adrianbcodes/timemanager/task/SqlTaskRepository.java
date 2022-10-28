package com.adrianbcodes.timemanager.task;

import com.adrianbcodes.timemanager.client.Client;
import com.adrianbcodes.timemanager.common.StatusEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface SqlTaskRepository extends TaskRepository, JpaRepository<Task, Long> {

    Page<Task> findByNameContainsIgnoreCaseAndDescriptionContainsIgnoreCaseAndStatus(String name, String description, StatusEnum status, Pageable pageable);

    Page<Task> findByNameContainsIgnoreCaseAndDescriptionContainsIgnoreCaseAndProject_IdAndStatus(String name, String description, Long projectId, StatusEnum status, Pageable pageable);


    @Override
    default Page<Task> getAllTasksByNameLikeAndDescriptionLikeAndProject_Id(String name, String description, Long projectId, Pageable pageable){
        return this.findByNameContainsIgnoreCaseAndDescriptionContainsIgnoreCaseAndProject_IdAndStatus(name, description, projectId, StatusEnum.ACTIVE ,pageable);
    }
    @Override
    default List<Task> getAllTasks() {
        return this.findAll();
    }

    @Override
    default Optional<Task> getTaskById(Long id) {
        return this.findById(id);
    }

    @Override
    default Task saveTask(Task task) {
        return this.save(task);
    }

    @Override
    default void deleteTask(Task task){
        task.setDeletedAt(new Date());
        this.save(task);
    }
}
