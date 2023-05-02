package com.adrianbcodes.timemanager.project;

import com.adrianbcodes.timemanager.common.StatusEnum;
import com.adrianbcodes.timemanager.exceptions.AlreadyDeletedException;
import com.adrianbcodes.timemanager.exceptions.BlankParameterException;
import com.adrianbcodes.timemanager.exceptions.NotFoundException;
import com.adrianbcodes.timemanager.exceptions.NotUniqueException;
import com.adrianbcodes.timemanager.user.FakeUserRepository;
import com.adrianbcodes.timemanager.user.User;
import com.adrianbcodes.timemanager.user.UserExample;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.BDDAssertions.assertThat;

class ProjectServiceTest {

    private ProjectService projectService;

    @BeforeEach
    void init() {
        projectService = new ProjectService(new FakeProjectRepository(), new FakeUserRepository());
    }

    @Test
    void getAllProjects() {
        //given
        Project project = ProjectExample.getProject1();
        Project project2 = ProjectExample.getProject2();
        projectService.saveProject(project);
        projectService.saveProject(project2);
        //when
        var result = projectService.getAllProjects();
        //then
        assertThat(result.size()).isEqualTo(2);
        assertThat(result.get(0)).isEqualTo(project);
        assertThat(result.get(1)).isEqualTo(project2);
    }

    @Test
    void getAllProjects_noProjectsInDatabase() {
        //given
        //when
        var result = projectService.getAllProjects();
        //then
        assertThat(result.size()).isEqualTo(0);
    }

    @Test
    void getAllProjects_onlyProjectsWithStatusDeletedInDatabase_returnsEmptyList() {
        //given
        Project project = ProjectExample.getProject1();
        Project project2 = ProjectExample.getProject2();
        projectService.saveProject(project);
        projectService.saveProject(project2);
        projectService.deleteProjectById(project.getId());
        projectService.deleteProjectById(project2.getId());
        //when
        var result = projectService.getAllProjects();
        //then
        assertThat(result.size()).isEqualTo(0);

    }

    @Test
    void getAllProjects_ignoresProjectsWithStatusDeleted() {
        //given
        Project project = ProjectExample.getProject1();
        Project project2 = ProjectExample.getProject2();
        projectService.saveProject(project);
        projectService.saveProject(project2);
        projectService.deleteProjectById(project2.getId());
        //when
        var result = projectService.getAllProjects();
        //then
        assertThat(result.size()).isEqualTo(1);
        assertThat(result.get(0)).isEqualTo(project);
    }

    @Test
    void getAllActiveProjectsPagedAndFiltered() {
        //given
        Project project = ProjectExample.getProject1();
        Project project2 = ProjectExample.getProject2();
        projectService.saveProject(project);
        projectService.saveProject(project2);
        //when
        var result = projectService.getAllActiveProjectsPagedAndFiltered(project.getName(), List.of(project.getClient().getId()), List.of(project.getOwner().getId()), 0, 5, "id,asc");
        //then
        assertThat(result.getTotalPages()).isEqualTo(1);
        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent().get(0)).isEqualTo(project);
    }

    @Test
    void getProjectById() {
        //given
        Project project = ProjectExample.getProject1();
        projectService.saveProject(project);
        //when
        var result = projectService.getProjectById(project.getId());
        //then
        assertThat(result).isEqualTo(project);
    }

    @Test
    void getProjectById_projectDoesNotExist_throwsNotFoundException() {
        //given
        //when
        var exception = catchThrowable(() -> projectService.getProjectById(1L));
        //then
        assertThat(exception).isInstanceOf(NotFoundException.class);
    }

    @Test
    void saveProject() {
        //given
        Project project = ProjectExample.getProject1();
        //when
        projectService.saveProject(project);
        //then
        assertThat(projectService.getProjectById(project.getId())).isEqualTo(project);
    }

    @Test
    void saveProject_withEmptyName_throwsBlankParameterException() {
        //given
        Project project = ProjectExample.getProject3WithEmptyName();
        //when
        var exception = catchThrowable(() -> projectService.saveProject(project)) ;
        //then
        assertThat(exception).isInstanceOf(BlankParameterException.class);
    }

    @Test
    void saveProject_activeProjectAlreadyHasSameName_throwsNotUniqueException() {
        //given
        Project project = ProjectExample.getProject1();
        Project project2 = ProjectExample.getProject1();
        projectService.saveProject(project);
        //when
        var exception = catchThrowable(() -> projectService.saveProject(project2)) ;
        //then
        assertThat(exception).isInstanceOf(NotUniqueException.class);
    }

    @Test
    void deleteProjectById() {
        //given
        Project project = ProjectExample.getProject1();
        project.setStatus(StatusEnum.ACTIVE);
        projectService.saveProject(project);
        //when
        projectService.deleteProjectById(project.getId());
        //then
        assertThat(projectService.getProjectById(project.getId()).getStatus()).isEqualTo(StatusEnum.DELETED);
    }

    @Test
    void deleteProjectById_projectHasStatusDeleted_throwsAlreadyDeletedException() {
        //given
        Project project = ProjectExample.getProject1();
        project.setStatus(StatusEnum.DELETED);
        projectService.saveProject(project);
        //when
        var exception = catchThrowable(() -> projectService.deleteProjectById(project.getId()));
        //then
        assertThat(exception).isInstanceOf(AlreadyDeletedException.class);
    }

    //TODO
    @Test
    void addParticipant() {
    }

    @Test
    void removeParticipant() {
    }

    @Test
    void getAllProjectsOfParticipantWithUsername() {
        //given
        User user = UserExample.getUser1();
        //when
        var result = projectService.getAllProjectsOfParticipantWithUsername(user.getUsername());
        //then
        assertThat(result).isEqualTo(user.getProjects().stream().toList());
    }
}