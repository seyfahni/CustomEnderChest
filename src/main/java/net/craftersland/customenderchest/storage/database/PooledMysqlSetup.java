package net.craftersland.customenderchest.storage.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class PooledMysqlSetup implements DatabaseSetup {

    private final HikariDataSource dataSource;
    private final String tablePrefix;


    public static Builder builder() {
        return new Builder();
    }

    public PooledMysqlSetup(String dataSourceClassName, String username, String password, String databaseName, String tablePrefix, String serverName, int portNumber, boolean verifyServerCertificate, boolean useSsl, boolean requireSsl) {
        HikariConfig config = new HikariConfig();
        config.setDataSourceClassName(dataSourceClassName);
        config.setUsername(username);
        config.setPassword(password);
        config.addDataSourceProperty("databaseName", databaseName);
        config.addDataSourceProperty("serverName", serverName);
        config.addDataSourceProperty("portNumber", String.valueOf(portNumber));
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        config.addDataSourceProperty("useServerPrepStmts", "true");
        config.addDataSourceProperty("useLocalSessionState", "true");
        config.addDataSourceProperty("rewriteBatchedStatements", "true");
        config.addDataSourceProperty("cacheResultSetMetadata", "true");
        config.addDataSourceProperty("cacheServerConfiguration", "true");
        config.addDataSourceProperty("elideSetAutoCommits", "true");
        config.addDataSourceProperty("maintainTimeStats", "false");
        config.addDataSourceProperty("socketTimeout", "30000");
        config.addDataSourceProperty("verifyServerCertificate", String.valueOf(verifyServerCertificate));
        config.addDataSourceProperty("useSSL", String.valueOf(useSsl));
        config.addDataSourceProperty("requireSSL", String.valueOf(requireSsl));

        dataSource = new HikariDataSource(config);
        this.tablePrefix = tablePrefix;
    }

    @Override
    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    @Override
    public String getTablePrefix() {
        return tablePrefix;
    }

    @Override
    public void close() {
        dataSource.close();
    }

    private static class Builder {

        private String dataSourceClassName = "net.craftersland.customenderchest.libraries.mariadb.jdbc.MariaDbDataSource";
        private String username = "minecraft";
        private String password = "minecraft";
        private String databaseName = "minecraft";
        private String tablePrefix = "enderchest_";
        private String serverName = "127.0.0.1";
        private int portNumber = 3306;
        private boolean verifyServerCertificate = false;
        private boolean useSsl = false;
        private boolean requireSsl = false;

        private Builder() {
        }

        public PooledMysqlSetup build() {
            return new PooledMysqlSetup(dataSourceClassName, username, password, databaseName, tablePrefix, serverName, portNumber, verifyServerCertificate, useSsl, requireSsl);
        }

        public Builder dataSourceClassName(String dataSourceClassName) {
            this.dataSourceClassName = dataSourceClassName;
            return this;
        }

        public Builder username(String username) {
            this.username = username;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public Builder databaseName(String databaseName) {
            this.databaseName = databaseName;
            return this;
        }

        public Builder tablePrefix(String tablePrefix) {
            this.tablePrefix = tablePrefix;
            return this;
        }

        public Builder serverName(String serverName) {
            this.serverName = serverName;
            return this;
        }

        public Builder portNumber(int portNumber) {
            this.portNumber = portNumber;
            return this;
        }

        public Builder verifyServerCertificate(boolean verifyServerCertificate) {
            this.verifyServerCertificate = verifyServerCertificate;
            return this;
        }

        public Builder useSsl(boolean useSsl) {
            this.useSsl = useSsl;
            return this;
        }

        public Builder requireSSL(boolean requireSsl) {
            this.requireSsl = requireSsl;
            return this;
        }
    }
}
