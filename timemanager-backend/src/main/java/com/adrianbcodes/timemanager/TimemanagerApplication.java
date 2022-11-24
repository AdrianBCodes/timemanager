package com.adrianbcodes.timemanager;

import com.adrianbcodes.timemanager.client.Client;
import com.adrianbcodes.timemanager.client.ClientService;
import com.adrianbcodes.timemanager.project.Project;
import com.adrianbcodes.timemanager.project.ProjectService;
import com.adrianbcodes.timemanager.tag.Tag;
import com.adrianbcodes.timemanager.tag.TagService;
import com.adrianbcodes.timemanager.task.Task;
import com.adrianbcodes.timemanager.task.TaskService;
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

import java.util.HashSet;
import java.util.List;

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
				HashSet<Role> adminRoleSet = new HashSet<>();
				adminRoleSet.add(adminRole);

				User user1 = new User("Ad","Min","admin@admin.com", "admin", encoder.encode("password"));
				userService.saveUser(user1);
				user1.setRoles(adminRoleSet);
				userService.saveUser(user1);
			}
		};
	}
}
