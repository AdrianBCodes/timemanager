package com.adrianbcodes.timemanager.trackerEvent;

import com.adrianbcodes.timemanager.common.TrackerEventExcelExporter;
import com.adrianbcodes.timemanager.dto.ProjectDTO;
import com.adrianbcodes.timemanager.dto.TrackerEventDTO;
import com.adrianbcodes.timemanager.exceptions.UnauthorizedException;
import com.adrianbcodes.timemanager.project.Project;
import com.adrianbcodes.timemanager.project.ProjectService;
import com.adrianbcodes.timemanager.task.TaskService;
import com.adrianbcodes.timemanager.trackerEvent.infrastructure.TrackerEventWriteModel;
import com.adrianbcodes.timemanager.user.UserService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

    @GetMapping("/paged")
    ResponseEntity<Page<TrackerEventDTO>> getAllTrackerEventsPagedAndFiltered(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "") String description,
            @RequestParam(defaultValue = "") List<Long> projectsIds,
            @RequestParam(defaultValue = "") List<Long> clientsIds,
            @RequestParam(defaultValue = "") List<Long> tasksIds,
            @RequestParam(required = false) Long duration,
            @RequestParam(required = false) String date,
            @RequestParam(defaultValue = "") List<Long> usersIds,
            @RequestParam(defaultValue = "id,asc") String sort
    ) {
        Page<TrackerEventDTO> foundTrackerEvents = trackerEventService.getAllTrackerEventsPagedAndFiltered(description, projectsIds,clientsIds , tasksIds, duration, date, usersIds, page, size, sort).map(TrackerEvent::convertToTrackerEventDTO);
        return ResponseEntity.ok(foundTrackerEvents);
    }

    @GetMapping("/current")
    ResponseEntity<List<TrackerEventDTO>> getAllTrackerEventsForCurrentUserByDate(@RequestParam String dateString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        LocalDateTime localDateTime = LocalDateTime.parse(dateString, formatter);
        List<TrackerEventDTO> foundTrackerEvents = trackerEventService.getAllTrackerEventsForCurrentUserByDate(localDateTime).stream().map(TrackerEvent::convertToTrackerEventDTO).toList();
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

    @GetMapping("/projects")
    ResponseEntity<List<ProjectDTO>> getProjectsToTrack(){
        UserDetails userDetails;
        try{
            userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        } catch (Exception e){
            throw new UnauthorizedException("Unauthorized");
        }

        if(userDetails.getAuthorities().stream().anyMatch(r -> r.getAuthority().equals("ROLE_USER"))){
            return ResponseEntity.ok(projectService.getAllProjectsOfParticipantWithUsername(userDetails.getUsername()).stream().map(Project::convertToProjectDTO).toList());
        }
        return ResponseEntity.ok(projectService.getAllProjects().stream().map(Project::convertToProjectDTO).toList());
    }
    @GetMapping("/reports/export")
    public void exportToExcel(
            @RequestParam(defaultValue = "") String description,
            @RequestParam(defaultValue = "") List<Long> projectsIds,
            @RequestParam(defaultValue = "") List<Long> clientsIds,
            @RequestParam(defaultValue = "") List<Long> tasksIds,
            @RequestParam(required = false) Long duration,
            @RequestParam(required = false) String date,
            @RequestParam(defaultValue = "") List<Long> usersIds,
            @RequestParam(defaultValue = "id,asc") String sort,
            final HttpServletResponse response){
        response.setHeader("Content-encoding", "UTF-8");
        response.setHeader("Content-disposition", "attachment; filename=fileee.xlsx");
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

        List<TrackerEventDTO> foundTrackerEvents = trackerEventService.getAllTrackerEventsFilteredAndSorted(description, projectsIds,clientsIds , tasksIds, duration, date, usersIds, sort).stream().map(TrackerEvent::convertToTrackerEventDTO).toList();
        TrackerEventExcelExporter excelExporter = new TrackerEventExcelExporter(foundTrackerEvents);
        excelExporter.export(response);
    }
}