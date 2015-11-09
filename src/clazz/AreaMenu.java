package clazz;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import main.Protector;

public class AreaMenu extends InventoryMenu{

	public AreaMenu(Area area) {
		super("Protector - Area", 27, getAllItems(area));
	}
	
	@Override
	public void runAction(int pos, String name){
		ItemStack itemStack = getInv().getItem(pos);
		Player player = Bukkit.getPlayer(name);
		if(itemStack == null || itemStack.getItemMeta() == null){
			return;
		}
		
		if(itemStack.getItemMeta() != null && itemStack.getItemMeta().getDisplayName().contains("Enlarge North")) {
			getArea().enlarge(1, 'n');
		}else if(itemStack.getItemMeta() != null && itemStack.getItemMeta().getDisplayName().contains("Enlarge South")) {
			getArea().enlarge(1, 's');
		}else if(itemStack.getItemMeta() != null && itemStack.getItemMeta().getDisplayName().contains("Enlarge East")) {
			getArea().enlarge(1, 'e');
		}else if(itemStack.getItemMeta() != null && itemStack.getItemMeta().getDisplayName().contains("Enlarge West")) {
			getArea().enlarge(1, 'w');
		}
		
		else if(itemStack.getItemMeta() != null && itemStack.getItemMeta().getDisplayName().contains("Allow Building")) {
			getArea().togglePermission("build");
		}else if(itemStack.getItemMeta() != null && itemStack.getItemMeta().getDisplayName().contains("Allow Access")) {
			getArea().togglePermission("access");
		}else if(itemStack.getItemMeta() != null && itemStack.getItemMeta().getDisplayName().contains("Allow Entry")) {
			getArea().togglePermission("entry");
		}
		
		else if(itemStack.getItemMeta() != null && itemStack.getItemMeta().getDisplayName().contains("Allow PvE")) {
			getArea().togglePermission("pve");
		}else if(itemStack.getItemMeta() != null && itemStack.getItemMeta().getDisplayName().contains("Allow PvP")) {
			getArea().togglePermission("pvp");
		}else if(itemStack.getItemMeta() != null && itemStack.getItemMeta().getDisplayName().contains("Allow Fire")) {
			getArea().togglePermission("fire");
		}else if(itemStack.getItemMeta() != null && itemStack.getItemMeta().getDisplayName().contains("Allow Explosions")) {
			getArea().togglePermission("expl");
		}
		
		else if(itemStack.getItemMeta() != null && itemStack.getItemMeta().getDisplayName().contains("Edit Build List")) {
			getArea().showListMenu("build", player);
			super.close();
		}else if(itemStack.getItemMeta() != null && itemStack.getItemMeta().getDisplayName().contains("Edit Access List")) {
			getArea().showListMenu("access", player);
			super.close();
		}else if(itemStack.getItemMeta() != null && itemStack.getItemMeta().getDisplayName().contains("Edit Entry List")) {
			getArea().showListMenu("entry", player);
			super.close();
		}else if(itemStack.getItemMeta() != null && itemStack.getItemMeta().getDisplayName().contains("Edit PvE List")) {
			getArea().showListMenu("pve", player);
			super.close();
		}
		
		else if(itemStack.getItemMeta() != null && itemStack.getItemMeta().getDisplayName().contains("Edit Blocks")) {
			//getArea().showBlockMenu(player, 1);
			getArea().showBlockAccessMenu(player);
		}
		
		else if (itemStack.getItemMeta() != null && itemStack.getItemMeta().getDisplayName().contains("Abandon Area")) {
			getArea().removeOwner(player.getName());
		} else if (itemStack.getItemMeta() != null && itemStack.getItemMeta().getDisplayName().contains("Display Area")) {
			getArea().displayCorners(player);
		}
		
		//update
		update(getAllItems(getArea()));
		for(String thisplayer : getPlayers()){
			Bukkit.getPlayer(thisplayer).updateInventory();
		}
	}
	
	private static HashMap<Integer, ItemStack> getAllItems(Area area){
		HashMap<Integer, ItemStack> items = new HashMap<Integer, ItemStack>();
		HashMap<Integer, ItemStack> stat = getStaticItems();
		HashMap<Integer, ItemStack> dyn = getDynItems(area);
		for(int i : stat.keySet()){
			items.put(i, stat.get(i));
		}
		for(int i : dyn.keySet()){
			items.put(i, dyn.get(i));
		}
		return items;
	}
	
