package net.craftersland.customenderchest;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.craftersland.customenderchest.commands.FileToMysqlCmd;
import net.craftersland.customenderchest.sound.*;
import net.craftersland.customenderchest.storage.FlatFileStorage;
import net.craftersland.customenderchest.storage.database.DatabaseSetup;
import net.craftersland.customenderchest.storage.database.MysqlSetup;
import net.craftersland.customenderchest.storage.MysqlStorage;
import net.craftersland.customenderchest.storage.StorageInterface;
import net.craftersland.customenderchest.utils.EnderChestUtils;
import net.craftersland.customenderchest.utils.ModdedSerializer;

import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class EnderChest extends JavaPlugin {

	private static final Pattern VERSION_PATTERN = Pattern.compile("(?<major>\\d+)\\.(?<minor>\\d+)(?:\\.(?<patch>\\d+))?");

	public static Logger log;
	public Map<Inventory, UUID> admin = new HashMap<>();
	public static boolean is19Server = true;
	public static boolean is13Server = false;
	public static String pluginName = "CustomEnderChest";
	
	private static ConfigHandler configHandler;
	private static StorageInterface storageInterface;
	private static EnderChestUtils enderchestUtils;
	private static DataHandler dH;
	private static DatabaseSetup mysqlSetup;
	private static SoundHandler sH;
	private static ModdedSerializer ms;
	private static FileToMysqlCmd ftmc;
	
		public void onEnable() {
			log = getLogger();
			getMcVersion();	    	
	        configHandler = new ConfigHandler(this);
	        checkForModdedNbtSupport();
	        enderchestUtils = new EnderChestUtils(this);
	        if (configHandler.getString("database.typeOfDatabase").equalsIgnoreCase("mysql")) {
	        	log.info("Using MySQL database for data.");
	        	mysqlSetup = new MysqlSetup(this);
	        	storageInterface = new MysqlStorage(this);
	        } else {
	        	log.info("Using FlatFile system for data. IMPORTANT! We recommend MySQL.");
	        	File pluginFolder = new File("plugins" + System.getProperty("file.separator") + pluginName + System.getProperty("file.separator") + "PlayerData");
	    		if (!pluginFolder.exists()) {
	        		pluginFolder.mkdir();
	        	}
		      	storageInterface = new FlatFileStorage(this);	
	        }
	        dH = new DataHandler(this);
	        if (configHandler.getBoolean("settings.disable-sounds")) {
	        	sH = new DisabledSoundHandler();
			} else if (is13Server) {
	        	sH = new Spigot13SoundHandler();
			} else if (is19Server) {
	        	sH = new Spigot9SoundHandler();
			} else {
	        	sH = new SpigotLegacySoundHandler();
			}
	        ftmc = new FileToMysqlCmd(this);
	    	PluginManager pm = getServer().getPluginManager();
	    	pm.registerEvents(new PlayerHandler(this), this);
	    	CommandHandler cH = new CommandHandler(this);
	    	getCommand("customec").setExecutor(cH);
	    	getCommand("ec").setExecutor(cH);
	    	getCommand("customenderchest").setExecutor(cH);
	    	log.info(pluginName + " loaded successfully!");
		}
		
		//Disabling plugin
		public void onDisable() {
			Bukkit.getScheduler().cancelTasks(this);
			if (configHandler.getString("database.typeOfDatabase").equalsIgnoreCase("mysql")) {
				mysqlSetup.close();
			}
			log.info("Cleaning internal data...");
			dH.clearLiveData();
			HandlerList.unregisterAll(this);
			log.info(pluginName + " is disabled!");
		}
		
		private boolean getMcVersion() {
			String version = Bukkit.getBukkitVersion().split("-")[0];

			Matcher versionMatcher = VERSION_PATTERN.matcher(version);
			if (!versionMatcher.matches()) {
				is19Server = true;
				is13Server = true;
				log.warning("Unable to identify server version: " + version);
				log.warning("Attempting to run in 1.13 - 1.15 API mode.");
				return false;
			}
			int major = Integer.parseInt(versionMatcher.group("major"));
			int minor = Integer.parseInt(versionMatcher.group("minor"));

			if (major == 1) {
				if (minor < 7) {
					is19Server = false;
					is13Server = false;
					log.warning("Very old and unsupported server version detected: " + version);
					log.warning("Attempting to run in legacy (pre 1.9) API mode.");
					return false;
				} else if (minor < 9) {
					is19Server = false;
					is13Server = false;
					log.info("Compatible server version detected: " + version);
					return true;
				} else if (minor < 13) {
					is19Server = true;
					is13Server = false;
					log.info("Compatible server version detected: " + version);
					return true;
				} else if (minor < 16) {
					is19Server = true;
					is13Server = true;
					log.info("Compatible server version detected: " + version);
					return true;
				}
			}

			is19Server = true;
			is13Server = true;
			log.warning("Unknown server version detected: " + version);
			log.warning("Attempting to run in 1.13 - 1.15 API mode.");
		    return false;
		}
		
		private void checkForModdedNbtSupport() {
			if (configHandler.getBoolean("settings.modded-NBT-data-support")) {
				if (configHandler.getString("database.typeOfDatabase").equalsIgnoreCase("mysql")) {
					if (Bukkit.getPluginManager().getPlugin("ProtocolLib") != null) {
						ms = new ModdedSerializer(this);
						log.info("ProtocolLib dependency found. Modded NBT data support is enabled!");
			        } else {
			        	log.warning("ProtocolLib dependency not found!!! Modded NBT data support is disabled!");
			        }
				} else {
					log.warning("NBT Modded data support only works for MySQL storage. Modded NBT data support is disabled!");
				}
			}
	    }
		
		public ConfigHandler getConfigHandler() {
			return configHandler;
		}
		public StorageInterface getStorageInterface() {
			return storageInterface;
		}
		public EnderChestUtils getEnderChestUtils() {
			return enderchestUtils;
		}
		public DatabaseSetup getMysqlSetup() {
			return mysqlSetup;
		}
		public SoundHandler getSoundHandler() {
			return sH;
		}
		public DataHandler getDataHandler() {
			return dH;
		}
		public ModdedSerializer getModdedSerializer() {
			return ms;
		}
		public FileToMysqlCmd getFileToMysqlCmd() {
			return ftmc;
		}

}
