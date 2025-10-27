package com.muzkat.reminder.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.muzkat.reminder.dto.RemindDTO;
import com.muzkat.reminder.model.User;
import com.muzkat.reminder.repository.RemindRepository;
import com.muzkat.reminder.repository.UserRepository;
import com.muzkat.reminder.service.RemindService;
import jakarta.persistence.EntityNotFoundException;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class RemindControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @MockitoBean
    private RemindService remindServiceMock;

    @BeforeEach
    void setupUser() {
        User user = new User();
        user.setName("Alice");
        user.setEmail("anyContactAlice@gmail.com");
        userRepository.save(user);
    }

    @Test
    @Transactional
    @WithMockUser(username = "anyContactAlice@gmail.com")
    void findById_ifRemindFound_returnRemindDto() throws Exception {
        RemindDTO remindDTO = new RemindDTO();
        remindDTO.setId(1L);
        remindDTO.setTitle("Краткое описание");
        remindDTO.setDescription("Полное описание");
        remindDTO.setDateOfRemind(LocalDate.of(2025, 9, 10));
        remindDTO.setTimeOfRemind(LocalTime.of(9, 0, 0));
        remindDTO.setUserId(1L);

        Mockito.when(remindServiceMock.findRemindById(1L)).thenReturn(Optional.of(remindDTO));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/remind/by-id/{id}", 1L))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Краткое описание"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("Полное описание"));
    }

    @Test
    @WithMockUser(username = "anyContactAlice@gmail.com")
    void findById_ifRemindNotFound_return404() throws Exception {
        Long id = 100L;

        Mockito.when(remindServiceMock.findRemindById(Mockito.any())).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/remind/by-id/{id}", id))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @Transactional
    @WithMockUser(username = "anyContactAlice@gmail.com")
    void findByTitle_ifRemindFound_returnRemindDto() throws Exception {
        RemindDTO remindDTO = new RemindDTO();
        remindDTO.setId(1L);
        remindDTO.setTitle("Краткое описание");
        remindDTO.setDescription("Полное описание");
        remindDTO.setDateOfRemind(LocalDate.of(2025, 9, 10));
        remindDTO.setTimeOfRemind(LocalTime.of(9, 0, 0));
        remindDTO.setUserId(1L);

        Mockito.when(remindServiceMock.findRemindByTitle("Краткое описание")).thenReturn(Optional.of(remindDTO));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/remind/by-title/{title}", "Краткое описание"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Краткое описание"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("Полное описание"));
    }

    @Test
    @WithMockUser(username = "anyContactAlice@gmail.com")
    void findByTitle_ifRemindNotFound_return404() throws Exception {
        String title = "Напоминания с таким кратким описанием нет";

        Mockito.when(remindServiceMock.findRemindByTitle(title)).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/remind/by-title/{title}", title))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @Transactional
    @WithMockUser(username = "anyContactAlice@gmail.com")
    void findByDescription_ifRemindFound_returnRemindDto() throws Exception {
        RemindDTO remindDTO = new RemindDTO();
        remindDTO.setId(1L);
        remindDTO.setTitle("Краткое описание");
        remindDTO.setDescription("Полное описание");
        remindDTO.setDateOfRemind(LocalDate.of(2025, 9, 10));
        remindDTO.setTimeOfRemind(LocalTime.of(9, 0, 0));
        remindDTO.setUserId(1L);

        Mockito.when(remindServiceMock.findRemindByDescription("Полное описание")).thenReturn(Optional.of(remindDTO));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/remind/by-description/{description}", "Полное описание"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Краткое описание"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("Полное описание"));
    }

    @Test
    @WithMockUser(username = "anyContactAlice@gmail.com")
    void findByDescription_ifRemindNotFound_return404() throws Exception {
        String description = "Напоминания с таким описанием нет";

        Mockito.when(remindServiceMock.findRemindByDescription(description)).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/remind/by-description/{description}", description))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @WithMockUser(username = "unknown@example.com")
    void testCreateRemind_UserNotFound_Returns500() throws Exception {
        RemindDTO remindDTO = new RemindDTO();
        remindDTO.setTitle("Краткое описание");
        remindDTO.setDescription("Полное описание");
        remindDTO.setDateOfRemind(LocalDate.of(2025, 9, 10));
        remindDTO.setTimeOfRemind(LocalTime.of(9, 0, 0));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/remind/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(remindDTO)))
                .andExpect(MockMvcResultMatchers.status().is5xxServerError())
                .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString("Внутренняя ошибка сервера")));
    }

    @Test
    @WithMockUser(username = "anyContactAlice@gmail.com")
    void deleteRemind_IfRemindExists_shouldReturnNoContent() throws Exception {
        Long id = 1L;

        Mockito.when(remindServiceMock.deleteRemind(id)).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/remind/{id}", 1L))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        Mockito.verify(remindServiceMock, Mockito.times(1)).deleteRemind(id);

    }

    @Test
    @WithMockUser(username = "anyContactAlice@gmail.com")
    void deleteRemind_ifRemindNotExists_shouldReturnNotFound() throws Exception {
        long id = 1L;

        Mockito.doThrow(new EntityNotFoundException("Remind not found: " + id))
                .when(remindServiceMock).deleteRemind(id);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/remind/{id}", id))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

        Mockito.verify(remindServiceMock, Mockito.times(1)).deleteRemind(id);
    }

    @Test
    @WithMockUser(username = "anyContactAlice@gmail.com")
    void getAllReminds_ifRemindsExists_returnAllReminds() throws Exception {
        RemindDTO remind1 = new RemindDTO();
        remind1.setId(1L);
        remind1.setTitle("1");
        remind1.setDescription("1 описание");
        remind1.setDateOfRemind(LocalDate.of(2025, 9, 10));
        remind1.setTimeOfRemind(LocalTime.of(9, 0, 0));;
        remind1.setUserId(1L);

        RemindDTO remind2 = new RemindDTO();
        remind2.setId(2L);
        remind2.setTitle("2");
        remind2.setDescription("2 описание");
        remind2.setDateOfRemind(LocalDate.of(2025, 9, 11));
        remind2.setTimeOfRemind(LocalTime.of(11, 0, 0));
        remind2.setUserId(1L);

        RemindDTO remind3 = new RemindDTO();
        remind3.setId(3L);
        remind3.setTitle("3");
        remind3.setDescription("3 описание");
        remind3.setDateOfRemind(LocalDate.of(2025, 9, 12));
        remind3.setTimeOfRemind(LocalTime.of(10, 0, 0));
        remind3.setUserId(1L);

        List<RemindDTO> resultList = List.of(remind1, remind2, remind3);
        Mockito.when(remindServiceMock.getAllReminds()).thenReturn(resultList);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/remind/all"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(2L))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].id").value(3L));
    }

    @Test
    @WithMockUser(username = "anyContactAlice@gmail.com")
    void filterReminds_allRemindFound_returnListOfReminds() throws Exception {
        RemindDTO remind1 = new RemindDTO();
        remind1.setId(1L);
        remind1.setTitle("1");
        remind1.setDescription("1 описание");
        remind1.setDateOfRemind(LocalDate.of(2025, 9, 10));
        remind1.setTimeOfRemind(LocalTime.of(9, 0, 0));
        remind1.setUserId(1L);

        RemindDTO remind2 = new RemindDTO();
        remind2.setId(2L);
        remind2.setTitle("2");
        remind2.setDescription("2 описание");
        remind2.setDateOfRemind(LocalDate.of(2025, 9, 11));
        remind2.setTimeOfRemind(LocalTime.of(9, 5, 5));
        remind2.setUserId(1L);

        RemindDTO remind3 = new RemindDTO();
        remind3.setId(3L);
        remind3.setTitle("3");
        remind3.setDescription("3 описание");
        remind3.setDateOfRemind(LocalDate.of(2025, 9, 11));
        remind3.setTimeOfRemind(LocalTime.of(10, 5, 5));
        remind3.setUserId(1L);

        List<RemindDTO> sorted = List.of(remind3);

        String filterTitle = "3";
        LocalDate filterDate = LocalDate.of(2025, 9, 11);
        LocalTime filterTime = LocalTime.of(10, 5, 5);


        Mockito.when(remindServiceMock.filterReminds(filterTitle, filterDate, filterTime))
                .thenReturn(sorted);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/remind/filter")
                        .param("title", filterTitle)
                        .param("date", "2025-09-11")
                        .param("time", "10:05:05")
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(3))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].title").value("3"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].dateOfRemind").value("2025-09-11"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].timeOfRemind").value("10:05:05"));

        Mockito.verify(remindServiceMock, Mockito.times(1))
                .filterReminds(filterTitle, filterDate, filterTime);

        Mockito.verifyNoMoreInteractions(remindServiceMock);
    }

    @Test
    @WithMockUser(username = "anyContactAlice@gmail.com")
    void filterReminds_allParamsNull_returnEmptyList() throws Exception {
        Mockito.when(remindServiceMock.filterReminds(null, null, null))
                .thenReturn(List.of());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/remind/filter"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(0));

        Mockito.verify(remindServiceMock, Mockito.times(1))
                .filterReminds(null, null, null);

        Mockito.verifyNoMoreInteractions(remindServiceMock);
    }

    @Test
