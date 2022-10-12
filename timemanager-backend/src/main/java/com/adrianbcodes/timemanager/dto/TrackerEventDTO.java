package com.adrianbcodes.timemanager.dto;

import java.util.Date;

public class TrackerEventDTO {
    private Long id;
    private String description;
    private Long projectId;
    private Long taskId;
    private Long duration;
    private Date date;
    private Long userId;

    public TrackerEventDTO(Long id, String description, Long projectId, Long taskId, Long duration, Date date, Long userId) {
        this.id = id;
        this.description = description;
        this.projectId = projectId;
        this.taskId = taskId;
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
