package com.muzkat.reminder;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ReminderApplication {

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(ReminderApplication.class);
		app.setLogStartupInfo(true);
		app.run(args);
	}
	
}
