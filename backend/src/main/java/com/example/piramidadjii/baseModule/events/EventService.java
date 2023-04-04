package com.example.piramidadjii.baseModule.events;


import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
public class EventService {

    private final ApplicationEventPublisher eventPublisher;

    public EventService(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

     public void renewSubscriptionPlan(){
        RenewSubscriptionPlansEvent renewSubscriptionPlansEvent=new RenewSubscriptionPlansEvent(RenewSubscriptionPlansEvent.class.getSimpleName());
        eventPublisher.publishEvent(renewSubscriptionPlansEvent);
     }

     public void distributeMoney(){
        DistributeMoneyEvent distributeMoneyEvent=new DistributeMoneyEvent(DistributeMoneyEvent.class.getSimpleName());
        eventPublisher.publishEvent(distributeMoneyEvent);
     }
}
