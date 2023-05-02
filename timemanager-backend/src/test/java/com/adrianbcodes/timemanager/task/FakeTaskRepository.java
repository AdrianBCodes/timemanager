package com.adrianbcodes.timemanager.task;

import com.adrianbcodes.timemanager.common.StatusEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.*;

public class FakeTaskRepository implements TaskRepository {
    private final Map<Long, Task> tasks = new HashMap<>();

    @Override
    public List<Task> getAllTasks() {
        return tasks.values().stream().toList();
    }

    @Override
    public Page<Task> getAllActiveTasksByNameLikeAndDescriptionLikeAndProject_Id(String name, String description, Long projectId, Pageable pageable) {
        List<Task> filteredTasks = tasks.values()
                .stream()
                .filter(task ->
                        task.getName().contains(name) &&
                        task.getDescription().contains(description) &&
                        task.getProject().getId().equals(projectId) &&
                        task.getStatus().equals(StatusEnum.ACTIVE))
                .toList();
        return new PageImpl<>(filteredTasks, pageable, filteredTasks.size());
    }

    @Override
    public List<Task> getAllTasksByProjectId(Long projectId) {
        return tasks.values()
                .stream()
                .filter(task ->
                        task.getProject().getId().equals(projectId))
                .toList();
    }

    @Override
    public Optional<Task> getTaskById(Long id) {
        return Optional.ofNullable(tasks.get(id));
    }

    @Override
    public List<Task> getAllTasksByNameAndProjectIdAndStatus(String name, Long projectId, StatusEnum status) {
        return tasks.values()
                .stream()
                .filter(task ->
                        task.getName().equals(name) &&
                        task.getProject().getId().equals(projectId) &&
                        task.getStatus().equals(status))
                .toList();
    }

    @Override
    public Task saveTask(Task task) {
        tasks.put(task.getId(), task);
        return tasks.get(task.getId());
    }

    @Override
    public void deleteTask(Task task) {
        task.setDeletedAt(new Date());
        tasks.put(task.getId(), task);
    }
}