package com.adrianbcodes.timemanager.tag;

import com.adrianbcodes.timemanager.client.Client;
import com.adrianbcodes.timemanager.common.StatusEnum;
import com.adrianbcodes.timemanager.exceptions.AlreadyDeletedException;
import com.adrianbcodes.timemanager.exceptions.BlankParameterException;
import com.adrianbcodes.timemanager.exceptions.NotFoundException;
import com.adrianbcodes.timemanager.exceptions.NotUniqueException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

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
        if(tag.getName().isBlank()){
            throw new BlankParameterException("Tag's name cannot be empty");
        }
        if(!isTagNameUnique(tag.getName(), StatusEnum.ACTIVE)){
            throw new NotUniqueException("Tag with name: " + tag.getName() + " already exists");
        }
        return tagRepository.saveTag(tag);
    }

    void deleteTagById(Long id){
        Tag toDelete = getTagById(id);
        if(toDelete.getStatus().equals(StatusEnum.DELETED)){
            throw new AlreadyDeletedException("Tag with id: " + id + " already has status DELETED");
        }
        tagRepository.deleteTag(toDelete);
    }

    private boolean isTagNameUnique(String name, StatusEnum status){
        List<Tag> tags = tagRepository.getAllTagsByNameAndStatus(name, status);
        return tags.isEmpty();
    }
}
