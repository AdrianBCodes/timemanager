package com.adrianbcodes.timemanager.task;

import com.adrianbcodes.timemanager.common.SortMapper;
import com.adrianbcodes.timemanager.common.StatusEnum;
import com.adrianbcodes.timemanager.exceptions.AlreadyDeletedException;
import com.adrianbcodes.timemanager.exceptions.BlankParameterException;
import com.adrianbcodes.timemanager.exceptions.NotFoundException;
import com.adrianbcodes.timemanager.exceptions.NotUniqueException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;

public class TaskService {
    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public List<Task> getAllTasks(){
        return taskRepository.getAllTasks().stream().filter(task -> task.getStatus().equals(StatusEnum.ACTIVE)).toList();
    }

    public List<Task> getAllTasksByProjectId(Long projectId){
        return taskRepository.getAllTasksByProjectId(projectId);
    }

    Page<Task> getAllActiveTasksPagedAndFiltered(String name, String description, Long projectId, int page, int size, String sort) {
        List<Sort.Order> orders = new ArrayList<>();

        String[] _sort = sort.split(",");
        orders.add(new Sort.Order(SortMapper.getSortDirection(_sort[1]), _sort[0]));
        Pageable pageable = PageRequest.of(page,size, Sort.by(orders));
        return taskRepository.getAllActiveTasksByNameLikeAndDescriptionLikeAndProject_Id(name, description, projectId, pageable);
    }
    public Task getTaskById(Long id){
        return taskRepository.getTaskById(id).orElseThrow(() -> new NotFoundException("Task with id: " + id + " not found"));
    }
    public Task saveTask(Task task){
        if(task.getName().isBlank()){
            throw new BlankParameterException("Task's name cannot be empty");
        }
        if(!isTaskNameUniqueInProject(task.getName(), task.getProject().getId() , StatusEnum.ACTIVE)){
            throw new NotUniqueException("Task with name: " + task.getName() + " already exists");
        }
        return taskRepository.saveTask(task);
    }

    void deleteTaskById(Long id){
        Task toDelete = getTaskById(id);
        if(toDelete.getStatus().equals(StatusEnum.DELETED)){
            throw new AlreadyDeletedException("Task with id: " + id + " already has status DELETED");
        }
        taskRepository.deleteTask(toDelete);
    }

    private boolean isTaskNameUniqueInProject(String name, Long projectId, StatusEnum status){
        List<Task> tasks = taskRepository.getAllTasksByNameAndProjectIdAndStatus(name, projectId, status);
        return tasks.isEmpty();
    }
}
