package listeners;

import java.util.Iterator;

import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

import clazz.Area;

public class ExplodeListener implements Listener{
	
	@EventHandler
	public void onBlockExplode(BlockExplodeEvent event){
		Iterator<Block> iter = event.blockList().listIterator();
		while(iter.hasNext()){
			Block b = iter.next();
			for(Area area : Area.areas){
				if(!area.permission(null, "expl") && area.isInside(b)){
					iter.remove();
				}
			}
		}
	}
	
	@EventHandler
	public void onEntityExplode(EntityExplodeEvent event){
		Iterator<Block> iter = event.blockList().listIterator();
		while(iter.hasNext()){
			Block b = iter.next();
			for(Area area : Area.areas){
				if(!area.permission(null, "expl") && area.isInside(b)){
					iter.remove();
				}
			}
		}
	}

}
