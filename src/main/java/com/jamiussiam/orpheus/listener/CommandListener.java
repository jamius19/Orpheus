package com.jamiussiam.orpheus.listener;

import com.jamiussiam.orpheus.model.GlobalValues;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.util.Objects;

import static com.jamiussiam.orpheus.model.BotCommands.BOT_DISCONNECT;
import static com.jamiussiam.orpheus.model.BotCommands.BOT_PLAY;

@Slf4j
@Component
public class CommandListener extends ListenerAdapter {

    private final AudioConnectListener audioConnectListener;

    public CommandListener(AudioConnectListener audioConnectListener) {
        this.audioConnectListener = audioConnectListener;
    }

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        if (event.getAuthor().isBot()) {
            return;
        }

        String query = event.getMessage().getContentDisplay();

        if (BOT_PLAY.isCommandGiven(query)) {
            query = BOT_PLAY.getFilteredQuery(query);
            log.info(query);

            boolean userInChannel = userInChannel(event.getMember());

            if (userInChannel) {
                log.info("Query Received!");
                audioConnectListener.handleQuery(event, query);
            } else {
                event.getChannel()
                        .sendMessage(GlobalValues.CHANNEL_NOT_FOUND_TEXT)
                        .queue();
            }
        } else if (BOT_DISCONNECT.isCommandGiven(query)) {
            event.getGuild().getAudioManager().closeAudioConnection();
        }
    }

    private boolean userInChannel(Member member) {
        return Objects.nonNull(member.getVoiceState().getChannel());
    }
}
