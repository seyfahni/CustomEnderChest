package net.craftersland.customenderchest.storage;

import org.bukkit.inventory.ItemStack;

import java.util.Objects;

public class ImmutableEnderchestData implements EnderchestData {

    private final PlayerData player;
    private final int slot;
    private final ItemStack item;

    public ImmutableEnderchestData(PlayerData player, int slot, ItemStack item) {
        this.player = Objects.requireNonNull(player);
        this.slot = slot;
        this.item = item;
    }

    @Override
    public PlayerData getPlayer() {
        return player;
    }

    @Override
    public int getSlot() {
        return slot;
    }

    @Override
    public EnderchestData setSlot(int slot) {
        return new ImmutableEnderchestData(player, slot, item);
    }

    @Override
    public ItemStack getContent() {
        return item;
    }

    @Override
    public EnderchestData setContent(ItemStack item) {
        return new ImmutableEnderchestData(player, slot, item);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ImmutableEnderchestData that = (ImmutableEnderchestData) o;
        return getSlot() == that.getSlot() &&
                Objects.equals(getPlayer(), that.getPlayer());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPlayer(), getSlot());
    }
}
