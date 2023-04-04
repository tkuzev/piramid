package com.example.piramidadjii.baseModule.events;

import org.springframework.context.ApplicationEvent;

public class RenewSubscriptionPlansEvent extends ApplicationEvent {
    public RenewSubscriptionPlansEvent(Object source) {
        super(source);
    }
}
