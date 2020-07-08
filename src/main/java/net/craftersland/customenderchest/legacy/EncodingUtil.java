package net.craftersland.customenderchest.legacy;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

/**
 * @deprecated old class for serialization
 */
@Deprecated
public class EncodingUtil {

    /**
     * @deprecated old method for serialization
     */
    @Deprecated
	public static String toBase64(Inventory inventory) throws IllegalStateException, IOException {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);
    
        // Write the size of the inventory
        dataOutput.writeInt(inventory.getSize());
    
        // Save every element in the list
        for (int i = 0; i < inventory.getSize(); i++) {
            dataOutput.writeObject(inventory.getItem(i));
        }
    
        // Serialize that array
        dataOutput.close();
        return Base64Coder.encodeLines(outputStream.toByteArray());   
    }

    /**
     * @deprecated old method for deserialization
     */
    @Deprecated
	public static Inventory fromBase64(String data) throws IOException, ClassNotFoundException {
        ItemStack[] items = itemStackArrayFromBase64(data);

        Inventory inventory = Bukkit.getServer().createInventory(null, items.length);
        for (int i = 0; i < inventory.getSize(); i++) {
            if (items[i] != null) {
                inventory.setItem(i, items[i]);
            }
        }
        
        return inventory;
    }

    /**
     * @deprecated old method for deserialization
     */
    @Deprecated
	public static ItemStack[] itemStackArrayFromBase64(String data) throws IOException {
    	try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
            ItemStack[] items = new ItemStack[dataInput.readInt()];
    
            // Read the serialized inventory
            for (int i = 0; i < items.length; i++) {
            	items[i] = (ItemStack) dataInput.readObject();
            }
            
            dataInput.close();
            return items;
        } catch (ClassNotFoundException e) {
            throw new IOException("Unable to decode class type.", e);
        }
    }
	
}
