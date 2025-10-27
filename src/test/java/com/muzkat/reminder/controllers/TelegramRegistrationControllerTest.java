package com.muzkat.reminder.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.muzkat.reminder.dto.TelegramUpdateDTO;
import com.muzkat.reminder.model.User;
import com.muzkat.reminder.repository.UserRepository;
import com.muzkat.reminder.security.JwtTokenProvider;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
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

import java.util.Optional;

@WebMvcTest(controllers = TelegramRegistrationController.class)
@AutoConfigureMockMvc
class TelegramRegistrationControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UserRepository userRepository;

    @MockitoBean
    private JwtTokenProvider jwtTokenProvider;

    @Test
    @WithMockUser(username = "anyContactAlice@gmail.com")
    void registrationUserByTelegram_ifUserExists_return200() throws Exception {
        String email = "anyContactAlice@gmail.com";
        Long chatId = 12345678L;
        User user = new User();
        user.setId(1L);
        user.setEmail(email);

        String body = """
          {
            "message": {
              "chat": { "id": 12345678 },
              "text": "anyContactAlice@gmail.com"
            }
          }
        """;

        Mockito.when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        Mockito.when(userRepository.save(Mockito.any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/telegram/webhook")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
               .andExpect(MockMvcResultMatchers.status().isOk())
               .andExpect(MockMvcResultMatchers.content().string("Telegram добавлен к учетной записи"));

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        Mockito.verify(userRepository).findByEmail(email);
        Mockito.verify(userRepository).save(captor.capture());
        Assertions.assertThat(captor.getValue().getTelegramChatId()).isEqualTo(chatId.toString());
        Mockito.verifyNoMoreInteractions(userRepository);
    }
}