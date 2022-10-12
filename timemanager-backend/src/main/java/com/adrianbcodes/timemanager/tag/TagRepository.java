package com.adrianbcodes.timemanager.tag;

import java.util.List;
import java.util.Optional;

public interface TagRepository {
    List<Tag> getAllTags();
    Optional<Tag> getTagById(Long id);
    Tag saveTag(Tag tag);
    void deleteTag(Tag tag);
}
