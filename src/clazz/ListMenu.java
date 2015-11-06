package clazz;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;

public class ListMenu {
	public static Set<ListMenu> listmenus = new HashSet<ListMenu>();
	Player player;
	Permission permission;
	String area;
	
	public ListMenu(Player player, Permission permission, String areaIdentifier){
		this.player = player;
		this.permission = permission;
		for(ListMenu menu : listmenus){
			if(menu.getPlayer().equals(player)){
				menu.close();
			}
		}
		area = areaIdentifier;
		listmenus.add(this);
		
		player.sendMessage("§5You are now editing the " + permission.getIdentifier() + " list for " + area + ".");
		player.sendMessage("§5Possible commands: add <player> remove <player> end");
		player.sendMessage("§5	add <player> [more players]");
		player.sendMessage("§5	remove <player> [more players]");
		player.sendMessage("§5	end");
		player.sendMessage("§5This permission is currently set to " + permission);
		player.sendMessage("§5The current list is:");
		for(String name : permission.set){
			player.sendMessage("§6" + name);
		}
	}
	
	public Player getPlayer(){
		return player;
	}
	
	public Permission getPermission(){
		return permission;
	}
	
	public Set<String> getList(){
		return permission.set;
	}
	
	public void close(){
		listmenus.remove(this);
	}
	
	public void parse(String message){
		String[] cmd = message.split(" ");
		if(cmd[0].equalsIgnoreCase("add")){
			for(int i = 1; i < cmd.length; i++){
				permission.set.add(cmd[i]);
				player.sendMessage("§5\"" + cmd[i] + "\" added to " + permission.getIdentifier() + " list of " + area + " successfully.");
			}
//			for(int i = 1; i < cmd.length; i++){
//				access.set.add(cmd[i]);
//				player.sendMessage("§5\"" + cmd[i] + "\" added to access list of area (" + srcBlock.getX() + ", " + srcBlock.getZ() + ") successfully.");
//			}
//			for(int i = 1; i < cmd.length; i++){
//				entry.set.add(cmd[i]);
//				player.sendMessage("§5\"" + cmd[i] + "\" added to entry list of area (" + srcBlock.getX() + ", " + srcBlock.getZ() + ") successfully.");
//			}
//			for(int i = 1; i < cmd.length; i++){
//				pve.set.add(cmd[i]);
//				player.sendMessage("§5\"" + cmd[i] + "\" added to PvE list of area (" + srcBlock.getX() + ", " + srcBlock.getZ() + ") successfully.");
//			}
		}
		else if(cmd[0].equalsIgnoreCase("remove")){
			for(int i = 1; i < cmd.length; i++){
				permission.set.remove(cmd[i]);
				player.sendMessage("§5\"" + cmd[i] + "\" removed from " + permission.getIdentifier() + " list of " + area + " successfully.");
			}
//			for(int i = 1; i < cmd.length; i++){
//				access.set.remove(cmd[i]);
//				player.sendMessage("§5\"" + cmd[i] + "\" removed from access list of area (" + srcBlock.getX() + ", " + srcBlock.getZ() + ") successfully.");
//			}
//			for(int i = 1; i < cmd.length; i++){
//				entry.set.remove(cmd[i]);
//				player.sendMessage("§5\"" + cmd[i] + "\" removed from entry list of area (" + srcBlock.getX() + ", " + srcBlock.getZ() + ") successfully.");
//			}
//			for(int i = 1; i < cmd.length; i++){
//				pve.set.remove(cmd[i]);
//				player.sendMessage("§5\"" + cmd[i] + "\" removed from PvE list of area (" + srcBlock.getX() + ", " + srcBlock.getZ() + ") successfully.");
//			}
		}
		else{
			player.sendMessage("§5Stopped editing the " + permission.getIdentifier() + " list of " + area + ".");
			listmenus.remove(this);
//			player.sendMessage("§5Stopped editing the access list of area (" + srcBlock.getX() + ", " + srcBlock.getZ() + ").");
//			player.sendMessage("§5Stopped editing the entry list of area (" + srcBlock.getX() + ", " + srcBlock.getZ() + ").");
//			player.sendMessage("§5Stopped editing the PvE list of area (" + srcBlock.getX() + ", " + srcBlock.getZ() + ").");
		}
	}

}
