package com.muzkat.reminder.repository;

import com.muzkat.reminder.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
@DataJpaTest
@DisplayName("Тесты для User Repository")
class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @DisplayName("Метод настраивает профиль пользователя перед запуском всех тестов")
    @BeforeEach
    void setUp() {
        User user = new User();
        user.setName("Alice");
        user.setEmail("contactAlice@gmail.com");
        user.setTelegramChatId("111111111");
        User savedUser = userRepository.saveAndFlush(user);
    }

    @DisplayName("Проверяет, что пользовтель существует в базе данных, должен вернуть true")
    @Test
    void existsByEmail_userExists_returnTrue() {
        boolean resultOfTest = userRepository.existsByEmail("contactAlice@gmail.com");

        assertThat(resultOfTest).isNotNull();
        assertThat(resultOfTest).isTrue();
    }


    @DisplayName("Проверяет, что пользователя нет в базе данных, должен вернуть false")
    @Test
    void existsByEmail_userExists_returnFalse() {
        boolean resultOfTest = userRepository.existsByEmail("anotherUser@gmail.com");

        assertThat(resultOfTest).isNotNull();
        assertThat(resultOfTest).isFalse();
    }

    @DisplayName("Проверяет, что метод возвращает пользователя по email")
    @Test
    void findByEmail_userExists_returnUser() {
        Optional<User> resultOfTest = userRepository.findByEmail("contactAlice@gmail.com");

        assertThat(resultOfTest).isNotNull();
        assertThat(resultOfTest.get().getEmail()).isEqualTo("contactAlice@gmail.com");
    }
}
