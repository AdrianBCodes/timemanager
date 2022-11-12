package com.adrianbcodes.timemanager.project;


import com.adrianbcodes.timemanager.client.Client;
import com.adrianbcodes.timemanager.common.StatusAudit;
import com.adrianbcodes.timemanager.dto.ProjectDTO;
import com.adrianbcodes.timemanager.task.Task;
import com.adrianbcodes.timemanager.user.User;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

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
    private User owner;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(	name = "project_participants",
            joinColumns = @JoinColumn(name = "project_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<User> participants = new HashSet<>();
    @OneToMany(fetch = FetchType.LAZY)
    private Set<Task> tasks = new HashSet<>();

    public Project() {
    }

    public Project(String name, Client client, User owner) {
        this.name = name;
        this.client = client;
        this.owner = owner;
    }

    public Project(Long id, String name, Client client, User owner) {
        this.id = id;
        this.name = name;
        this.client = client;
        this.owner = owner;
    }

    public Project(String name, Client client, User owner, Set<User> participants) {
        this.name = name;
        this.client = client;
        this.owner = owner;
        this.participants = participants;
    }

    public Project(Long id, String name, Client client, User owner, Set<User> participants) {
        this.id = id;
        this.name = name;
        this.client = client;
        this.owner = owner;
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

    public User getOwner() {
        return owner;
    }

    public void setOwner(User projectManager) {
        this.owner = projectManager;
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
                this.client.convertToClientDTO(),
                this.owner.convertToUserDTO()
        );
    }
}
