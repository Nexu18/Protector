package listeners;

import java.util.Iterator;

import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockIgniteEvent.IgniteCause;

import clazz.Area;

public class FireListener implements Listener{
	
	@EventHandler
	public void onBlockBurn(BlockBurnEvent event){
		for(Area area : Area.areas){
			if(!area.permission(null, "fire") && area.isInside(event.getBlock())){
				event.setCancelled(true);
				return;
			}
		}
	}
	
	@EventHandler
	public void onBlockIgnite(BlockIgniteEvent event){
		if(IgniteCause.FLINT_AND_STEEL.equals(event.getCause()))
			return;
		for(Area area : Area.areas){
			if(!area.permission(null, "fire") && area.isInside(event.getBlock())){
				event.setCancelled(true);
				return;
			}
		}
	}

}
