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

import java.util.HashSet;

@SpringBootApplication
public class TimemanagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(TimemanagerApplication.class, args);
	}

//	@Bean
//	CommandLineRunner commandLineRunner(ClientService clientService,
//										UserService userService,
//										ProjectService projectService,
//										TagService tagService,
//										TaskService taskService,
//										RoleRepository roleRepository){
//		return args -> {
//			Role adminRole = roleRepository.save(new Role(ERole.ROLE_ADMIN));
//			Role managerRole = roleRepository.save(new Role(ERole.ROLE_MANAGER));
//			Role userRole = roleRepository.save(new Role(ERole.ROLE_USER));
//			Client client1 = clientService.saveClient(new Client("ClientName1", "aa"));
//			clientService.saveClient(new Client("ClientName3", "aa"));
//			clientService.saveClient(new Client("ClientName2", "bb"));
//			clientService.saveClient(new Client("ClientName4", "bb"));
//
//			HashSet<Role> userRoleSet = new HashSet<>();
//			userRoleSet.add(userRole);
//
//			HashSet<Role> managerRoleSet = new HashSet<>();
//			managerRoleSet.add(managerRole);
//
//			HashSet<Role> adminRoleSet = new HashSet<>();
//			adminRoleSet.add(adminRole);
//
//			User user1 = new User("test", "test@test.com", "test");
//			userService.saveUser(user1);
//
//			Project project1 = projectService.saveProject(new Project("Project1", client1, user1));
//			tagService.saveTag(new Tag("tag1"));
//			taskService.saveTask(new Task("task1", "desc1", project1));
//		};
//	}
}
