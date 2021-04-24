package com.jamiussiam.orpheus.manager;

import com.jamiussiam.orpheus.misc.Utils;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.util.Objects;

import static com.jamiussiam.orpheus.model.BotCommands.*;
import static com.jamiussiam.orpheus.model.GlobalValues.*;

@Slf4j
@Component
public class CommandListener extends ListenerAdapter {

    private final AudioManager audioManager;

    public CommandListener(AudioManager audioManager) {
        this.audioManager = audioManager;
    }

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        if (event.getAuthor().isBot()) {
            return;
        }

        String query = event.getMessage().getContentDisplay();
        GuildMusicManager guildMusicManager = audioManager.getGuildMusicManager(event.getGuild());

        if (BOT_PLAY.isCommandGiven(query)) {
            query = BOT_PLAY.getFilteredQuery(query);

            boolean userInChannel = userInChannel(event.getMember());

            if (userInChannel) {
                audioManager.handleQuery(event, query);
            } else {
                event.getChannel()
                        .sendMessage(CHANNEL_NOT_FOUND_TEXT)
                        .queue();
            }
        } else if (BOT_SKIP.isCommandGiven(query)) {

            Utils.doIfGuildMusicManagerAvilable(event.getChannel(),
                    NO_MUSIC_PLAYING,
                    () -> Utils.doIfPredicate(() -> guildMusicManager.getScheduler().isNextTrackAvailable(),
                            event.getChannel(),
                            NEXT_TRACK_NOT_AVAILABLE,
                            () -> {
                                guildMusicManager.getEventListener().setTextChannel(event.getChannel());
                                guildMusicManager.getScheduler().nextTrack();
                            }));
        } else if (BOT_DISCONNECT.isCommandGiven(query)) {
            event.getMessage().addReaction("U+23F9").queue();
            disconnect(event.getGuild());
        } else if (BOT_LOOP.isCommandGiven(query)) {
            guildMusicManager.getScheduler().setLooping(true);
            event.getMessage().addReaction("U+1F501").queue();
        }
    }

    @Override
    public void onGuildVoiceLeave(@NotNull GuildVoiceLeaveEvent event) {
        // Self Disconnected
        if (event.getMember().getUser().getIdLong() == event.getJDA().getSelfUser().getIdLong()) {
            disconnect(event.getGuild());
        }
    }

    private void disconnect(Guild guild) {
        guild.getAudioManager().closeAudioConnection();
        audioManager.stopPlayer(guild);
    }

    private boolean userInChannel(Member member) {
        return Objects.nonNull(member.getVoiceState().getChannel());
    }
}
