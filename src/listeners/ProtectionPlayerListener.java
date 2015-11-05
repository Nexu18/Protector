package listeners;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import clazz.Area;
import main.Protector;

public class ProtectionPlayerListener implements Listener{
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event){
		if(event.getAction().equals(Action.RIGHT_CLICK_BLOCK)){
			if(event.getClickedBlock() != null){
				for(Area area : Area.areas){
					if(area.getBlock().getX() == event.getClickedBlock().getX() && area.getBlock().getZ() == event.getClickedBlock().getZ()){
						area.showMenu(event.getPlayer());
						event.setCancelled(true);
						return;
					}
					
				}
			}
		}
		if(event.getClickedBlock() != null){
			for(Area area : Area.areas){
				if(area.isInside(event.getClickedBlock()) && !area.permission(event.getPlayer().getName(), event.getClickedBlock()) 
						&& Protector.accessible(event.getClickedBlock().getType())){
					event.setCancelled(true);
				}
			}
		}
	}
	
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event){
		for(Area area : Area.areas){
			if(area.isInside(event.getPlayer().getLocation())){
				if(!area.permission(event.getPlayer().getName(), "entry")){
					event.setCancelled(true);
					area.lockOut(event.getPlayer());
					event.getPlayer().sendMessage("You are not allowed to enter this area. If you are stuck, try /warp spawn");
				}
			}
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerChat(AsyncPlayerChatEvent event){
		boolean cancel = false;
		for(Area area : Area.areas){
			if(area.listMenuParser(event.getMessage(), event.getPlayer())){
				cancel = true;
			}
		}
		event.setCancelled(cancel);
	}

}
