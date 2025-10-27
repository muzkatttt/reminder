package com.muzkat.reminder.repository;

import com.muzkat.reminder.model.Remind;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Тесты для Remind Repository")
@DataJpaTest
class RemindRepositoryTest {
    @Autowired
    @Mock
    RemindRepository remindRepository;

    @DisplayName("Проверяет, что метод возвращает напоминание по краткому описанию")
    @Test
    void findByTitle_remindExists_returnRemind() {
        Remind remind = new Remind();
        remind.setTitle("Текст краткого описания");
        remind.setDescription("Полное описание тестового напоминания");
        remind.setDateTimeOfRemind(LocalDateTime.of(LocalDate.now(), LocalTime.of(10, 0)));
        remind.setUserId(1L);
        remindRepository.save(remind);

        List<Remind> result = remindRepository.findByTitle("Текст краткого описания");

        assertThat(result).isNotEmpty();
        assertThat(result.get(0).getTitle()).isEqualTo("Текст краткого описания");
    }

    @DisplayName("Проверяет, что метод возвращает напоминание по полному описанию")
    @Test
    void findByDescription_remindExists_returnRemind() {
        Remind remind = new Remind();
        remind.setTitle("Текст краткого описания");
        remind.setDescription("Полное описание тестового напоминания");
        remind.setDateTimeOfRemind(LocalDateTime.of(LocalDate.now(), LocalTime.of(10, 0)));
        remind.setUserId(1L);
        remindRepository.save(remind);

        List<Remind> result = remindRepository.findByDescription("Полное описание тестового напоминания");

        assertThat(result).isNotEmpty();
        assertThat(result.get(0).getDescription()).isEqualTo("Полное описание тестового напоминания");
    }

    @DisplayName("Проверяет, что метод возвращает напоминание, у которого дата и время наступили, " +
                 "но оно не было отправлено")
    @Test
    void findByDateTimeOfRemindBeforeAndNotifiedFalse_withOverdueUnnotifiedRemind_returnMatchingRemind() {
        Remind remindWasSentEarly = new Remind();
        remindWasSentEarly.setTitle("Напоминание отправлено ранее");
        remindWasSentEarly.setDescription("Напоминание отправлено ранее - не подходит");
        remindWasSentEarly.setDateTimeOfRemind(LocalDateTime.of(
                LocalDate.now(), LocalTime.now().minusMinutes(50)));
        remindWasSentEarly.setUserId(1L);
        remindWasSentEarly.setNotified(true);
        remindRepository.save(remindWasSentEarly);

        Remind futureRemindWithFalseAttribute = new Remind();
        futureRemindWithFalseAttribute.setTitle("Будущее напоминание");
        futureRemindWithFalseAttribute.setDescription("Будущее напоминание - не отправлено");
        futureRemindWithFalseAttribute.setDateTimeOfRemind(LocalDateTime.of(
                LocalDate.now(), LocalTime.now().plusMinutes(50)));
        futureRemindWithFalseAttribute.setUserId(1L);
        futureRemindWithFalseAttribute.setNotified(false);
        remindRepository.save(futureRemindWithFalseAttribute);

        Remind remindAlreadyNotified = new Remind();
        remindAlreadyNotified.setTitle("Напоминание отправлено в прошлом");
        remindAlreadyNotified.setDescription("Напоминание отправлено");
        remindAlreadyNotified.setDateTimeOfRemind(LocalDateTime.of(
                LocalDate.now(), LocalTime.now().minusMinutes(50)));
        remindAlreadyNotified.setUserId(1L);
        remindAlreadyNotified.setNotified(true);
        remindRepository.save(remindAlreadyNotified);


        Remind validRemind = new Remind();
        validRemind.setTitle("Подходит по условиям");
        validRemind.setDescription("Дата и время напомниания наступили, не отправлено");
        validRemind.setDateTimeOfRemind(LocalDateTime.now().minusMinutes(50));
        validRemind.setUserId(1L);
        validRemind.setNotified(false);
        remindRepository.save(validRemind);


        List<Remind> resultOfTest = remindRepository.findByDateTimeOfRemindBeforeAndNotifiedFalse(
                LocalDateTime.now());

        assertThat(resultOfTest).hasSize(1);
        assertThat(resultOfTest.get(0).getTitle()).isEqualTo("Подходит по условиям");
    }
}
