package com.adrianbcodes.timemanager.task;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface SqlTaskRepository extends TaskRepository, JpaRepository<Task, Long> {


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
