package com.jamiussiam.orpheus.handler;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;

public class GuildMusicManager {

    private final AudioPlayer player;

    private final TrackScheduler scheduler;

    public GuildMusicManager(AudioPlayerManager manager) {
        player = manager.createPlayer();
        scheduler = new TrackScheduler(player);
        player.addListener(scheduler);
    }

    public AudioPlayer getPlayer() {
        return player;
    }

    public MusicHandler getHandler() {
        return new MusicHandler(player);
    }

    public TrackScheduler getScheduler() {
        return scheduler;
    }
}
