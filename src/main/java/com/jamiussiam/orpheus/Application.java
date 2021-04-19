package com.jamiussiam.orpheus;

import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.hooks.EventListener;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class Application implements ApplicationRunner {

    private final Initilizer initilizer;

    private final List<EventListener> listenerList;

    public Application(Initilizer initilizer, List<EventListener> listenerList) {
        this.initilizer = initilizer;
        this.listenerList = listenerList;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        JDA jda = initilizer.getJda(listenerList.toArray(new EventListener[0]));
    }
}
