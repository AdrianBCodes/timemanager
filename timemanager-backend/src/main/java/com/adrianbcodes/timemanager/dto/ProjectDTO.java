package com.adrianbcodes.timemanager.dto;

import java.util.HashSet;
import java.util.Set;

public class ProjectDTO {
    private Long id;
    private String name;
    private Long clientId;
    private Long projectManagerId;
    private Set<Long> participantsId;
    private Set<Long> tasksId;

    public ProjectDTO(Long id, String name, Long clientId, Long projectManagerId, Set<Long> participantsId, Set<Long> tasksId) {
        this.id = id;
        this.name = name;
        this.clientId = clientId;
        this.projectManagerId = projectManagerId;
        this.participantsId = participantsId;
        this.tasksId = tasksId;
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

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public Long getProjectManagerId() {
        return projectManagerId;
    }

    public void setProjectManagerId(Long projectManagerId) {
        this.projectManagerId = projectManagerId;
    }

    public Set<Long> getParticipantsId() {
        return participantsId;
    }

    public void setParticipantsId(Set<Long> participantsId) {
        this.participantsId = participantsId;
    }

    public Set<Long> getTasksId() {
        return tasksId;
    }

    public void setTasksId(Set<Long> tasksId) {
        this.tasksId = tasksId;
    }
}
