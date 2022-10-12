package com.adrianbcodes.timemanager.tag;

import com.adrianbcodes.timemanager.common.StatusEnum;
import com.adrianbcodes.timemanager.exceptions.AlreadyDeletedException;
import com.adrianbcodes.timemanager.exceptions.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.BDDAssertions.assertThat;

class TagServiceTest {

    private TagService tagService;

    @BeforeEach
    void init() {
        tagService = new TagService(new FakeTagRepository());
    }

    @Test
    void getAllTags() {
        //given
        Tag tag = TagBuilder.builder()
                .withId(1L)
                .withName("Tag1")
                .buildWithId();
        Tag tag2 = TagBuilder.builder()
                .withId(2L)
                .withName("Tag2")
                .buildWithId();
        tagService.saveTag(tag);
        tagService.saveTag(tag2);
        //when
        var result = tagService.getAllTags();
        //then
        assertThat(result.size()).isEqualTo(2);
        assertThat(result.get(0)).isEqualTo(tag);
        assertThat(result.get(1)).isEqualTo(tag2);
    }

    @Test
    void getAllTags_noTagsInDatabase() {
        //given
        //when
        var result = tagService.getAllTags();
        //then
        assertThat(result.size()).isEqualTo(0);
    }

    @Test
    void getAllTags_onlyTagsWithStatusDeletedInDatabase_returnsEmptyList() {
        //given
        Tag tag = TagBuilder.builder()
                .withId(1L)
                .withName("Tag1")
                .buildWithId();
        Tag tag2 = TagBuilder.builder()
                .withId(2L)
                .withName("Tag2")
                .buildWithId();
        tagService.saveTag(tag);
        tagService.saveTag(tag2);
        tagService.deleteTagById(tag.getId());
        tagService.deleteTagById(tag2.getId());
        //when
        var result = tagService.getAllTags();
        //then
        assertThat(result.size()).isEqualTo(0);
    }

    @Test
    void getAllTags_ignoresTagsWithStatusDeleted() {
        //given
        Tag tag = TagBuilder.builder()
                .withId(1L)
                .withName("Tag1")
                .buildWithId();
        Tag tag2 = TagBuilder.builder()
                .withId(2L)
                .withName("Tag2")
                .buildWithId();
        Tag tag3 = TagBuilder.builder()
                .withId(3L)
                .withName("Tag3")
                .buildWithId();
        tagService.saveTag(tag);
        tagService.saveTag(tag2);
        tagService.saveTag(tag3);
        tagService.deleteTagById(tag3.getId());
        //when
        var result = tagService.getAllTags();
        //then
        assertThat(result.size()).isEqualTo(2);
        assertThat(result.get(0)).isEqualTo(tag);
        assertThat(result.get(1)).isEqualTo(tag2);
    }

    @Test
    void getTagById() {
        //given
        Tag tag = TagBuilder.builder()
                .withId(1L)
                .withName("Tag1")
                .buildWithId();
        tagService.saveTag(tag);
        //when
        var result = tagService.getTagById(tag.getId());
        //then
        assertThat(result).isEqualTo(tag);
    }

    @Test
    void getTagById_tagDoesNotExist_throwsNotFoundException() {
        //given
        //when
        var exception = catchThrowable(() -> tagService.getTagById(1L));
        //then
        assertThat(exception).isInstanceOf(NotFoundException.class);
    }

    @Test
    void saveTag() {
        //given
        Tag tag = TagBuilder.builder()
                .withId(1L)
                .withName("Tag1")
                .buildWithId();
        //when
        tagService.saveTag(tag);
        //then
        assertThat(tagService.getTagById(tag.getId())).isEqualTo(tag);
    }

    @Test
    void deleteTagById() {
        //given
        Tag tag = TagBuilder.builder()
                .withId(1L)
                .withName("Tag1")
                .buildWithId();
        tag.setStatus(StatusEnum.ACTIVE);
        tagService.saveTag(tag);
        //when
        tagService.deleteTagById(tag.getId());
        //then
        assertThat(tagService.getTagById(tag.getId()).getStatus()).isEqualTo(StatusEnum.DELETED);
    }

    @Test
    void deleteTagById_tagHasStatusDeleted_throwsAlreadyDeletedException() {
        //given
        Tag tag = TagBuilder.builder()
                .withId(1L)
                .withName("Tag1")
                .buildWithId();
        tag.setStatus(StatusEnum.DELETED);
        tagService.saveTag(tag);
        //when
        var exception = catchThrowable(() -> tagService.deleteTagById(tag.getId()));
        //then
        assertThat(exception).isInstanceOf(AlreadyDeletedException.class);
    }
}