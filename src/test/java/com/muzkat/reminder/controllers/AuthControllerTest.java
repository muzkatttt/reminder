package com.muzkat.reminder.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.muzkat.reminder.exception.UserAlreadyExistsException;
import com.muzkat.reminder.security.JwtTokenProvider;
import com.muzkat.reminder.service.AuthService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;


@WebMvcTest(controllers = AuthController.class)
class AuthControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockitoBean
    private AuthService authServiceMock;

    @MockitoBean
    private JwtTokenProvider jwtTokenProvider;

    @Test
    @WithMockUser(username = "anyContactAlice@gmail.com")
    void login_success() throws Exception {
        String email = "anyContactAlice@gmail.com";
        String password = "password";
        String token = "jsonWebTokenHeaderPlayLoaderSignature";

        Mockito.when(authServiceMock.login(ArgumentMatchers.anyString(), ArgumentMatchers.anyString())).thenReturn(token);
        Mockito.when(jwtTokenProvider.generateToken(ArgumentMatchers.anyString())).thenReturn(token);

        String body = """
                    {
                    "email":"%s",
                    "password":"%s"
                    }
                """.formatted(email, password);

        mockMvc.perform(post("/auth/login")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.token").value(token));

        Mockito.verify(authServiceMock).login(email, password);
        Mockito.verifyNoMoreInteractions(authServiceMock);
    }

    @Test
    @WithMockUser(username = "anyContactAlice@gmail.com")
    void login_badCredentials_return401() throws Exception {
        String email = "badCredentials@gmail.com";
        String password = "badPassword";

        Mockito.when(authServiceMock.login(email, password))
                .thenThrow(new BadCredentialsException("Bad credentials exception"));

        String body = """
                {
                    "email":"%s",
                    "password":"%s"
                }
                """.formatted(email, password);

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/login")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());

        Mockito.verify(authServiceMock).login(email, password);
        Mockito.verifyNoMoreInteractions(authServiceMock);
    }

    @Test
    @WithMockUser(username = "anyContactAlice@gmail.com")
    void register_success_returnMessage() throws Exception {
        String email = "newContact@gmail.com";
        String password = "newPassword";

        Mockito.doNothing().when(authServiceMock).register(email, password);

        String body = """
                {
                    "email":"%s",
                    "password":"%s"
                }
                """.formatted(email, password);

         mockMvc.perform(MockMvcRequestBuilders.post("/auth/register")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Пользователь успешно зарегистрирован"));

        Mockito.verify(authServiceMock).register(email, password);
        Mockito.verifyNoMoreInteractions(authServiceMock);
    }

    @Test
    @WithMockUser(username = "anyContactAlice@gmail.com")
    void register_userAlreadyExists_returns400() throws Exception {
        String email = "anyContactAlice@gmail.com";
        String password = "password";

        Mockito.doThrow(new UserAlreadyExistsException("Произошла ошибка при регистрации! Пользователь уже зарегистирован в системе"))
                .when(authServiceMock).register(eq(email), eq(password));

        String body = """
                  {
                    "email":"%s",
                    "password":"%s"
                  }
                """.formatted(email, password);

        mockMvc.perform(post("/auth/register")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(body))
                .andExpect(MockMvcResultMatchers.status().isConflict());

        Mockito.verify(authServiceMock).register(email, password);
        Mockito.verifyNoMoreInteractions(authServiceMock);
    }
}


