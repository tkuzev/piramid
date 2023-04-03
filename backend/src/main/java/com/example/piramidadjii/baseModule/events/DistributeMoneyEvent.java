package com.example.piramidadjii.baseModule.events;

import org.springframework.context.ApplicationEvent;

public class DistributeMoneyEvent extends ApplicationEvent {
    public DistributeMoneyEvent(Object source) {
        super(source);
    }
}
