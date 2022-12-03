package com.adrianbcodes.timemanager.project;

import com.adrianbcodes.timemanager.common.SortMapper;
import com.adrianbcodes.timemanager.common.StatusEnum;
import com.adrianbcodes.timemanager.exceptions.AlreadyDeletedException;
import com.adrianbcodes.timemanager.exceptions.BlankParameterException;
import com.adrianbcodes.timemanager.exceptions.NotFoundException;
import com.adrianbcodes.timemanager.exceptions.NotUniqueException;
import com.adrianbcodes.timemanager.user.User;
import org.springframework.data.domain.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ProjectService {
    private final ProjectRepository projectRepository;
    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public List<Project> getAllProjects(){
        return projectRepository.getAllProjects().stream().filter(project -> project.getStatus().equals(StatusEnum.ACTIVE)).toList();
    }

    Page<Project> getAllActiveProjectsPagedAndFiltered(String name, List<Long> clientsIds, List<Long> ownersIds, int page, int size, String sort){
        List<Sort.Order> orders = new ArrayList<>();

        String[] _sort = sort.split(",");
        orders.add(new Sort.Order(SortMapper.getSortDirection(_sort[1]), _sort[0]));

        Pageable pageable = PageRequest.of(page,size, Sort.by(orders));
        return projectRepository.getAllActiveProjectsPagedAndFiltered(name, clientsIds, ownersIds, pageable);
    }
    public Project getProjectById(Long id){
        return projectRepository.getProjectById(id).orElseThrow(() -> new NotFoundException("Project with id: " + id + " not found"));
    }
    public Project saveProject(Project project){
        if(project.getName().isBlank()){
            throw new BlankParameterException("Project's name cannot be empty");
        }
        if(!isProjectNameUnique(project.getName(), StatusEnum.ACTIVE)){
            throw new NotUniqueException("Project with name: " + project.getName() + " already exists");
        }
        return projectRepository.saveProject(project);
    }
    void deleteProjectById(Long id){
        Project toDelete = getProjectById(id);
        if(toDelete.getStatus().equals(StatusEnum.DELETED)){
            throw new AlreadyDeletedException("Project with id: " + id + " already has status DELETED");
        }
        projectRepository.deleteProject(toDelete);
    }

    public Long addParticipant(Long id, User userToAdd) {
        Project project = this.getProjectById(id);
        Set<User> participants = project.getParticipants();
        participants.add(userToAdd);
        project.setParticipants(participants);
        this.saveProject(project);

        return project.getId();
    }

    public Long removeParticipant(Long id, User userToRemove) {
        Project project = this.getProjectById(id);
        Set<User> participants = project.getParticipants();
        participants.remove(userToRemove);
        project.setParticipants(participants);
        this.saveProject(project);

        return project.getId();
    }

    public List<Project> getProjectsByUserUsername(String username){
        return projectRepository.getAllProjectsOfParticipantWithUsername(username);
    }

    private boolean isProjectNameUnique(String name, StatusEnum status){
        List<Project> projects = projectRepository.getAllProjectsByNameAndStatus(name, status);
        return projects.isEmpty();
    }

}
