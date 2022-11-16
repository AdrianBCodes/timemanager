package com.adrianbcodes.timemanager.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public class TrackerEventDTO {
    private Long id;
    private String description;
    private ProjectDTO project;
    private TaskDTO task;
    private Long duration;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDateTime date;
    private Long userId;

    public TrackerEventDTO(Long id, String description, ProjectDTO project, TaskDTO task, Long duration, LocalDateTime date, Long userId) {
        this.id = id;
        this.description = description;
        this.project = project;
        this.task = task;
        this.duration = duration;
        this.date = date;
        this.userId = userId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ProjectDTO getProject() {
        return project;
    }

    public void setProject(ProjectDTO project) {
        this.project = project;
    }

    public TaskDTO getTask() {
        return task;
    }

    public void setTask(TaskDTO task) {
        this.task = task;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
