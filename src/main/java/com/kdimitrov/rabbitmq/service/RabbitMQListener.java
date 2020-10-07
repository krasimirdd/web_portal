package com.kdimitrov.rabbitmq.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.kdimitrov.rabbitmq.model.VisitRequest;
import com.kdimitrov.rabbitmq.processors.MQTask;
import lombok.SneakyThrows;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Service
public class RabbitMQListener implements MessageListener {

    private final MailService mailService;
    private final ObjectMapper mapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .registerModule(new Jdk8Module())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    private final ExecutorService executor = new ThreadPoolExecutor(
            5, 5,
            60L, TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(20),
            new BasicThreadFactory.Builder().namingPattern(
                    "MailManagerThreadPool-%s").daemon(true).build());

    public RabbitMQListener(MailService mailService) {
        this.mailService = mailService;
    }

    @SneakyThrows
    @RabbitListener(queues = "edentist.queue")
    public void onMessage(final Message message) {
        System.out.println("Consuming Message - " + message);

        VisitRequest value = mapper.readValue(message.getBody(), VisitRequest.class);
        executor.submit(new MQTask(value, mailService));
    }

}
