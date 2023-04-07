package com.example.piramidadjii.kafka;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class Producer {
    private static final Logger logger = LoggerFactory.getLogger(Producer.class);

    private final KafkaTemplate<String, PraznimKonteineriEvent> kafkaTemplate;

    @Autowired
    public Producer(KafkaTemplate<String, PraznimKonteineriEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }


    public void sendEvent(PraznimKonteineriEvent prazneneEvent) {
        logger.debug("Message sent " + prazneneEvent.getPayload());
        kafkaTemplate.send("praznene", prazneneEvent);
    }
}
