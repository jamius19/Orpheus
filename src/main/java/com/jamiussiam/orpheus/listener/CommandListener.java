package com.jamiussiam.orpheus.listener;

import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Slf4j
@Component
public class CommandListener extends ListenerAdapter {

    private final AudioConnectListener audioConnectListener;

    private static final String CHANNEL_NOT_FOUND_TEXT = "```diff\n- You have to be in a voice channel to use Orpheus!\n```";

    public CommandListener(AudioConnectListener audioConnectListener) {
        this.audioConnectListener = audioConnectListener;
    }

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        if (event.getAuthor().isBot()) {
            return;
        }

        String message = event.getMessage().getContentDisplay();

        if (message.startsWith("op")) {
            message = message.substring(2);

            boolean userInChannel = userInChannel(event.getMember());

            if (userInChannel) {
                audioConnectListener.connect(event.getMember().getVoiceState().getChannel(),
                        event.getGuild(),
                        event.getMember());
            } else {
                event.getChannel()
                        .sendMessage(CHANNEL_NOT_FOUND_TEXT)
                        .queue();
            }
        }
    }

    private boolean userInChannel(Member member) {
        return Objects.nonNull(member.getVoiceState().getChannel());
    }
}
