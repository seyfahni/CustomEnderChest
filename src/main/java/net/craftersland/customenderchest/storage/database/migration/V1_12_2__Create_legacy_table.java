package net.craftersland.customenderchest.storage.database.migration;

import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;

import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

public class V1_12_2__Create_legacy_table extends BaseJavaMigration {

    @Override
    public void migrate(Context context) throws SQLException {

        final Map<String, String> placeholders = context.getConfiguration().getPlaceholders();
        String legacyTableName = placeholders.getOrDefault("legacyTableName", "cec_enderchests");

        // Update before creation as creation already includes the updates,
        // if no table exist or the table is up to date, updates will be skipped.
        updateOutdatedLegacyTables(context, legacyTableName);
        createLegacyTableIfNotExists(context, legacyTableName);
    }

    private void updateOutdatedLegacyTables(Context context, String legacyTableName) throws SQLException {
        DatabaseMetaData md = context.getConnection().getMetaData();
        try (ResultSet rs1 = md.getColumns(null, null, legacyTableName, "enderchest")) {
            if (rs1.next()) {
                String data1 = "ALTER TABLE `" + legacyTableName + "` CHANGE COLUMN enderchest enderchest_data LONGTEXT NOT NULL;";
                try (PreparedStatement query1 = context.getConnection().prepareStatement(data1)) {
                    query1.execute();
                }
            } else {
                try (ResultSet rs2 = md.getColumns(null, null, legacyTableName, "enderchest_data")) {
                    if (rs2.next() && rs2.getString("TYPE_NAME").matches("VARCHAR")) {
                        String data2 = "ALTER TABLE `" + legacyTableName + "` MODIFY enderchest_data LONGTEXT NOT NULL;";
                        try (PreparedStatement query2 = context.getConnection().prepareStatement(data2)) {
                            query2.execute();
                        }
                    }
                }
            }
        }
    }

    private void createLegacyTableIfNotExists(Context context, String legacyTableName) throws SQLException {
        String createLegacyTableSql =
                "CREATE TABLE IF NOT EXISTS `" + legacyTableName + "` (" +
                        "id int(10) AUTO_INCREMENT, " +
                        "player_uuid varchar(50) NOT NULL UNIQUE, " +
                        "player_name varchar(50) NOT NULL, " +
                        "enderchest_data LONGTEXT NOT NULL, " +
                        "size int(3) NOT NULL, " +
                        "last_seen varchar(30) NOT NULL, " +
                        "PRIMARY KEY(id)" +
                        ")";

        try (PreparedStatement stmnt = context.getConnection().prepareStatement(createLegacyTableSql)) {
            stmnt.execute();
        }
    }
}
