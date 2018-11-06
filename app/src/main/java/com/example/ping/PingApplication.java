package com.example.ping;

import java.util.Arrays;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.core.env.Environment;
import org.springframework.core.env.SimpleCommandLinePropertySource;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.scheduling.annotation.EnableScheduling;

@Slf4j
@EnableJms
@EnableScheduling
@EnableEurekaClient
@EnableFeignClients
@SpringBootApplication
public class PingApplication {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(PingApplication.class);
        if (noCloud(args)) {
            app.setAdditionalProfiles("local");
        }
        Environment environment = app.run(args).getEnvironment();
        log.info("\n----------------------------------------------------------\n"
                + "Application '{}' is running!\n"
                + "Url:      \thttp://127.0.0.1:{}\n"
                + "Profiles: \t{}"
                + "\n----------------------------------------------------------",
            environment.getProperty("spring.application.name"),
            environment.getProperty("server.port"),
            Arrays.toString(environment.getActiveProfiles()));
    }

    private static boolean noCloud(String[] args) {
        SimpleCommandLinePropertySource source = new SimpleCommandLinePropertySource(args);
        return !source.containsProperty("spring.profiles.active")
            && !System.getenv().containsKey("SPRING_PROFILES_ACTIVE");
    }

}
