package net.craftersland.customenderchest.storage;

import java.time.LocalDate;
import java.util.*;

public interface PlayerStorage {

    Optional<PlayerData> getPlayer(long id) throws StorageException;
    Optional<PlayerData> getPlayer(UUID uuid) throws StorageException;

    SortedSet<PlayerData> findPlayer(String name) throws StorageException;
    Set<PlayerData> findPlayersStartingWith(String name) throws StorageException;

    default PlayerData createPlayer(UUID uuid, String name) throws StorageException {
        return createPlayer(uuid, name, LocalDate.now());
    }

    PlayerData createPlayer(UUID uuid, String name, LocalDate lastSeen) throws StorageException;

    void updatePlayer(PlayerData player) throws StorageException;

    default void deletePlayer(PlayerData player) throws StorageException {
        deletePlayer(player.getId());
    }

    void deletePlayer(long id) throws StorageException;

    int deletePlayersLastSeenBefore(LocalDate before) throws StorageException;

}
