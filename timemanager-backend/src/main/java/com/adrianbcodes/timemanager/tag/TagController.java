package com.adrianbcodes.timemanager.tag;

import com.adrianbcodes.timemanager.dto.TagDTO;
import com.adrianbcodes.timemanager.tag.infrastructure.TagWriteModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/tags")
public class TagController {
    TagService tagService;

    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @GetMapping()
    ResponseEntity<?> getAllTags() {
        List<TagDTO> foundTags = tagService.getAllTags().stream().map(Tag::convertToTagDTO).toList();
        return ResponseEntity.ok(foundTags);
    }

    @GetMapping("{id}")
    ResponseEntity<?> getTagById(@PathVariable Long id) {
        TagDTO foundTag = tagService.getTagById(id).convertToTagDTO();
        return ResponseEntity.ok(foundTag);
    }

    @PostMapping
    ResponseEntity<?> addTag(@RequestBody TagWriteModel tagWM) {
        Tag toAdd = TagBuilder.builder()
                .withName(tagWM.getName())
                .build();
        TagDTO added = tagService.saveTag(toAdd).convertToTagDTO();
        return ResponseEntity.ok(added);
    }

    @PutMapping("{id}")
    ResponseEntity<?> editTag(@PathVariable Long id, @RequestBody TagWriteModel tagWM) {
        Tag previous = tagService.getTagById(id);
        Tag toEdit = TagBuilder.builder()
                .withId(previous.getId())
                .withName(tagWM.getName())
                .buildWithId();
        TagDTO edited = tagService.saveTag(toEdit).convertToTagDTO();
        return ResponseEntity.ok(edited);
    }

    @DeleteMapping("{id}")
    ResponseEntity<?> deleteTagById(@PathVariable Long id) {
        tagService.deleteTagById(id);
        return ResponseEntity.noContent().build();
    }
}
