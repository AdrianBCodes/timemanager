package com.adrianbcodes.timemanager.dto;

import com.adrianbcodes.timemanager.user.role.Role;

import java.util.HashSet;
import java.util.Set;

public class UserDTO {
    private Long id;
    private String name;
    private String surname;
    private String email;
    private Set<Role> roles = new HashSet<>();

    public UserDTO() {
    }

    public UserDTO(Long id, String name, String surname, String email) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.email = email;
    }

    public UserDTO(Long id, String name, String surname, String email, Set<Role> roles) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.roles = roles;
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

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
}
