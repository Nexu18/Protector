package main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.common.reflect.ClassPath;

import clazz.Area;
import clazz.CommandHandler;
import clazz.ItemHandler;

public class Protector extends JavaPlugin{
	
	public static HashMap<ItemStack, Integer> areaMenuItems;
	public Logger log;
	private static Protector instance;
	//public static List<Area> areas;
	
	public static final Material[] ACCESS = {Material.CHEST, Material.FURNACE, Material.BURNING_FURNACE, Material.WOODEN_DOOR, Material.BIRCH_DOOR, Material.DARK_OAK_DOOR, 
			Material.ACACIA_DOOR, Material.ACACIA_FENCE_GATE, Material.ARMOR_STAND, Material.BED, Material.BED_BLOCK, Material.BIRCH_FENCE_GATE, Material.BREWING_STAND, 
			Material.CAKE, Material.CAKE_BLOCK, Material.CAULDRON, Material.JUNGLE_FENCE_GATE, Material.DARK_OAK_FENCE_GATE, Material.SPRUCE_FENCE_GATE, Material.BEACON, 
			Material.DROPPER, Material.ANVIL, Material.ENCHANTMENT_TABLE, Material.JUKEBOX, Material.NOTE_BLOCK, Material.DARK_OAK_DOOR, Material.IRON_DOOR_BLOCK, 
			Material.SPRUCE_DOOR, Material.JUNGLE_DOOR, Material.WOODEN_DOOR, Material.TRAP_DOOR, Material.IRON_TRAPDOOR, Material.TRAPPED_CHEST, Material.DISPENSER, 
			Material.HOPPER, Material.FENCE_GATE, Material.FENCE, Material.ACACIA_FENCE, Material.BIRCH_FENCE, Material.DARK_OAK_FENCE,
			Material.IRON_FENCE, Material.JUNGLE_FENCE, Material.NETHER_FENCE, Material.SPRUCE_FENCE, Material.LEASH};
	
	public static boolean accessible(Material material){
		for(int i = 0; i < ACCESS.length; i++){
			if(ACCESS[i] == material){
				return true;
			}
		}
		return false;
	}
	
	private void setup() {
		CommandHandler.handle();
		Config.createConfig();
		
		Area.areas = new ArrayList<Area>();
		System.out.println(Area.areas);
		areaMenuItems = new HashMap<ItemStack, Integer>();
		
		
		PluginManager pluginManager = Bukkit.getPluginManager();
		try {
			for (ClassPath.ClassInfo classInfo : ClassPath.from(getClassLoader()).getTopLevelClasses("listeners")) {
				@SuppressWarnings("rawtypes")
				Class clazz = Class.forName(classInfo.getName());
				
				if (Listener.class.isAssignableFrom(clazz)) {
					pluginManager.registerEvents((Listener) clazz.newInstance(), this);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
	
	@Override
	public void onEnable(){
		instance = this;
		log = Logger.getLogger("Minecraft");
		setup();
		log.info("Protector has been enabled sucsessfully!");
	}
	
	@Override
	public void onDisable(){
		for(Player player : Bukkit.getServer().getOnlinePlayers()){
			if(player.getOpenInventory() != null && player.getOpenInventory().getTitle() != null && player.getOpenInventory().getTitle().contains("Protector")){
				player.closeInventory();
			}
		}
		for(Area area : Area.areas){
			area.stopDisplay();
		}
		instance = null;
		log.info("Protector has been disabled sucsessfully!");
	}

	
	public static Protector getInstance() {
		return instance;
	}
}
