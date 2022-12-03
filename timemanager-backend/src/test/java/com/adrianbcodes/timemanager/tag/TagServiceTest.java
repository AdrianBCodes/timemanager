package com.adrianbcodes.timemanager.tag;

import com.adrianbcodes.timemanager.common.StatusEnum;
import com.adrianbcodes.timemanager.exceptions.AlreadyDeletedException;
import com.adrianbcodes.timemanager.exceptions.BlankParameterException;
import com.adrianbcodes.timemanager.exceptions.NotFoundException;
import com.adrianbcodes.timemanager.exceptions.NotUniqueException;
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
        Tag tag = TagExample.getTag1();
        Tag tag2 = TagExample.getTag2();
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
        Tag tag = TagExample.getTag1();
        Tag tag2 = TagExample.getTag2();
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
        Tag tag = TagExample.getTag1();
        Tag tag2 = TagExample.getTag2();
        tagService.saveTag(tag);
        tagService.saveTag(tag2);
        tagService.deleteTagById(tag2.getId());
        //when
        var result = tagService.getAllTags();
        //then
        assertThat(result.size()).isEqualTo(1);
        assertThat(result.get(0)).isEqualTo(tag);
    }

    @Test
    void getAllActiveTagsPagedAndFiltered(){
        //given
        Tag tag = TagExample.getTag1();
        Tag tag2 = TagExample.getTag2();
        tagService.saveTag(tag);
        tagService.saveTag(tag2);
        //when
        var result = tagService.getAllActiveTagsPagedAndFiltered(tag.getName(), 0, 5, "id,asc");
        //then
        assertThat(result.getTotalPages()).isEqualTo(1);
        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent().get(0)).isEqualTo(tag);
    }

    @Test
    void getTagById() {
        //given
        Tag tag = TagExample.getTag1();
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
        Tag tag = TagExample.getTag1();
        //when
        tagService.saveTag(tag);
        //then
        assertThat(tagService.getTagById(tag.getId())).isEqualTo(tag);
    }

    @Test
    void saveTag_withEmptyName_throwsBlankParameterException() {
        //given
        Tag tag = TagExample.getTag3WithEmptyName();
        //when
        var exception = catchThrowable(() -> tagService.saveTag(tag));
        //then
        assertThat(exception).isInstanceOf(BlankParameterException.class);
    }

    @Test
    void saveTag_activeTagAlreadyHasSameName_throwsNotUniqueException() {
        //given
        Tag tag = TagExample.getTag1();
        Tag tag2 = TagExample.getTag1();
        tagService.saveTag(tag);
        //when
        var exception = catchThrowable(() -> tagService.saveTag(tag2));
        //then
        assertThat(exception).isInstanceOf(NotUniqueException.class);
    }

    @Test
    void deleteTagById() {
        //given
        Tag tag = TagExample.getTag1();
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
        Tag tag = TagExample.getTag1();
        tag.setStatus(StatusEnum.DELETED);
        tagService.saveTag(tag);
        //when
        var exception = catchThrowable(() -> tagService.deleteTagById(tag.getId()));
        //then
        assertThat(exception).isInstanceOf(AlreadyDeletedException.class);
    }
}