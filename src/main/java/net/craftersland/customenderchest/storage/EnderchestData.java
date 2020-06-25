package net.craftersland.customenderchest.storage;

import org.bukkit.inventory.ItemStack;

import java.util.Comparator;

public interface EnderchestData extends Comparable<EnderchestData> {

    Comparator<EnderchestData> PLAYER_THEN_SLOT_COMPARATOR = Comparator.comparing(EnderchestData::getPlayer)
            .thenComparingInt(EnderchestData::getSlot);

    PlayerData getPlayer();

    int getSlot();

    EnderchestData setSlot(int slot);

    ItemStack getContent();

    EnderchestData setContent(ItemStack item);

    @Override
    default int compareTo(EnderchestData other) {
        return PLAYER_THEN_SLOT_COMPARATOR.compare(this, other);
    }
}
