package com.adrianbcodes.timemanager.dto;

public class ProjectDTO {
    private Long id;
    private String name;
    private ClientDTO client;
    private UserDTO owner;

    public ProjectDTO() {
    }

    public ProjectDTO(Long id, String name, ClientDTO client, UserDTO owner) {
        this.id = id;
        this.name = name;
        this.client = client;
        this.owner = owner;
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

    public ClientDTO getClient() {
        return client;
    }

    public void setClient(ClientDTO client) {
        this.client = client;
    }

    public UserDTO getOwner() {
        return owner;
    }

    public void setOwner(UserDTO owner) {
        this.owner = owner;
    }
}
