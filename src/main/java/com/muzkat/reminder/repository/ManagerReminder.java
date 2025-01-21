package com.muzkat.reminder.repository;

import com.muzkat.reminder.model.Remind;

public class ManagerReminder {
    private ReminderBuilder builder;

    public ManagerReminder(ReminderBuilder builder) {
        super();
        this.builder = builder;
        if (this.builder == null) {
            throw new IllegalArgumentException("Напоминание невозможно создать без строителя");
        }
    }

    public Remind buildRemind(){
        return builder.fixUUID().fixTitle().fixDescription().fixRemind().build();
    }
}

