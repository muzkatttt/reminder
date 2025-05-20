package com.muzkat.reminder.config;

import com.muzkat.reminder.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


/**
 * Конфигурационный класс безопасности приложения.
 * Настраивает фильтры безопасности, обработку сессий, CSRF-защиту,
 * а также интеграцию с фильтром JWT-аутентификации.
 * <p>
 *     Использует Stateless-аутентификацию (JWT), отключает форму логина и Basic Auth.
 * </p>
 */
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    /**
     * Поле фильтр проверки JWT-токена в каждом запросе
     */
    private final JwtAuthenticationFilter jwtAuthenticationFilter;


    /**
     * Метод настройки цепочки фильтров безопасности
     * <p>
     *     Отключает CSRF, Basic Auth и форму логина.
     *     Включает stateless-аутентификацию через JWT.
     *     Разрешает доступ к /auth/login, /auth/register без авторизации.
     *     Все остальные запросы требуют валидного токена.
     * </p>
     * <p>
     * Конфигурация безопасности:
     * <ul>
     *     <li><b> csrf().disable()</b> — отключает CSRF-защиту (актуально для REST API)</li>
     *     <li><b>authorizeHttpRequests(...)</b> — настраивает доступ к маршрутам:
     *         <ul>
     *             <li><b>/auth/login</b> и <b>/auth/register</b> доступны без токена</li>
     *             <li>все остальные запросы требуют авторизации (валидного JWT)</li>
     *         </ul>
     *     </li>
     *     <li><b>sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)</b>
     *     — отключает хранение сессий на сервере (stateless-режим)</li>
     *     <li><b>httpBasic().disable()</b> — отключает базовую авторизацию</li>
     *     <li><b>formLogin().disable()</b> — отключает HTML-форму логина</li>
     *     <li><b>addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)</b>
     *     — добавляет фильтр JWT-аутентификации до стандартного фильтра логина</li>
     * </ul>
     * </p>
     * @param http объект конфигурации безопасности
     * @return цепочка фильтров безопасности
     * @throws Exception в случае ошибки конфигурации
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf()
                .disable()
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/login", "/auth/register").permitAll()
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }


    /**
     * Бин для шифрования паролей пользователей
     * @return экземпляр BCryptPasswordEncoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}