package clazz;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.util.Vector;

import main.Protector;

public class Area {
	public static List<Area> areas;
	/*es muss gespeichert werden:
	 * srcblock (block)
	 * owners (uuid set)
	 * mehrere permission
	 * blockallow (map<block, permission)
	 * 2 vector(x, z)
	 */
	
	private Block srcBlock;
	private Set<String> owners;
	private Set<InventoryMenu> menus;
	private Set<ListMenu> listmenus;
	
	String identifier;
	
	boolean allowpvp;
	boolean allowfire;
	boolean allowexpl;
	Permission pve;
	Permission build;
	Permission access;
	Permission entry;
	HashMap<Block, Permission> blockaccess;//0: only owner, 1: owner + listeded people 2: everyone but listed people 3: everyone
	List<Block> blocksforaccess;
	
	private Vector x;
	private Vector z;
	HashMap<Player, ArrayList<Block>> displayedblocklist = new HashMap<Player, ArrayList<Block>>();
	Map<String, Character> playersInListMenu;
	
	public Area(Block block, Player owner){
		Vector newx = new Vector(2, 0, 2);
		Vector newz = new Vector(-2, 0, -2);
		int highx = newx.getBlockX() + block.getX();
		int highz = newx.getBlockZ() + block.getZ();
		int lowx = newz.getBlockX() + block.getX();
		int lowz = newz.getBlockZ() + block.getZ();
		for(Area area : areas){
			if(highx >= area.getLowerX() && area.getHigherX() >= lowx && area.getHigherZ() >= lowz && highz >= area.getLowerZ()){
				System.out.println("Could not create area, collision: " + area);
				owner = null;
				srcBlock = null;
				return;
			}
		}
		this.srcBlock = block;
		//this.owner = owner;
		this.owners = new HashSet<String>();
		this.owners.add(owner.getName());
		
		menus = new HashSet<InventoryMenu>();
		x = newx;
		z = newz;
		allowpvp = false;
		pve = new Permission(Perm.OWNER_ONLY, "PvE");
		build = new Permission(Perm.OWNER_ONLY, "build");
		access = new Permission(Perm.OWNER_ONLY, "access");
		entry = new Permission(Perm.EVERYONE, "entry");
		blockaccess = new HashMap<Block, Permission>();
		blocksforaccess = new ArrayList<Block>();
		playersInListMenu = new HashMap<String, Character>();
		allowfire = false;
		allowexpl= false;
		
		identifier = "Area at (" + srcBlock.getX() + ", " + srcBlock.getY() + ", " + srcBlock.getZ() + ")";
		
	}
	
	public Area(Block block, Player owner, String identifier){
		Vector newx = new Vector(2, 0, 2);
		Vector newz = new Vector(-2, 0, -2);
		int highx = newx.getBlockX() + block.getX();
		int highz = newx.getBlockZ() + block.getZ();
		int lowx = newz.getBlockX() + block.getX();
		int lowz = newz.getBlockZ() + block.getZ();
		for(Area area : areas){
			if(highx >= area.getLowerX() && area.getHigherX() >= lowx && area.getHigherZ() >= lowz && highz >= area.getLowerZ()){
				System.out.println("Could not create area, collision: " + area);
				owner = null;
				srcBlock = null;
				return;
			}
		}
		this.srcBlock = block;
		//this.owner = owner;
		this.owners = new HashSet<String>();
		this.owners.add(owner.getName());
		
		
		menus = new HashSet<InventoryMenu>();
		x = newx;
		z = newz;
		allowpvp = false;
		pve = new Permission(Perm.OWNER_ONLY, "PvE");
		build = new Permission(Perm.OWNER_ONLY, "build");
		access = new Permission(Perm.OWNER_ONLY, "access");
		entry = new Permission(Perm.EVERYONE, "entry");
		blockaccess = new HashMap<Block, Permission>();
		blocksforaccess = new ArrayList<Block>();
		playersInListMenu = new HashMap<String, Character>();
		
		this.identifier = identifier;
		
	}
	
