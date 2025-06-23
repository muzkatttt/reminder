package com.muzkat.reminder.service;

import com.muzkat.reminder.config.HunterApiProperties;
import com.muzkat.reminder.dto.HunterResponseDTO;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;


/**
 * Сервис для проверки корректности email-адреса с использованием стороннего API Hunter.io.
 * Выполняет HTTP-запрос к {@code https://api.hunter.io} с адресом email и API-ключом,
 * получает результат в виде объекта {@link HunterResponseDTO}.
 * Применяется при регистрации нового пользователя
 */
@Service
@RequiredArgsConstructor
@Getter
public class EmailValidationService {

    /**
     * Компонент для выполнения HTTP-запросов к внешнему API
     */
    @Qualifier("externalRestTemplate")
    private final RestTemplate restTemplate;

    /**
     * Свойства с API-ключом для доступа к сервису Hunter.io
     */
    private final HunterApiProperties hunterApiProperties;


    /**
     * Метод проверяет, является ли указанный email-адрес валидным на внешнем сервисе Hunter.io.
     * @param email email-адрес, который необходимо проверить
     * @return {@code true}, если email прошёл проверку (валиден по данным API), иначе {@code false}
     */
    public boolean isEmailValid(String email) {
        String url = UriComponentsBuilder.fromUriString(hunterApiProperties.getUrl())
                .queryParam("email", email)
                .queryParam("api_key", hunterApiProperties.getKey())
                .toUriString();

        HunterResponseDTO response = restTemplate.getForObject(url, HunterResponseDTO.class);

        return response.getData() != null && response.getData().isValid();
    }
}
