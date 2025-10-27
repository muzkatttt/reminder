package com.muzkat.reminder.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.muzkat.reminder.dto.EmailResponseDTO;
import com.muzkat.reminder.security.JwtAuthenticationFilter;
import com.muzkat.reminder.security.JwtTokenProvider;
import com.muzkat.reminder.service.RemindService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.NoSuchElementException;

@WebMvcTest(controllers = EmailController.class)
@AutoConfigureMockMvc
class EmailControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    private RemindService remindService;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private JwtTokenProvider jwtTokenProvider;

    @Test
    @WithMockUser(username = "anyContactAlice@gmail.com")
    void sendRemindToEmail_ifUserExists_return200() throws Exception {
        long remindId = 1L;
        String status = "SENT";

        EmailResponseDTO emailResponseDTO = new EmailResponseDTO();
        emailResponseDTO.setId(remindId);
        emailResponseDTO.setStatus(status);
        emailResponseDTO.setDateOfRemind(LocalDate.of(2025, 9, 10));
        emailResponseDTO.setTimeOfRemind(LocalTime.of(10, 0, 0));

        Mockito.when(remindService.sendRemindById(remindId)).thenReturn(emailResponseDTO);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/email/send/{remindId}", remindId)
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));


        Mockito.verify(remindService).sendRemindById(remindId);
        Mockito.verifyNoMoreInteractions(remindService);
    }

    @Test
    @WithMockUser(username = "anyContactAlice@gmail.com")
    void sendRemindToEmail_ifUserNotFound_return404() throws Exception {
        long remindId = 11111L;

        Mockito.when(remindService.sendRemindById(remindId)).thenThrow(
                new NoSuchElementException("Не найден пользователь для напоминания: " + remindId));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/email/send/{remindId}", remindId)
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

        Mockito.verify(remindService).sendRemindById(remindId);
        Mockito.verifyNoMoreInteractions(remindService);
    }
}