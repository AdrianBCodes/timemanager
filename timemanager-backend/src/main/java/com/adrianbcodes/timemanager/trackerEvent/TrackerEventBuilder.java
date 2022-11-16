package com.adrianbcodes.timemanager.trackerEvent;

import com.adrianbcodes.timemanager.project.Project;
import com.adrianbcodes.timemanager.task.Task;
import com.adrianbcodes.timemanager.user.User;

import java.time.LocalDateTime;

public class TrackerEventBuilder {
    private Long id;
    private String description;
    private Project project;
    private Task task;
    private Long duration;
    private LocalDateTime date;
    private User user;

    public static TrackerEventBuilder builder() {
        return new TrackerEventBuilder();
    }

    public TrackerEventBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public TrackerEventBuilder withDescription(String description) {
        this.description = description;
        return this;
    }

    public TrackerEventBuilder withProject(Project project) {
        this.project = project;
        return this;
    }

    public TrackerEventBuilder withTask(Task task) {
        this.task = task;
        return this;
    }

    public TrackerEventBuilder withDuration(Long duration) {
        this.duration = duration;
        return this;
    }

    public TrackerEventBuilder withDate(LocalDateTime date) {
        this.date = date;
        return this;
    }

    public TrackerEventBuilder withUser(User user) {
        this.user = user;
        return this;
    }


    public TrackerEvent build() {
        return new TrackerEvent(description, project, task, duration, date, user);
    }

    public TrackerEvent buildWithId() {
        return new TrackerEvent(id, description, project, task, duration, date, user);
    }
}
