package net.craftersland.customenderchest.legacy;

import java.io.IOException;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.comphenix.protocol.utility.StreamSerializer;

public class ModdedSerializer {

	private final StreamSerializer streamSerializer;

	public ModdedSerializer() {
		this(StreamSerializer.getDefault());
	}

	public ModdedSerializer(StreamSerializer streamSerializer) {
		this.streamSerializer = streamSerializer;
	}

	/**
	 * @deprecated old method for serialization
	 */
	@Deprecated
	public String toBase64(ItemStack[] itemStacks) throws IOException {
		StringBuilder stringBuilder = new StringBuilder();
	    for (int i = 0; i < itemStacks.length; i++) {
	      if (i > 0) {
	        stringBuilder.append(";");
	      }
	      if ((itemStacks[i] != null) && (itemStacks[i].getType() != Material.AIR)) {
	    	  stringBuilder.append(StreamSerializer.getDefault().serializeItemStack(itemStacks[i]));
	      }
	    }
	    return stringBuilder.toString();
    }
	
	public ItemStack[] fromBase64(String data) throws IOException {
		String[] strings = data.split(";");
	    ItemStack[] itemStacks = new ItemStack[strings.length];
	    for (int i = 0; i < strings.length; i++) {
	      if (!strings[i].equals("")) {
			  itemStacks[i] = streamSerializer.deserializeItemStack(strings[i]);
	      }
	    }
	    return itemStacks;
    }

}
