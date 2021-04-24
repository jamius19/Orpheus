package com.jamiussiam.orpheus.misc;

import com.jamiussiam.orpheus.manager.AudioManager;
import com.jamiussiam.orpheus.sterotypes.Tester;
import net.dv8tion.jda.api.entities.TextChannel;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class Utils {

    private static AudioManager audioManager;

    public Utils(AudioManager audioManager) {
        Utils.audioManager = audioManager;
    }

    public static void doIfGuildMusicManagerAvilable(TextChannel textChannel,
                                                     String message,
                                                     Runnable runnable) {

        doIfPredicate(() -> Objects.nonNull(audioManager.getMusicManagers().get(textChannel.getGuild().getIdLong())),
                textChannel,
                message,
                runnable);
    }

    public static void doIfPredicate(Tester tester,
                                     TextChannel textChannel,
                                     String message,
                                     Runnable runnable) {

        if (tester.isTrue()) {
            runnable.run();
        } else {
            textChannel.sendMessage(message).queue();
        }
    }
}
