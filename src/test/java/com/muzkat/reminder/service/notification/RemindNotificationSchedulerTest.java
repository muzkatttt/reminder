package com.muzkat.reminder.service.notification;

import com.muzkat.reminder.model.Remind;
import com.muzkat.reminder.repository.RemindRepository;
import com.muzkat.reminder.service.RemindService;
import com.muzkat.reminder.service.TelegramService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@DisplayName("Тесты для Remind Notification Scheduler")
@ExtendWith(MockitoExtension.class)
class RemindNotificationSchedulerTest {
    @Mock
    RemindRepository remindRepository;

    @Mock
    RemindService remindService;

    @Mock
    TelegramService telegramService;

    @InjectMocks
    RemindNotificationScheduler remindNotificationScheduler;

    @DisplayName("Проверяет, что метод ищет сообщение и отправляет его при помощи планировщика")
    @Test
    void checkDateTimeAndSendRemind_shouldSendMessage() {

        Remind remind = new Remind();
        remind.setRemindId(1L);
        remind.setTitle("Текст краткого описания");
        remind.setDescription("Полное описание тестового напоминания");
        remind.setDateTimeOfRemind(LocalDateTime.of(LocalDate.now(), LocalTime.now().minusMinutes(50)));
        remind.setUserId(1L);
        remind.setNotified(false);

        when(remindRepository.findByDateTimeOfRemindBeforeAndNotifiedFalse(any()))
                .thenReturn(List.of(remind));

        remindNotificationScheduler.checkDateTimeAndSendRemind();

        verify(remindService).sendRemindById(1L);
        verify(telegramService).sendMessage(contains("Текст краткого описания"));
    }
}
