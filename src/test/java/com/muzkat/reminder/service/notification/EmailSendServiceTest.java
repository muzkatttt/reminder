package com.muzkat.reminder.service.notification;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

@DisplayName("Тесты для Email Send Service")
@ExtendWith(MockitoExtension.class)
class EmailSendServiceTest {

    @Mock
    private JavaMailSender javaMailSender;

    @InjectMocks
    private EmailSendService emailSendService;

    @DisplayName("Проверяет отправку корректного письма через JavaMailSender. " +
                 "Метод создаёт объект, который будет захватывать аргумент типа SimpleMailMessage")
    @Test
    void sendEmail_shouldSendSimpleMailMessage_returnSendMessage() {

        String mailTo = "contactAlice@gmail.com";
        String messageSubject = "messageSubject";
        String textOfRemind = "Текст напоминания";

        emailSendService.sendEmail(mailTo, messageSubject, textOfRemind);

        ArgumentCaptor<SimpleMailMessage> argumentCaptor = ArgumentCaptor.forClass(SimpleMailMessage.class);

        verify(javaMailSender, times(1)).send(argumentCaptor.capture());

        SimpleMailMessage message = argumentCaptor.getValue();

        assertThat(message.getTo()).containsExactly(mailTo);
        assertThat(message.getSubject()).isEqualTo(messageSubject);
        assertThat(message.getText()).isEqualTo(textOfRemind);
    }
}