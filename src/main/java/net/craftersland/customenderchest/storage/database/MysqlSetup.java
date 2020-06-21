package net.craftersland.customenderchest.storage.database;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;

import org.bukkit.Bukkit;

import net.craftersland.customenderchest.EnderChest;

public class MysqlSetup implements DatabaseSetup {
	
	private Connection conn = null;
	private final EnderChest enderchest;
	private boolean tablesChecked = false;
	
	public MysqlSetup(EnderChest enderchest) {
		this.enderchest = enderchest;
		setupDatabase();
		updateTables();
	}
	
	public void setupDatabase() {
		connectToDatabase();
		databaseMaintenanceTask();
	}
	
	private void tableMaintenance(long inactiveTime, Connection conn, String tableName) {
		String sql = "DELETE FROM `" + tableName + "` WHERE `last_seen` < ?";
		try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
			preparedStatement.setString(1, String.valueOf(inactiveTime));
			preparedStatement.execute();
		} catch (Exception e) {
			EnderChest.log.log(Level.SEVERE, "Could not execute database maintenance task!", e);
		}
	}
	
	private void databaseMaintenanceTask() {
		if (enderchest.getConfigHandler().getBoolean("database.mysql.removeOldUsers.enabled")) {
			Bukkit.getScheduler().runTaskLaterAsynchronously(enderchest, new Runnable() {

				@Override
				public void run() {
					if (conn != null) {
						long inactivityDays = Long.parseLong(enderchest.getConfigHandler().getString("database.mysql.removeOldUsers.inactive"));
						long inactivityMils = inactivityDays * 24 * 60 * 60 * 1000;
						long curentTime = System.currentTimeMillis();
						long inactiveTime = curentTime - inactivityMils;
						EnderChest.log.info("Database maintenance task started...");
						tableMaintenance(inactiveTime, getConnection(), enderchest.getConfigHandler().getString("database.mysql.tableName"));
						EnderChest.log.info("Database maintenance complete!");
					}
				}
				
			}, 100 * 20L);
		}
	}
	
	private void connectToDatabase() {
		try {
       	 	//Load Drivers
            Class.forName("com.mysql.jdbc.Driver");
            
            Properties properties = new Properties();
            properties.setProperty("user", enderchest.getConfigHandler().getString("database.mysql.user"));
            properties.setProperty("password", enderchest.getConfigHandler().getString("database.mysql.password"));
            properties.setProperty("autoReconnect", "true");
            properties.setProperty("verifyServerCertificate", "false");
            properties.setProperty("useSSL", enderchest.getConfigHandler().getString("database.mysql.ssl"));
            properties.setProperty("requireSSL", enderchest.getConfigHandler().getString("database.mysql.ssl"));
            
            //Connect to database
            conn = DriverManager.getConnection("jdbc:mysql://" + enderchest.getConfigHandler().getString("database.mysql.host") + ":" + enderchest.getConfigHandler().getString("database.mysql.port") + "/" + enderchest.getConfigHandler().getString("database.mysql.databaseName"), properties);
            EnderChest.log.info("Database connection established!");
            if (!tablesChecked) {
            	setupTables();
            }
          } catch (ClassNotFoundException e) {
        	  EnderChest.log.log(Level.SEVERE, "Could not locate drivers for mysql!", e);
          } catch (SQLException e) {
        	  EnderChest.log.log(Level.SEVERE, "Could not connect to mysql database!", e);
          }
	}
	
	public void setupTables() {
		if (conn != null) {
			String data = "CREATE TABLE IF NOT EXISTS `" + enderchest.getConfigHandler().getString("database.mysql.tableName") + "` (id int(10) AUTO_INCREMENT, player_uuid varchar(50) NOT NULL UNIQUE, player_name varchar(50) NOT NULL, enderchest_data LONGTEXT NOT NULL, size int(3) NOT NULL, last_seen varchar(30) NOT NULL, PRIMARY KEY(id));";
			try (PreparedStatement query1 = conn.prepareStatement(data)) {
		        query1.execute();
		        tablesChecked = true;
			} catch (SQLException e) {
				EnderChest.log.log(Level.SEVERE, "Error creating tables!", e);
			}
		}
	}
	
	public Connection getConnection() {
		checkConnection();
		return conn;
	}

	public void closeConnection() {
		try {
			EnderChest.log.info("Closing database connection...");
			if (conn != null) {
				conn.close();
				conn = null;
			}
		} catch (SQLException e) {
			EnderChest.log.log(Level.SEVERE, "Failed closing database connection!", e);
		}
	}
	
	private void checkConnection() {
		try {
			if (conn == null) {
				EnderChest.log.warning("Database connection failed. Reconnecting...");
				conn = null;
				connectToDatabase();
			} else if (!conn.isValid(3)) {
				EnderChest.log.warning("Database connection failed. Reconnecting...");
				conn = null;
				connectToDatabase();
			} else if (conn.isClosed()) {
				EnderChest.log.warning("Database connection failed. Reconnecting...");
				conn = null;
				connectToDatabase();
			}
		} catch (Exception e) {
			EnderChest.log.severe("Error re-connecting to the database! Error: " + e.getMessage());
		}
	}

	/**
	 * @deprecated use {@link #close()}
	 */
	@Deprecated
	public void closeDatabase() {
		this.close();
	}

	@Override
	public void close() {
		if (conn != null) {
			try {
				conn.close();
				conn = null;
			} catch (SQLException e) {
				EnderChest.log.log(Level.SEVERE, "Failed shutting down database connection!", e);
			}
		}
	}

	private void updateTables() {
		if (conn != null) {
			try {
				DatabaseMetaData md = conn.getMetaData();
				try (ResultSet rs1 = md.getColumns(null, null, enderchest.getConfigHandler().getString("database.mysql.tableName"), "enderchest")) {
					if (rs1.next()) {
						String data1 = "ALTER TABLE `" + enderchest.getConfigHandler().getString("database.mysql.tableName") + "` CHANGE COLUMN enderchest enderchest_data LONGTEXT NOT NULL;";
						try (PreparedStatement query1 = conn.prepareStatement(data1)) {
							query1.execute();
						}
					} else {
						try (ResultSet rs2 = md.getColumns(null, null, enderchest.getConfigHandler().getString("database.mysql.tableName"), "enderchest_data")) {
							if (rs2.next() && rs2.getString("TYPE_NAME").matches("VARCHAR")) {
								String data2 = "ALTER TABLE `" + enderchest.getConfigHandler().getString("database.mysql.tableName") + "` MODIFY enderchest_data LONGTEXT NOT NULL;";
								try (PreparedStatement query2 = conn.prepareStatement(data2)) {
									query2.execute();
								}
							}
						}
					}
				}
			} catch (SQLException e) {
				EnderChest.log.log(Level.WARNING, "Error on table update!", e);
			}
		}
	}
}
