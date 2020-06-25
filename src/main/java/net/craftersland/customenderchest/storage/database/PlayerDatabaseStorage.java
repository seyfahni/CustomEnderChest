package net.craftersland.customenderchest.storage.database;

import net.craftersland.customenderchest.storage.PlayerData;
import net.craftersland.customenderchest.storage.PlayerStorage;
import net.craftersland.customenderchest.storage.StorageException;
import net.craftersland.customenderchest.utils.UuidUtil;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

public class PlayerDatabaseStorage implements PlayerStorage {

    private final DatabaseSetup database;

    public PlayerDatabaseStorage(DatabaseSetup database) {
        this.database = database;
    }

    @Override
    public Optional<PlayerData> getPlayer(long id) throws StorageException {
        try (Connection connection = database.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT `uuid`, `name`, `last_seen` FROM `" + database.getTablePrefix() + "user` WHERE `id` = ?")) {
            statement.setString(1, Long.toUnsignedString(id)); // workaround for BIGINT UNSIGNED
            try (ResultSet result = statement.executeQuery()) {
                PlayerData player = null;
                if (result.next()) {
                    player = new DatabasePlayerData(
                            id,
                            result.getBytes("uuid"),
                            result.getString("name"),
                            result.getDate("last_seen"));
                }
                if (result.next()) {
                    throw new StorageException("non unique player entry: id: " + id);
                }
                return Optional.ofNullable(player);
            }
        } catch (SQLException sqlException) {
            throw new StorageException(sqlException);
        }
    }

    @Override
    public Optional<PlayerData> getPlayer(UUID uuid) throws StorageException {
        try (Connection connection = database.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT `id`, `name`, `last_seen` FROM `" + database.getTablePrefix() + "user` WHERE `uuid` = ?")) {
            statement.setBytes(1, UuidUtil.toBytes(uuid));
            try (ResultSet result = statement.executeQuery()) {
                PlayerData player = null;
                if (result.next()) {
                    player = new DatabasePlayerData(
                            result.getString("id"),
                            uuid,
                            result.getString("name"),
                            result.getDate("last_seen"));
                }
                if (result.next()) {
                    throw new StorageException("non unique player entry: uuid: " + uuid.toString());
                }
                return Optional.ofNullable(player);
            }
        } catch (SQLException sqlException) {
            throw new StorageException(sqlException);
        }
    }

    @Override
    public SortedSet<PlayerData> findPlayer(String name) throws StorageException {
        try (Connection connection = database.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT `id`, `uuid`, `name`, `last_seen` FROM `" + database.getTablePrefix() + "user` WHERE `name` LIKE ?")) {
            statement.setString(1, name);
            try (ResultSet result = statement.executeQuery()) {
                SortedSet<PlayerData> playerSet = new TreeSet<>(PlayerData.NAME_THEN_LAST_SEEN_COMPARATOR);
                while (result.next()) {
                    playerSet.add(new DatabasePlayerData(
                            result.getString("id"),
                            result.getBytes("uuid"),
                            result.getString("name"),
                            result.getDate("last_seen")));
                }
                return playerSet;
            }
        } catch (SQLException sqlException) {
            throw new StorageException(sqlException);
        }
    }

    @Override
    public SortedSet<PlayerData> findPlayersStartingWith(String name) throws StorageException {
        try (Connection connection = database.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT `id`, `uuid`, `name`, `last_seen` FROM `" + database.getTablePrefix() + "user` WHERE `name` LIKE ?")) {
            statement.setString(1, name + "%");
            try (ResultSet result = statement.executeQuery()) {
                SortedSet<PlayerData> playerSet = new TreeSet<>(PlayerData.NAME_THEN_LAST_SEEN_COMPARATOR);
                while (result.next()) {
                    playerSet.add(new DatabasePlayerData(
                            result.getString("id"),
                            result.getBytes("uuid"),
                            result.getString("name"),
                            result.getDate("last_seen")));
                }
                return playerSet;
            }
        } catch (SQLException sqlException) {
            throw new StorageException(sqlException);
        }
    }

    @Override
    public PlayerData createPlayer(UUID uuid, String name, LocalDate lastSeen) throws StorageException {
        try (Connection connection = database.getConnection();
             PreparedStatement statement = connection.prepareStatement("INSERT INTO `" + database.getTablePrefix() + "user` (`uuid`, `name`, `last_seen`) VALUES (?, ?, ?)")) {
            statement.setBytes(1, UuidUtil.toBytes(uuid));
            statement.setString(2, name);
            statement.setDate(3, Date.valueOf(lastSeen));
            statement.executeUpdate();

            return getPlayer(uuid).orElseThrow(() -> new StorageException("could not fetch newly created player: uuid: " + uuid.toString()));
        } catch (SQLException sqlException) {
            throw new StorageException(sqlException);
        }
    }

    @Override
    public void updatePlayer(PlayerData player) throws StorageException {
        try (Connection connection = database.getConnection();
             PreparedStatement statement = connection.prepareStatement("UPDATE `" + database.getTablePrefix() + "user` SET `uuid` = ?, `name` = ?, `last_seen` = ? WHERE `id` = ?")) {
            statement.setBytes(1, UuidUtil.toBytes(player.getUuid()));
            statement.setString(2, player.getName());
            statement.setDate(3, Date.valueOf(player.getLastSeen()));
            statement.setString(4, Long.toUnsignedString(player.getId()));
            statement.executeUpdate();
        } catch (SQLException sqlException) {
            throw new StorageException(sqlException);
        }
    }

    @Override
    public void deletePlayer(long id) throws StorageException {
        try (Connection connection = database.getConnection();
             PreparedStatement statement = connection.prepareStatement("DELETE FROM `" + database.getTablePrefix() + "user` WHERE `id` = ?")) {
            statement.setString(1, Long.toUnsignedString(id));
            statement.executeUpdate();
        } catch (SQLException sqlException) {
            throw new StorageException(sqlException);
        }
    }

    @Override
    public int deletePlayersLastSeenBefore(LocalDate before) throws StorageException {
        try (Connection connection = database.getConnection();
             PreparedStatement statement = connection.prepareStatement("DELETE FROM `" + database.getTablePrefix() + "user` WHERE `last_seen` < ?")) {
            statement.setDate(1, Date.valueOf(before));
            return statement.executeUpdate();
        } catch (SQLException sqlException) {
            throw new StorageException(sqlException);
        }
    }
}
