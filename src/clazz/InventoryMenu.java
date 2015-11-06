package clazz;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class InventoryMenu {
	public static HashMap<String, InventoryMenu> playersinmenu = new HashMap<String, InventoryMenu>();
	
	private Inventory inv;
	private HashMap<Integer, ItemStack> itemstacks;
	private Set<String> players;
	
	/**
	 * TITLE MUST CONTAIN "Protector"!!!
	 * ITEMSTACKS MUST CONTAIN ONE LOCATION ITEM!!!
	 */
	public InventoryMenu(String title, int size, HashMap<Integer, ItemStack> itemstacks){
		this.itemstacks = itemstacks;
		inv = Bukkit.createInventory(null, size, title);
		prepareItems();
		
		players = new HashSet<String>();
	}
	
	public void open(Player player){
		getPlayers().add(player.getName());
		playersinmenu.put(player.getName(), this);
		player.setCompassTarget(new Location(player.getWorld(),1000000, 64,0));
		player.openInventory(inv);
	}
	
	private void prepareItems(){
		for(int i : itemstacks.keySet()){
			inv.setItem(i, itemstacks.get(i));
		}
	}
	
	/**
	 * ITEMS MUST NOT CONTAIN AN ITEM NAMED "Location"
	 */
	public void update(HashMap<Integer, ItemStack> items){
		for(int i : items.keySet()){
			itemstacks.put(i, items.get(i));
			inv.setItem(i, items.get(i));
		}
		for(String player : getPlayers()){
			Bukkit.getPlayer(player).updateInventory();
		}
	}
	
	public void updateTitle(String title){
		inv = Bukkit.createInventory(null, inv.getSize(), title);
		prepareItems();
		for(String player : players){
			Bukkit.getPlayer(player).openInventory(inv);
		}
	}
	
	public Area getArea(){
		String desc = "";
		
		for(int i = 0; i < inv.getSize(); i++){
			if(inv.getItem(i) != null && inv.getItem(i).getItemMeta() != null && inv.getItem(i).getItemMeta().getDisplayName() != null 
					&& "§6Location".equalsIgnoreCase(inv.getItem(i).getItemMeta().getDisplayName())){
				desc = inv.getItem(i).getItemMeta().getLore().get(0);
			}
		}
		String[] loc = desc.split("f")[1].split(", ");
		int x = Integer.parseInt(loc[0]);
		int y = Integer.parseInt(loc[1]);
		int z = Integer.parseInt(loc[2]);
		Area currentArea = null;
		for(Area area : Area.areas){
			if(area.isAt(x, y, z)){
				currentArea = area;
			}
		}
		return currentArea;
	}
	
	public Inventory getInv(){
		return inv;
	}
	
	public void runAction(int pos, String player){
		
	}

	public Set<String> getPlayers() {
		return players;
	}
	
	public void close(){
		for(String player : players){
			Bukkit.getPlayer(player).closeInventory();
			playersinmenu.remove(player);
		}
	}

}