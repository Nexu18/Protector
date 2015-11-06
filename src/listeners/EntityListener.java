package listeners;

import java.util.Iterator;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

import clazz.Area;
import main.Protector;

public class EntityListener implements Listener{
	
	@EventHandler
	public void onEntityDamageByEntity(EntityDamageByEntityEvent event){//pvp
		if(event.getDamager() instanceof Player && event.getEntity() instanceof Player){
			for(Area area : Area.areas){
				if(area.isInside(event.getDamager().getLocation()) || area.isInside(event.getEntity().getLocation())){
					if(!area.permission("", "pvp")){
						event.setCancelled(true);
						return;
					}
				}
				
			}
		}else if(event.getDamager() instanceof Player){//pve
			for(Area area : Area.areas){
				if(!area.permission(((Player) event.getDamager()).getName(), "pve")){
					if(area.isInside(event.getDamager().getLocation()) || area.isInside(event.getEntity().getLocation())){
						event.setCancelled(true);
						return;
					}
				}
			}
		}else if(event.getEntity() instanceof Player){//pve
			for(Area area : Area.areas){
				if(!area.permission(((Player) event.getEntity()).getName(), "pve")){
					if(area.isInside(event.getDamager().getLocation()) || area.isInside(event.getEntity().getLocation())){
						event.setCancelled(true);
						return;
					}
				}
			}
		}
	}

}