	public Block getBlock(){
		return srcBlock;
	}
	
	public List<Block> getBlocks(){
		return blocksforaccess;
	}
	
	public Set<String> getOwners(){
		return owners;
	}
	
	public Vector getVecX(){
		return x;
	}
	
	public Vector getVecZ(){
		return z;
	}
	
	public Set<InventoryMenu> getInventoryMenus(){
		return menus;
	}
	
	public String permissionBoolToString(boolean bool){
		if(bool){
			return "allowed";
		}
		return "not allowed";
	}
	
	/**
	 * Will remove this block if it exists
	 * @param block
	 */
	public void addAccessBlock(Block block){
		if(blockaccess.keySet().contains(block)){
			blockaccess.remove(block);
			blocksforaccess.remove(block);
		}else{
			blockaccess.put(block, new Permission(Perm.OWNER_ONLY, "block access (" + block.getX() + ", " + block.getY() + ", " + block.getZ() + ")"));
			blocksforaccess.add(block);
		}
	}
	
	/**
	 * DO NOT USE THIS FOR SPECIFIC BLOCK PERMISSIONS.
	 */
	public void togglePermission(String str){
		if("pvp".equalsIgnoreCase(str)){
			if(!allowpvp)
					allowpvp = true;
			else
				allowpvp = false;
		}else if("fire".equalsIgnoreCase(str)){
			if(!allowfire)
				allowfire = true;
			else
				allowfire = false;
		}else if("expl".equalsIgnoreCase(str)){
			if(!allowexpl)
				allowexpl = true;
			else
				allowexpl = false;
		}else if("pve".equalsIgnoreCase(str)){
			pve.toggle();
		}else if("build".equalsIgnoreCase(str)){
			build.toggle();
		}else if("access".equalsIgnoreCase(str)){
			access.toggle();
		}else if("entry".equalsIgnoreCase(str)){
			entry.toggle();
		}
	}
	
	public void togglePermission(Block block){
		if(blockaccess.containsKey(block)){
			blockaccess.get(block).toggle();
		}
	}
	
