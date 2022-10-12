package com.adrianbcodes.timemanager.tag;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SqlTagRepository extends TagRepository, JpaRepository<Tag, Long> {


    @Override
    default List<Tag> getAllTags() {
        return this.findAll();
    }

    @Override
    default Optional<Tag> getTagById(Long id) {
        return this.findById(id);
    }

    @Override
    default Tag saveTag(Tag tag) {
        return this.save(tag);
    }

    @Override
    default void deleteTag(Tag tag){
        this.delete(tag);
    }
}
