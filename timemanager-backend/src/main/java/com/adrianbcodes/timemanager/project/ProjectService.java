package com.adrianbcodes.timemanager.project;

import com.adrianbcodes.timemanager.common.StatusEnum;
import com.adrianbcodes.timemanager.exceptions.AlreadyDeletedException;
import com.adrianbcodes.timemanager.exceptions.NotFoundException;
import com.adrianbcodes.timemanager.user.User;
import com.adrianbcodes.timemanager.user.UserRepository;
import com.querydsl.core.BooleanBuilder;
import org.springframework.data.domain.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ProjectService {
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    @PersistenceContext
    private EntityManager entityManager;



    public ProjectService(ProjectRepository projectRepository, UserRepository userRepository) {
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
    }

    List<Project> getAllProjects(){
        return projectRepository.getAllProjects().stream().filter(project -> project.getStatus().equals(StatusEnum.ACTIVE)).toList();
    }

    Page<Project> getAllProjectsPaged(String name, List<Long> clientsIds, List<Long> ownersIds, int page, int size, String sort){
        QProject project = QProject.project;

        BooleanBuilder builder = new BooleanBuilder();

        builder.and(project.name.contains(name)).and(project.status.eq(StatusEnum.ACTIVE));

        if(!clientsIds.isEmpty()){
            builder.and(project.client.id.in(clientsIds));
        }
        if(!ownersIds.isEmpty()){
            builder.and(project.owner.id.in(ownersIds));
        }

        List<Sort.Order> orders = new ArrayList<>();

        String[] _sort = sort.split(",");
        orders.add(new Sort.Order(getSortDirection(_sort[1]), _sort[0]));

        Pageable pageable = PageRequest.of(page,size, Sort.by(orders));


        return projectRepository.getAllProjectsPaged(builder, pageable);
    }
    public Project getProjectById(Long id){
        return projectRepository.getProjectById(id).orElseThrow(() -> new NotFoundException("Project with id: " + id + " not found"));
    }
    public Project saveProject(Project project){
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

        Set<Project> userProjects = userToAdd.getProjects();
        userProjects.add(project);
        userToAdd.setProjects(userProjects);

        userRepository.saveUser(userToAdd);
        this.saveProject(project);

        return project.getId();
    }

    public Long removeParticipant(Long id, User userToRemove) {
        Project project = this.getProjectById(id);
        Set<User> participants = project.getParticipants();
        participants.remove(userToRemove);
        project.setParticipants(participants);

        Set<Project> userProjects = userToRemove.getProjects();
        userProjects.remove(project);
        userToRemove.setProjects(userProjects);

        userRepository.saveUser(userToRemove);
        this.saveProject(project);

        return project.getId();
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
