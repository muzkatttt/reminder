package com.muzkat.reminder;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;


/**
 * Главный класс запуска Spring Boot-приложения Reminder.
 * Аннотации:
 * {@link org.springframework.boot.autoconfigure.SpringBootApplication} — включает авто-конфигурацию и компонент-сканирование.
 * {@link org.springframework.scheduling.annotation.EnableScheduling} — активирует планировщик задач для работы с @Scheduled.
 * Метод {@code main} инициализирует и запускает контекст приложения через {@link org.springframework.boot.SpringApplication}
 */
@SpringBootApplication(scanBasePackages = "com.muzkat.reminder")
@EnableScheduling
@EnableAsync
public class ReminderApplication {

	/**
	 * Точка входа в приложение
	 * @param args аргументы командной строки
	 */
	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(ReminderApplication.class);
		app.setLogStartupInfo(true);
		app.run(args);
	}
}
