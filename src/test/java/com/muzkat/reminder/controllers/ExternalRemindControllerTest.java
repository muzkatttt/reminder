package com.muzkat.reminder.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.muzkat.reminder.dto.RemindDTO;
import com.muzkat.reminder.security.JwtTokenProvider;
import com.muzkat.reminder.service.ExternalRemindService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(controllers = ExternalRemindController.class)
@AutoConfigureMockMvc
class ExternalRemindControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ExternalRemindService externalRemindService;

    @MockitoBean
    private JwtTokenProvider jwtTokenProvider;

    @Test
    @WithMockUser(username = "anyContactAlice@gmail.com")
    void getExternalReminds_ifExists_returnListOfReminds200() throws Exception {
        RemindDTO r1 = new RemindDTO(); r1.setId(1L); r1.setTitle("Первое напоминание");
        RemindDTO r2 = new RemindDTO(); r2.setId(2L); r2.setTitle("Второе напоминание");
        RemindDTO[] expected = { r1, r2 };

        Mockito.when(externalRemindService.fetchAllReminds()).thenReturn(expected);

        mockMvc.perform(MockMvcRequestBuilders.get("/external/reminds"))
               .andExpect(MockMvcResultMatchers.status().isOk())
               .andExpect(MockMvcResultMatchers.content()
                        .json(objectMapper.writeValueAsString(expected)));

        Mockito.verify(externalRemindService).fetchAllReminds();
        Mockito.verifyNoMoreInteractions(externalRemindService);
    }
}
