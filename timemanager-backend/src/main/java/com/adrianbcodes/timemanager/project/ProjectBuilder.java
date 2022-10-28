package com.adrianbcodes.timemanager.project;

import com.adrianbcodes.timemanager.client.Client;
import com.adrianbcodes.timemanager.user.User;

import java.util.HashSet;
import java.util.Set;

public class ProjectBuilder {
    private Long id;
    private String name;
    private Client client;
    private User owner;

    private Set<User> participants = new HashSet<>();


    public static ProjectBuilder builder() {
        return new ProjectBuilder();
    }

    public ProjectBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public ProjectBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public ProjectBuilder withClient(Client client) {
        this.client = client;
        return this;
    }

    public ProjectBuilder withOwner(User owner) {
        this.owner = owner;
        return this;
    }

    public ProjectBuilder withParticipants(Set<User> participants){
        this.participants = participants;
        return this;
    }

    public Project build() {
        return new Project(name, client, owner, participants);
    }

    public Project buildWithId() {
        return new Project(id, name, client, owner, participants);
    }
}
