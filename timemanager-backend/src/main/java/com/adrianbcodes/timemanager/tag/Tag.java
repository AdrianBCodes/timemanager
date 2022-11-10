package com.adrianbcodes.timemanager.tag;

import com.adrianbcodes.timemanager.common.StatusAudit;
import com.adrianbcodes.timemanager.dto.TagDTO;
import com.adrianbcodes.timemanager.task.Task;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Set;

@Entity
@Table(name = "tags")
public class Tag extends StatusAudit {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;
    @NotBlank
    private String name;

    public Tag() {
    }

    public Tag(String name) {
        this.name = name;
    }

    public Tag(Long id, String name) {
        this.id = id;
        this.name = name;
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

    public TagDTO convertToTagDTO(){
        return new TagDTO(this.id,
                this.name);
    }
}
