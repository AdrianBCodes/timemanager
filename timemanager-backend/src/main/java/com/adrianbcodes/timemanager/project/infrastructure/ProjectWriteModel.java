package com.adrianbcodes.timemanager.project.infrastructure;


public class ProjectWriteModel {
    private String name;
    private Long clientId;
    private Long ownerId;

    public ProjectWriteModel() {
    }

    public ProjectWriteModel(String name, Long clientId, Long ownerId) {
        this.name = name;
        this.clientId = clientId;
        this.ownerId = ownerId;
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

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }
}
