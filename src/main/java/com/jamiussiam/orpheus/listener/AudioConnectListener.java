package com.jamiussiam.orpheus.listener;

import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.VoiceChannel;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AudioConnectListener {

    public void connect(VoiceChannel voiceChannel, Guild guild, Member member) {

    }
}
