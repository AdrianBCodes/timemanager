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
			clientService.saveClient(new Client("ClientName1", "aa"));
			clientService.saveClient(new Client("ClientName3", "aa"));
			clientService.saveClient(new Client("ClientName2", "bb"));
			clientService.saveClient(new Client("ClientName4", "bb"));
		};
	}
}
