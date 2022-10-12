package com.adrianbcodes.timemanager.project;


import com.adrianbcodes.timemanager.client.Client;
import com.adrianbcodes.timemanager.common.StatusAudit;
import com.adrianbcodes.timemanager.dto.ProjectDTO;
import com.adrianbcodes.timemanager.task.Task;
import com.adrianbcodes.timemanager.user.User;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "projects")
public class Project extends StatusAudit {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;
    private String name;
    @ManyToOne(fetch = FetchType.LAZY)
    private Client client;
    @ManyToOne(fetch = FetchType.LAZY)
    private User projectManager;
    @ManyToMany(fetch = FetchType.LAZY)
    private Set<User> participants = new HashSet<>();
    @OneToMany(fetch = FetchType.LAZY)
    private Set<Task> tasks = new HashSet<>();

    public Project() {
    }

    public Project(String name, Client client, User projectManager) {
        this.name = name;
        this.client = client;
        this.projectManager = projectManager;
    }

    public Project(Long id, String name, Client client, User projectManager) {
        this.id = id;
        this.name = name;
        this.client = client;
        this.projectManager = projectManager;
    }

    public Project(String name, Client client, User projectManager, Set<User> participants) {
        this.name = name;
        this.client = client;
        this.projectManager = projectManager;
        this.participants = participants;
    }

    public Project(Long id, String name, Client client, User projectManager, Set<User> participants) {
        this.id = id;
        this.name = name;
        this.client = client;
        this.projectManager = projectManager;
        this.participants = participants;
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

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public User getProjectManager() {
        return projectManager;
    }

    public void setProjectManager(User projectManager) {
        this.projectManager = projectManager;
    }

    public Set<User> getParticipants() {
        return participants;
    }

    public void setParticipants(Set<User> participants) {
        this.participants = participants;
    }

    public Set<Task> getTasks() {
        return tasks;
    }

    public void setTasks(Set<Task> tasks) {
        this.tasks = tasks;
    }

    public ProjectDTO convertToProjectDTO(){
        return new ProjectDTO(
                this.id,
                this.name,
                this.client.getId(),
                this.projectManager.getId(),
                this.participants.stream().map(User::getId).collect(Collectors.toSet()),
                this.tasks.stream().map(Task::getId).collect(Collectors.toSet())
        );
    }
}
