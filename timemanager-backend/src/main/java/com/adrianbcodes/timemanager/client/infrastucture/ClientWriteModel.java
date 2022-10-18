package com.adrianbcodes.timemanager.client.infrastucture;

public class ClientWriteModel {
    private Long id;
    private String name;
    private String note;

    public ClientWriteModel() {
    }

    public ClientWriteModel(String name, String note) {
        this.name = name;
        this.note = note;
    }

    public ClientWriteModel(Long id, String name, String note) {
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
}
