package com.adrianbcodes.timemanager.user;

import com.adrianbcodes.timemanager.common.StatusEnum;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface SqlUserRepository extends UserRepository, JpaRepository<User, Long>, QuerydslPredicateExecutor<User> {

    @NonNull
    Page<User> findAll(@NonNull Predicate predicate, @NonNull Pageable pageable);
    @NonNull
    List<User> findAll(@NonNull Predicate predicate);

    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    @NonNull
    <S extends User> List<S> saveAll(@NonNull Iterable<S> entities);

    @Override
    default Page<User> getAllUsersPagedAndFiltered(String name, String surname, String email, Long projectId, Pageable pageable) {
        QUser user = QUser.user;

        BooleanBuilder builder = new BooleanBuilder();

        builder.and(user.status.eq(StatusEnum.ACTIVE));

        if (!name.isEmpty()) {
            builder.and(user.name.containsIgnoreCase(name));
        }
        if (!surname.isEmpty()) {
            builder.and(user.surname.containsIgnoreCase(surname));
        }
        if (!email.isEmpty()) {
            builder.and(user.email.containsIgnoreCase(email));
        }
        if (projectId != null) {
            builder.and(user.projects.any().id.eq(projectId));
        }
        return this.findAll(builder, pageable);
    }

    @Override
    default List<User> saveAllUsers(List<User> users) {
        return this.saveAll(users);
    }

    @Override
    default List<User> getAllUsers() {
        return this.findAll();
    }

    @Override
    default List<User> getUsersReadyToAddToProject(Long projectId) {
        QUser user = QUser.user;
        BooleanBuilder builder = new BooleanBuilder();
        builder.andNot(user.projects.any().id.eq(projectId));
        return this.findAll(builder);
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
    default void deleteUser(User user) {
        user.setDeletedAt(new Date());
        this.save(user);
    }

    @Override
    default Optional<User> getByUsername(String username) {
        return this.findByUsername(username);
    }

    @Override
    default Boolean existByUsername(String username) {
        return this.existsByUsername(username);
    }

    @Override
    default Boolean existByEmail(String email) {
        return this.existsByEmail(email);
    }
}
