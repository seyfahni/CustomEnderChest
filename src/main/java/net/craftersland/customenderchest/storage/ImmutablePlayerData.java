package net.craftersland.customenderchest.storage;

import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

public class ImmutablePlayerData implements PlayerData {

    private final long id;
    private final UUID uuid;
    private final String name;
    private final LocalDate lastSeen;

    public ImmutablePlayerData(long id, UUID uuid, String name, LocalDate lastSeen) {
        this.id = id;
        this.uuid = uuid;
        this.name = name;
        this.lastSeen = lastSeen;
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public UUID getUuid() {
        return uuid;
    }

    @Override
    public PlayerData setUuid(UUID uuid) {
        return new ImmutablePlayerData(id, uuid, name, lastSeen);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public PlayerData setName(String name) {
        return new ImmutablePlayerData(id, uuid, name, lastSeen);
    }

    @Override
    public LocalDate getLastSeen() {
        return lastSeen;
    }

    @Override
    public PlayerData setLastSeen(LocalDate lastSeen) {
        return new ImmutablePlayerData(id, uuid, name, lastSeen);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ImmutablePlayerData that = (ImmutablePlayerData) o;
        return getId() == that.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
