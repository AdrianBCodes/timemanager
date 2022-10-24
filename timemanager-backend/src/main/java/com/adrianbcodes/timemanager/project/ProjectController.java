package com.adrianbcodes.timemanager.project;

import com.adrianbcodes.timemanager.client.Client;
import com.adrianbcodes.timemanager.client.ClientService;
import com.adrianbcodes.timemanager.dto.ProjectDTO;
import com.adrianbcodes.timemanager.dto.ProjectDTO;
import com.adrianbcodes.timemanager.project.infrastructure.ProjectWriteModel;
import com.adrianbcodes.timemanager.project.Project;
import com.adrianbcodes.timemanager.task.TaskService;
import com.adrianbcodes.timemanager.user.User;
import com.adrianbcodes.timemanager.user.UserService;
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
@RequestMapping("api/v1/projects")
public class ProjectController {
    ProjectService projectService;
    ClientService clientService;
    UserService userService;
    TaskService taskService;

    public ProjectController(ProjectService projectService, ClientService clientService, UserService userService, TaskService taskService) {
        this.projectService = projectService;
        this.clientService = clientService;
        this.userService = userService;
        this.taskService = taskService;
    }

    @GetMapping()
    ResponseEntity<Page<ProjectDTO>> getAllProjectsPaged(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "") String name,
            @RequestParam(defaultValue = "") String clientName,
            @RequestParam(defaultValue = "") String ownerFullName,
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

        Page<ProjectDTO> foundProjects = projectService.getAllProjectsPaged(name, clientName, ownerFullName, pageable).map(Project::convertToProjectDTO);
        return ResponseEntity.ok(foundProjects);
    }

    @GetMapping("{id}")
    ResponseEntity<ProjectDTO> getProjectById(@PathVariable Long id) {
        ProjectDTO foundProject = projectService.getProjectById(id).convertToProjectDTO();
        return ResponseEntity.ok(foundProject);
    }

    @PostMapping
    ResponseEntity<?> addProject(@RequestBody ProjectWriteModel projectWM) {
        Project toAdd = ProjectBuilder
                .builder()
                .withName(projectWM.getName())
                .withClient(clientService.getClientById(projectWM.getClientId()))
                .withProjectManager(userService.getUserById(projectWM.getProjectManagerId()))
                .withParticipants(projectWM.getParticipantsId().stream().map(participantId -> userService.getUserById(participantId)).collect(Collectors.toSet()))
                .build();
        ProjectDTO added = projectService.saveProject(toAdd).convertToProjectDTO();
        return ResponseEntity.ok(added);
    }

    //TODO - FIX Editing without participants
    @PutMapping("{id}")
    ResponseEntity<?> editProject(@PathVariable Long id, @RequestBody ProjectWriteModel projectWM) {
        Project previous = projectService.getProjectById(id);
        Client newClient = clientService.getClientById(projectWM.getClientId());
        User newProjectManager = userService.getUserById(projectWM.getProjectManagerId());
        Project toEdit = ProjectBuilder
                .builder()
                .withId(previous.getId())
                .withName(projectWM.getName())
                .withClient(newClient)
                .withProjectManager(newProjectManager)
                .withParticipants(projectWM.getParticipantsId().stream().map(participantId -> userService.getUserById(participantId)).collect(Collectors.toSet()))
                .buildWithId();
        ProjectDTO edited = projectService.saveProject(toEdit).convertToProjectDTO();
        return ResponseEntity.ok(edited);
    }

    @DeleteMapping("{id}")
    ResponseEntity<?> deleteProjectById(@PathVariable Long id) {
        projectService.deleteProjectById(id);
        return ResponseEntity.noContent().build();
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
