package com.muzkat.reminder.repository;

import com.muzkat.reminder.model.User;
import com.muzkat.reminder.model.UserCredentials;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;


import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@DisplayName("Тесты для User Credentials Repository")
class UserCredentialsRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserCredentialsRepository userCredentialsRepository;

    @DisplayName("Проверяет, что метод возвращает учетные данные пользователя по userId")
    @Test
    void findByUserId_userCredentialsExists_returnUser() {
        User user = new User();
        user.setName("Alice");
        user.setEmail("contactAlice@gmail.com");
        user.setTelegramChatId("111111111");

        User savedUser = userRepository.saveAndFlush(user);

        UserCredentials credentials = new UserCredentials();
        credentials.setUser(savedUser);
        credentials.setPassword("password");

        userCredentialsRepository.saveAndFlush(credentials);
        Optional<UserCredentials> savedUserCredentials = userCredentialsRepository.findByUserId(savedUser.getId());

        assertThat(savedUserCredentials).isPresent();
        assertThat(savedUserCredentials.get().getPassword()).isEqualTo("password");
    }
}
