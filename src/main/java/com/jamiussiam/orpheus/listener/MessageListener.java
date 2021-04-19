package com.jamiussiam.orpheus.listener;

import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MessageListener implements EventListener {


    @Override
    public void onEvent(@NotNull GenericEvent event) {
        if (event instanceof GuildMessageReceivedEvent) {
            GuildMessageReceivedEvent messageEvent = (GuildMessageReceivedEvent) event;
            String message = messageEvent.getMessage().getContentDisplay();
            log.info(message);

            if (message.startsWith("op")) {
                message = message.substring(2);
            }
        }
    }
}
