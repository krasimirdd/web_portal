package com.kdimitrov.edentist.api.services;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class MQSender<T> {

    private final AmqpTemplate rabbitTemplate;

    @Value("${edentist.mq.exchange}")
    private String exchange;

    @Value("${edentist.mq.routingkey}")
    private String routingkey;

    public MQSender(AmqpTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void send(T entity) {
        rabbitTemplate.convertAndSend(exchange, routingkey, entity);
        System.out.println("Send msg = " + entity);
    }
}
