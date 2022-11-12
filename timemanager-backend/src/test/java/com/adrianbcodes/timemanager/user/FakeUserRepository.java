package com.adrianbcodes.timemanager.user;

import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.*;

public class FakeUserRepository implements UserRepository{

    private final Map<Long, User> users = new HashMap<>();

    //TODO
    @Override
    public Page<User> getAllUsersPaged(Predicate predicate, Pageable pageable) {
        return null;
    }

    @Override
    public List<User> getAllUsers() {
        return users.values().stream().toList();
    }


    //TODO
    @Override
    public List<User> getAllUsers(Predicate predicate) {
        return null;
    }

    @Override
    public Optional<User> getUserById(Long id) {
        return Optional.ofNullable(users.get(id));
    }

    @Override
    public User saveUser(User user) {
        // TODO Generate ID
        users.put(user.getId(), user);
        return users.get(user.getId());
    }

    @Override
    public void deleteUser(User user) {
        user.setDeletedAt(new Date());
        users.put(user.getId(), user);
    }

    @Override
    public Optional<User> getByUsername(String username) {
        return Optional.empty();
    }

    @Override
    public Boolean existByUsername(String username) {
        return null;
    }

    @Override
    public Boolean existByEmail(String email) {
        return null;
    }
}
