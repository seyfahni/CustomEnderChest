package net.craftersland.customenderchest.storage.database;

import net.craftersland.customenderchest.serialize.ItemSerializer;
import net.craftersland.customenderchest.serialize.SerializationException;
import net.craftersland.customenderchest.storage.*;
import org.bukkit.inventory.ItemStack;

import java.sql.*;
import java.util.Optional;
import java.util.SortedSet;
import java.util.TreeSet;

public class EnderchestDatabaseStorage implements EnderchestStorage {

    private final DatabaseSetup database;
    private final ItemSerializer serializer;

    public EnderchestDatabaseStorage(DatabaseSetup database, ItemSerializer serializer) {
        this.database = database;
        this.serializer = serializer;
    }

    @Override
    public Optional<EnderchestData> getEnderchestItem(PlayerData player, int slot) throws StorageException {
        try (Connection connection = database.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT `content` FROM `" + database.getTablePrefix() + "inventory` WHERE `user_id` = ? AND `slot` = ?")) {
            statement.setString(1, Long.toUnsignedString(player.getId())); // workaround for BIGINT UNSIGNED
            statement.setInt(2, slot);
            try (ResultSet result = statement.executeQuery()) {
                EnderchestData enderchestEntry = null;
                if (result.next()) {
                    Blob content = result.getBlob("content");
                    byte[] contentBinary = content.getBytes(1, Math.toIntExact(content.length()));
                    enderchestEntry = new ImmutableEnderchestData(
                            player,
                            slot,
                            serializer.deserializeItem(contentBinary));
                }
                if (result.next()) {
                    throw new StorageException("non unique enderchest entry: player id: " + player.getId() + " ; slot: " + slot);
                }
                return Optional.ofNullable(enderchestEntry);
            }
        } catch (SQLException | ArithmeticException | SerializationException exception) {
            throw new StorageException(exception);
        }
    }

    @Override
    public SortedSet<EnderchestData> getPlayersEnderchestItems(PlayerData player) throws StorageException {
        try (Connection connection = database.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT `slot`, `content` FROM `" + database.getTablePrefix() + "inventory` WHERE `user_id` = ?")) {
            statement.setString(1, Long.toUnsignedString(player.getId())); // workaround for BIGINT UNSIGNED
            try (ResultSet result = statement.executeQuery()) {
                SortedSet<EnderchestData> enderchest = new TreeSet<>(EnderchestData.PLAYER_THEN_SLOT_COMPARATOR);
                while (result.next()) {
                    Blob content = result.getBlob("content");
                    byte[] contentBinary = content.getBytes(1, Math.toIntExact(content.length()));
                    enderchest.add(new ImmutableEnderchestData(
                            player,
                            result.getInt("slot"),
                            serializer.deserializeItem(contentBinary)));
                }
                return enderchest;
            }
        } catch (SQLException | ArithmeticException | SerializationException exception) {
            throw new StorageException(exception);
        }
    }

    @Override
    public EnderchestData addEnderchestItem(PlayerData player, int slot, ItemStack item) throws StorageException {
        try (Connection connection = database.getConnection();
             PreparedStatement statement = connection.prepareStatement("INSERT INTO `" + database.getTablePrefix() + "inventory` (`user_id`, `slot`, `content`) VALUES (?, ?, ?)")) {
            Blob content = connection.createBlob();
            content.setBytes(1, serializer.serializeItem(item));
            statement.setString(1, Long.toUnsignedString(player.getId())); // workaround for BIGINT UNSIGNED
            statement.setInt(2, slot);
            statement.setBlob(3, content);
            statement.setBlob(4, content);
            statement.executeUpdate();

            return getEnderchestItem(player, slot).orElseThrow(() -> new StorageException("could not fetch newly created enderchest entry: player id: " + player.getId() + " ; slot: " + slot));
        } catch (SQLException | SerializationException exception) {
            throw new StorageException(exception);
        }
    }

    @Override
    public EnderchestData setEnderchestItem(EnderchestData enderchestData) throws StorageException {
        try (Connection connection = database.getConnection();
             PreparedStatement statement = connection.prepareStatement("INSERT INTO `" + database.getTablePrefix() + "inventory` (`user_id`, `slot`, `content`) VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE `content` = ?")) {
            Blob content = connection.createBlob();
            content.setBytes(1, serializer.serializeItem(enderchestData.getContent()));
            statement.setString(1, Long.toUnsignedString(enderchestData.getPlayer().getId())); // workaround for BIGINT UNSIGNED
            statement.setInt(2, enderchestData.getSlot());
            statement.setBlob(3, content);
            statement.setBlob(4, content);
            statement.executeUpdate();

            return getEnderchestItem(enderchestData.getPlayer(), enderchestData.getSlot())
                    .orElseThrow(() -> new StorageException("could not fetch newly created enderchest entry: player id: " + enderchestData.getPlayer().getId() + " ; slot: " + enderchestData.getSlot()));
        } catch (SQLException | SerializationException exception) {
            throw new StorageException(exception);
        }
    }

    @Override
    public void deleteEnderchestItem(PlayerData player, int slot) throws StorageException {
        try (Connection connection = database.getConnection();
             PreparedStatement statement = connection.prepareStatement("DELETE FROM `" + database.getTablePrefix() + "inventory` WHERE `user_id` = ? AND `slot` = ?")) {
            statement.setString(1, Long.toUnsignedString(player.getId())); // workaround for BIGINT UNSIGNED
            statement.setInt(2, slot);
            statement.executeUpdate();
        } catch (SQLException sqlException) {
            throw new StorageException(sqlException);
        }
    }

    @Override
    public int deletePlayersEnderchestItems(PlayerData player) throws StorageException {
        try (Connection connection = database.getConnection();
             PreparedStatement statement = connection.prepareStatement("DELETE FROM `" + database.getTablePrefix() + "inventory` WHERE `user_id` = ?")) {
            statement.setString(1, Long.toUnsignedString(player.getId())); // workaround for BIGINT UNSIGNED
            return statement.executeUpdate();
        } catch (SQLException sqlException) {
            throw new StorageException(sqlException);
        }
    }
}
