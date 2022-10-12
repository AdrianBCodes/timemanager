package com.adrianbcodes.timemanager.tag;

import java.util.*;

public class FakeTagRepository implements TagRepository{

    private final Map<Long, Tag> tags = new HashMap<>();
    @Override
    public List<Tag> getAllTags() {
        return tags.values().stream().toList();
    }

    @Override
    public Optional<Tag> getTagById(Long id) {
        return Optional.ofNullable(tags.get(id));
    }

    @Override
    public Tag saveTag(Tag tag) {
        // TODO Generate ID
        tags.put(tag.getId(), tag);
        return tags.get(tag.getId());
    }

    @Override
    public void deleteTag(Tag tag) {
        tag.setDeletedAt(new Date());
        tags.put(tag.getId(), tag);
    }
}
