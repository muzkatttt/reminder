package com.muzkat.reminder.service.notification;

import lombok.AllArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * Сервис для отправки напоминаний по электронной почте
 * <p>
 * Использует {@link JavaMailSender} для отправки простых текстовых сообщений
 * указанному получателю. Отправка поддерживает только текст (без HTML и вложений)
 * </p>
 */
@Service
@AllArgsConstructor
public class EmailSendService {

    /**
     * Поле экземпляр {@link JavaMailSender}
     */
    private final JavaMailSender javaMailSender;


    /**
     * Метод отправляет текстовое письмо по указанному адресу
     * <p>
     *     Метод использует {@link JavaMailSender} для отправки письма
     *     пользователю, содержит тему и текст сообщения.
     *     Поддерживает отправку только текстовых (не HTML) сообщений
     * </p>
     * @param mailTo адрес получателя письма
     * @param messageSubject тема письма
     * @param textOfRemind текст письма (содержимое напоминания)
     */
    public void sendEmail(String mailTo, String messageSubject, String textOfRemind){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(mailTo);
        message.setSubject(messageSubject);
        message.setText(textOfRemind);
        javaMailSender.send(message);
    }
}