package com.adrianbcodes.timemanager.user.role;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SqlRoleRepository extends RoleRepository, JpaRepository<Role, Long> {

    Optional<Role> findByName(ERole name);
    @Override
    default Optional<Role> getByName(ERole name){
        return this.findByName(name);
    }
}
