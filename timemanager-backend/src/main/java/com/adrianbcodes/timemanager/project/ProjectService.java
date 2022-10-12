package com.adrianbcodes.timemanager.project;

import com.adrianbcodes.timemanager.common.StatusEnum;
import com.adrianbcodes.timemanager.exceptions.AlreadyDeletedException;
import com.adrianbcodes.timemanager.exceptions.NotFoundException;

import java.util.List;

public class ProjectService {
    private final ProjectRepository projectRepository;

    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    List<Project> getAllProjects(){
        return projectRepository.getAllProjects().stream().filter(project -> project.getStatus().equals(StatusEnum.ACTIVE)).toList();
    }
    public Project getProjectById(Long id){
        return projectRepository.getProjectById(id).orElseThrow(() -> new NotFoundException("Project with id: " + id + " not found"));
    }
    Project saveProject(Project project){
        return projectRepository.saveProject(project);
    }
    void deleteProjectById(Long id){
        Project toDelete = getProjectById(id);
        if(toDelete.getStatus().equals(StatusEnum.DELETED)){
            throw new AlreadyDeletedException("Project with id: " + id + " already has status DELETED");
        }
        projectRepository.deleteProject(toDelete);
    }
}
