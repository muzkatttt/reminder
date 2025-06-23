package com.muzkat.reminder.service;

import com.muzkat.reminder.dto.RemindDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * Сервис для получения напоминаний из внешнего API.
 * Использует {@link RestTemplate} без авторизации (JWT не добавляется)
 */
@Service
@RequiredArgsConstructor
public class ExternalRemindService {

    @Qualifier("externalRestTemplate")
    private final RestTemplate restTemplate;


    /**
     * URL внешнего сервиса, откуда запрашиваются напоминания
     */
    @Value("${external.remind.url}")
    private String fetchUrl;


    /**
     * Выполняет GET-запрос ко внешнему API для получения массива напоминаний
     * @return массив DTO с напоминаниями
     */
    public RemindDTO[] fetchAllReminds(){
        return restTemplate.getForObject(fetchUrl, RemindDTO[].class);
    }
}


