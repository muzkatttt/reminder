//
//@ActiveProfiles("test")
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@AutoConfigureMockMvc
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
//class RemindControllerCreateUpdateTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @MockitoBean
//    private RemindService remindServiceMock;
//
//    @Autowired
//    private RemindRepository remindRepository;
//
//    @BeforeEach
//    void setupUser() {
//        User user = new User();
//        user.setName("Alice");
//        user.setEmail("anyContactAlice@gmail.com");
//        userRepository.save(user);
//    }
//    @Test
//    void createRemind() {
//    }
//
//    @Transactional
//    @Rollback
//    @Test
//    @WithMockUser(username = "anyContactAlice@gmail.com")
//    void createRemind_userEmailIsValid_returnStatusIsCreated() throws Exception {
//        User user = userRepository.findByEmail("anyContactAlice@gmail.com")
//                .orElseThrow(() -> new IllegalStateException("Тестовый пользователь не найден"));
//
//        RemindDTO remindDTO = new RemindDTO();
//        remindDTO.setId(1L);
//        remindDTO.setTitle("Краткое описание");
//        remindDTO.setDescription("Полное описание");
//        remindDTO.setDateOfRemind(LocalDate.now().plusDays(1));
//        remindDTO.setTimeOfRemind(LocalTime.now().plusHours(5));
//        remindDTO.setUserId(1L);
//
//        Mockito.when(remindServiceMock.createRemind(
//                Mockito.any(RemindDTO.class), Mockito.any(User.class))).thenReturn(remindDTO);
//
//        mockMvc.perform(MockMvcRequestBuilders.post("/api/remind/create")
//                        .with(SecurityMockMvcRequestPostProcessors.csrf())
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(remindDTO)))
//                .andExpect(MockMvcResultMatchers.status().isCreated());
//    }
//
//    @Test
//    void updateRemindByTitle() {
//    }
//    @Test
//    @Transactional
//    @WithMockUser(username = "anyContactAlice@gmail.com")
//    void updateRemindById_ifRemindFound_shouldReturn200AndUpdatedDto() throws Exception {
//        User testUser = userRepository.findByEmail("anyContactAlice@gmail.com").orElseThrow();
//
//        LocalDate createDate = LocalDate.now().plusDays(1);
//        LocalTime createTime = LocalTime.of(12, 0, 0);
//
//
//        RemindDTO oldRemind = new RemindDTO();
//        oldRemind.setId(1L);
//        oldRemind.setTitle("Старое краткое описание");
//        oldRemind.setDescription("Старое полное описание");
//        oldRemind.setDateOfRemind(createDate);
//        oldRemind.setTimeOfRemind(createTime);
//        oldRemind.setUserId(testUser.getId());
//
//        String testResponse = mockMvc.perform(MockMvcRequestBuilders.post("/api/remind/create")
//                        .with(SecurityMockMvcRequestPostProcessors.csrf())
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(oldRemind)))
//                .andDo(MockMvcResultHandlers.print())
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Старое краткое описание"))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("Старое полное описание"))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("anyContactAlice@gmail.com"))
//                .andReturn()
//                .getResponse()
//                .getContentAsString();
//
//        RemindDTO createdRemindDto = objectMapper.readValue(testResponse, RemindDTO.class);
//        createdRemindDto.setUserId(testUser.getId());
//
//        LocalDate newDate = LocalDate.now().plusDays(2);
//        LocalTime newTime = LocalTime.of(13, 10, 0);
//        RemindDTO newRemind = new RemindDTO();
//        newRemind.setId(createdRemindDto.getId());
//        newRemind.setTitle("Новое краткое описание");
//        newRemind.setDescription("Новое полное описание");
//        newRemind.setDateOfRemind(newDate);
//        newRemind.setTimeOfRemind(newTime);
//        //newRemind.setUserId(testUser.getId());
//
//
//        mockMvc.perform(MockMvcRequestBuilders.put("/api/remind/{id}", createdRemindDto.getId())
//                        .with(SecurityMockMvcRequestPostProcessors.csrf())
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(newRemind)))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.id")
//                        .value(newRemind.getId()))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.title")
//                        .value("Новое краткое описание"))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.description")
//                        .value("Новое полное описание"))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.dateOfRemind")
//                        .value(newDate.toString()))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.timeOfRemind")
//                        .value(newTime.toString()));
//
//    }


//    @Test
//    @WithMockUser(username = "alice@example.com")
//    void updateRemind_whenFound_returns200AndUpdatedDto() throws Exception {
//        User user = userRepository.findByEmail("alice@example.com").orElseThrow();
//
//        // создаём напоминание в БД
//        Remind remind = new Remind();
//        remind.setShortDescription("старое описание");
//        remind.setFullDescription("старое полное");
//        remind.setReminderDateTime(LocalDateTime.of(2025, 9, 1, 10, 0));
//        remind.setUser(user);
//        remind = remindRepository.save(remind);
//
//        // dto для обновления
//        RemindDTO dto = new RemindDTO();
//        dto.setTitle("новое краткое");
//        dto.setDescription("новое полное");
//        dto.setDateOfRemind(LocalDate.of(2025, 9, 2));
//        dto.setTimeOfRemind(LocalTime.of(12, 30));
//        dto.setUserId(user.getId());
//
//        mockMvc.perform(put("/api/remind/{id}", remind.getId())
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(dto)))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value(remind.getId()))
//                .andExpect(jsonPath("$.title").value("новое краткое"))
//                .andExpect(jsonPath("$.description").value("новое полное"))
//                .andExpect(jsonPath("$.dateOfRemind").value("2025-09-02"))
//                .andExpect(jsonPath("$.timeOfRemind").value("12:30:00"));
//    }
    //
    //    @Test
    //    @WithMockUser(username = "alice@example.com")
    //    void updateRemind_whenNotFound_returns404() throws Exception {
    //        User user = userRepository.findByEmail("alice@example.com").orElseThrow();
    //
    //        RemindDTO dto = new RemindDTO();
    //        dto.setTitle("новое краткое");
    //        dto.setDescription("новое полное");
    //        dto.setDateOfRemind(LocalDate.of(2025, 9, 2));
    //        dto.setTimeOfRemind(LocalTime.of(12, 30));
    //        dto.setUserId(user.getId());
    //
    //        // отправляем PUT на несуществующий id
    //        mockMvc.perform(put("/api/remind/{id}", 9999L)
    //                        .contentType(MediaType.APPLICATION_JSON)
    //                        .content(objectMapper.writeValueAsString(dto)))
    //                .andDo(print())
    //                .andExpect(status().isNotFound());
    //    }
    //}
//    @Test
//    void updateRemindById() {
//    }
//}