package com.adrianbcodes.timemanager.task.infrastructure;

import com.adrianbcodes.timemanager.project.Project;
import com.adrianbcodes.timemanager.tag.Tag;

import java.util.HashSet;
import java.util.Set;


public class TaskWriteModel {
    private String name;
    private String description;
    private Long projectId;
    private Set<Tag> tags = new HashSet<>();

    public TaskWriteModel() {
    }

    public TaskWriteModel(String name, String description, Long projectId) {
        this.name = name;
        this.description = description;
        this.projectId = projectId;
    }

    public TaskWriteModel(String name, String description, Long projectId, Set<Tag> tags) {
        this.name = name;
        this.description = description;
        this.projectId = projectId;
        this.tags = tags;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }
}
