package com.adrianbcodes.timemanager.task;

import com.adrianbcodes.timemanager.common.StatusEnum;
import com.adrianbcodes.timemanager.exceptions.AlreadyDeletedException;
import com.adrianbcodes.timemanager.exceptions.BlankParameterException;
import com.adrianbcodes.timemanager.exceptions.NotFoundException;
import com.adrianbcodes.timemanager.exceptions.NotUniqueException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.BDDAssertions.assertThat;

class TaskServiceTest {

    private TaskService taskService;

    @BeforeEach
    void init() {
        taskService = new TaskService(new FakeTaskRepository());
    }

    @Test
    void getAllTasks() {
        //given
        Task task = TaskExample.getTask1();
        Task task2 = TaskExample.getTask2();
        taskService.saveTask(task);
        taskService.saveTask(task2);
        //when
        var result = taskService.getAllTasks();
        //then
        assertThat(result.size()).isEqualTo(2);
        assertThat(result.get(0)).isEqualTo(task);
        assertThat(result.get(1)).isEqualTo(task2);
    }

    @Test
    void getAllTasks_noTasksInDatabase() {
        //given
        //when
        var result = taskService.getAllTasks();
        //then
        assertThat(result.size()).isEqualTo(0);
    }

    @Test
    void getAllTasks_onlyTasksWithStatusDeletedInDatabase_returnsEmptyList() {
        //given
        Task task = TaskExample.getTask1();
        Task task2 = TaskExample.getTask2();
        taskService.saveTask(task);
        taskService.saveTask(task2);
        taskService.deleteTaskById(task.getId());
        taskService.deleteTaskById(task2.getId());
        //when
        var result = taskService.getAllTasks();
        //then
        assertThat(result.size()).isEqualTo(0);
    }

    @Test
    void getAllTasks_ignoresTasksWithStatusDeleted() {
        //given
        Task task = TaskExample.getTask1();
        Task task2 = TaskExample.getTask2();
        taskService.saveTask(task);
        taskService.saveTask(task2);
        taskService.deleteTaskById(task2.getId());
        //when
        var result = taskService.getAllTasks();
        //then
        assertThat(result.size()).isEqualTo(1);
        assertThat(result.get(0)).isEqualTo(task);
    }

    @Test
    void getAllTasksByProjectId(){
        //given
        Task task = TaskExample.getTask1();
        Task task2 = TaskExample.getTask2();
        taskService.saveTask(task);
        taskService.saveTask(task2);
        //when
        var result = taskService.getAllTasksByProjectId(1L);
        //then
        assertThat(result.size()).isEqualTo(1);
        assertThat(result.get(0)).isEqualTo(task);
    }

    @Test
    void getAllTasksByProjectId_WrongProjectId_ReturnsEmptyList(){
        //given
        Task task = TaskExample.getTask1();
        taskService.saveTask(task);
        //when
        var result = taskService.getAllTasksByProjectId(9999L);
        //then
        assertThat(result.size()).isEqualTo(0);
    }

    @Test
    void getAllActiveTasksPagedAndFiltered(){
        //given
        Task task = TaskExample.getTask1();
        Task task2 = TaskExample.getTask2();
        taskService.saveTask(task);
        taskService.saveTask(task2);
        //when
        var result = taskService.getAllActiveTasksPagedAndFiltered(task.getName(), task.getDescription(), task.getProject().getId(), 0,5, "id,asc");
        //then
        assertThat(result.getTotalPages()).isEqualTo(1);
        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent().get(0)).isEqualTo(task);
    }

    @Test
    void getTaskById() {
        //given
        Task task = TaskExample.getTask1();
        taskService.saveTask(task);
        //when
        var result = taskService.getTaskById(task.getId());
        //then
        assertThat(result).isEqualTo(task);
    }

    @Test
    void getTaskById_taskDoesNotExist_throwsNotFoundException() {
        //given
        //when
        var exception = catchThrowable(() -> taskService.getTaskById(1L));
        //then
        assertThat(exception).isInstanceOf(NotFoundException.class);
    }

    @Test
    void saveTask() {
        //given
        Task task = TaskExample.getTask1();
        //when
        taskService.saveTask(task);
        //then
        assertThat(taskService.getTaskById(task.getId())).isEqualTo(task);
    }

    @Test
    void saveTask_withEmptyName_throwsBlankParameterException() {
        //given
        Task task = TaskExample.getTask3WithEmptyName();
        //when
        var exception = catchThrowable(() -> taskService.saveTask(task));
        //then
        assertThat(exception).isInstanceOf(BlankParameterException.class);
    }

    @Test
    void saveTask_activeTaskAlreadyHasSameNameInSameProject_throwsNotUniqueException() {
        //given
        Task task = TaskExample.getTask1();
        Task task2 = TaskExample.getTask1();
        taskService.saveTask(task);
        //when
        var exception = catchThrowable(() -> taskService.saveTask(task2));
        //then
        assertThat(exception).isInstanceOf(NotUniqueException.class);
    }

    @Test
    void deleteTaskById() {
        //given
        Task task = TaskExample.getTask1();
        task.setStatus(StatusEnum.ACTIVE);
        taskService.saveTask(task);
        //when
        taskService.deleteTaskById(task.getId());
        //then
        assertThat(taskService.getTaskById(task.getId()).getStatus()).isEqualTo(StatusEnum.DELETED);
    }

    @Test
    void deleteTaskById_taskHasStatusDeleted_throwsAlreadyDeletedException() {
        //given
        Task task = TaskExample.getTask1();
        task.setStatus(StatusEnum.DELETED);
        taskService.saveTask(task);
        //when
        var exception = catchThrowable(() -> taskService.deleteTaskById(task.getId()));
        //then
        assertThat(exception).isInstanceOf(AlreadyDeletedException.class);
    }
}