package com.adrianbcodes.timemanager.task;

import com.adrianbcodes.timemanager.project.Project;
import com.adrianbcodes.timemanager.tag.Tag;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

public class TaskBuilder {
    private Long id;
    private String name;
    private String description;
    private Project project;
    private Set<Tag> tags = new HashSet<>();

    public static TaskBuilder builder() {
        return new TaskBuilder();
    }

    public TaskBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public TaskBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public TaskBuilder withDescription(String description) {
        this.description = description;
        return this;
    }

    public TaskBuilder withProject(Project project) {
        this.project = project;
        return this;
    }

    public TaskBuilder withTags(Set<Tag> tags) {
        this.tags = tags;
        return this;
    }

    public Task build() {
        return new Task(name, description, project, tags);
    }

    public Task buildWithId() {
        return new Task(id, name, description, project, tags);
    }
}
