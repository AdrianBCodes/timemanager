package com.adrianbcodes.timemanager.task.infrastructure;

import com.adrianbcodes.timemanager.project.Project;
import com.adrianbcodes.timemanager.tag.Tag;

import java.util.HashSet;
import java.util.Set;


public class TaskWriteModel {
    private String name;
    private String description;
    private Long projectId;
    private Set<Long> tagsId = new HashSet<>();

    public TaskWriteModel() {
    }

    public TaskWriteModel(String name, String description, Long projectId) {
        this.name = name;
        this.description = description;
        this.projectId = projectId;
    }

    public TaskWriteModel(String name, String description, Long projectId, Set<Long> tagsId) {
        this.name = name;
        this.description = description;
        this.projectId = projectId;
        this.tagsId = tagsId;
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

    public Set<Long> getTagsId() {
        return tagsId;
    }

    public void setTagsId(Set<Long> tagsId) {
        this.tagsId = tagsId;
    }
}
