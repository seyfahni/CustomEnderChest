package net.craftersland.customenderchest.sound;

import org.bukkit.entity.Player;

public interface SoundHandler {
    void sendAnvilLandSound(Player p);

    void sendCompleteSound(Player p);

    void sendEnderchestCloseSound(Player p);

    void sendEnderchestOpenSound(Player p);

    void sendFailedSound(Player p);
}
