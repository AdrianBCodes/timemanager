package com.adrianbcodes.timemanager.task;

import com.adrianbcodes.timemanager.client.Client;
import com.adrianbcodes.timemanager.dto.ClientDTO;
import com.adrianbcodes.timemanager.dto.TaskDTO;
import com.adrianbcodes.timemanager.project.ProjectService;
import com.adrianbcodes.timemanager.tag.TagService;
import com.adrianbcodes.timemanager.task.infrastructure.TaskWriteModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/v1/tasks")
public class TaskController {
    TaskService taskService;
    ProjectService projectService;
    TagService tagService;

    public TaskController(TaskService taskService, ProjectService projectService, TagService tagService) {
        this.taskService = taskService;
        this.projectService = projectService;
        this.tagService = tagService;
    }

    @GetMapping()
    ResponseEntity<Page<TaskDTO>> getAllTasksPaged(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "") String name,
            @RequestParam(defaultValue = "") String description,
            @RequestParam(defaultValue = "") Long projectId,
            @RequestParam(defaultValue = "id,asc") String[] sort
    ) {
        List<Sort.Order> orders = new ArrayList<>();

        if (sort[0].contains(",")) {
            for (String sortOrder : sort) {
                String[] _sort = sortOrder.split(",");
                orders.add(new Sort.Order(getSortDirection(_sort[1]), _sort[0]));
            }
        } else {
            orders.add(new Sort.Order(getSortDirection(sort[1]), sort[0]));
        }
        Pageable pageable = PageRequest.of(page,size, Sort.by(orders));

        Page<TaskDTO> foundTasks = taskService.getAllClientsPaged(name, description, projectId, pageable).map(Task::convertToTaskDTO);
        return ResponseEntity.ok(foundTasks);
    }

    @GetMapping("{id}")
    ResponseEntity<TaskDTO> getTaskById(@PathVariable Long id) {
        TaskDTO foundTask = taskService.getTaskById(id).convertToTaskDTO();
        return ResponseEntity.ok(foundTask);
    }

    @PostMapping
    ResponseEntity<?> addTask(@RequestBody TaskWriteModel taskWM) {
        Task toAdd = TaskBuilder.builder()
                .withName(taskWM.getName())
                .withDescription(taskWM.getDescription())
                .withProject(projectService.getProjectById(taskWM.getProjectId()))
                .withTags(taskWM.getTagsId().stream().map(tagId -> tagService.getTagById(tagId)).collect(Collectors.toSet()))
                .build();
        TaskDTO added = taskService.saveTask(toAdd).convertToTaskDTO();
        return ResponseEntity.ok(added);
    }

    @PutMapping("{id}")
    ResponseEntity<?> editTask(@PathVariable Long id, @RequestBody TaskWriteModel taskWM) {
        Task previous = taskService.getTaskById(id);
        Task toEdit = TaskBuilder.builder()
                .withId(previous.getId())
                .withName(taskWM.getName())
                .withDescription(taskWM.getDescription())
                .withProject(projectService.getProjectById(taskWM.getProjectId()))
                .withTags(taskWM.getTagsId().stream().map(tagId -> tagService.getTagById(tagId)).collect(Collectors.toSet()))
                .buildWithId();
        TaskDTO edited = taskService.saveTask(toEdit).convertToTaskDTO();
        return ResponseEntity.ok(edited);
    }

    @DeleteMapping("{id}")
    ResponseEntity<?> deleteTaskById(@PathVariable Long id) {
        taskService.deleteTaskById(id);
        return ResponseEntity.ok(id);
    }

    private Sort.Direction getSortDirection(String direction) {
        if (direction.equals("asc")) {
            return Sort.Direction.ASC;
        } else if (direction.equals("desc")) {
            return Sort.Direction.DESC;
        }
        return Sort.Direction.ASC;
    }
}
