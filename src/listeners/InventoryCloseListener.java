package listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class InventoryCloseListener implements Listener{
	
	@EventHandler
	public void onClose(InventoryCloseEvent event) {
		if(event.getInventory() != null && event.getInventory().getName().contains("Protector") && event.getPlayer() instanceof Player) {
			Player player = (Player) event.getPlayer();
			player.setCompassTarget(player.getWorld().getSpawnLocation());
		}
	}

}
