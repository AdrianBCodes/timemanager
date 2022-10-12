package com.adrianbcodes.timemanager.tag.infrastructure;

public class TagWriteModel {
    private String name;

    public TagWriteModel() {
    }

    public TagWriteModel(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
