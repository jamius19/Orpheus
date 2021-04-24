package com.jamiussiam.orpheus.misc;

import com.jamiussiam.orpheus.model.AudioTrackQueue;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TrackScheduler extends AudioEventAdapter {

    private final AudioPlayer player;

    private final AudioTrackQueue queue;

    public TrackScheduler(AudioPlayer player) {
        this.player = player;
        queue = new AudioTrackQueue();
    }

    public void add(AudioTrack track) {
        queue.add(track);

        if (player.startTrack(track, true)) {
            queue.poll();
        }
    }

    public void addPlaylist(AudioPlaylist playlist) {
        queue.addAll(playlist.getTracks());
        player.startTrack(queue.poll(), true);
    }

    public void nextTrack() {
        player.startTrack(queue.poll(), false);
    }

    @Override
    public void onPlayerPause(AudioPlayer player) {
    }

    @Override
    public void onPlayerResume(AudioPlayer player) {
    }

    @Override
    public void onTrackStart(AudioPlayer player, AudioTrack track) {
    }

    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        if (endReason.mayStartNext) {
            nextTrack();
        }
    }

    public void clearQueue() {
        queue.clear();
    }

    public boolean isNextTrackAvailable() {
        return queue.isNextTrackAvailable();
    }

    public boolean isLooping() {
        return queue.isLooping();
    }

    public void setLooping(boolean looping) {
        queue.setLooping(looping);
    }
}
