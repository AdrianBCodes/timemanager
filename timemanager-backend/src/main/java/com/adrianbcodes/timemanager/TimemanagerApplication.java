package com.adrianbcodes.timemanager;

import com.adrianbcodes.timemanager.user.User;
import com.adrianbcodes.timemanager.user.UserService;
import com.adrianbcodes.timemanager.user.role.ERole;
import com.adrianbcodes.timemanager.user.role.Role;
import com.adrianbcodes.timemanager.user.role.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@SpringBootApplication
public class TimemanagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(TimemanagerApplication.class, args);
	}

	@Bean
	CommandLineRunner commandLineRunner(UserService userService,
										RoleRepository roleRepository,
										PasswordEncoder encoder){
		return args -> {
			if(roleRepository.getByName(ERole.ROLE_ADMIN).isEmpty()){
				roleRepository.save(new Role(ERole.ROLE_ADMIN));
			}
			if(roleRepository.getByName(ERole.ROLE_MANAGER).isEmpty()){
				roleRepository.save(new Role(ERole.ROLE_MANAGER));
			}
			if(roleRepository.getByName(ERole.ROLE_USER).isEmpty()){
				roleRepository.save(new Role(ERole.ROLE_USER));
			}
			if(userService.getAllUsers().isEmpty()){
				Role adminRole = roleRepository.getByName(ERole.ROLE_ADMIN).orElseThrow(() -> new RuntimeException("No default roles in system"));
				Role managerRole = roleRepository.getByName(ERole.ROLE_MANAGER).orElseThrow(() -> new RuntimeException("No default roles in system"));
				Role userRole = roleRepository.getByName(ERole.ROLE_USER).orElseThrow(() -> new RuntimeException("No default roles in system"));

				User user1 = new User("Admin","Admin","admin@admin.com", "admin", encoder.encode("password"));
				User user2 = new User("Manager","Manager","manager@manager.com", "manager", encoder.encode("password"));
				User user3 = new User("User","User","user@user.com", "user", encoder.encode("password"));

				userService.saveAllUsers(List.of(user1, user2, user3));

				user1.setRoles(Set.of(adminRole));
				user2.setRoles(Set.of(managerRole));
				user3.setRoles(Set.of(userRole));

				userService.saveAllUsers(List.of(user1, user2, user3));
			}
		};
	}
}
