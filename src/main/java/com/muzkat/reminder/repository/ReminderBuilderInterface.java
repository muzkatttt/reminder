package com.muzkat.reminder.repository;

import com.muzkat.reminder.model.Remind;

public interface ReminderBuilderInterface {

    ReminderBuilderInterface fixUUID();

    ReminderBuilderInterface fixTitle();

    ReminderBuilderInterface fixDescription();

    ReminderBuilderInterface fixRemind();

    Remind build();

}
