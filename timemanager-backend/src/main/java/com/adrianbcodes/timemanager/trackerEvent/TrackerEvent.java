package com.adrianbcodes.timemanager.trackerEvent;

import com.adrianbcodes.timemanager.dto.TrackerEventDTO;
import com.adrianbcodes.timemanager.project.Project;
import com.adrianbcodes.timemanager.task.Task;
import com.adrianbcodes.timemanager.user.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tracker_events")
public class TrackerEvent {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;
    private String description;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id")
    private Task task;
    private Long duration;
    @JsonFormat(pattern = "dd/MM/yyyy")
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDateTime date;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public TrackerEvent() {
    }

    public TrackerEvent(String description, Project project, Task task, Long duration, LocalDateTime date, User user) {
        this.description = description;
        this.project = project;
        this.task = task;
        this.duration = duration;
        this.date = date;
        this.user = user;
    }

    public TrackerEvent(Long id, String description, Project project, Task task, Long duration, LocalDateTime date, User user) {
        this.id = id;
        this.description = description;
        this.project = project;
        this.task = task;
        this.duration = duration;
        this.date = date;
        this.user = user;
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

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public TrackerEventDTO convertToTrackerEventDTO(){
        return new TrackerEventDTO(
                this.id,
                this.description,
                this.project.convertToProjectDTO(),
                this.task.convertToTaskDTO(),
                this.duration,
                this.date,
                this.user.convertToUserDTO()
        );
    }
}
