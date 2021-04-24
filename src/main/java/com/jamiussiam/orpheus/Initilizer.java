package com.jamiussiam.orpheus;

import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.hooks.EventListener;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

import javax.annotation.PostConstruct;
import javax.security.auth.login.LoginException;
import java.util.EnumSet;
import java.util.List;
import java.util.Objects;

@Slf4j
public class Initilizer {

    private final String TOKEN;

    private JDA jdaInstance;

    private List<EventListener> listenerList;

    public Initilizer(final String TOKEN, List<EventListener> listenerList) {
        this.TOKEN = TOKEN;
        this.listenerList = listenerList;
    }

    @PostConstruct
    public JDA initializeJda() {
        if (Objects.nonNull(jdaInstance)) {
            return jdaInstance;
        } else {
            JDABuilder builder = JDABuilder.createDefault(TOKEN, getGatewayIntents())
                    .setActivity(Activity.playing("Music"))
                    .enableCache(CacheFlag.VOICE_STATE, CacheFlag.EMOTE);

            if (!listenerList.isEmpty()) {
                builder.addEventListeners(listenerList.toArray(new EventListener[0]));
            }

            try {
                jdaInstance = builder.build();
                jdaInstance.awaitReady();

                return jdaInstance;
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (LoginException e) {
                log.error("Can't login.");
                e.printStackTrace();
            }

            throw new IllegalStateException("No JDA can be built.");
        }
    }

    private EnumSet<GatewayIntent> getGatewayIntents() {
        return EnumSet.of(
                GatewayIntent.GUILD_MESSAGES,
                GatewayIntent.GUILD_EMOJIS,
                GatewayIntent.GUILD_VOICE_STATES
        );
    }
}
