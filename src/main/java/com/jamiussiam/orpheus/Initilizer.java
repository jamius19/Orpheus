package com.jamiussiam.orpheus;

import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.hooks.EventListener;
import net.dv8tion.jda.api.utils.Compression;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.springframework.stereotype.Component;

import javax.security.auth.login.LoginException;

@Slf4j
@Component
public class Initilizer {

    private static final String TOKEN = "ODMzNjkwMDIwNTMwODgwNTUy.YH2AVg.eXrWjMvehO0V2FKSwfYcIowO5vU";

    public JDA getJda(EventListener... eventListeners) throws LoginException {
        JDABuilder builder = JDABuilder.createDefault(TOKEN);

        // Disable parts of the cache
        builder.disableCache(CacheFlag.MEMBER_OVERRIDES, CacheFlag.VOICE_STATE);
        // Enable the bulk delete event
        builder.setBulkDeleteSplittingEnabled(false);
        // Disable compression (not recommended)
        builder.setCompression(Compression.NONE);
        // Set activity (like "playing Something")
        builder.setActivity(Activity.watching("TV"));

        if (eventListeners.length != 0) {
            builder.addEventListeners(eventListeners);
        }

        return builder.build();
    }
}
