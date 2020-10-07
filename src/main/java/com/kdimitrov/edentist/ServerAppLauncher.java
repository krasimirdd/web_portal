package com.kdimitrov.edentist;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class ServerAppLauncher {
    public static void main(String[] args) {
        new SpringApplicationBuilder()
                .sources(ServerAppLauncher.class)
                .profiles("server")
                .run(args);
    }
}
