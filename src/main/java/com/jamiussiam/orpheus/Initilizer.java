package com.jamiussiam.orpheus;

import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.hooks.EventListener;
import net.dv8tion.jda.api.utils.Compression;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.security.auth.login.LoginException;

@Slf4j
@Component
public class Initilizer {

    @Value("${TOKEN}")
    private String TOKEN;

    public JDA getJda(EventListener... eventListeners) throws LoginException {
        JDABuilder builder = JDABuilder.createDefault(TOKEN);

        // Disable parts of the cache
        builder.disableCache(CacheFlag.MEMBER_OVERRIDES, CacheFlag.VOICE_STATE);
        // Enable the bulk delete event
        builder.setBulkDeleteSplittingEnabled(false);
        // Disable compression (not recommended)
        builder.setCompression(Compression.NONE);
        // Set activity (like "playing Something")
        builder.setActivity(Activity.playing("Music"));

        if (eventListeners.length != 0) {
            builder.addEventListeners(eventListeners);
        }

        return builder.build();
    }
}
