package com.adrianbcodes.timemanager.dto;

import java.util.HashSet;
import java.util.Set;

public class TaskDTO {
    private Long id;
    private String name;
    private String description;
    private Long projectId;
    private Set<TagDTO> tags = new HashSet<>();

    public TaskDTO(Long id, String name, String description, Long projectId, Set<TagDTO> tags) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.projectId = projectId;
        this.tags = tags;
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

    public Set<TagDTO> getTags() {
        return tags;
    }

    public void setTags(Set<TagDTO> tags) {
        this.tags = tags;
    }
}
