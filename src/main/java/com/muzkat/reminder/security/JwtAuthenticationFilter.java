package com.muzkat.reminder.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;

/**
 * Класс-фильтр аутентификации JsonWebToken (JWT), который перехватывает входящие HTTP-запросы,
 * извлекает JWT из заголовка Authorization, валидирует его и устанавливает
 * соответствующую аутентификацию в контексте безопасности приложения Spring.
 * <p>
 *     Этот фильтр обеспечивает, что каждый запрос обрабатывается только один раз
 *     благодаря расширению {@link OncePerRequestFilter}.
 * </p>
 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    /**
     * Поле компонент для обработки и валидации JWT-токенов.
     * Используется для извлечения информации о пользователе из токена
     * и проверки его подлинности
     */
    private final JwtTokenProvider provider;


    /**
     * Метод обрабатывает каждый входящий HTTP-запрос, извлекая JWT из заголовка Authorization,
     * проверяет его действительность и устанавливает аутентификацию в контексте безопасности Spring.
     * <p>
     *     Если токен действителен, извлекается адрес электронной почты пользователя, создаётся объект
     *     {@link UsernamePasswordAuthenticationToken}, который устанавливается в {@link SecurityContextHolder}.
     *     Затем запрос передаётся дальше по цепочке фильтров
     * </p>
     * @param request текущий HTTP-запрос
     * @param response текущий HTTP-ответ
     * @param filterChain цепочка фильтров для продолжения обработки запроса
     * @throws ServletException в случае ошибок сервлета
     * @throws IOException в случае ошибок ввода-вывода
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String token = getTokenFromRequest(request);

        writeTokenToFile(token);

        if (token != null && provider.validateToken(token)) {
            String email = provider.getEmailFromToken(token);

            List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_USER"));
            UsernamePasswordAuthenticationToken auth =
                    new UsernamePasswordAuthenticationToken(email, null, authorities);
            SecurityContextHolder.getContext().setAuthentication(auth);
        }

        filterChain.doFilter(request, response);
    }


    /**
     * Метод извлекает JWT из заголовка Authorization-запроса
     * @param request текущий HTTP-запрос
     * @return JWT-строка без префикса "Bearer ", или null,
     * если заголовок отсутствует или некорректен
     */
    private String getTokenFromRequest(HttpServletRequest request) {
        String bearer = request.getHeader("Authorization");
        if (bearer != null && bearer.startsWith("Bearer ")) {
            return bearer.substring(7);
        }
        return null;
    }


    /**
     * Метод записывает предоставленный JWT-токен в текстовый файл для целей аудита или отладки.
     * <p>
     *     Токен добавляется в конец файла, указанного в переменной <b>logFilePath</b>.
     *     Если токен равен null, метод завершает выполнение без записи.
     * </p>
     * @param token JWT-токен, который записывается в файл.
     *              В случае, если null, запись не производится
     * @throws IOException если возникает ошибка при создании или записи в файл
     */
    private void writeTokenToFile(String token) throws IOException {
        if (token == null) return;
        Path logFilePath = Paths.get("logs/token.log");
        Files.createDirectories(logFilePath.getParent());

        String logEntry = "Token: " + token + System.lineSeparator();
        Files.writeString(logFilePath, logEntry, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
    }
}

