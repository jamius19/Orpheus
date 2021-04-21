package com.jamiussiam.orpheus.handler;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import lombok.extern.slf4j.Slf4j;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

@Slf4j
public class TrackScheduler extends AudioEventAdapter {

    private final AudioPlayer audioPlayer;

    private final Queue<AudioTrack> queue;

    public TrackScheduler(AudioPlayer audioPlayer) {
        this.audioPlayer = audioPlayer;
        queue = new LinkedBlockingQueue<>();
    }

    public void add(AudioTrack track) {
        queue.add(track);
        audioPlayer.startTrack(queue.poll(), true);
    }

    public void addPlaylist(AudioPlaylist playlist) {
        queue.addAll(playlist.getTracks());
        audioPlayer.startTrack(queue.poll(), true);
    }

    public void nextTrack() {
        audioPlayer.startTrack(queue.poll(), false);
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
}
