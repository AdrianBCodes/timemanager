package com.adrianbcodes.timemanager.tag;

import com.adrianbcodes.timemanager.tag.Tag;
import com.adrianbcodes.timemanager.common.StatusEnum;
import com.adrianbcodes.timemanager.exceptions.AlreadyDeletedException;
import com.adrianbcodes.timemanager.exceptions.NotFoundException;
import com.adrianbcodes.timemanager.task.TaskService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Set;

public class TagService {
    private final TagRepository tagRepository;

    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    List<Tag> getAllTags(){
        return tagRepository.getAllTags().stream().filter(tag -> tag.getStatus().equals(StatusEnum.ACTIVE)).toList();
    }

    Page<Tag> getAllTagsPaged(String name, Pageable pageable) {
        return tagRepository.getAllTagsByNameLike(name, pageable);
    }

    public Tag getTagById(Long id){
        return tagRepository.getTagById(id).orElseThrow(() -> new NotFoundException("Tag with id: " + id + " not found"));
    }
    public Tag saveTag(Tag tag){
        return tagRepository.saveTag(tag);
    }

    void deleteTagById(Long id){
        Tag toDelete = getTagById(id);
        if(toDelete.getStatus().equals(StatusEnum.DELETED)){
            throw new AlreadyDeletedException("Tag with id: " + id + " already has status DELETED");
        }
        tagRepository.deleteTag(toDelete);
    }
}
