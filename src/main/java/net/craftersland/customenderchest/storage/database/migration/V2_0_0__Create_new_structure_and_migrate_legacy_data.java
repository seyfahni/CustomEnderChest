package net.craftersland.customenderchest.storage.database.migration;

import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;

import java.sql.PreparedStatement;
import java.util.Map;

public class V2_0_0__Create_new_structure_and_migrate_legacy_data extends BaseJavaMigration {

    @Override
    public void migrate(Context context) throws Exception {

        final Map<String, String> placeholders = context.getConfiguration().getPlaceholders();
        String legacyTableName = placeholders.getOrDefault("legacyTableName", "cec_enderchests");
        String tablePrefix = placeholders.getOrDefault("tablePrefix", "enderchest_");

        try (PreparedStatement stmnt = context.getConnection()
                .prepareStatement("CREATE TABLE `" + tablePrefix + "user` (" +
                        " `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT," +
                        " `uuid` BINARY(16) NOT NULL," +
                        " `name` VARCHAR(16) CHARACTER SET ascii COLLATE ascii_general_ci NOT NULL," +
                        " `last_seen` DATE NOT NULL DEFAULT CURRENT_DATE" +
                        " PRIMARY KEY (`id`)," +
                        " UNIQUE KEY (`uuid`)," +
                        " INDEX (`name`)" +
                        ") ENGINE InnoDB DEFAULT CHARACTER SET utf8mb4 DEFAULT COLLATION utf8mb4_unicode_520_ci")) {
            stmnt.execute();
        }

        try (PreparedStatement stmnt = context.getConnection()
                .prepareStatement("CREATE TABLE `" + tablePrefix + "inventory` (" +
                        "    `user_id` BIGINT UNSIGNED NOT NULL," +
                        "    `slot` MEDIUMINT UNSIGNED NOT NULL," +
                        "    `content` MEDIUMBLOB NOT NULL," +
                        "    PRIMARY KEY (`user_id`, `slot`)," +
                        "    FOREIGN KEY (`user_id`) REFERENCES `enderchest_user` (`id`) ON DELETE CASCADE" +
                        ") ENGINE InnoDB DEFAULT CHARACTER SET utf8mb4 DEFAULT COLLATION utf8mb4_unicode_520_ci")) {
            stmnt.execute();
        }

        // TODO: Convert old data to new structure

        try (PreparedStatement stmnt = context.getConnection()
                .prepareStatement("DROP TABLE `" + legacyTableName + "`")) {
            stmnt.execute();
        }
    }
}
