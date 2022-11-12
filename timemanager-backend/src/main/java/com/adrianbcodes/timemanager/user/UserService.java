package com.adrianbcodes.timemanager.user;

import com.adrianbcodes.timemanager.common.StatusEnum;
import com.adrianbcodes.timemanager.exceptions.AlreadyDeletedException;
import com.adrianbcodes.timemanager.exceptions.NotFoundException;
import com.adrianbcodes.timemanager.project.QProject;
import com.querydsl.core.BooleanBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;

public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    List<User> getAllUsers(){
        return userRepository.getAllUsers().stream().filter(user -> user.getStatus().equals(StatusEnum.ACTIVE)).toList();
    }

    Page<User> getAllUsersPaged(String name, String surname, String email, Long projectId, int page, int size, String sort) {
        QUser user = QUser.user;

        BooleanBuilder builder = new BooleanBuilder();

        builder.and(user.status.eq(StatusEnum.ACTIVE));

        if(!name.isEmpty()){
            builder.and(user.surname.contains(name));
        }
        if(!surname.isEmpty()){
            builder.and(user.surname.contains(surname));
        }
        if(!email.isEmpty()){
            builder.and(user.email.contains(email));
        }
        if(projectId != null){
            builder.and(user.projects.any().id.eq(projectId));
        }

        List<Sort.Order> orders = new ArrayList<>();

        String[] _sort = sort.split(",");
        orders.add(new Sort.Order(getSortDirection(_sort[1]), _sort[0]));

        Pageable pageable = PageRequest.of(page,size, Sort.by(orders));

        return userRepository.getAllUsersPaged(builder, pageable);
    }
    public User getUserById(Long id){
        return userRepository.getUserById(id).orElseThrow(() -> new NotFoundException("User with id: " + id + " not found"));
    }
    public User saveUser(User user){
        return userRepository.saveUser(user);
    }

    void deleteUserById(Long id){
        User toDelete = getUserById(id);
        if(toDelete.getStatus().equals(StatusEnum.DELETED)){
            throw new AlreadyDeletedException("User with id: " + id + " already has status DELETED");
        }
        userRepository.deleteUser(toDelete);
    }

    public List<User> getUsersReadyToAddToProject(Long projectId){
        QUser user = QUser.user;
        BooleanBuilder builder = new BooleanBuilder();
        builder.andNot(user.projects.any().id.eq(projectId));

        return userRepository.getAllUsers(builder);
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
