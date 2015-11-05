package listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import clazz.Area;
import main.Protector;

public class BlockBreakListener implements Listener{
	
	@EventHandler
	public static void onBlockBreak(BlockBreakEvent event){
		if(event.getPlayer() != null){
			for(Area area : Area.areas){
				if(area.isInside(event.getBlock()) && !area.permission(event.getPlayer().getName(), "build")){
					event.setCancelled(true);
				}
			}
		}
	}
	
	@EventHandler
	public static void onBlockPlace(BlockPlaceEvent event){
		if(event.getPlayer() != null && event.getBlock() != null){
			for(Area area : Area.areas){
				if(area.isInside(event.getBlock()) && !area.permission(event.getPlayer().getName(), "build")){
					event.setCancelled(true);
				}
			}
		}
	}

}
