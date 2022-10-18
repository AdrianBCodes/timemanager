package com.adrianbcodes.timemanager.client;

import com.adrianbcodes.timemanager.common.StatusAudit;
import com.adrianbcodes.timemanager.dto.ClientDTO;
import com.adrianbcodes.timemanager.dto.ProjectNameDTO;
import com.adrianbcodes.timemanager.project.Project;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "clients")
public class Client extends StatusAudit {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @NotBlank
    private String name;
    private String note;
    @OneToMany(mappedBy = "client", fetch = FetchType.LAZY)
    private Set<Project> projects = new HashSet<>();

    public Client() {
    }

    public Client(String name, String note) {
        this.name = name;
        this.note = note;
    }

    public Client(Long id, String name, String note) {
        this.id = id;
        this.name = name;
        this.note = note;
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

    public Set<Project> getProjects() {
        return projects;
    }

    public void setProjects(Set<Project> projects) {
        this.projects = projects;
    }

    public ClientDTO convertToClientDTO(){
        return new ClientDTO(
                this.id,
                this.name,
                this.note);
    }
}
