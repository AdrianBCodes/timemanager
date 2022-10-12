package com.adrianbcodes.timemanager.dto;

import java.util.Set;

public class ClientDTO {
    private Long id;
    private String name;
    private String note;
    private Set<ProjectNameDTO> projectNames;

    public ClientDTO(Long id, String name, String note, Set<ProjectNameDTO> projectNames) {
        this.id = id;
        this.name = name;
        this.note = note;
        this.projectNames = projectNames;
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

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Set<ProjectNameDTO> getProjectNames() {
        return projectNames;
    }

    public void setProjectNames(Set<ProjectNameDTO> projectNames) {
        this.projectNames = projectNames;
    }
}
