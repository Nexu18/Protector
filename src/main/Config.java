package main;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.YamlConfiguration;

public class Config {
	
	public static String ordner = "plugins/Protector";
	public static File configFile = new File(ordner = File.separator + "config.yml");
	public static YamlConfiguration config;
	
	private static YamlConfiguration loadConfig() {
		try {
			YamlConfiguration config = new YamlConfiguration();
			config.load(configFile);
			return config;
		}catch (Exception e) {
			e.printStackTrace();
		}return null;
	}
	
	public static void createConfig() {
		new File(ordner).mkdir();
		if(!configFile.exists()) {
			try {
				configFile.createNewFile();
				
				config = loadConfig();

				config.set("Test", "Testabc");
				
				config.save(configFile);
				Protector.getInstance().log.warning("Config Datei erstellt.");
				
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		
		config = loadConfig();
		Protector.getInstance().log.info("Config Datei geladen");
	}
	
	public static void reloadConfig() {
		new File(ordner).mkdir();
		if(!configFile.exists()) {
			try {
				configFile.createNewFile();
				
				config = loadConfig();
				config.save(configFile);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		config = loadConfig();
	}
	
	public static String get(String key) {
		reloadConfig();
		String value = config.getString(key);
		return value;
	}
	
	public static int readInt(String key) {
		reloadConfig();
		int value = config.getInt(key);
		return value;
	}
	
	public static boolean readBoolean(String key) {
		reloadConfig();
		boolean value = config.getBoolean(key);
		return value;
	}
	
	public static double readDouble(String key) {
		reloadConfig();
		double value = config.getDouble(key);
		return value;
	}
	
	public static void setString(String key, String string) {
		config.set(key, string);
		try {
			config.save(configFile);
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void setInt(String key, int intkey) {
		config.set(key, Integer.valueOf(intkey));
		try {
			config.save(configFile);
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void setBoolean(String key, boolean booleankey) {
		config.set(key, Boolean.valueOf(booleankey));
		try {
			config.save(configFile);
		}catch(IOException e) {
			e.printStackTrace();
		}
	}

}