	private static HashMap<Integer, ItemStack> getStaticItems(/*Area area*/){
		HashMap<Integer, ItemStack> items = new HashMap<Integer, ItemStack>();
		
		
		//static items
		items.put(1, ItemHandler.createItem(Material.WOOL, "§8Enlarge North", 1, 13, "§8Makes the Area bigger by one block."));
		items.put(10, ItemHandler.createItem(Material.COMPASS, "§8North", 1));
		items.put(19, ItemHandler.createItem(Material.WOOL, "§8Decrease North", 1, 14, "§8Makes the Area smaller by one block."));
		
		items.put(2, ItemHandler.createItem(Material.WOOL, "§8Enlarge South", 1, 13, "§8Makes the Area bigger by one block."));
		items.put(11, ItemHandler.createItem(Material.COMPASS, "§8South", 1));
		items.put(20, ItemHandler.createItem(Material.WOOL, "§8Decrease South", 1, 14, "§8Makes the Area smaller by one block."));
		
		items.put(3, ItemHandler.createItem(Material.WOOL, "§8Enlarge East", 1, 13, "§8Makes the Area bigger by one block."));
		items.put(12, ItemHandler.createItem(Material.COMPASS, "§8East", 1));
		items.put(21, ItemHandler.createItem(Material.WOOL, "§8Decrease East", 1, 14, "§8Makes the Area smaller by one block."));
		
		items.put(4, ItemHandler.createItem(Material.WOOL, "§8Enlarge West", 1, 13, "§8Makes the Area bigger by one block."));
		items.put(13, ItemHandler.createItem(Material.COMPASS, "§8West", 1));
		items.put(22, ItemHandler.createItem(Material.WOOL, "§8Decrease West", 1, 14, "§8Makes the Area smaller by one block."));
		
		items.put(9, ItemHandler.createItem(Material.GLASS, "§8Display Area", 1, "§8Shows you the bounds of this area."));
//		items.put(5, ItemHandler.createItem(Material.BRICK, "§6Allow Building", 1, ""));
//		items.put(6, ItemHandler.createItem(Material.GOLD_INGOT, "§6Allow Access", 1, ""));
//		items.put(7, ItemHandler.createItem(Material.FENCE_GATE, "§6Allow Entry", 1, ""));
//		items.put(24, ItemHandler.createItem(Material.CHAINMAIL_HELMET, "§6Allow PvP", 1, "", "§5Toggle PvP in your area."));
//		items.put(8, ItemHandler.createItem(Material.MOB_SPAWNER, "§6Allow PvE", 1, "", "§5Allow or restrict PvE in your area."));
//		items.put(26, ItemHandler.createItem(Material.STONE, "§6Edit Blocks", 1, "§5Edit block-specific access rules."));
//		items.put(14, ItemHandler.createItem(Material.PAPER, "§6Edit Build List", 1, "§5Edit listed people for building."));
//		items.put(15, ItemHandler.createItem(Material.PAPER, "§6Edit Access List", 1, "§5Edit listed people for access."));
//		items.put(16, ItemHandler.createItem(Material.PAPER, "§6Edit Entry List", 1, "§5Edit listed people for entry."));
//		items.put(17, ItemHandler.createItem(Material.PAPER, "§6Edit PvE List", 1, "§5Edit listed people for PvE."));
		items.put(18, ItemHandler.createItem( Material.BEDROCK, "§4Abandon Area", 1, "§8Deletes this Area. All territory will be lost!"));
		
//		//dynamic items
//		items.put(0, ItemHandler.createItem(Material.GOLD_BLOCK, "§6Location", 1, "§f" + area.getBlock().getX() + ", " + area.getBlock().getY() + ", " 
//				+ area.getBlock().getZ()));
//		for(int i  : items.keySet()){
//			if(items.get(i).getItemMeta().getDisplayName().contains("Allow Building")){
//				items.put(i, ItemHandler.editDescription(items.get(i), "§f" + area.build, 0));
//			}else if(items.get(i).getItemMeta().getDisplayName().contains("Allow Access")){
//				items.put(i, ItemHandler.editDescription(items.get(i), "§f" + area.access, 0));
//			}else if(items.get(i).getItemMeta().getDisplayName().contains("Allow Entry")){
//				items.put(i, ItemHandler.editDescription(items.get(i), "§f" + area.entry, 0));
//			}else if(items.get(i).getItemMeta().getDisplayName().contains("Allow PvE")){
//				items.put(i, ItemHandler.editDescription(items.get(i), "§f" + area.pve, 0));
//			}else if(items.get(i).getItemMeta().getDisplayName().contains("Allow PvP")){
//				items.put(i, ItemHandler.editDescription(items.get(i), "§f" + area.permissionBoolToString(area.allowpvp), 0));
//			}
//			
//		}
		
		return items;
	}
	
