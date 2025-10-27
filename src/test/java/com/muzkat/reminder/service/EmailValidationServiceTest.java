package com.muzkat.reminder.service;

import com.muzkat.reminder.config.HunterApiProperties;
import com.muzkat.reminder.dto.HunterResponseDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("Тесты для Email Validation Service")
@ExtendWith(MockitoExtension.class)
class EmailValidationServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private HunterApiProperties hunterApiProperties;

    @InjectMocks
    private EmailValidationService emailValidationService;

    @DisplayName("Проверяет, что сервис EmailValidationService корректно интерпретирует ответ " +
                 "от внешнего API Hunter.io и возвращает true, если email считается валидным " +
                 "по данным от Hunter.io")
    @Test
    void isEmailValid_ApiRespondsWithValidTrue_returnTrue() {

        String email = "contactAlice@gmail.com";
        String testUrl = "https://api.hunter.io/v2/email-verifier";
        String testApiKey = "hunter-key";

        when(hunterApiProperties.getUrl()).thenReturn(testUrl);
        when(hunterApiProperties.getKey()).thenReturn(testApiKey);

        HunterResponseDTO hunterResponseDTO = new HunterResponseDTO();
        HunterResponseDTO.HunterData hunterData = new HunterResponseDTO.HunterData();
        hunterData.setStatus("valid");
        hunterData.setScore(80);
        hunterResponseDTO.setData(hunterData);

        String expectedUrl = UriComponentsBuilder
                .fromUriString(testUrl)
                .queryParam("email", email)
                .queryParam("api_key", testApiKey)
                .toUriString();

        when(restTemplate.getForObject(expectedUrl, HunterResponseDTO.class)).thenReturn(hunterResponseDTO);

        boolean testResult = emailValidationService.isEmailValid(email);

        assertTrue(testResult);

        verify(restTemplate).getForObject(expectedUrl, HunterResponseDTO.class);

    }
}