package com.assignment.notification.services;


import org.apache.kafka.clients.producer.Producer;
import org.slf4j.*;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.*;

@Service
public class RequestProducerService {

    private static final Logger logger = LoggerFactory.getLogger(Producer.class);
    private static final String TOPIC = "smsRequest";

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void sendRequestToConsumer(String smsId) {
        logger.info(String.format("#### -> Producing message -> %s", smsId));
        this.kafkaTemplate.send(TOPIC, smsId);
    }
}
