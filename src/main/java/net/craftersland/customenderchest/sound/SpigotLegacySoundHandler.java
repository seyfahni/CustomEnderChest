package net.craftersland.customenderchest.sound;

import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class SpigotLegacySoundHandler implements SoundHandler {
    @Override
    public void sendAnvilLandSound(Player p) {
        p.playSound(p.getLocation(), Sound.valueOf("ANVIL_LAND"), 1F, 1F);
    }

    @Override
    public void sendCompleteSound(Player p) {
        p.playSound(p.getLocation(), Sound.valueOf("LEVEL_UP"), 1F, 1F);
    }

    @Override
    public void sendEnderchestCloseSound(Player p) {
        p.playSound(p.getLocation(), Sound.valueOf("CHEST_CLOSE"), 1F, 1F);
    }

    @Override
    public void sendEnderchestOpenSound(Player p) {
        p.playSound(p.getLocation(), Sound.valueOf("CHEST_OPEN"), 1F, 1F);
    }

    @Override
    public void sendFailedSound(Player p) {
        p.playSound(p.getLocation(), Sound.valueOf("NOTE_PLING"), 3F, 3F);
    }
}
