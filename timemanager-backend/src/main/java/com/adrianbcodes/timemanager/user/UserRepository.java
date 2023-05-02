package com.adrianbcodes.timemanager.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    Page<User> getAllUsersPagedAndFiltered(String name, String surname, String email, Long projectId, Pageable pageable);

    List<User> saveAllUsers(List<User> users);

    List<User> getAllUsers();

    List<User> getUsersReadyToAddToProject(Long projectId);

    Optional<User> getUserById(Long id);
    User saveUser(User user);
    void deleteUser(User user);

    Optional<User> getByUsername(String username);

    Boolean existByUsername(String username);

    Boolean existByEmail(String email);
}
