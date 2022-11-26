package com.adrianbcodes.timemanager.tag;

import com.adrianbcodes.timemanager.common.StatusEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface SqlTagRepository extends TagRepository, JpaRepository<Tag, Long> {
    List<Tag> findByNameAndStatus(String name, StatusEnum status);

    Page<Tag> findByNameContainsIgnoreCaseAndStatus(String name, StatusEnum status, Pageable pageable);
    @Override
    default Page<Tag> getAllTagsByNameLike(String name, Pageable pageable){
        return this.findByNameContainsIgnoreCaseAndStatus(name, StatusEnum.ACTIVE ,pageable);
    }
    @Override
    default List<Tag> getAllTags() {
        return this.findAll();
    }

    @Override
    default Optional<Tag> getTagById(Long id) {
        return this.findById(id);
    }

    @Override
    default List<Tag> getAllTagsByNameAndStatus(String name, StatusEnum status){
        return this.findByNameAndStatus(name, status);
    }

    @Override
    default Tag saveTag(Tag tag) {
        return this.save(tag);
    }

    @Override
    default void deleteTag(Tag tag){
        tag.setDeletedAt(new Date());
        this.save(tag);
    }
}
