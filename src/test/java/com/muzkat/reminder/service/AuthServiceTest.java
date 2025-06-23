package com.muzkat.reminder.service;

import com.muzkat.reminder.exception.InvalidEmailException;
import com.muzkat.reminder.exception.UserAlreadyExistsException;
import com.muzkat.reminder.model.User;
import com.muzkat.reminder.model.UserCredentials;
import com.muzkat.reminder.repository.UserCredentialsRepository;
import com.muzkat.reminder.repository.UserRepository;
import com.muzkat.reminder.security.JwtTokenProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@DisplayName("Тесты для Auth Service")
@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserCredentialsRepository userCredentialsRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private EmailValidationService emailValidationService;

    @InjectMocks
    private AuthService authService;

    @DisplayName("Проверяет, что метод возвращает токен при условии, что учетные данные пользователя валидны")
    @Test
    void login_credentialsAreValid_returnToken() {
        String email = "contactAlice@gmail.com";
        String password = "testPassword";
        String encodedPassword = "testPasswordEncoder";
        String jwtToken = "jwtToken";

        User user = new User();
        user.setEmail(email);

        UserCredentials userCredentials = new UserCredentials();
        userCredentials.setUser(user);
        userCredentials.setPassword(encodedPassword);
        userCredentials.setId(1L);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(userCredentialsRepository.findByUserId(user.getId())).thenReturn(Optional.of(userCredentials));
        when(passwordEncoder.matches(password, encodedPassword)).thenReturn(true);
        when(jwtTokenProvider.generateToken(email)).thenReturn(jwtToken);

        String token = authService.login(email, password);

        assertEquals(jwtToken, token);

        verify(userRepository).findByEmail(email);
        verify(userCredentialsRepository).findByUserId(user.getId());
        verify(passwordEncoder).matches(password, encodedPassword);
        verify(jwtTokenProvider).generateToken(email);
    }

    @DisplayName("Проверяет, что метод выбрасывает исключение в случае, если пароль пользователя некорректный")
    @Test
    void login_passwordIncorrect_throwBadCredentialsException(){
        String email = "contactAlice@gmail.com";
        String password = "incorrectPassword";
        String encodedPassword = "encodedPassword";

        User user = new User();
        user.setEmail(email);

        UserCredentials userCredentials = new UserCredentials();
        userCredentials.setUser(user);
        userCredentials.setPassword(encodedPassword);
        userCredentials.setId(1L);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(userCredentialsRepository.findByUserId(user.getId())).thenReturn(Optional.of(userCredentials));
        when(passwordEncoder.matches(password, encodedPassword)).thenReturn(false);

        assertThrows(BadCredentialsException.class, ()-> {
            authService.login(email, password);
        });

        verify(jwtTokenProvider, never()).generateToken(any());
    }


    @DisplayName("Проверяет, что метод выбрасывает исключение в случае, если пользователь не найден")
    @Test
    void login_userNotFound_throwBadCredentialsException() {
        String email = "anyEmail@gmail.com";
        String password = "anyPassword";

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        assertThrows(BadCredentialsException.class, () ->
                authService.login(email, password));

        verify(userRepository).findByEmail(email);
        verifyNoInteractions(userCredentialsRepository, passwordEncoder, jwtTokenProvider);
    }


    @DisplayName("Проверяет, что метод выбрасывает исключение в случае, если пользователь " +
                 "и его зашифрованный пароль не найдены")
    @Test
    void login_userCredentialsAreMissing_throwBadCredentialsException(){
        String email = "anyEmail@gmail.com";
        String password = "anyPassword";

        User user = new User();
        user.setEmail(email);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(userCredentialsRepository.findByUserId(user.getId())).thenReturn(Optional.empty());

        assertThrows(BadCredentialsException.class, () ->
        {
            authService.login(email, password);
        });

        verify(userRepository).findByEmail(email);
        verify(userCredentialsRepository).findByUserId(user.getId());
        verifyNoInteractions(passwordEncoder, jwtTokenProvider);

    }


    @DisplayName("Проверяет, что метод регистрирует пользователя и его учетные данные, если пользователь" +
                 "не зарегистрирован в приложении")
    @Test
    void register_emailIsValidAndNotRegistered_shouldSaveUserAndCredentials() {

        String email = "contactAlice@gmail.com";
        String password = "password";
        String encodedPassword = "encodedPassword";

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());
        when(emailValidationService.isEmailValid(email)).thenReturn(true);
        when(passwordEncoder.encode(password)).thenReturn(encodedPassword);

        User testUser = new User();
        testUser.setId(1L);
        testUser.setName("Alice");
        testUser.setEmail("contactAlice@gmail.com");
        testUser.setTelegramChatId("11111111");

        when(userRepository.save(any())).thenReturn(testUser);

        authService.register(email, password);

        verify(userRepository).save(argThat(user -> user.getEmail().equalsIgnoreCase(email)));

        verify(userCredentialsRepository).save(argThat(userCredentials ->
                userCredentials != null &&
                userCredentials.getUser() != null &&
                userCredentials.getUser().getEmail().equals(email) &&
                userCredentials.getPassword().equals(encodedPassword)
                ));
    }

    @DisplayName("Проверяет, что метод выбрасывает исключение UserAlreadyExistsException, если пользователь" +
                 "с таким email ранее зарегистирован в приложении")
    @Test
    void register_emailAlreadyExists_shouldThrowUserAlreadyExistsException(){
        String email = "contactAlice@gmail.com";
        String password = "password";

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(new User()));

        assertThrows(UserAlreadyExistsException.class, () ->
                authService.register(email, password));

        verify(userRepository, never()).save(any());

        verify(userCredentialsRepository, never()).save(any());
    }

    @DisplayName("Проверяет, что метод выбрасывает исключение InvalidEmailException, если email пользователя" +
                 "не прошел валидацию")
    @Test
    void register_emailIsInvalid_shouldThrowInvalidEmailException(){
        String invalidEmail = "contactAlice@gmail";
        String invalidPassword = "invalidPassword";

        when(userRepository.findByEmail(invalidEmail)).thenReturn(Optional.empty());
        when(emailValidationService.isEmailValid(invalidEmail)).thenReturn(false);

        assertThrows(InvalidEmailException.class, () ->
                authService.register(invalidEmail, invalidPassword));

        verify(userRepository, never()).save(any());
        verify(userCredentialsRepository, never()).save(any());
    }
}
