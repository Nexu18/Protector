package commands;

import java.util.Set;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import clazz.Area;
import main.Protector;

public class CommandProtect implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player){
			Player player = (Player) sender;
			if(args.length == 0){
				Block block = player.getTargetBlock((Set<Material>)null, 20);
				if(block.getType().equals(Material.GOLD_BLOCK)){
					Area area = new Area(block,player);
					if(area.getBlock() != null){
						Area.areas.add(area);
						player.sendMessage("Protection created successfully at (" + block.getX() + "," + block.getZ() + ").");
						return true;
					}
					player.sendMessage("Could not create Protection.");
					
				}
				
			}else if(args.length == 1 && args[0].equalsIgnoreCase("block")){
				Block block = player.getTargetBlock((Set<Material>)null, 20);
				if(block != null && !block.getType().equals(Material.AIR)){
					for(Area area : Area.areas){
						if(area.isInside(block.getLocation()) && area.isOwner(player.getName())){
							area.addAccessBlock(block);
						}
					}
				}
			}else if(args.length == 1 && args[0].equalsIgnoreCase("debug")){
				player.chat("hi");
				//create area and fill with blocks
//				Block block = player.getTargetBlock((Set<Material>)null, 20);
//				if(block.getType().equals(Material.GOLD_BLOCK)){
//					Area area = new Area(block,player);
//					if(area.getBlock() != null){
//						Area.areas.add(area);
//						player.sendMessage("Protection created successfully at (" + block.getX() + "," + block.getZ() + ").");
//						for(int i = 0; i < 200; i++){
//							Vector vec = new Vector((Math.random()-0.5)*2*5, Math.random()*(-64), (Math.random()-0.5)*2*5);
//							Block block2 = block.getLocation().getWorld().getBlockAt(block.getLocation().add(vec));
//							if(block2 != null && !block2.getType().equals(Material.AIR)){
//								for(Area area2 : Area.areas){
//									if(area2.isInside(block2.getLocation()) && area2.isOwner(player.getName())){
//										area2.addAccessBlock(block2);
//										System.out.println("Added block " + block2 + " to area " +area);
//									}
//								}
//							}
//						}
//						
//						return true;
//					}
//					player.sendMessage("Could not create Protection.");
//					
//				}
				
			}
				
		} else{
			sender.sendMessage("This command works for players only. (Yes, console discrimination)");
		}
		return true;
	}

}