	public void listMenu(Player player, char list){
		for(Area area : areas){
			if(area.playersInListMenu.containsKey(player.getName())){
				player.sendMessage("§5You are currently editing another list. Type \"end\" to stop.");
				return;
			}
		}
		player.closeInventory();
		playersInListMenu.put(player.getName(), list);
		switch(list){//b build a access e entry p pve
		case 'b':
			player.sendMessage("§5You are now editing the build list for area (" + srcBlock.getX() + ", " + srcBlock.getZ() + ").");
			player.sendMessage("§5Possible commands: add <player> remove <player> end");
			player.sendMessage("§5The current build list is:");
			for(String name : build.set){
				player.sendMessage("§6" + name);
			}
			break;
		case 'a':
			player.sendMessage("§5You are now editing the access list for area (" + srcBlock.getX() + ", " + srcBlock.getZ() + ").");
			player.sendMessage("§5Possible commands: add <player> remove <player> end");
			player.sendMessage("§5The current access list is:");
			for(String name : build.set){
				player.sendMessage("§6" + name);
			}
			break;
		case 'e':
			player.sendMessage("§5You are now editing the entry list for area (" + srcBlock.getX() + ", " + srcBlock.getZ() + ").");
			player.sendMessage("§5Possible commands: add <player> remove <player> end");
			player.sendMessage("§5The current entry list is:");
			for(String name : build.set){
				player.sendMessage("§6" + name);
			}
			break;
		case 'p':
			player.sendMessage("§5You are now editing the PvE list for area (" + srcBlock.getX() + ", " + srcBlock.getZ() + ").");
			player.sendMessage("§5Possible commands: add <player> remove <player> end");
			player.sendMessage("§5The current entry list is:");
			for(String name : pve.set){
				player.sendMessage("§6" + name);
			}
			break;
		}
		
	}
	
//	public boolean listMenuParser(String s, Player player){
//		if(!playersInListMenu.keySet().contains(player.getName())){
//			return false;
//		}
//		char list = playersInListMenu.get(player.getName());
//		String[] cmd = s.split(" ");
//		if(cmd[0].equalsIgnoreCase("add")){
//			switch(list){
//			case 'b':
//				for(int i = 1; i < cmd.length; i++){
//					build.set.add(cmd[i]);
//					player.sendMessage("§5\"" + cmd[i] + "\" added to build list of area (" + srcBlock.getX() + ", " + srcBlock.getZ() + ") successfully.");
//				}
//				break;
//			case 'a':
//				for(int i = 1; i < cmd.length; i++){
//					access.set.add(cmd[i]);
//					player.sendMessage("§5\"" + cmd[i] + "\" added to access list of area (" + srcBlock.getX() + ", " + srcBlock.getZ() + ") successfully.");
//				}
//				break;
//			case 'e':
//				for(int i = 1; i < cmd.length; i++){
//					entry.set.add(cmd[i]);
//					player.sendMessage("§5\"" + cmd[i] + "\" added to entry list of area (" + srcBlock.getX() + ", " + srcBlock.getZ() + ") successfully.");
//				}
//				break;
//			case 'p':
//				for(int i = 1; i < cmd.length; i++){
//					pve.set.add(cmd[i]);
//					player.sendMessage("§5\"" + cmd[i] + "\" added to PvE list of area (" + srcBlock.getX() + ", " + srcBlock.getZ() + ") successfully.");
//				}
//				break;
//			}
//		}
//		else if(cmd[0].equalsIgnoreCase("remove")){
//			switch(list){
//			case 'b':
//				for(int i = 1; i < cmd.length; i++){
//					build.set.remove(cmd[i]);
//					player.sendMessage("§5\"" + cmd[i] + "\" removed from build list of area (" + srcBlock.getX() + ", " + srcBlock.getZ() + ") successfully.");
//				}
//				break;
//			case 'a':
//				for(int i = 1; i < cmd.length; i++){
//					access.set.remove(cmd[i]);
//					player.sendMessage("§5\"" + cmd[i] + "\" removed from access list of area (" + srcBlock.getX() + ", " + srcBlock.getZ() + ") successfully.");
//				}
//				break;
//			case 'e':
//				for(int i = 1; i < cmd.length; i++){
//					entry.set.remove(cmd[i]);
//					player.sendMessage("§5\"" + cmd[i] + "\" removed from entry list of area (" + srcBlock.getX() + ", " + srcBlock.getZ() + ") successfully.");
//				}
//				break;
//			case 'p':
//				for(int i = 1; i < cmd.length; i++){
//					pve.set.remove(cmd[i]);
//					player.sendMessage("§5\"" + cmd[i] + "\" removed from PvE list of area (" + srcBlock.getX() + ", " + srcBlock.getZ() + ") successfully.");
//				}
//				break;
//			}
//		}
//		else{
//			playersInListMenu.remove(player.getName());
//			switch(list){
//			case 'b':
//				player.sendMessage("§5Stopped editing the build list of area (" + srcBlock.getX() + ", " + srcBlock.getZ() + ").");
//				break;
//			case 'a':
//				player.sendMessage("§5Stopped editing the access list of area (" + srcBlock.getX() + ", " + srcBlock.getZ() + ").");
//				break;
//			case 'e':
//				player.sendMessage("§5Stopped editing the entry list of area (" + srcBlock.getX() + ", " + srcBlock.getZ() + ").");
//				break;
//			case 'p':
//				player.sendMessage("§5Stopped editing the PvE list of area (" + srcBlock.getX() + ", " + srcBlock.getZ() + ").");
//				break;
//			}
//		}
//		return true;
//	}
	
	public void showMenu(Player player){
		if(owners.contains(player.getName())){
			InventoryMenu menu = new AreaMenu(this);
			menu.open(player);
			menus.add(menu);
		}
		else{
			player.sendMessage("This area is owned by " + owners + ". You do not have permission to access it.");
		}
		
	}
	
