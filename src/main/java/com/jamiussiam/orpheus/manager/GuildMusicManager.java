package com.jamiussiam.orpheus.manager;

import com.jamiussiam.orpheus.handler.MusicEventListener;
import com.jamiussiam.orpheus.handler.MusicHandler;
import com.jamiussiam.orpheus.misc.TrackScheduler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;

public class GuildMusicManager {

    private final AudioPlayer player;

    private final TrackScheduler scheduler;

    private final MusicEventListener eventListener;

    public GuildMusicManager(AudioPlayerManager manager, MusicEventListener eventListener) {
        player = manager.createPlayer();
        player.addListener(eventListener);

        this.eventListener = eventListener;
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

    public MusicEventListener getEventListener() {
        return eventListener;
    }
}
