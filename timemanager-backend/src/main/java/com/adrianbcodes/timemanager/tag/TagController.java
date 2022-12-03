package com.adrianbcodes.timemanager.tag;

import com.adrianbcodes.timemanager.dto.TagDTO;
import com.adrianbcodes.timemanager.tag.infrastructure.TagWriteModel;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/tags")
public class TagController {
    TagService tagService;

    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @GetMapping()
    ResponseEntity<Page<TagDTO>> getAllActiveTagsPagedAndFiltered(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "") String name,
            @RequestParam(defaultValue = "id,asc") String sort
    ) {
        Page<TagDTO> foundTags = tagService.getAllActiveTagsPagedAndFiltered(name, page, size, sort).map(Tag::convertToTagDTO);
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
        return ResponseEntity.ok(id);
    }
}
