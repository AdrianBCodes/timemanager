package com.adrianbcodes.timemanager.user;

import com.adrianbcodes.timemanager.user.User;
import com.adrianbcodes.timemanager.user.UserRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface SqlUserRepository extends UserRepository, JpaRepository<User, Long> {
    @Override
    default List<User> getAllUsers() {
        return this.findAll();
    }

    @Override
    default Optional<User> getUserById(Long id) {
        return this.findById(id);
    }

    @Override
    default User saveUser(User user) {
        return this.save(user);
    }

    @Override
    default void deleteUser(User user){
        user.setDeletedAt(new Date());
        this.save(user);
    }
}
