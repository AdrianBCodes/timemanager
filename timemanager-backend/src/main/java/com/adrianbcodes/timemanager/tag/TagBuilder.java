package com.adrianbcodes.timemanager.tag;

import com.adrianbcodes.timemanager.client.Client;
import com.adrianbcodes.timemanager.tag.Tag;
import com.adrianbcodes.timemanager.user.User;

public class TagBuilder {
    private Long id;
    private String name;

    public static TagBuilder builder() {
        return new TagBuilder();
    }

    public TagBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public TagBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public Tag build() {
        return new Tag(name);
    }

    public Tag buildWithId() {
        return new Tag(id, name);
    }
}
