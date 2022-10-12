package com.adrianbcodes.timemanager.project;

import com.adrianbcodes.timemanager.client.Client;
import com.adrianbcodes.timemanager.client.ClientService;
import com.adrianbcodes.timemanager.dto.ProjectDTO;
import com.adrianbcodes.timemanager.project.infrastructure.ProjectWriteModel;
import com.adrianbcodes.timemanager.task.TaskService;
import com.adrianbcodes.timemanager.user.User;
import com.adrianbcodes.timemanager.user.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    ResponseEntity<List<ProjectDTO>> getAllProjects() {
        List<ProjectDTO> foundProjects = projectService
                .getAllProjects()
                .stream()
                .map(Project::convertToProjectDTO)
                .toList();
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
}
