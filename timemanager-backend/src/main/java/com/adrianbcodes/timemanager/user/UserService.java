package com.adrianbcodes.timemanager.user;

import com.adrianbcodes.timemanager.common.StatusEnum;
import com.adrianbcodes.timemanager.exceptions.AlreadyDeletedException;
import com.adrianbcodes.timemanager.exceptions.NotFoundException;
import com.adrianbcodes.timemanager.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    List<User> getAllUsers(){
        return userRepository.getAllUsers().stream().filter(user -> user.getStatus().equals(StatusEnum.ACTIVE)).toList();
    }

    Page<User> getAllUsersPaged(String name, String surname, String email, Pageable pageable) {
        return userRepository.getAllUsersByNameContainsIgnoreCaseAndSurnameContainsIgnoreCaseAndEmailContainsIgnoreCase(name, surname, email, pageable);
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
}
