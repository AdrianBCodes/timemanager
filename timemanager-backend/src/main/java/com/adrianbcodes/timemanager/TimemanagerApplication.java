package com.adrianbcodes.timemanager;

import com.adrianbcodes.timemanager.client.Client;
import com.adrianbcodes.timemanager.client.ClientService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class TimemanagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(TimemanagerApplication.class, args);
	}

	@Bean
	CommandLineRunner commandLineRunner(ClientService clientService){
		return args -> {
			clientService.saveClient(new Client("ClientName1", "ClientNote1"));
			clientService.saveClient(new Client("ClientName2", "ClientNote2"));
			clientService.saveClient(new Client("ClientName3", "ClientNote3"));
		};
	}
}
