package net.craftersland.customenderchest.sound;

import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class Spigot13SoundHandler implements SoundHandler {
	
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
		p.playSound(p.getLocation(), Sound.BLOCK_ENDER_CHEST_CLOSE, 1F, 1F);
	}
	
	@Override
	public void sendEnderchestOpenSound(Player p) {
		p.playSound(p.getLocation(), Sound.BLOCK_ENDER_CHEST_OPEN, 1F, 1F);
	}
	
	@Override
	public void sendFailedSound(Player p) {
		p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 3F, 3F);
	}

}
