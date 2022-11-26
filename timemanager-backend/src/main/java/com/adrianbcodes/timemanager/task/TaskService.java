package com.adrianbcodes.timemanager.task;

import com.adrianbcodes.timemanager.common.StatusEnum;
import com.adrianbcodes.timemanager.exceptions.BlankParameterException;
import com.adrianbcodes.timemanager.exceptions.NotFoundException;
import com.adrianbcodes.timemanager.exceptions.NotUniqueException;
import com.adrianbcodes.timemanager.tag.Tag;
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
        if(task.getName().isBlank()){
            throw new BlankParameterException("Task's name cannot be empty");
        }
        if(!isTaskNameUnique(task.getName(), StatusEnum.ACTIVE)){
            throw new NotUniqueException("Task with name: " + task.getName() + " already exists");
        }
        return taskRepository.saveTask(task);
    }

    void deleteTaskById(Long id){
        Task toDelete = getTaskById(id);
        taskRepository.deleteTask(toDelete);
    }

    private boolean isTaskNameUnique(String name, StatusEnum status){
        List<Task> tasks = taskRepository.getAllTasksByNameAndStatus(name, status);
        return tasks.isEmpty();
    }
}