@WithMockUser(username = "anyContactAlice@gmail.com")
    void filterReminds_paramOnlyTitle_returnByTitle() throws Exception {
        RemindDTO remind = new RemindDTO();
        remind.setId(3L);
        remind.setTitle("3");
        remind.setDescription("3 описание");
        remind.setDateOfRemind(LocalDate.of(2025, 9, 11));
        remind.setTimeOfRemind(LocalTime.of(10, 5, 5));
        remind.setUserId(1L);

        String filterTitle = "3";
        Mockito.when(remindServiceMock.filterReminds(filterTitle, null, null))
                .thenReturn(List.of(remind));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/remind/filter")
                        .param("title", filterTitle))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].title").value(filterTitle));

        Mockito.verify(remindServiceMock).filterReminds(filterTitle, null, null);
        Mockito.verifyNoMoreInteractions(remindServiceMock);
    }

    @Test
    @WithMockUser(username = "anyContactAlice@gmail.com")
    void filterReminds_paramOnlyDate_returnByDate() throws Exception {
        RemindDTO remind = new RemindDTO();
        remind.setId(3L);
        remind.setTitle("3");
        remind.setDescription("3 описание");
        remind.setDateOfRemind(LocalDate.of(2025, 9, 11));
        remind.setTimeOfRemind(LocalTime.of(10, 5, 5));
        remind.setUserId(1L);

        LocalDate filterDate = LocalDate.of(2025, 9, 11);

        Mockito.when(remindServiceMock.filterReminds(null, filterDate, null)).thenReturn(List.of(remind));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/remind/filter")
                        .param("date", "2025-09-11"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].dateOfRemind").value("2025-09-11"));

        Mockito.verify(remindServiceMock).filterReminds(null, filterDate, null);

        Mockito.verifyNoMoreInteractions(remindServiceMock);
    }
    @Test
    @WithMockUser(username = "anyContactAlice@gmail.com")
    void filterReminds_paramOnlyTime_returnByTime() throws Exception {
        RemindDTO remind = new RemindDTO();
        remind.setId(3L);
        remind.setTitle("3");
        remind.setDescription("3 описание");
        remind.setDateOfRemind(LocalDate.of(2025, 9, 11));
        remind.setTimeOfRemind(LocalTime.of(10, 5, 5));
        remind.setUserId(1L);

        LocalTime filterTime = LocalTime.of(10, 5, 5);

        Mockito.when(remindServiceMock.filterReminds(null, null, filterTime))
                .thenReturn(List.of(remind));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/remind/filter")
                .param("time", "10:05:05"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].timeOfRemind").value("10:05:05"));

        Mockito.verify(remindServiceMock).filterReminds(null, null, filterTime);

        Mockito.verifyNoMoreInteractions(remindServiceMock);
    }

    @Test
    @WithMockUser(username = "anyContactAlice@gmail.com")
    void filterReminds_paramTitleAndDate_returnByTitleAndDate() throws Exception {
        RemindDTO remind = new RemindDTO();
        remind.setId(3L);
        remind.setTitle("3");
        remind.setDescription("3 описание");
        remind.setDateOfRemind(LocalDate.of(2025, 9, 11));
        remind.setTimeOfRemind(LocalTime.of(10, 5, 5));
        remind.setUserId(1L);

        String filterTitle = "3";
        LocalDate filterDate = LocalDate.of(2025, 9, 11);

        Mockito.when(remindServiceMock.filterReminds(filterTitle, filterDate, null))
                .thenReturn(List.of(remind));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/remind/filter")
                .param("title", filterTitle)
                .param("date", "2025-09-11"))
                .andExpect(MockMvcResultMatchers.status().isOk());

        Mockito.verify(remindServiceMock).filterReminds(filterTitle, filterDate, null);

    }
    @Test
    @WithMockUser(username = "anyContactAlice@gmail.com")
    void getSortedReminds_sortedByTitle_returnListOfReminds() throws Exception {
        RemindDTO remind1 = new RemindDTO();
        remind1.setId(1L);
        remind1.setTitle("3");
        remind1.setDescription("3 описание");
        remind1.setDateOfRemind(LocalDate.of(2025, 9, 10));
        remind1.setTimeOfRemind(LocalTime.of(9, 0, 0));
        remind1.setUserId(1L);

        RemindDTO remind2 = new RemindDTO();
        remind2.setId(2L);
        remind2.setTitle("2");
        remind2.setDescription("2 описание");
        remind2.setDateOfRemind(LocalDate.of(2025, 9, 11));
        remind2.setTimeOfRemind(LocalTime.of(9, 5, 5));
        remind2.setUserId(1L);

        RemindDTO remind3 = new RemindDTO();
        remind3.setId(3L);
        remind3.setTitle("1");
        remind3.setDescription("1 описание");
        remind3.setDateOfRemind(LocalDate.of(2025, 9, 11));
        remind3.setTimeOfRemind(LocalTime.of(10, 5, 5));
        remind3.setUserId(1L);

        String sortByTitle = "title";

        Mockito.when(remindServiceMock.getSortedReminds(sortByTitle)).thenReturn(List.of(remind3,remind2, remind1));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/remind/sorted")
                .param("sortBy", sortByTitle))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(3))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].title").value("1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].title").value("2"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].title").value("3"));

        Mockito.verify(remindServiceMock).getSortedReminds(sortByTitle);
    }

    @Test
    @WithMockUser(username = "anyContactAlice@gmail.com")
    void getSortedReminds_sortedByDescription_returnListOfReminds() throws Exception {
        RemindDTO remind1 = new RemindDTO();
        remind1.setId(1L);
        remind1.setTitle("3");
        remind1.setDescription("Б описание");
        remind1.setDateOfRemind(LocalDate.of(2025, 9, 10));
        remind1.setTimeOfRemind(LocalTime.of(9, 0, 0));
        remind1.setUserId(1L);

        RemindDTO remind2 = new RemindDTO();
        remind2.setId(2L);
        remind2.setTitle("2");
        remind2.setDescription("В описание");
        remind2.setDateOfRemind(LocalDate.of(2025, 9, 11));
        remind2.setTimeOfRemind(LocalTime.of(9, 5, 5));
        remind2.setUserId(1L);

        RemindDTO remind3 = new RemindDTO();
        remind3.setId(3L);
        remind3.setTitle("1");
        remind3.setDescription("А описание");
        remind3.setDateOfRemind(LocalDate.of(2025, 9, 12));
        remind3.setTimeOfRemind(LocalTime.of(10, 5, 5));
        remind3.setUserId(1L);

        String sortByDescription = "description";

        Mockito.when(remindServiceMock.getSortedReminds(sortByDescription)).thenReturn(List.of(remind3,remind1, remind2));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/remind/sorted")
                        .param("sortBy", sortByDescription))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(3))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].description").value("А описание"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].description").value("Б описание"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].description").value("В описание"));

        Mockito.verify(remindServiceMock).getSortedReminds(sortByDescription);
    }

    @Test
    @WithMockUser(username = "anyContactAlice@gmail.com")
    void getSortedReminds_sortedByDate_returnListOfReminds() throws Exception {
        RemindDTO remind1 = new RemindDTO();
        remind1.setId(1L);
        remind1.setTitle("3");
        remind1.setDescription("Б описание");
        remind1.setDateOfRemind(LocalDate.of(2025, 9, 10));
        remind1.setTimeOfRemind(LocalTime.of(9, 0, 0));
        remind1.setUserId(1L);

        RemindDTO remind2 = new RemindDTO();
        remind2.setId(2L);
        remind2.setTitle("2");
        remind2.setDescription("В описание");
        remind2.setDateOfRemind(LocalDate.of(2025, 9, 11));
        remind2.setTimeOfRemind(LocalTime.of(9, 5, 5));
        remind2.setUserId(1L);

        RemindDTO remind3 = new RemindDTO();
        remind3.setId(3L);
        remind3.setTitle("1");
        remind3.setDescription("А описание");
        remind3.setDateOfRemind(LocalDate.of(2025, 9, 12));
        remind3.setTimeOfRemind(LocalTime.of(10, 5, 5));
        remind3.setUserId(1L);

        String sortByDate = "date";

        Mockito.when(remindServiceMock.getSortedReminds(sortByDate)).thenReturn(List.of(remind1, remind2, remind3));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/remind/sorted")
                        .param("sortBy", sortByDate))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(3))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].dateOfRemind").value("2025-09-10"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].dateOfRemind").value("2025-09-11"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].dateOfRemind").value("2025-09-12"));

        Mockito.verify(remindServiceMock).getSortedReminds(sortByDate);
    }

    @Test
    @WithMockUser(username = "anyContactAlice@gmail.com")
    void getSortedReminds_sortedByTime_returnListOfReminds() throws Exception {
        RemindDTO remind1 = new RemindDTO();
        remind1.setId(1L);
        remind1.setTitle("3");
        remind1.setDescription("Б описание");
        remind1.setDateOfRemind(LocalDate.of(2025, 9, 10));
        remind1.setTimeOfRemind(LocalTime.of(9, 0, 0));
        remind1.setUserId(1L);

        RemindDTO remind2 = new RemindDTO();
        remind2.setId(2L);
        remind2.setTitle("2");
        remind2.setDescription("В описание");
        remind2.setDateOfRemind(LocalDate.of(2025, 9, 11));
        remind2.setTimeOfRemind(LocalTime.of(19, 5, 5));
        remind2.setUserId(1L);

        RemindDTO remind3 = new RemindDTO();
        remind3.setId(3L);
        remind3.setTitle("1");
        remind3.setDescription("А описание");
        remind3.setDateOfRemind(LocalDate.of(2025, 9, 12));
        remind3.setTimeOfRemind(LocalTime.of(10, 5, 5));
        remind3.setUserId(1L);

        String sortByTime = "time";

        Mockito.when(remindServiceMock.getSortedReminds(sortByTime)).thenReturn(List.of(remind1, remind3, remind2));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/remind/sorted")
                        .param("sortBy", sortByTime))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(3))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].timeOfRemind").value("09:00:00"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].timeOfRemind").value("10:05:05"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].timeOfRemind").value("19:05:05"));

        Mockito.verify(remindServiceMock).getSortedReminds(sortByTime);
    }
}