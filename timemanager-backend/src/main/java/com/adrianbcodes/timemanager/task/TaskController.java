package com.adrianbcodes.timemanager.task;

import com.adrianbcodes.timemanager.dto.TaskDTO;
import com.adrianbcodes.timemanager.project.ProjectService;
import com.adrianbcodes.timemanager.tag.TagService;
import com.adrianbcodes.timemanager.task.infrastructure.TaskWriteModel;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
            @RequestParam(defaultValue = "id,asc") String sort
    ) {
        Page<TaskDTO> foundTasks = taskService.getAllActiveTasksPagedAndFiltered(name, description, projectId, page, size, sort).map(Task::convertToTaskDTO);
        return ResponseEntity.ok(foundTasks);
    }

    @GetMapping("{id}")
    ResponseEntity<TaskDTO> getTaskById(@PathVariable Long id) {
        TaskDTO foundTask = taskService.getTaskById(id).convertToTaskDTO();
        return ResponseEntity.ok(foundTask);
    }

    @GetMapping("/project/{id}")
    ResponseEntity<List<TaskDTO>> getTasksByProjectId(@PathVariable Long id){
        return ResponseEntity.ok(taskService.getAllTasksByProjectId(id)
                .stream()
                .map(Task::convertToTaskDTO)
                .toList());
    }

    @PostMapping
    ResponseEntity<?> addTask(@RequestBody TaskWriteModel taskWM) {
        Task toAdd = TaskBuilder.builder()
                .withName(taskWM.getName())
                .withDescription(taskWM.getDescription())
                .withProject(projectService.getProjectById(taskWM.getProjectId()))
                .withTags(taskWM.getTags())
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
                .withTags(taskWM.getTags())
                .buildWithId();
        TaskDTO edited = taskService.saveTask(toEdit).convertToTaskDTO();
        return ResponseEntity.ok(edited);
    }

    @DeleteMapping("{id}")
    ResponseEntity<?> deleteTaskById(@PathVariable Long id) {
        taskService.deleteTaskById(id);
        return ResponseEntity.ok(id);
    }
}
