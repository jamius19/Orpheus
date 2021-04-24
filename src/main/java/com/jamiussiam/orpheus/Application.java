package com.jamiussiam.orpheus;

import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.hooks.EventListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Slf4j
@Configuration
public class Application implements ApplicationRunner {

    @Value("${TOKEN}")
    private String TOKEN;

    private final List<EventListener> listenerList;

    public Application(List<EventListener> listenerList) {
        this.listenerList = listenerList;
    }

    @Override
    public void run(ApplicationArguments args) {
        new Initilizer(TOKEN, listenerList).initializeJda();
    }
}
