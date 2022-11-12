package com.adrianbcodes.timemanager.project;

import com.adrianbcodes.timemanager.client.Client;
import com.adrianbcodes.timemanager.client.ClientService;
import com.adrianbcodes.timemanager.dto.ProjectDTO;
import com.adrianbcodes.timemanager.project.infrastructure.ProjectWriteModel;
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
            @RequestParam(defaultValue = "") List<Long> clientsIds,
            @RequestParam(defaultValue = "") List<Long> ownersIds,
            @RequestParam(defaultValue = "id,asc") String sort
    ) {

        Page<ProjectDTO> foundProjects = projectService.getAllProjectsPaged(name, clientsIds, ownersIds, page, size, sort).map(Project::convertToProjectDTO);
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
                .withOwner(userService.getUserById(projectWM.getOwnerId()))
                .build();
        ProjectDTO added = projectService.saveProject(toAdd).convertToProjectDTO();
        return ResponseEntity.ok(added);
    }
    @PutMapping("{id}")
    ResponseEntity<?> editProject(@PathVariable Long id, @RequestBody ProjectWriteModel projectWM) {
        Project previous = projectService.getProjectById(id);
        Client newClient = clientService.getClientById(projectWM.getClientId());
        User newProjectManager = userService.getUserById(projectWM.getOwnerId());
        Project toEdit = ProjectBuilder
                .builder()
                .withId(previous.getId())
                .withName(projectWM.getName())
                .withClient(newClient)
                .withOwner(newProjectManager)
                .buildWithId();
        ProjectDTO edited = projectService.saveProject(toEdit).convertToProjectDTO();
        return ResponseEntity.ok(edited);
    }

    @DeleteMapping("{id}")
    ResponseEntity<?> deleteProjectById(@PathVariable Long id) {
        projectService.deleteProjectById(id);
        return ResponseEntity.ok(id);
    }

    @GetMapping("/{id}/participants/toAdd")
    ResponseEntity<?> getParticipantsToAdd(@PathVariable Long id){
        return ResponseEntity.ok(userService.getUsersReadyToAddToProject(id));
    }

    @PutMapping("/{id}/participants/{userId}/add")
    ResponseEntity<?> addParticipant(@PathVariable Long id,
                                     @PathVariable Long userId){
        User userToAdd = userService.getUserById(userId);
        Long editedProjectId = projectService.addParticipant(id, userToAdd);
        return ResponseEntity.ok(editedProjectId);
    }

    @PutMapping("/{id}/participants/{userId}/remove")
    ResponseEntity<?> removeParticipant(@PathVariable Long id,
                                        @PathVariable Long userId){
        User userToAdd = userService.getUserById(userId);
        Long editedProjectId = projectService.removeParticipant(id, userToAdd);
        return ResponseEntity.ok(editedProjectId);
    }
}
