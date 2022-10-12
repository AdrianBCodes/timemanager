package com.adrianbcodes.timemanager.task;

import java.util.List;
import java.util.Optional;

public interface TaskRepository {
    List<Task> getAllTasks();
    Optional<Task> getTaskById(Long id);
    Task saveTask(Task task);
    void deleteTask(Task task);
}