	public void showBlockAccessMenu(Player player){
		if(blockaccess.keySet() == null || blockaccess.keySet().isEmpty() || blocksforaccess == null || blocksforaccess.isEmpty()){
			return;
		}
		InventoryMenu menu = new BlockAccessMenu(this);
		menu.open(player);
		menus.add(menu);
	}
	
	public void showListMenu(String permission, Player player){
		if("pve".equalsIgnoreCase(permission)){
			new ListMenu(player, pve, identifier);
		}else if("build".equalsIgnoreCase(permission)){
			new ListMenu(player, build, identifier);
		}else if("entry".equalsIgnoreCase(permission)){
			new ListMenu(player, entry, identifier);
		}else if("access".equalsIgnoreCase(permission)){
			new ListMenu(player, access, identifier);
		}
	}
	
	public void showListMenu(Block block, Player player){
		for(Block b : blockaccess.keySet()){
			if(b.equals(block)){
				new ListMenu(player, blockaccess.get(b), identifier);
			}
		}
	}
	
	public int getLowerX(){
		return srcBlock.getX() + z.getBlockX();
	}
	public int getLowerZ(){
		return srcBlock.getZ() + z.getBlockZ();
	}
	public int getHigherX(){
		return srcBlock.getX() + x.getBlockX();
	}
	public int getHigherZ(){
		return srcBlock.getZ() + x.getBlockZ();
	}
	
	public void enlarge(int size, char dir){
		Vector newx = null;
		Vector newz = null;
		switch(dir){
		case 'n':
			newx = new Vector(x.getX() + size, x.getY(), x.getZ());
			newz = new Vector(z.getX(), z.getY(), z.getZ());
			break;
		case 's':
			newx = new Vector(x.getX(), x.getY(), x.getZ());
			newz = new Vector(z.getX() - size, z.getY(), z.getZ());
			break;
		case 'e':
			newx = new Vector(x.getX(), x.getY(), x.getZ() + size);
			newz = new Vector(z.getX(), z.getY(), z.getZ());
			break;
		case 'w':
			newx = new Vector(x.getX(), x.getY(), x.getZ());
			newz = new Vector(z.getX(), z.getY(), z.getZ() - size);
			break;
		}
		int highx = newx.getBlockX() + srcBlock.getX();
		int highz = newx.getBlockZ() + srcBlock.getZ();
		int lowx = newz.getBlockX() + srcBlock.getX();
		int lowz = newz.getBlockZ() + srcBlock.getZ();
		for(Area area : areas){
			if(!toString().equals(area.toString())){
				if(highx >= area.getLowerX() && area.getHigherX() >= lowx && area.getHigherZ() >= lowz && highz >= area.getLowerZ()){
					System.out.println("Collision: " + area + ", " + toString());
					return;
				}
			}
		}
		Set<Player> players = stopDisplay();
		x = newx;
		z = newz;
		System.out.println("Enlarged successfulls towars '" + dir + "': " + toString());
		System.out.println(players);
		displayCorners(players);
	}
	
	public boolean isAt(int x, int y, int z){
		if(srcBlock.getX() == x && srcBlock.getY() == y && srcBlock.getZ() == z) return true;
		return false;
	}
	
	public boolean isInside(int x, int z){
		if(getHigherX() <= x && x >= getLowerX()){
			if(getHigherZ() <= z && z >= getLowerZ()){
				return true;
			}
		}
		return false;
	}
	public boolean isInside(Block block){
		return isInside(block.getLocation());
	}
	
	public boolean isInside(Location loc){
		if(srcBlock.getWorld().equals(loc.getWorld())){
			if(getHigherX() >= loc.getX() && loc.getX() >= getLowerX() && getHigherZ() >= loc.getZ() && loc.getZ() >= getLowerZ()){
				return true;
			}
		}
		return false;
	}
	
