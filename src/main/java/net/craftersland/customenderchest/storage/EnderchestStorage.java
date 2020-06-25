package net.craftersland.customenderchest.storage;

import org.bukkit.inventory.ItemStack;

import java.util.*;

public interface EnderchestStorage {

    Optional<EnderchestData> getEnderchestItem(PlayerData player, int slot) throws StorageException;
    SortedSet<EnderchestData> getPlayersEnderchestItems(PlayerData player) throws StorageException;

    EnderchestData addEnderchestItem(PlayerData player, int slot, ItemStack item) throws StorageException;

    EnderchestData setEnderchestItem(EnderchestData enderchestData) throws StorageException;

    default void deleteEnderchestItem(EnderchestData enderchestData) throws StorageException {
        deleteEnderchestItem(enderchestData.getPlayer(), enderchestData.getSlot());
    }

    void deleteEnderchestItem(PlayerData playerData, int slot) throws StorageException;

    int deletePlayersEnderchestItems(PlayerData playerData) throws StorageException;
}
