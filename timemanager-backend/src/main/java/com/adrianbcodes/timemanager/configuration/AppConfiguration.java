package com.adrianbcodes.timemanager.configuration;

import com.adrianbcodes.timemanager.client.ClientService;
import com.adrianbcodes.timemanager.client.SqlClientRepository;
import com.adrianbcodes.timemanager.project.ProjectService;
import com.adrianbcodes.timemanager.project.SqlProjectRepository;
import com.adrianbcodes.timemanager.tag.SqlTagRepository;
import com.adrianbcodes.timemanager.tag.TagService;
import com.adrianbcodes.timemanager.task.SqlTaskRepository;
import com.adrianbcodes.timemanager.task.TaskService;
import com.adrianbcodes.timemanager.trackerEvent.SqlTrackerEventRepository;
import com.adrianbcodes.timemanager.trackerEvent.TrackerEventService;
import com.adrianbcodes.timemanager.user.SqlUserRepository;
import com.adrianbcodes.timemanager.user.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfiguration {
    @Bean
    ClientService clientService(SqlClientRepository sqlClientRepository) {
        return new ClientService(sqlClientRepository);
    }

    @Bean
    ProjectService projectService(SqlProjectRepository sqlProjectRepository) {
        return new ProjectService(sqlProjectRepository);
    }

    @Bean
    TaskService taskService(SqlTaskRepository sqlTaskRepository) {
        return new TaskService(sqlTaskRepository);
    }

    @Bean
    TagService tagService(SqlTagRepository sqlTagRepository) {
        return new TagService(sqlTagRepository);
    }

    @Bean
    TrackerEventService trackerEventService(SqlTrackerEventRepository sqlTrackerEventRepository) {
        return new TrackerEventService(sqlTrackerEventRepository);
    }

    @Bean
    UserService userService(SqlUserRepository sqlUserRepository) {
        return new UserService(sqlUserRepository);
    }
}
