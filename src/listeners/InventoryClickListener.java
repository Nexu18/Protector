package listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import clazz.Area;
import clazz.InventoryMenu;
import main.Protector;

public class InventoryClickListener implements Listener{
	
	@EventHandler
	public void onClick(InventoryClickEvent event) {
		if (!(event.getWhoClicked() instanceof Player)) {
			return;
		}
		
		Player player = (Player)event.getWhoClicked();
		
		if(event.getInventory() != null && event.getInventory().getName().contains("Protector - Area")) {
			event.setCancelled(true);
			if(event.getCurrentItem() != null) {
				ItemStack itemStack = event.getCurrentItem();
				
				//get Area
				String desc = "";
				
				for(int i = 0; i < event.getInventory().getSize(); i++){
					if(event.getInventory().getItem(i) != null && event.getInventory().getItem(i).getItemMeta() != null 
							&& event.getInventory().getItem(i).getItemMeta().getDisplayName() != null && "§6Location".equalsIgnoreCase(event.getInventory().getItem(i).getItemMeta().getDisplayName())){
						desc = event.getInventory().getItem(i).getItemMeta().getLore().get(0);
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
				if(currentArea == null){
					player.closeInventory();
					return;
				}
				
				try{
				for(InventoryMenu menu : currentArea.getInventoryMenus()){//
					if(menu.getInv().equals(event.getInventory())){
						menu.runAction(event.getSlot(), player.getName());
					}
				}}catch(Exception e){e.printStackTrace();}
				
//				if(itemStack.getItemMeta() != null && itemStack.getItemMeta().getDisplayName().contains("Enlarge North")) {
//					currentArea.enlarge(1, 'n');
//				}else if(itemStack.getItemMeta() != null && itemStack.getItemMeta().getDisplayName().contains("Enlarge South")) {
//					currentArea.enlarge(1, 's');
//				}else if(itemStack.getItemMeta() != null && itemStack.getItemMeta().getDisplayName().contains("Enlarge East")) {
//					currentArea.enlarge(1, 'e');
//				}else if(itemStack.getItemMeta() != null && itemStack.getItemMeta().getDisplayName().contains("Enlarge West")) {
//					currentArea.enlarge(1, 'w');
//				}
//				
//				else if(itemStack.getItemMeta() != null && itemStack.getItemMeta().getDisplayName().contains("Allow Building")) {
//					currentArea.togglePermission("build");
//				}else if(itemStack.getItemMeta() != null && itemStack.getItemMeta().getDisplayName().contains("Allow Access")) {
//					currentArea.togglePermission("access");
//				}else if(itemStack.getItemMeta() != null && itemStack.getItemMeta().getDisplayName().contains("Allow Entry")) {
//					currentArea.togglePermission("entry");
//				}
//				
//				else if(itemStack.getItemMeta() != null && itemStack.getItemMeta().getDisplayName().contains("Allow PvE")) {
//					currentArea.togglePermission("pve");
//				}else if(itemStack.getItemMeta() != null && itemStack.getItemMeta().getDisplayName().contains("Allow PvP")) {
//					currentArea.togglePermission("pvp");
//				}
//				
//				else if(itemStack.getItemMeta() != null && itemStack.getItemMeta().getDisplayName().contains("Edit Build List")) {
//					currentArea.listMenu(player, 'b');
//				}else if(itemStack.getItemMeta() != null && itemStack.getItemMeta().getDisplayName().contains("Edit Access List")) {
//					currentArea.listMenu(player, 'a');
//				}else if(itemStack.getItemMeta() != null && itemStack.getItemMeta().getDisplayName().contains("Edit Entry List")) {
//					currentArea.listMenu(player, 'e');
//				}else if(itemStack.getItemMeta() != null && itemStack.getItemMeta().getDisplayName().contains("Edit PvE List")) {
//					currentArea.listMenu(player, 'p');
//				}
//				
//				else if(itemStack.getItemMeta() != null && itemStack.getItemMeta().getDisplayName().contains("Edit Blocks")) {
//					//currentArea.showBlockMenu(player, 1);
//				}
//				
//				else if (itemStack.getItemMeta() != null && itemStack.getItemMeta().getDisplayName().contains("Abandon Area")) {
//					Area.areas.remove(currentArea);
//					player.closeInventory();
//				} else if (itemStack.getItemMeta() != null && itemStack.getItemMeta().getDisplayName().contains("Display Area")) {
//					currentArea.displayCorners(player);
//				}
			}
		}
		if(event.getInventory() != null && event.getInventory().getName().contains("Protector - BlockAccess")){
			event.setCancelled(true);
			if(event.getCurrentItem() != null) {
				ItemStack itemStack = event.getCurrentItem();
				
				//get Area
				String desc = event.getInventory().getItem(9*4+1).getItemMeta().getLore().get(0);
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
				if(currentArea == null){
					player.closeInventory();
					return;
				}
				
				try{
				for(InventoryMenu menu : currentArea.getInventoryMenus()){
					if(menu.getInv().equals(event.getInventory())){
						menu.runAction(event.getSlot(), player.getName());
					}
				}}catch(Exception e){System.out.println("komischeexception");}
				
//				if(itemStack.getItemMeta() != null && itemStack.getItemMeta().getDisplayName().contains("Previous")) {
//					//currentArea.showBlockMenu(player, page - 1);
//				}else if(itemStack.getItemMeta() != null && itemStack.getItemMeta().getDisplayName().contains("Next")) {
//					//currentArea.showBlockMenu(player, page + 1);
//				}else if(itemStack.getItemMeta() != null && itemStack.getItemMeta().getDisplayName().contains("Back")) {
//					currentArea.showMenu(player);
//				}
//				
//				
//				else if(itemStack.getItemMeta() != null && itemStack.getItemMeta().getDisplayName().contains("list")) {
//					//
//				}
			}
		}
	}

}
