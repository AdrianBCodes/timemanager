package com.adrianbcodes.timemanager.user;

import com.adrianbcodes.timemanager.common.StatusEnum;
import com.adrianbcodes.timemanager.exceptions.AlreadyDeletedException;
import com.adrianbcodes.timemanager.exceptions.NotFoundException;

import java.util.List;

public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    List<User> getAllUsers(){
        return userRepository.getAllUsers().stream().filter(user -> user.getStatus().equals(StatusEnum.ACTIVE)).toList();
    }
    public User getUserById(Long id){
        return userRepository.getUserById(id).orElseThrow(() -> new NotFoundException("User with id: " + id + " not found"));
    }
    User saveUser(User user){
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
