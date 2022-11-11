package com.adrianbcodes.timemanager.trackerEvent;

import com.adrianbcodes.timemanager.dto.ProjectDTO;
import com.adrianbcodes.timemanager.dto.TrackerEventDTO;
import com.adrianbcodes.timemanager.project.Project;
import com.adrianbcodes.timemanager.project.ProjectService;
import com.adrianbcodes.timemanager.task.TaskService;
import com.adrianbcodes.timemanager.trackerEvent.infrastructure.TrackerEventWriteModel;
import com.adrianbcodes.timemanager.user.UserService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("api/v1/trackerEvents")
public class TrackerEventController {
    TrackerEventService trackerEventService;
    ProjectService projectService;
    TaskService taskService;
    UserService userService;

    public TrackerEventController(TrackerEventService trackerEventService, ProjectService projectService, TaskService taskService, UserService userService) {
        this.trackerEventService = trackerEventService;
        this.projectService = projectService;
        this.taskService = taskService;
        this.userService = userService;
    }

    @GetMapping()
    ResponseEntity<Page<TrackerEventDTO>> getAllTrackerEventsPaged(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "") String description,
            @RequestParam(defaultValue = "") List<Long> projectsIds,
            @RequestParam(defaultValue = "") List<Long> tasksIds,
            @RequestParam(required = false) Long duration,
            @RequestParam(required = false) Date date,
            @RequestParam(defaultValue = "") List<Long> usersIds,
            @RequestParam(defaultValue = "id,asc") String sort
    ) {

        Page<TrackerEventDTO> foundTrackerEvents = trackerEventService.getAllTrackerEventsPaged(description, projectsIds, tasksIds, duration, date, usersIds, page, size, sort).map(TrackerEvent::convertToTrackerEventDTO);
        return ResponseEntity.ok(foundTrackerEvents);
    }

    @GetMapping("{id}")
    ResponseEntity<?> getTrackerEventById(@PathVariable Long id) {
        TrackerEventDTO foundTrackerEvent = trackerEventService.getTrackerEventById(id).convertToTrackerEventDTO();
        return ResponseEntity.ok(foundTrackerEvent);
    }

    @PostMapping
    ResponseEntity<?> addTrackerEvent(@RequestBody TrackerEventWriteModel trackerEventWM) {
        TrackerEvent toAdd = TrackerEventBuilder
                .builder()
                .withDescription(trackerEventWM.getDescription())
                .withProject(projectService.getProjectById(trackerEventWM.getProjectId()))
                .withTask(taskService.getTaskById(trackerEventWM.getTaskId()))
                .withDuration(trackerEventWM.getDuration())
                .withDate(trackerEventWM.getDate())
                .withUser(userService.getUserById(trackerEventWM.getUserId()))
                .build();
        TrackerEventDTO added = trackerEventService.saveTrackerEvent(toAdd).convertToTrackerEventDTO();
        return ResponseEntity.ok(added);
    }

    @PutMapping("{id}")
    ResponseEntity<?> editTrackerEvent(@PathVariable Long id, @RequestBody TrackerEventWriteModel trackerEventWM) {
        TrackerEvent previous = trackerEventService.getTrackerEventById(id);
        TrackerEvent toEdit = TrackerEventBuilder
                .builder()
                .withId(previous.getId())
                .withDescription(trackerEventWM.getDescription())
                .withProject(projectService.getProjectById(trackerEventWM.getProjectId()))
                .withTask(taskService.getTaskById(trackerEventWM.getTaskId()))
                .withDuration(trackerEventWM.getDuration())
                .withDate(trackerEventWM.getDate())
                .withUser(userService.getUserById(trackerEventWM.getUserId()))
                .buildWithId();
        TrackerEventDTO edited = trackerEventService.saveTrackerEvent(toEdit).convertToTrackerEventDTO();
        return ResponseEntity.ok(edited);
    }

    @DeleteMapping("{id}")
    ResponseEntity<?> deleteTrackerEventById(@PathVariable Long id) {
        trackerEventService.deleteTrackerEventById(id);
        return ResponseEntity.noContent().build();
    }
}