package net.craftersland.customenderchest.storage.database;

import java.sql.Connection;
import java.sql.SQLException;

public interface DatabaseSetup extends AutoCloseable {

    Connection getConnection() throws SQLException;

    @Override
    void close();
}
