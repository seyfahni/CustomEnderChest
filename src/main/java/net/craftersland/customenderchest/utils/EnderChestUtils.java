package net.craftersland.customenderchest.utils;

import java.util.UUID;

import net.craftersland.customenderchest.EnderChest;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.permissions.Permissible;

public class EnderChestUtils {
	
	private final EnderChest enderchest;
	
	public EnderChestUtils(EnderChest enderchest) {
		this.enderchest = enderchest;
	}
	
    public String getTitle(Player p) {

		int enderchestLevel = getEnderchestLevel(p);

		String chestTitle = enderchest.getConfigHandler().getString("enderChestTitle.enderChestName")
				.replace("${level}", enderchest.getConfigHandler().getString("enderChestTitle.level." + enderchestLevel))
				.replace("${player}", p.getName())
				.replace("${$}", "$");

		if (chestTitle.length() > 32) {
			chestTitle = chestTitle.substring(0, 32);
		}

		return chestTitle.replace('&', '§');
	}

	private int getEnderchestLevel(Permissible p) {
		return getEnderchestLevel(p, 5);
	}

	private int getEnderchestLevel(Permissible p, int maxLevel) {
		if (p.isOp()) {
			return maxLevel;
		}
		for (int level = maxLevel; level >= 0; --level) {
			if (p.hasPermission("CustomEnderChest.level." + level)) {
				return level;
			}
		}
		return -1;
	}

	public String getCmdTitle(Player p) {
		return getCmdTitle(p.getName());
	}
    
    public String getCmdTitle(UUID p) {
		return getCmdTitle(enderchest.getStorageInterface().loadName(p));
	}

	private String getCmdTitle(String name) {
		String chestTitle = ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "" + name + "'s " + ChatColor.LIGHT_PURPLE + "Ender Chest";

		if (chestTitle.length() <= 32) {
			return chestTitle.replaceAll("&", "§");
		} else {
			return chestTitle.substring(0, 32).replaceAll("&", "§");
		}
	}

	public Integer getSize(Permissible p) {
    	
    	return (getEnderchestLevel(p) + 1) * 9;
	}
    
    public void openMenu(Player p) {
		//Cancel vanilla enderchest
		p.closeInventory();
				
		int size = enderchest.getEnderChestUtils().getSize(p);
		//No enderchest permission
		if (size == 0) {
			enderchest.getConfigHandler().printMessage(p, "chatMessages.noPermission");
			enderchest.getSoundHandler().sendFailedSound(p);
			return;
		}
		Inventory inv = enderchest.getDataHandler().getData(p.getUniqueId());
		if (inv == null) {
			String enderChestTitle = enderchest.getEnderChestUtils().getTitle(p);
			inv = Bukkit.getServer().createInventory(p, size, enderChestTitle);
		} else if (inv.getSize() != size) {
			String enderChestTitle = enderchest.getEnderChestUtils().getTitle(p);
			Inventory newInv = Bukkit.getServer().createInventory(p, size, enderChestTitle);
			if (size > inv.getSize()) {
				//TODO run this async to prevent tps drops on slow connections
				if (enderchest.getStorageInterface().hasDataFile(p.getUniqueId())) {
					enderchest.getStorageInterface().loadEnderChest(p, newInv);
				}
			} else {
				for (int i = 0; i < size; i++) {
	    			ItemStack item = inv.getItem(i);
	    			newInv.setItem(i, item);
	            }
			}
			inv = newInv;
		}
		enderchest.getDataHandler().setData(p.getUniqueId(), inv);
		enderchest.getSoundHandler().sendEnderchestOpenSound(p);
		p.openInventory(inv);
	}

}
