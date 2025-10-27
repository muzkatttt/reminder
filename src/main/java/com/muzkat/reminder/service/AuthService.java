package com.muzkat.reminder.service;

import com.muzkat.reminder.exception.InvalidEmailException;
import com.muzkat.reminder.exception.UserAlreadyExistsException;
import com.muzkat.reminder.model.User;
import com.muzkat.reminder.model.UserCredentials;
import com.muzkat.reminder.repository.UserCredentialsRepository;
import com.muzkat.reminder.repository.UserRepository;
import com.muzkat.reminder.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Сервис для обработки аутентификации и регистрации пользователей.
 * Предоставляет методы для входа пользователя в приложение и регистрацию нового пользователя
 */
@Service
@RequiredArgsConstructor
public class AuthService {

    /**
     * Поле экземпляр {@link UserRepository}
     */
    private final UserRepository userRepository;

    /**
     * Поле экземпляр {@link UserCredentialsRepository}
     */
    private final UserCredentialsRepository userCredentialsRepository;

    /**
     * Поле компонент для шифрования и проверки пароля пользователя
     */
    private final PasswordEncoder passwordEncoder;

    /**
     * Провайдер для JWT-токена {@link JwtTokenProvider}
     */
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * Сервис для проверки email-адресов через API Hunter.io
     */
    private final EmailValidationService emailValidationService;


    /**
     * Метод для аутентификации пользователя.
     * Возвращает JWT-токен при успешной проверке
     * @param email почта пользователя
     * @param password пароль пользователя
     * @return JWT-токен
     * @throws BadCredentialsException в случае, если имя пользователя или пароль некорректные
     */
    public String login(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BadCredentialsException("Почта не найдена " + email));

        UserCredentials userCredentials = userCredentialsRepository.findByUserId(user.getId())
                .orElseThrow(() -> new BadCredentialsException("Учетные данные не найдены"));

        if (!passwordEncoder.matches(password, userCredentials.getPassword())) {
            throw new BadCredentialsException("Пароль неверный");
        }

        return jwtTokenProvider.generateToken(user.getEmail());
    }


    /**
     * Метод для регистрации нового пользователя с указанными email и паролем
     * <p>
     *      Проверяет, существует ли пользователь с указанным email.
     *      Выполняет валидацию email через внешний сервис {@link EmailValidationService},
     *      использующий API Hunter.io. Создаёт нового пользователя и сохраняет его в базу данных.
     *      Шифрует пароль с помощью {@link PasswordEncoder} и сохраняет учетные данные
     * </p>
     * @param email email нового пользователя
     * @param password пароль нового пользователя
     * @throws UserAlreadyExistsException если пользователь с таким email уже существует
     * @throws InvalidEmailException если email не прошёл валидацию на внешнем сервисе
     */
    public void register(String email, String password) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new UserAlreadyExistsException("Пользователь уже существует: " + email);
        }
        if (!emailValidationService.isEmailValid(email)) {
            throw new InvalidEmailException("Не пройдена проверка email: " + email);
        }
        User user = new User();
        user.setEmail(email);
        userRepository.save(user);

        UserCredentials credentials = new UserCredentials();
        credentials.setUser(user);
        credentials.setPassword(passwordEncoder.encode(password));
        userCredentialsRepository.save(credentials);
    }
}


