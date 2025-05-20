package com.muzkat.reminder.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Класс для получения учетных данных пользователя из конфигурации.
 * Этот класс используется для хранения имени пользователя и пароля.
 * Пароль хэшируется при установке для повышения безопасности и
 * предотвращения хранения пароля в открытом виде.
 * Значения для полей извлекаются из конфигурации с префиксом "credentials" в файле настроек
 * {@code application-secrets.yml}
 */
@Component
@ConfigurationProperties(prefix = "credentials")
@Data
public class DefaultUserCredentials {

    /**
     * Поле почта пользователя.
     * Извлекается из конфигурации с помощью аннотации {@link ConfigurationProperties}
     */
    private String email;

    /**
     * Хэшированный пароль пользователя.
     * Пароль будет хэшироваться при помощи BCryptPasswordEncoder
     */
    private String password;


    /**
     * Setter устанавливает пароль пользователя с хэшированием.
     * Пароль будет автоматически хэшироваться с использованием алгоритма BCrypt.
     * <p>
     *      ВАЖНО: Этот метод следует оставить, так как он выполняет дополнительную логику хэширования.
     *      Аннотация {@link Data} генерирует стандартные геттеры и сеттеры для полей, кроме тех,
     *      которые определены вручную. Следовательно, данный {@code setPassword} будет использоваться
     *      вместо автоматически сгенерированного.
     * </p>
     * @param password исходный пароль пользователя в открытом виде
     * @see BCryptPasswordEncoder
     */
    public void setPassword(String password) {
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        this.password = encoder.encode(password);
    }


    /**
     * Getter получает хэшированный пароль пользователя.
     * <p>
     *     ВАЖНО: Этот метод следует оставить, так как он выполняет дополнительную логику хэширования,
     *     генерируется автоматически аннотацией {@link Data} и используется для получения хэшированного пароля
     * </p>
     * @return хэшированный пароль пользователя
     */
    public String getPassword() {
        return password;
    }
}