	public boolean isNear(Location loc){
		if(srcBlock.getWorld().equals(loc.getWorld())){
			if(getHigherX()+2 >= loc.getX() && loc.getX() >= getLowerX()-2 && getHigherZ()+2 >= loc.getZ() && loc.getZ() >= getLowerZ()-2){
				return true;
			}
		}
		return false;
	}
	
	public boolean isOwner(String player){
		for(String owner : owners){
			if(owner.equals(player)){
				return true;
			}
		}
		return false;
	}
	
	public String toString(){
		return "Area(Owner: " + owners + ", Location: (" + srcBlock.getX() + ", " + srcBlock.getY() + ", " + srcBlock.getZ() + "))";
	}
	
	public ArrayList<Block> borderblocklist(){
		ArrayList<Block> blocklist = new ArrayList<Block>();
		blocklist.addAll(pillarblocklist(z.getBlockX() + srcBlock.getX(), z.getBlockZ() + srcBlock.getZ()));
		blocklist.addAll(pillarblocklist(x.getBlockX() + srcBlock.getX(), z.getBlockZ() + srcBlock.getZ()));
		blocklist.addAll(pillarblocklist(z.getBlockX() + srcBlock.getX(), x.getBlockZ() + srcBlock.getZ()));
		blocklist.addAll(pillarblocklist(x.getBlockX() + srcBlock.getX(), x.getBlockZ() + srcBlock.getZ()));
		
		return blocklist;
	}
	
	public ArrayList<Block> pillarblocklist(int x, int z){
		ArrayList<Block> blocklist = new ArrayList<Block>();
		for(int y = 0; y < 128; y++){
			if(y % 3 == 0 && (srcBlock.getWorld()).getBlockAt(x, y, z).getType().equals(Material.AIR)){
				blocklist.add(srcBlock.getWorld().getBlockAt(x, y, z));
			}
		}
		return blocklist;
	}
	
	@SuppressWarnings("deprecation")
	public void displayCorners(Player player){
		if(displayedblocklist.containsKey(player)){
			stopDisplay(player);
			return;
		}
		ArrayList<Block> blocklist = new ArrayList<Block>();
		blocklist = borderblocklist();
		displayedblocklist.put(player, blocklist);
		for(Block block : blocklist){
			player.sendBlockChange(block.getLocation(), Material.GLASS, (byte) 0);
		}
//		BukkitScheduler bukkitScheduler = Bukkit.getScheduler();
//		
//		bukkitScheduler.scheduleAsyncDelayedTask(Protector.getInstance(), new Runnable() {
//
//			@Override
//			public void run() {
//				stopDisplay(player);
//				
//			}
//			
//		}, 10*20L);
	}
	
	public void displayCorners(Set<Player> players){
		for(Player player : players){
			displayCorners(player);
		}
	}
	
	@SuppressWarnings("deprecation")
	public void stopDisplay(Player player){
		if(!displayedblocklist.containsKey(player)){
			return;
		}
		ArrayList<Block> blocklist = new ArrayList<Block>();
		blocklist = displayedblocklist.get(player);
		for(Block block : blocklist){
			player.sendBlockChange(block.getLocation(), block.getType(), (byte) 0);
		}
		displayedblocklist.remove(player);
	}
	
	public Set<Player> stopDisplay(){
		Set<Player> players = ((HashMap<Player, ArrayList<Block>>) displayedblocklist.clone()).keySet();
		Iterator<Player> iter = displayedblocklist.keySet().iterator();
		while(iter.hasNext()){
			stopDisplay(iter.next());
		}
		return players;
	}
	
