package clazz;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;

import main.Protector;

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
		
		player.sendMessage(Protector.PREFIX + "§8You are now editing the §3" + permission.getIdentifier() + "§8 list for §3" + area + "§8.");
		player.sendMessage(Protector.PREFIX + "§8Possible commands: ");
		player.sendMessage(Protector.PREFIX + "§8    §3add§8 <§3player§8> [§3more players§8]");
		player.sendMessage(Protector.PREFIX + "§8    §3remove§8 <§3player§8> [§3more players§8]");
		player.sendMessage(Protector.PREFIX + "§8    §3end");
		player.sendMessage(Protector.PREFIX + "§8This permission is currently set to §3" + permission);
		player.sendMessage(Protector.PREFIX + "§8The current list is:");
		for(String name : permission.set){
			player.sendMessage(Protector.PREFIX + "§3" + name);
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
				player.sendMessage(Protector.PREFIX + "§8\"§3" + cmd[i] + "§8\" added to §3" + permission.getIdentifier() + "§8 list of §3" + area + "§8 successfully.");
			}
		}
		else if(cmd[0].equalsIgnoreCase("remove")){
			for(int i = 1; i < cmd.length; i++){
				permission.set.remove(cmd[i]);
				player.sendMessage(Protector.PREFIX + "§8\"§3" + cmd[i] + "§8\" removed from §3" + permission.getIdentifier() + "§8 list of §3" + area + "§8 successfully.");
			}
		}
		else{
			player.sendMessage(Protector.PREFIX + "§8Stopped editing the §3" + permission.getIdentifier() + "§8 list of §3" + area + "§8.");
			listmenus.remove(this);
		}
	}

}
