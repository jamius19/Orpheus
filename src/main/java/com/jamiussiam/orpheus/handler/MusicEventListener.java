package com.jamiussiam.orpheus.handler;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;

import java.awt.*;

@Slf4j
public class MusicEventListener extends AudioEventAdapter {

    private TextChannel textChannel;

    @Override
    public void onTrackStart(AudioPlayer player, AudioTrack track) {
        currentlyPlayingMessage(track);
    }

    private void currentlyPlayingMessage(AudioTrack track) {
        MessageEmbed embed = new EmbedBuilder()
                .setTitle(track.getInfo().title)
                .setAuthor("Now Playing")
                .addField(track.getInfo().author, "", true)
                .setFooter(track.getInfo().uri)
                .setColor(Color.RED)
                .build();

        textChannel.sendMessage(" ").embed(embed).queue();
    }

    public void setTextChannel(TextChannel textChannel) {
        this.textChannel = textChannel;
    }
}