	/**
	 * DO NOT USE THIS FOR ACCESS PERMISSION
	 * Param player can be null when asking "pvp", "fire or "expl" permission
	 */
	public boolean permission(String player, String permission){
		if("pvp".equalsIgnoreCase(permission)){
				return allowpvp;
		}else if("fire".equalsIgnoreCase(permission)){
			return allowfire;
		}else if("expl".equalsIgnoreCase(permission)){
			return allowexpl;
		}else if("pve".equalsIgnoreCase(permission)){
			return pve.hasPermission(player, owners);
		}else if("build".equalsIgnoreCase(permission)){
			return build.hasPermission(player, owners);
		}else if("entry".equalsIgnoreCase(permission)){
			return entry.hasPermission(player, owners);
		}
		return false;
	}
	
	public boolean permission(String player, Block block){
		for(Block block2 : blockaccess.keySet()){
			if(block2.equals(block)){
				return blockaccess.get(block2).hasPermission(player, owners);
			}
		}
		return access.hasPermission(player, owners);
	}
	
	public Permission getPermissionForBlock(Block block){
		for(Block b : blockaccess.keySet()){
			if(b.equals(block)){
				return blockaccess.get(b);
			}
		}
		return null;
	}
	
	public void lockOut(Player player){
		if(isInside(player.getLocation())){
			//if(srcBlock.getX() + x.getX() <= player.getLocation().getX() && player.getLocation().getX() <= srcBlock.getX() + x.getX() - 1){
				HashMap<Double, Vector> distances = new HashMap<Double, Vector>();
				//Math.abs(srcBlock.getX() + x.getX() - player.getLocation().getX())
				distances.put(Math.abs(srcBlock.getX() + x.getX() - player.getLocation().getX()), new Vector(Math.abs(srcBlock.getX() + x.getX() - player.getLocation().getX())+2, 0, 0));
				distances.put(Math.abs(srcBlock.getX() + z.getX() - player.getLocation().getX()), new Vector(-(Math.abs(srcBlock.getX() + z.getX() - player.getLocation().getX())+1), 0, 0));
				distances.put(Math.abs(srcBlock.getZ() + x.getZ() - player.getLocation().getZ()), new Vector(0, 0, Math.abs(srcBlock.getZ() + x.getZ() - player.getLocation().getZ())+2));
				distances.put(Math.abs(srcBlock.getZ() + z.getZ() - player.getLocation().getZ()), new Vector(0, 0, -(Math.abs(srcBlock.getZ() + z.getZ() - player.getLocation().getZ())+1)));
				double mindist = Double.MAX_VALUE;
				for(double dist : distances.keySet()){
					if(dist < mindist)
						mindist = dist;
				}
				if(mindist != Double.MAX_VALUE){
					player.teleport(player.getLocation().add(distances.get(mindist)));
				}
					
			//}
		}
//		ArrayList<Block> blocklist = new ArrayList<Block>();
//		if(player.getLocation().getX() - srcBlock.getX() + x.getX() <= 5 ){//highx side close
//			Location pos = player.getLocation().add(new Vector(player.getLocation().getX() - srcBlock.getX() + x.getX(), 0, 0));
//			blocklist.add(pos.getBlock());
//			blocklist.add(pos.add(new Vector(0, 1, 0)).getBlock());
////			blocklist.add(pos.add(new Vector(0, -1, 0)).getBlock());
////			blocklist.add(pos.add(new Vector(1, 0, 0)).getBlock());
//			blocklist.add(pos.add(new Vector(-1, 0, 0)).getBlock());
//			System.out.println(blocklist);
//			for(int i = 0; i > blocklist.size(); i++){
//				if(blocklist.get(i).getType().equals(Material.AIR)){
//					blocklist.remove(blocklist.get(i));
//				}
//			}
//			
//		}
//		for(Block block : blocklist){
//			player.sendBlockChange(block.getLocation(), Material.GLASS, (byte) 0);
//		}
		
	}
	

	
	public ItemStack getLocationItem(){
		return ItemHandler.createItem(Material.GOLD_BLOCK, "§6Location", 1, "§f" + getBlock().getX() + ", " + getBlock().getY() + ", " 
				+ getBlock().getZ());
	}
	
	public Set<InventoryMenu> getMenus(){
		return menus;
	}

}
