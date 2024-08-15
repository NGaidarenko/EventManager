package com.example.eventmanager.kafka;

import com.example.eventmanager.dto.EventChangeKafkaMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Component
public class KafkaProducer {
    private static final Logger log = LoggerFactory.getLogger(KafkaProducer.class);

    @Autowired
    private KafkaTemplate<String, EventChangeKafkaMessage> kafkaTemplate;

    public void sendMessage(EventChangeKafkaMessage changeKafkaMessage) {
        log.info("Send new message with changes: {}", changeKafkaMessage);
        Message<EventChangeKafkaMessage> message = MessageBuilder
                .withPayload(changeKafkaMessage)
                .setHeader(KafkaHeaders.TOPIC, "event-notification")
                .build();


        kafkaTemplate.send(message);
    }
}
