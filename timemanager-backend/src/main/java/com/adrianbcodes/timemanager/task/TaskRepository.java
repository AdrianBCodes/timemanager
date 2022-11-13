package com.adrianbcodes.timemanager.task;

import com.adrianbcodes.timemanager.client.Client;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface TaskRepository {
    List<Task> getAllTasks();
    Page<Task> getAllTasksByNameLikeAndDescriptionLikeAndProject_Id(String name, String description, Long projectId, Pageable pageable);

    List<Task> getAllTasksByProjectId(Long projectId);

    Optional<Task> getTaskById(Long id);
    Task saveTask(Task task);
    void deleteTask(Task task);
}
