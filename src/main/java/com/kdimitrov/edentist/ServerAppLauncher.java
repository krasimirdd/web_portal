package com.kdimitrov.edentist;

import com.kdimitrov.baseApp.profiles.Server;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

import static com.kdimitrov.baseApp.profiles.ApplicationProfile.Constants.SERVER;

@Server
@SpringBootApplication
public class ServerAppLauncher {
    public static void main(String[] args) {
        new SpringApplicationBuilder()
                .sources(ServerAppLauncher.class)
                .profiles(SERVER)
                .run(args);
    }
}
