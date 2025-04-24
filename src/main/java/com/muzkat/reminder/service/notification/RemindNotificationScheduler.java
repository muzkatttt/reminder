package com.muzkat.reminder.service.notification;

import com.muzkat.reminder.model.Remind;
import com.muzkat.reminder.repository.RemindRepository;
import com.muzkat.reminder.service.RemindService;
import com.muzkat.reminder.service.TelegramService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Планировщик задач для автоматической отправки напоминаний по электронной почте
 * <p>
 *     Периодически проверяет базу данных на наличие просроченных и неотправленных напоминаний
 *     и инициирует их отправку через {@link RemindService}
 * </p>
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class RemindNotificationScheduler {

    /**
     * Поле экземпляр {@link RemindRepository}
     */
    private final RemindRepository remindRepository;

    /**
     * Поле экземпляр {@link RemindService}
     */
    private final RemindService remindService;

    /**
     * Поле экземпляр {@link TelegramService}
     */
    private final TelegramService telegramService;


    /**
     * Метод запускается по расписанию, проверяет напоминания,
     * дата которых уже наступила и которые ещё не были отправлены (notified = false).
     * Отправляет напоминания и логирует результат
     */
    @Scheduled(fixedRateString = "${reminder.scheduler.interval-ms}")
    public void checkDateTimeAndSendRemind(){
        LocalDateTime now = LocalDateTime.now();
        List<Remind> findRemind = remindRepository.findByDateTimeOfRemindBeforeAndNotifiedFalse(now);
        log.info("Найдено {} напоминаний для отправки", findRemind.size());

        for (Remind remind : findRemind){
            try{
                remindService.sendRemindById(remind.getRemindId());
                String message = "Напоминание: *" + remind.getTitle() + "*\n\n" +
                                 remind.getDescription() + "\n" +
                                 remind.getDateTimeOfRemind().
                                         format(DateTimeFormatter.ofPattern("\n*время начала* HH:mm\n*дата* dd-MM-yyyy"));
                telegramService.sendMessage(message);
                log.info("Напоминание id {}: отправлены уведомления в Telеgram и на почту пользователя", remind.getRemindId());

            } catch (Exception e) {
                log.warn("Ошибка при отправке напоминания id {}: {}", remind.getRemindId(), e.getMessage());
            }
        }
    }
}
