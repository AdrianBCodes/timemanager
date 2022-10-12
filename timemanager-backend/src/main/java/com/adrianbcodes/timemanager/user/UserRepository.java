package com.adrianbcodes.timemanager.user;

import com.adrianbcodes.timemanager.user.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    List<User> getAllUsers();
    Optional<User> getUserById(Long id);
    User saveUser(User user);
    void deleteUser(User user);
}