	private static HashMap<Integer, ItemStack> getDynItems(Area area){
		HashMap<Integer, ItemStack> items = new HashMap<Integer, ItemStack>();
		
		items.put(5, ItemHandler.createItem(Material.BRICK, "§8Allow Building", 1, "", "§8Allow or restrict building."));
		items.put(6, ItemHandler.createItem(Material.GOLD_INGOT, "§8Allow Access", 1, "", "§8Allow or restrict access (Chests, Furnaces, etc)."));
		items.put(7, ItemHandler.createItem(Material.FENCE_GATE, "§8Allow Entry", 1, "", "§8Allow or restrict entry."));
		items.put(23, ItemHandler.createItem(Material.CHAINMAIL_HELMET, "§8Allow PvP", 1, "", "§8Allow or restrict PvP."));
		items.put(24, ItemHandler.createItem(Material.FLINT_AND_STEEL, "§8Allow Fire", 1, "", "§8Enable or disable fire spread"));
		items.put(25, ItemHandler.createItem(Material.TNT, "§8Allow Explosions", 1, "", "§8Enable or disable explosion block damage."));
		items.put(8, ItemHandler.createItem(Material.MOB_SPAWNER, "§8Allow PvE", 1, "", "§8Allow or restrict PvE."));
		items.put(26, ItemHandler.createItem(Material.STONE, "§8Edit Blocks", 1, "§8Edit block-specific access rules."));
		
		items.put(14, ItemHandler.createListItem("§8Edit Build List", area.build.set, Material.PAPER));
		items.put(15, ItemHandler.createListItem("§8Edit Access List", area.access.set, Material.PAPER));
		items.put(16, ItemHandler.createListItem("§8Edit Entry List", area.entry.set, Material.PAPER));
		items.put(17, ItemHandler.createListItem("§8Edit PvE List", area.pve.set, Material.PAPER));
		
		items.put(0, area.getLocationItem());
		for(int i  : items.keySet()){
			if(items.get(i).getItemMeta().getDisplayName().contains("Allow Building")){
				items.put(i, ItemHandler.editDescription(items.get(i), "§3" + area.build, 0));
			}else if(items.get(i).getItemMeta().getDisplayName().contains("Allow Access")){
				items.put(i, ItemHandler.editDescription(items.get(i), "§3" + area.access, 0));
			}else if(items.get(i).getItemMeta().getDisplayName().contains("Allow Entry")){
				items.put(i, ItemHandler.editDescription(items.get(i), "§3" + area.entry, 0));
			}else if(items.get(i).getItemMeta().getDisplayName().contains("Allow PvE")){
				items.put(i, ItemHandler.editDescription(items.get(i), "§3" + area.pve, 0));
			}else if(items.get(i).getItemMeta().getDisplayName().contains("Allow PvP")){
				items.put(i, ItemHandler.editDescription(items.get(i), "§3" + area.permissionBoolToString(area.allowpvp), 0));
			}else if(items.get(i).getItemMeta().getDisplayName().contains("Allow Fire")){
				items.put(i, ItemHandler.editDescription(items.get(i), "§3" + area.permissionBoolToString(area.allowfire), 0));
			}else if(items.get(i).getItemMeta().getDisplayName().contains("Allow Explosions")){
				items.put(i, ItemHandler.editDescription(items.get(i), "§3" + area.permissionBoolToString(area.allowexpl), 0));
			}
			
		}
		
		return items;
	}

}
