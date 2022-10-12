package com.adrianbcodes.timemanager.dto;

import java.util.HashSet;
import java.util.Set;

public class TaskDTO {
    private Long id;
    private String name;
    private String description;
    private Long projectId;
    private Set<Long> tagsId = new HashSet<>();

    public TaskDTO(Long id, String name, String description, Long projectId, Set<Long> tagsId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.projectId = projectId;
        this.tagsId = tagsId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
