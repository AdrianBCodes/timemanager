package com.adrianbcodes.timemanager.project;

import com.adrianbcodes.timemanager.client.Client;
import com.adrianbcodes.timemanager.client.ClientBuilder;
import com.adrianbcodes.timemanager.client.ClientService;
import com.adrianbcodes.timemanager.client.FakeClientRepository;
import com.adrianbcodes.timemanager.common.StatusEnum;
import com.adrianbcodes.timemanager.exceptions.AlreadyDeletedException;
import com.adrianbcodes.timemanager.exceptions.NotFoundException;
import com.adrianbcodes.timemanager.user.FakeUserRepository;
import com.adrianbcodes.timemanager.user.User;
import com.adrianbcodes.timemanager.user.UserBuilder;
import com.adrianbcodes.timemanager.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.BDDAssertions.assertThat;

class ProjectServiceTest {

    private ProjectService projectService;

    @BeforeEach
    void init() {
        projectService = new ProjectService(new FakeProjectRepository());
    }

    @Test
    void getAllProjects() {
        //given
        Client client = ClientBuilder
                .builder()
                .withId(1L)
                .buildWithId();
        User user = UserBuilder
                .builder()
                .withId(1L)
                .buildWithId();
        Project project = ProjectBuilder
                .builder()
                .withId(1L)
                .withName("Project1")
                .withClient(client)
                .withProjectManager(user)
                .buildWithId();
        Project project2 = ProjectBuilder
                .builder()
                .withId(2L)
                .withName("Project2")
                .withClient(client)
                .withProjectManager(user)
                .buildWithId();
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
        Client client = ClientBuilder
                .builder()
                .withId(1L)
                .buildWithId();
        User user = UserBuilder
                .builder()
                .withId(1L)
                .buildWithId();
        Project project = ProjectBuilder
                .builder()
                .withId(1L)
                .withName("Project1")
                .withClient(client)
                .withProjectManager(user)
                .buildWithId();
        Project project2 = ProjectBuilder
                .builder()
                .withId(2L)
                .withName("Project2")
                .withClient(client)
                .withProjectManager(user)
                .buildWithId();
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
        Client client = ClientBuilder
                .builder()
                .withId(1L)
                .buildWithId();
        User user = UserBuilder
                .builder()
                .withId(1L)
                .buildWithId();
        Project project = ProjectBuilder
                .builder()
                .withId(1L)
                .withName("Project1")
                .withClient(client)
                .withProjectManager(user)
                .buildWithId();
        Project project2 = ProjectBuilder
                .builder()
                .withId(2L)
                .withName("Project2")
                .withClient(client)
                .withProjectManager(user)
                .buildWithId();
        Project project3 = ProjectBuilder
                .builder()
                .withId(3L)
                .withName("Project3")
                .withClient(client)
                .withProjectManager(user)
                .buildWithId();
        projectService.saveProject(project);
        projectService.saveProject(project2);
        projectService.saveProject(project3);
        projectService.deleteProjectById(project3.getId());
        //when
        var result = projectService.getAllProjects();
        //then
        assertThat(result.size()).isEqualTo(2);
        assertThat(result.get(0)).isEqualTo(project);
        assertThat(result.get(1)).isEqualTo(project2);
    }

    @Test
    void getProjectById() {
        //given
        Client client = ClientBuilder
                .builder()
                .withId(1L)
                .buildWithId();
        User user = UserBuilder
                .builder()
                .withId(1L)
                .buildWithId();
        Project project = ProjectBuilder
                .builder()
                .withId(1L)
                .withName("Project1")
                .withClient(client)
                .withProjectManager(user)
                .buildWithId();
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
        Client client = ClientBuilder
                .builder()
                .withId(1L)
                .buildWithId();
        User user = UserBuilder
                .builder()
                .withId(1L)
                .buildWithId();
        Project project = ProjectBuilder
                .builder()
                .withId(1L)
                .withName("Project1")
                .withClient(client)
                .withProjectManager(user)
                .buildWithId();
        //when
        projectService.saveProject(project);
        //then
        assertThat(projectService.getProjectById(project.getId())).isEqualTo(project);
    }

    @Test
    void deleteProjectById() {
        //given
        Client client = ClientBuilder
                .builder()
                .withId(1L)
                .buildWithId();
        User user = UserBuilder
                .builder()
                .withId(1L)
                .buildWithId();
        Project project = ProjectBuilder
                .builder()
                .withId(1L)
                .withName("Project1")
                .withClient(client)
                .withProjectManager(user)
                .buildWithId();
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
        Client client = ClientBuilder
                .builder()
                .withId(1L)
                .buildWithId();
        User user = UserBuilder
                .builder()
                .withId(1L)
                .buildWithId();
        Project project = ProjectBuilder
                .builder()
                .withId(1L)
                .withName("Project1")
                .withClient(client)
                .withProjectManager(user)
                .buildWithId();
        project.setStatus(StatusEnum.DELETED);
        projectService.saveProject(project);
        //when
        var exception = catchThrowable(() -> projectService.deleteProjectById(project.getId()));
        //then
        assertThat(exception).isInstanceOf(AlreadyDeletedException.class);
    }
}