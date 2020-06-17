package net.craftersland.customenderchest.sound;

import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class Spigot9SoundHandler implements SoundHandler {

    @Override
    public void sendAnvilLandSound(Player p) {
        p.playSound(p.getLocation(), Sound.BLOCK_ANVIL_LAND, 1F, 1F);
    }

    @Override
    public void sendCompleteSound(Player p) {
        p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1F, 1F);
    }

    @Override
    public void sendEnderchestCloseSound(Player p) {
        p.playSound(p.getLocation(), Sound.valueOf("BLOCK_ENDERCHEST_CLOSE"), 1F, 1F);
    }

    @Override
    public void sendEnderchestOpenSound(Player p) {
        p.playSound(p.getLocation(), Sound.valueOf("BLOCK_ENDERCHEST_OPEN"), 1F, 1F);
    }

    @Override
    public void sendFailedSound(Player p) {
        p.playSound(p.getLocation(), Sound.valueOf("BLOCK_NOTE_PLING"), 3F, 3F);
    }
}
