package net.craftersland.customenderchest.sound;

import org.bukkit.entity.Player;

/**
 * Implementation of the {@link SoundHandler} that disables all sounds.
 */
public class DisabledSoundHandler implements SoundHandler {
    @Override
    public void sendAnvilLandSound(Player p) {
    }

    @Override
    public void sendCompleteSound(Player p) {
    }

    @Override
    public void sendEnderchestCloseSound(Player p) {
    }

    @Override
    public void sendEnderchestOpenSound(Player p) {
    }

    @Override
    public void sendFailedSound(Player p) {
    }
}
