package com.jamiussiam.orpheus.listener;

import com.jamiussiam.orpheus.handler.GuildMusicManager;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class AudioConnectListener {

    private final AudioPlayerManager playerManager;

    private final Map<Long, GuildMusicManager> musicManagers;

    public AudioConnectListener() {
        playerManager = new DefaultAudioPlayerManager();
        musicManagers = new HashMap<>();

        AudioSourceManagers.registerRemoteSources(playerManager);
    }

    public void handleQuery(GuildMessageReceivedEvent event, String query) {
        Guild guild = event.getGuild();
        GuildMusicManager guildMusicManager = getGuildMusicManager(event.getGuild());

        guild.getAudioManager().setSendingHandler(guildMusicManager.getHandler());
        guild.getAudioManager().openAudioConnection(event.getMember().getVoiceState().getChannel());

        playerManager.loadItemOrdered(guildMusicManager, query, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {
                guildMusicManager.getScheduler().add(track);
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {

            }

            @Override
            public void noMatches() {

            }

            @Override
            public void loadFailed(FriendlyException exception) {

            }
        });
    }

    private GuildMusicManager getGuildMusicManager(Guild guild) {
        if (musicManagers.containsKey(guild.getIdLong())) {
            return musicManagers.get(guild.getIdLong());
        } else {
            GuildMusicManager guildMusicManager = new GuildMusicManager(playerManager);
            musicManagers.put(guild.getIdLong(), guildMusicManager);

            return guildMusicManager;
        }
    }
}
