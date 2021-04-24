package com.jamiussiam.orpheus.manager;

import com.jamiussiam.orpheus.handler.MusicEventListener;
import com.jamiussiam.orpheus.misc.UrlValidator;
import com.jamiussiam.orpheus.misc.Utils;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioTrack;
import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeSearchProvider;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.BasicAudioPlaylist;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.jamiussiam.orpheus.model.GlobalValues.*;

@Slf4j
@Component
public class AudioManager {

    private final AudioPlayerManager playerManager;

    private final Map<Long, GuildMusicManager> musicManagers;

    public AudioManager() {
        playerManager = new DefaultAudioPlayerManager();
        musicManagers = new HashMap<>();

        AudioSourceManagers.registerRemoteSources(playerManager);
    }

    public void handleQuery(GuildMessageReceivedEvent event, String query) {
        Guild guild = event.getGuild();
        GuildMusicManager guildMusicManager = getGuildMusicManager(event.getGuild());

        switch (UrlValidator.getQueryType(query)) {
            case VALID_URL:
                playerManager.loadItemOrdered(guildMusicManager, query, new AudioLoadResultHandler() {
                    @Override
                    public void trackLoaded(AudioTrack track) {
                        openAudioConnection(event, guild, guildMusicManager);

                        guildMusicManager.getScheduler().add(track);
                    }

                    @Override
                    public void playlistLoaded(AudioPlaylist playlist) {
                        if (!playlist.getTracks().isEmpty()) {
                            openAudioConnection(event, guild, guildMusicManager);
                            guildMusicManager.getScheduler().addPlaylist(playlist);
                        }
                    }

                    @Override
                    public void noMatches() {
                        event.getChannel().sendMessage(CANT_LOAD_URL).queue();
                    }

                    @Override
                    public void loadFailed(FriendlyException exception) {
                        event.getChannel().sendMessage(CANT_LOAD_URL).queue();
                    }
                });
                break;
            case QUERY:
                openAudioConnection(event, guild, guildMusicManager);

                BasicAudioPlaylist playlist = (BasicAudioPlaylist) new YoutubeSearchProvider()
                        .loadSearchResult(query, audioTrackInfo ->
                                new YoutubeAudioTrack(audioTrackInfo,
                                        playerManager.source(YoutubeAudioSourceManager.class))
                        );

                Utils.doIfPredicate(() -> !playlist.getTracks().isEmpty(),
                        event.getChannel(),
                        NO_SEARCH_RESULTS_AVAILABLE,
                        () -> guildMusicManager.getScheduler().add(playlist.getTracks().get(0)));
                break;
            case URL:
                event.getChannel().sendMessage(INVALID_URL).queue();
        }
    }

    private void openAudioConnection(GuildMessageReceivedEvent event, Guild guild, GuildMusicManager guildMusicManager) {
        event.getMessage().addReaction("U+25B6").queue();

        guildMusicManager.getEventListener().setTextChannel(event.getChannel());
        guild.getAudioManager().setSendingHandler(guildMusicManager.getHandler());
        guild.getAudioManager().openAudioConnection(event.getMember().getVoiceState().getChannel());
    }

    public void stopPlayer(Guild guild) {
        if (Objects.nonNull(musicManagers.get(guild.getIdLong()))) {
            musicManagers.get(guild.getIdLong()).getPlayer().stopTrack();
            musicManagers.get(guild.getIdLong()).getScheduler().clearQueue();
        }
    }

    public Map<Long, GuildMusicManager> getMusicManagers() {
        return musicManagers;
    }

    public GuildMusicManager getGuildMusicManager(Guild guild) {
        if (musicManagers.containsKey(guild.getIdLong())) {
            return musicManagers.get(guild.getIdLong());
        } else {
            GuildMusicManager guildMusicManager = new GuildMusicManager(playerManager, new MusicEventListener());
            musicManagers.put(guild.getIdLong(), guildMusicManager);

            return guildMusicManager;
        }
    }
}
