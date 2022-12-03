package com.adrianbcodes.timemanager.tag;

import com.adrianbcodes.timemanager.common.StatusEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.*;

public class FakeTagRepository implements TagRepository{

    private final Map<Long, Tag> tags = new HashMap<>();

    @Override
    public Page<Tag> getAllTagsByNameLikeAndStatus(String name, StatusEnum status, Pageable pageable) {
        List<Tag> filteredTags = tags.values()
                .stream()
                .filter(tag ->
                        tag.getName().equals(name) &&
                        tag.getStatus().equals(status))
                .toList();
        return new PageImpl<>(filteredTags, pageable, filteredTags.size());
    }

    @Override
    public List<Tag> getAllTags() {
        return tags.values().stream().toList();
    }

    @Override
    public Optional<Tag> getTagById(Long id) {
        return Optional.ofNullable(tags.get(id));
    }

    @Override
    public List<Tag> getAllTagsByNameAndStatus(String name, StatusEnum status) {
        List<Tag> filteredTags = tags.values()
                .stream()
                .filter(tag -> tag.getName().equals(name) && tag.getStatus().equals(status))
                .toList();
        return filteredTags;
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
