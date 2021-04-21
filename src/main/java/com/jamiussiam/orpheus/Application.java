package com.jamiussiam.orpheus;

import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.hooks.EventListener;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Slf4j
@Configuration
public class Application implements ApplicationRunner {
    
    private final JDA jda;

    public Application(List<EventListener> listenerList, JDA jda) {
        this.jda = jda;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {

    }
}
