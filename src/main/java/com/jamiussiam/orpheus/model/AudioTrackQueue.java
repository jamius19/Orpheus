package com.jamiussiam.orpheus.model;


import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import lombok.extern.slf4j.Slf4j;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

@Slf4j
public class AudioTrackQueue {

    private List<AudioTrack> audioTracks;

    private boolean isLooping;

    private int lastIndex = 0;

    public AudioTrackQueue() {
        audioTracks = new LinkedList<>();
    }

    public void add(AudioTrack audioTrack) {
        if (isLooping && audioTracks.size() == 1) {
            lastIndex++;
        }

        audioTracks.add(audioTrack);
    }

    public AudioTrack peek() {
        if (audioTracks.isEmpty() || lastIndex == audioTracks.size()) {
            return null;
        }

        return audioTracks.get(lastIndex);
    }

    public AudioTrack poll() {
        if (audioTracks.isEmpty() || lastIndex == audioTracks.size()) {
            return null;
        }

        AudioTrack audioTrack = audioTracks.get(lastIndex++);

        cloneQueue();

        log.info("Current index " + lastIndex);
        return audioTrack;
    }

    private void cloneQueue() {
        if (isLooping && lastIndex == audioTracks.size()) {
            lastIndex = lastIndex % audioTracks.size();

            for (int i = 0; i < audioTracks.size(); i++) {
                AudioTrack tmpAudioTrack = audioTracks.get(i);
                audioTracks.set(i, tmpAudioTrack.makeClone());
            }
        }
    }

    public void addAll(List<AudioTrack> audioTrackList) {
        audioTracks.addAll(audioTrackList);
    }

    public void clear() {
        audioTracks.clear();
        lastIndex = 0;
    }

    public boolean isLooping() {
        return isLooping;
    }

    public void setLooping(boolean looping) {
        isLooping = looping;
        cloneQueue();
    }

    public boolean isNextTrackAvailable() {
        if (isLooping) {
            return audioTracks.size() != 0;
        } else {
            return Objects.nonNull(peek());
        }
    }

    public int size() {
        return audioTracks.size();
    }

    public int getLastIndex() {
        return lastIndex;
    }

    public void setLastIndex(int lastIndex) {
        this.lastIndex = lastIndex;
    }
}
