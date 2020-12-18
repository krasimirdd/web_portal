package com.kdimitrov.rabbitmq;


import com.kdimitrov.baseApp.profiles.MessageQueue;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

import static com.kdimitrov.baseApp.profiles.ApplicationProfile.Constants.MESSAGE_QUEUE;

@MessageQueue
@SpringBootApplication
public class RabbitMQLauncher {
    public static void main(String[] args) {
        new SpringApplicationBuilder()
                .sources(RabbitMQLauncher.class)
                .profiles(MESSAGE_QUEUE)
                .run(args);
    }
}
