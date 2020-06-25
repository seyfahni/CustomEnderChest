package net.craftersland.customenderchest.storage.database;

import net.craftersland.customenderchest.storage.ImmutablePlayerData;
import net.craftersland.customenderchest.utils.UuidUtil;

import java.sql.Date;
import java.time.LocalDate;
import java.util.UUID;

public class DatabasePlayerData extends ImmutablePlayerData {

    // TODO: As factory?

    public DatabasePlayerData(String id, byte[] uuid, String name, Date lastSeen) {
        super(Long.parseUnsignedLong(id), UuidUtil.fromBytes(uuid), name, lastSeen.toLocalDate());
    }

    public DatabasePlayerData(long id, byte[] uuid, String name, Date lastSeen) {
        super(id, UuidUtil.fromBytes(uuid), name, lastSeen.toLocalDate());
    }

    public DatabasePlayerData(String id, UUID uuid, String name, Date lastSeen) {
        super(Long.parseUnsignedLong(id), uuid, name, lastSeen.toLocalDate());
    }

    public DatabasePlayerData(long id, UUID uuid, String name, LocalDate lastSeen) {
        super(id, uuid, name, lastSeen);
    }

}
