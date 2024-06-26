package com.adrianbcodes.timemanager.trackerEvent.infrastructure;

import java.time.LocalDateTime;

public class TrackerEventWriteModel {
    private String description;
    private Long projectId;
    private Long taskId;
    private Long duration;
    private LocalDateTime date;
    private Long userId;

    public TrackerEventWriteModel() {
    }

    public TrackerEventWriteModel(String description, Long projectId, Long taskId, Long duration, LocalDateTime date, Long userId) {
        this.description = description;
        this.projectId = projectId;
        this.taskId = taskId;
        this.duration = duration;
        this.date = date;
        this.userId = userId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
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
