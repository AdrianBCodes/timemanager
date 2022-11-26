package com.adrianbcodes.timemanager.tag;

import com.adrianbcodes.timemanager.common.StatusEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface TagRepository {
    Page<Tag> getAllTagsByNameLike(String name, Pageable pageable);
    List<Tag> getAllTags();
    Optional<Tag> getTagById(Long id);

    List<Tag> getAllTagsByNameAndStatus(String name, StatusEnum status);

    Tag saveTag(Tag tag);
    void deleteTag(Tag tag);
}
