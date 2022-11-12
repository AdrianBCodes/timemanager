package com.adrianbcodes.timemanager.user;

import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    Page<User> getAllUsersPaged(Predicate predicate, Pageable pageable);

    List<User> getAllUsers();

    List<User> getAllUsers(Predicate predicate);

    Optional<User> getUserById(Long id);
    User saveUser(User user);
    void deleteUser(User user);

    Optional<User> getByUsername(String username);

    Boolean existByUsername(String username);

    Boolean existByEmail(String email);
}
