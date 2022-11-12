package com.adrianbcodes.timemanager.user;

import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface SqlUserRepository extends UserRepository, JpaRepository<User, Long>, QuerydslPredicateExecutor<User> {

    Page<User> findAll(Predicate predicate, Pageable pageable);
    List<User> findAll(Predicate predicate);

    @Override
    default Page<User> getAllUsersPaged(Predicate predicate, Pageable pageable){
        return this.findAll(predicate, pageable);
    }

    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    @Override
    default List<User> getAllUsers() {
        return this.findAll();
    }
    @Override
    default List<User> getAllUsers(Predicate predicate) {
        return this.findAll(predicate);
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

    @Override
    default Optional<User> getByUsername(String username){
        return this.findByUsername(username);
    }

    @Override
    default Boolean existByUsername(String username){
        return this.existsByUsername(username);
    }

    @Override
    default Boolean existByEmail(String email){
        return this.existsByEmail(email);
    }
}
