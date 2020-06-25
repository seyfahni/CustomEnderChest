package net.craftersland.customenderchest.storage;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.UUID;

public interface PlayerData extends Comparable<PlayerData> {

    Comparator<PlayerData> ID_COMPARATOR = Comparator.comparing(PlayerData::getId, Long::compareUnsigned);
    Comparator<PlayerData> NAME_THEN_LAST_SEEN_COMPARATOR = Comparator.comparing(PlayerData::getName)
            .thenComparing(PlayerData::getLastSeen)
            .thenComparing(ID_COMPARATOR);

    long getId();

    UUID getUuid();

    PlayerData setUuid(UUID uuid);

    String getName();

    PlayerData setName(String name);

    LocalDate getLastSeen();

    PlayerData setLastSeen(LocalDate lastSeen);

    @Override
    default int compareTo(PlayerData other) {
        return ID_COMPARATOR.compare(this, other);
    }
}
