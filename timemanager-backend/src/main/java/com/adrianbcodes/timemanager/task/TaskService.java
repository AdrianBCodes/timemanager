package com.adrianbcodes.timemanager.task;

import com.adrianbcodes.timemanager.exceptions.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public class TaskService {
    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public List<Task> getAllTasks(){
        return taskRepository.getAllTasks();
    }

    public List<Task> getAllTasksByProjectId(Long projectId){
        return taskRepository.getAllTasksByProjectId(projectId);
    }

    Page<Task> getAllTasksPaged(String name, String description, Long projectId, Pageable pageable) {
        return taskRepository.getAllTasksByNameLikeAndDescriptionLikeAndProject_Id(name, description, projectId, pageable);
    }
    public Task getTaskById(Long id){
        return taskRepository.getTaskById(id).orElseThrow(() -> new NotFoundException("Task with id: " + id + " not found"));
    }
    public Task saveTask(Task task){
        return taskRepository.saveTask(task);
    }

    void deleteTaskById(Long id){
        Task toDelete = getTaskById(id);
        taskRepository.deleteTask(toDelete);
    }
}
