package com.jamiussiam.orpheus;

import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.hooks.EventListener;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.security.auth.login.LoginException;
import java.util.EnumSet;
import java.util.List;

@Slf4j
@Configuration
public class Initilizer {

    @Value("${TOKEN}")
    private String TOKEN;

    private final List<EventListener> listenerList;

    public Initilizer(List<EventListener> listenerList) {
        this.listenerList = listenerList;
    }

    @Bean
    public JDA getJda() {
        JDABuilder builder = JDABuilder.createDefault(TOKEN, getGatewayIntents())
                .setActivity(Activity.playing("Music"))
                .enableCache(CacheFlag.VOICE_STATE, CacheFlag.EMOTE);

        if (!listenerList.isEmpty()) {
            builder.addEventListeners(listenerList.toArray(new EventListener[0]));
        }

        try {
            JDA jda = builder.build();
            jda.awaitReady();

            return jda;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (LoginException e) {
            log.error("Can't login.");
            e.printStackTrace();
        }

        throw new IllegalStateException("No JDA can be built.");
    }

    private EnumSet<GatewayIntent> getGatewayIntents() {
        return EnumSet.of(
                GatewayIntent.GUILD_MESSAGES,
                GatewayIntent.GUILD_EMOJIS,
                GatewayIntent.GUILD_VOICE_STATES
        );
    }
}
