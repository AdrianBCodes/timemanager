package com.adrianbcodes.timemanager.user;

import com.adrianbcodes.timemanager.common.StatusEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.*;

public class FakeUserRepository implements UserRepository{

    private final Map<Long, User> users = new HashMap<>();
    @Override
    public Page<User> getAllUsersPagedAndFiltered(String name, String surname, String email, Long projectId, Pageable pageable) {
        List<User> filteredUsers = users.values()
                .stream()
                .filter(user ->
                        user.getName().contains(name) &&
                        user.getSurname().contains(surname) &&
                        user.getEmail().contains(email) &&
                        user.getProjects().stream().anyMatch(project -> project.getId().equals(projectId)))
                .toList();
        return new PageImpl<>(filteredUsers, pageable, filteredUsers.size());
    }

    @Override
    public List<User> saveAllUsers(List<User> users) {
        users.forEach(this::saveUser);
        return users
                .stream()
                .mapToLong(User::getId)
                .mapToObj(i -> getUserById(i)
                        .orElseThrow(() ->
                                new RuntimeException(String.format("User with id %d not found", i))))
                .toList();
    }

    @Override
    public List<User> getAllUsers() {
        return users.values().stream().filter(user -> user.getStatus().equals(StatusEnum.ACTIVE)).toList();
    }

    @Override
    public List<User> getUsersReadyToAddToProject(Long projectId) {
        return users.values()
                .stream()
                .filter(user -> user.getProjects().stream().noneMatch(project -> project.getId().equals(projectId)))
                .toList();
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
        return users.values()
                .stream()
                .filter(user -> user.getUsername().equals(username))
                .findFirst();
    }

    @Override
    public Boolean existByUsername(String username) {
        return users.values()
                .stream()
                .anyMatch(user -> user.getUsername().equals(username));
    }

    @Override
    public Boolean existByEmail(String email) {
        return users.values()
                .stream()
                .anyMatch(user -> user.getEmail().equals(email));
    }
}
