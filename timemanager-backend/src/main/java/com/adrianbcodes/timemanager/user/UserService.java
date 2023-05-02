package com.adrianbcodes.timemanager.user;

import com.adrianbcodes.timemanager.common.SortMapper;
import com.adrianbcodes.timemanager.common.StatusEnum;
import com.adrianbcodes.timemanager.exceptions.AlreadyDeletedException;
import com.adrianbcodes.timemanager.exceptions.NotFoundException;
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

    public List<User> getAllUsers(){
        return userRepository.getAllUsers().stream().filter(user -> user.getStatus().equals(StatusEnum.ACTIVE)).toList();
    }

    Page<User> getAllUsersPagedAndFiltered(String name, String surname, String email, Long projectId, int page, int size, String sort) {
        List<Sort.Order> orders = new ArrayList<>();
        String[] _sort = sort.split(",");
        orders.add(new Sort.Order(SortMapper.getSortDirection(_sort[1]), _sort[0]));
        Pageable pageable = PageRequest.of(page,size, Sort.by(orders));
        return userRepository.getAllUsersPagedAndFiltered(name, surname, email, projectId, pageable);
    }
    public User getUserById(Long id){
        return userRepository.getUserById(id).orElseThrow(() -> new NotFoundException("User with id: " + id + " not found"));
    }
    public User saveUser(User user){
        return userRepository.saveUser(user);
    }

    public List<User> saveAllUsers(List<User> users){
        return userRepository.saveAllUsers(users);
    }

    void deleteUserById(Long id){
        User toDelete = getUserById(id);
        if(toDelete.getStatus().equals(StatusEnum.DELETED)){
            throw new AlreadyDeletedException("User with id: " + id + " already has status DELETED");
        }
        userRepository.deleteUser(toDelete);
    }

    public List<User> getUsersReadyToAddToProject(Long projectId){
        return userRepository.getUsersReadyToAddToProject(projectId);
    }
}
