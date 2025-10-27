package com.muzkat.reminder.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.muzkat.reminder.security.JwtTokenProvider;
import com.muzkat.reminder.service.EmailValidationService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(controllers = EmailValidationController.class)
@AutoConfigureMockMvc
class EmailValidationControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    private EmailValidationService emailValidationService;

    @MockitoBean
    private JwtTokenProvider jwtTokenProvider;

    @Test
    @WithMockUser(username = "anyContactAlice@gmail.com")
    void validateEmail_ifEmailValid_return200() throws Exception {
        String email = "anyContactAlice@gmail.com";

        Mockito.when(emailValidationService.isEmailValid(email)).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/email/validate")
                .param("email", email)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().string("true"));

        Mockito.verify(emailValidationService).isEmailValid(email);
        Mockito.verifyNoMoreInteractions(emailValidationService);
    }

    @Test
    @WithMockUser(username = "anyContactAlice@gmail.com")
    void validateEmail_ifEmailInvalid_return200False() throws Exception {
        String email = "bad-bad-email";

        Mockito.when(emailValidationService.isEmailValid(email)).thenReturn(false);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/email/validate")
                        .param("email", email)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().string("false"));

        Mockito.verify(emailValidationService).isEmailValid(email);
        Mockito.verifyNoMoreInteractions(emailValidationService);
    }
}