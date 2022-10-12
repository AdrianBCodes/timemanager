package com.adrianbcodes.timemanager.task;

import com.adrianbcodes.timemanager.common.StatusAudit;
import com.adrianbcodes.timemanager.dto.TaskDTO;
import com.adrianbcodes.timemanager.project.Project;
import com.adrianbcodes.timemanager.tag.Tag;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "tasks")
public class Task extends StatusAudit {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;
    private String name;
    private String description;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST,CascadeType.REFRESH})
    @JoinTable(
            name = "tasks_tags",
            joinColumns = @JoinColumn(name = "task_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private Set<Tag> tags = new HashSet<>();

    public Task() {
    }

    public Task(String name, String description, Project project, Set<Tag> tags) {
        this.name = name;
        this.description = description;
        this.project = project;
        this.tags = tags;
    }

    public Task(Long id, String name, String description, Project project, Set<Tag> tags) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.project = project;
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

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    public TaskDTO convertToTaskDTO(){
        return new TaskDTO(
                this.id,
                this.name,
                this.description,
                this.project.getId(),
                this.tags.stream().map(Tag::getId).collect(Collectors.toSet())
        );
    }
}
