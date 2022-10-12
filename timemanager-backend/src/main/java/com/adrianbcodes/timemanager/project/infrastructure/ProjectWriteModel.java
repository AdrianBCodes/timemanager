package com.adrianbcodes.timemanager.project.infrastructure;


import java.util.HashSet;
import java.util.Set;

public class ProjectWriteModel {
    private String name;
    private Long clientId;
    private Long projectManagerId;
    private Set<Long> participantsId = new HashSet<>();
    private Set<Long> tasksId = new HashSet<>();

    public ProjectWriteModel() {
    }

    public ProjectWriteModel(String name, Long clientId, Long projectManagerId) {
        this.name = name;
        this.clientId = clientId;
        this.projectManagerId = projectManagerId;
    }

    public ProjectWriteModel(String name, Long clientId, Long projectManagerId, Set<Long> participantsId, Set<Long> tasksId) {
        this.name = name;
        this.clientId = clientId;
        this.projectManagerId = projectManagerId;
        this.participantsId = participantsId;
        this.tasksId = tasksId;
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
