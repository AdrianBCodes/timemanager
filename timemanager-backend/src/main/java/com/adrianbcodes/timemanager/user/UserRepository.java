package com.adrianbcodes.timemanager.user;

import com.adrianbcodes.timemanager.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    List<User> getAllUsers();

    Page<User> getAllUsersByNameContainsIgnoreCaseAndSurnameContainsIgnoreCaseAndEmailContainsIgnoreCase(String name, String surname, String email, Pageable pageable);

    Optional<User> getUserById(Long id);
    User saveUser(User user);
    void deleteUser(User user);

    Optional<User> getByUsername(String username);

    Boolean existByUsername(String username);

    Boolean existByEmail(String email);
}
