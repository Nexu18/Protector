package clazz;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
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
	
	public static String test(){
		return "test";
	}
	
	private Block srcBlock;
	//private Player owner;
	private Set<String> owners;
	private Set<InventoryMenu> menus;
	
	boolean allowpvp;
//	byte allowpve;
//	Set<String> pve.set;
	Permission pve;
//	byte allowbuild;//0: only owner, 1: owner + listeded people 2: everyone but listed people 3: everyone
//	Set<String> build.set;
	Permission build;
//	byte allowaccess;//0: only owner, 1: owner + listeded people 2: everyone but listed people 3: everyone
//	Set<String> access.set;
	Permission access;
//	byte allowentry;
//	Set<String> entry.set;
	Permission entry;
//	ArrayList<Block> blocks;
	HashMap<Block, Permission> blockaccess;//0: only owner, 1: owner + listeded people 2: everyone but listed people 3: everyone
	List<Block> blocksforaccess;
	//	HashMap<Block, ArrayList<String>> blocklist;
	
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
		
		owners.add("Blaxuni");
		
		menus = new HashSet<InventoryMenu>();
		x = newx;
		z = newz;
		allowpvp = false;
//		allowpve = 3;
//		pve.set = new HashSet<String>();
		pve = new Permission(Perm.OWNER_ONLY);
//		allowbuild = 0;
//		build.set = new HashSet<String>();
		build = new Permission(Perm.OWNER_ONLY);
//		allowaccess = 0;
//		access.set = new HashSet<String>();
		access = new Permission(Perm.OWNER_ONLY);
//		allowentry = 3;
//		entry.set = new HashSet<String>();
		entry = new Permission(Perm.EVERYONE);
//		blocks = new ArrayList<Block>();
//		blockallow = new HashMap<Block, Byte>();
//		blocklist = new HashMap<Block, ArrayList<String>>();
		blockaccess = new HashMap<Block, Permission>();
		blocksforaccess = new ArrayList<Block>();
		playersInListMenu = new HashMap<String, Character>();
		
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
	
//	public String permissionToString(byte b){
//		switch(b){
//		case 0:
//			return "owner only";
//		case 1:
//			return "owner and listed people";
//		case 2:
//			return "everyone but listed people";
//		case 3:
//			return "everyone";
//		}
//		return "invalid permission byte";
//	}
	
//	public void toggleBuild(Player player){
//		build.toggle();
//		Inventory inv = player.getOpenInventory().getTopInventory();
//		inv.setItem(5, ItemHandler.editDescription(inv.getItem(5), "§f" + build, 0));
//		player.updateInventory();
//	}
//	
//	public void toggleAccess(Player player){
//		access.toggle();
//		Inventory inv = player.getOpenInventory().getTopInventory();
//		inv.setItem(6, ItemHandler.editDescription(inv.getItem(6), "§f" + access, 0));
//		player.updateInventory();
//	}
//	
//	public void toggleEntry(Player player){
//		entry.toggle();
//		Inventory inv = player.getOpenInventory().getTopInventory();
//		inv.setItem(7, ItemHandler.editDescription(inv.getItem(7), "§f" + entry, 0));
//		player.updateInventory();
//	}
//	
//	public void togglePvE(Player player){
//		pve.toggle();
//		Inventory inv = player.getOpenInventory().getTopInventory();
//		inv.setItem(8, ItemHandler.editDescription(inv.getItem(8), "§f" + pve, 0));
//		player.updateInventory();
//	}
//	
//	public void togglePvP(Player player){
//		if(allowpvp){
//			allowpvp = false;
//		}else{
//			allowpvp = true;
//		}
//		Inventory inv = player.getOpenInventory().getTopInventory();
//		inv.setItem(24, ItemHandler.editDescription(inv.getItem(24), "§f" + permissionBoolToString(allowpvp), 0));
//		player.updateInventory();
//	}
//	
//	public void toggleBlock(Block block){
//		if(blockaccess.keySet().contains(block)){
//			blockaccess.remove(block);
//		}else{
//			blockaccess.put(block, new Permission(Perm.OWNER_ONLY));
//		}
//	}
	
	/**
	 * Will remove this block if it exists
	 * @param block
	 */
	public void addAccessBlock(Block block){
		if(blockaccess.keySet().contains(block)){
			blockaccess.remove(block);
			blocksforaccess.remove(block);
		}else{
			blockaccess.put(block, new Permission(Perm.OWNER_ONLY));
			blocksforaccess.add(block);
		}
	}
	
	/**
	 * DO NOT USE THIS FOR SPECIFIC BLOCK PERMISSIONS.
	 */
	public void togglePermission(String str){
		if("pvp".equalsIgnoreCase(str)){
			if(allowpvp)
				allowpvp = true;
			allowpvp = false;
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
	
	public boolean listMenuParser(String s, Player player){
		if(!playersInListMenu.keySet().contains(player.getName())){
			return false;
		}
		char list = playersInListMenu.get(player.getName());
		String[] cmd = s.split(" ");
		if(cmd[0].equalsIgnoreCase("add")){
			switch(list){
			case 'b':
				for(int i = 1; i < cmd.length; i++){
					build.set.add(cmd[i]);
					player.sendMessage("§5\"" + cmd[i] + "\" added to build list of area (" + srcBlock.getX() + ", " + srcBlock.getZ() + ") successfully.");
				}
				break;
			case 'a':
				for(int i = 1; i < cmd.length; i++){
					access.set.add(cmd[i]);
					player.sendMessage("§5\"" + cmd[i] + "\" added to access list of area (" + srcBlock.getX() + ", " + srcBlock.getZ() + ") successfully.");
				}
				break;
			case 'e':
				for(int i = 1; i < cmd.length; i++){
					entry.set.add(cmd[i]);
					player.sendMessage("§5\"" + cmd[i] + "\" added to entry list of area (" + srcBlock.getX() + ", " + srcBlock.getZ() + ") successfully.");
				}
				break;
			case 'p':
				for(int i = 1; i < cmd.length; i++){
					pve.set.add(cmd[i]);
					player.sendMessage("§5\"" + cmd[i] + "\" added to PvE list of area (" + srcBlock.getX() + ", " + srcBlock.getZ() + ") successfully.");
				}
				break;
			}
		}
		else if(cmd[0].equalsIgnoreCase("remove")){
			switch(list){
			case 'b':
				for(int i = 1; i < cmd.length; i++){
					build.set.remove(cmd[i]);
					player.sendMessage("§5\"" + cmd[i] + "\" removed from build list of area (" + srcBlock.getX() + ", " + srcBlock.getZ() + ") successfully.");
				}
				break;
			case 'a':
				for(int i = 1; i < cmd.length; i++){
					access.set.remove(cmd[i]);
					player.sendMessage("§5\"" + cmd[i] + "\" removed from access list of area (" + srcBlock.getX() + ", " + srcBlock.getZ() + ") successfully.");
				}
				break;
			case 'e':
				for(int i = 1; i < cmd.length; i++){
					entry.set.remove(cmd[i]);
					player.sendMessage("§5\"" + cmd[i] + "\" removed from entry list of area (" + srcBlock.getX() + ", " + srcBlock.getZ() + ") successfully.");
				}
				break;
			case 'p':
				for(int i = 1; i < cmd.length; i++){
					pve.set.remove(cmd[i]);
					player.sendMessage("§5\"" + cmd[i] + "\" removed from PvE list of area (" + srcBlock.getX() + ", " + srcBlock.getZ() + ") successfully.");
				}
				break;
			}
		}
		else{
			playersInListMenu.remove(player.getName());
			switch(list){
			case 'b':
				player.sendMessage("§5Stopped editing the build list of area (" + srcBlock.getX() + ", " + srcBlock.getZ() + ").");
				break;
			case 'a':
				player.sendMessage("§5Stopped editing the access list of area (" + srcBlock.getX() + ", " + srcBlock.getZ() + ").");
				break;
			case 'e':
				player.sendMessage("§5Stopped editing the entry list of area (" + srcBlock.getX() + ", " + srcBlock.getZ() + ").");
				break;
			case 'p':
				player.sendMessage("§5Stopped editing the PvE list of area (" + srcBlock.getX() + ", " + srcBlock.getZ() + ").");
				break;
			}
		}
		return true;
	}
	
//	@SuppressWarnings("deprecation")
//	public void showBlockMenu(Player player, int page){
//		if(owners.contains(player.getName())){
//			Inventory inv = Bukkit.createInventory(null, 9*5, "Protector - Blocks " + page);
//			for(int i = 0; i < 18; i++){
//				try{
////					if((i/9)%2 == 0){
////						inv.setItem(i, ItemHandler.createItem(blocks.get(54*(page-1)+i).getType(), "§6" + blocks.get(54*(page-1)+i).getX() + ", " + blocks.get(54*(page-1)+i).getY() 
////								+ ", " + blocks.get(54*(page-1)+i).getZ(), 1, blocks.get(54*(page-1)+i).getData(),"§f" + permissionToString(blockallow.get(blocks.get(54*(page-1)+i)))));
////						
////						ItemStack item = ItemHandler.createItem(Material.PAPER, "§6List of " + blocks.get(54*(page-1)+i).getX() + ", " + blocks.get(54*(page-1)+i).getY() 
////								+ ", " + blocks.get(54*(page-1)+i).getZ(), 1);
////						for(int j = 0; j < blocklist.get(blocks.get(54*(page-1)+i)).size(); j++){
////							ItemHandler.editDescription(item, "§f" + blocklist.get(blocks.get(54*(page-1)+i)), j);
////						}
////						inv.setItem(i+9, item);
////					}
//					inv.setItem(((i/9)*2*9)+(i%9), ItemHandler.createItem(blocks.get(18*(page-1) + i).getType(), "§6" + blocks.get(18*(page-1) + i).getX() + ", " + blocks.get(18*(page-1) + i).getY() 
//							+ ", " + blocks.get(54*(page-1)+i).getZ(), 1, blocks.get(18*(page-1) + i).getData(), "§f" + permissionToString(blockallow.get(blocks.get(18*(page-1)+i)))));
//					
//					ItemStack item = ItemHandler.createItem(Material.PAPER, "§6List of " + blocks.get(18*(page-1) + i).getX() + ", " + blocks.get(18*(page-1) + i).getY() 
//							+ ", " + blocks.get(18*(page-1) + i).getZ(), 1);
//					for(int j = 0; j < blocklist.get(blocks.get(18*(page-1) + i)).size(); j++){
//						ItemHandler.editDescription(item, "§f" + blocklist.get(blocks.get(18*(page-1) + i)), j);
//					}
//					inv.setItem(((i/9)*2*9)+(i%9)+9, item);
//				}
//				catch(IndexOutOfBoundsException e){
//					
//				}
//			}
//			if(page > 1){
//				inv.setItem(inv.getSize() - 9, ItemHandler.createItem(Material.WOOL, "§6<- Previous", 1, 15));
//			}
//			if(blockaccess.keySet().size() > 18*page){
//				inv.setItem(inv.getSize() - 1, ItemHandler.createItem(Material.WOOL, "§6Next ->", 1, 15));
//			}
//			inv.setItem(inv.getSize() - 2, ItemHandler.createItem(Material.BEDROCK, "§6Back", 1));
//			inv.setItem(41, ItemHandler.createItem(Material.GOLD_BLOCK, "§6Location", 1, "§f" + srcBlock.getX() + ", " + srcBlock.getY() + ", " + srcBlock.getZ()));
//			player.openInventory(inv);
//		}
//	}
	
	public void showMenu(Player player){
		if(owners.contains(player.getName())){
//			Inventory inv = Bukkit.createInventory(null, 27, "Protector - Area");
//			inv.setItem(0, ItemHandler.createItem(Material.GOLD_BLOCK, "§6Location", 1, "§f" + srcBlock.getX() + ", " + srcBlock.getY() + ", " + srcBlock.getZ()));
//			for(ItemStack item : Protector.areaMenuItems.keySet()){
//				System.out.println("Item: " + item);
//				if(item.getItemMeta().getDisplayName().contains("Allow Building")){
//					inv.setItem(Protector.areaMenuItems.get(item), ItemHandler.editDescription(item, "§f" + build, 0));
//				}else if(item.getItemMeta().getDisplayName().contains("Allow Access")){
//					inv.setItem(Protector.areaMenuItems.get(item), ItemHandler.editDescription(item, "§f" + access, 0));
//				}else if(item.getItemMeta().getDisplayName().contains("Allow Entry")){
//					inv.setItem(Protector.areaMenuItems.get(item), ItemHandler.editDescription(item, "§f" + entry, 0));
//				}else if(item.getItemMeta().getDisplayName().contains("Allow PvE")){
//					inv.setItem(Protector.areaMenuItems.get(item), ItemHandler.editDescription(item, "§f" + pve, 0));
//				}else if(item.getItemMeta().getDisplayName().contains("Allow PvP")){
//					inv.setItem(Protector.areaMenuItems.get(item), ItemHandler.editDescription(item, "§f" + permissionBoolToString(allowpvp), 0));
//				}
//				else{
//					inv.setItem(Protector.areaMenuItems.get(item), item);
//				}
//				
//			}
//			
//			player.setCompassTarget(new Location(player.getWorld(),1000000, 64,0));
//			player.openInventory(inv);
			InventoryMenu menu = new AreaMenu(this);
			menu.open(player);
			menus.add(menu);
		}
		else{
			player.sendMessage("This area is owned by " + owners + ". You do not have permission to access it.");
		}
		
	}
	
	public void showBlockAccessMenu(Player player){
		InventoryMenu menu = new BlockAccessMenu(this);
		menu.open(player);
		menus.add(menu);
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
		for(Player player : displayedblocklist.keySet()){
			stopDisplay(player);
		}
		return players;
	}
	
	/**
	 * DO NOT USE THIS FOR ACCESS PERMISSION
	 */
	public boolean permission(String player, String permission){
		if("pvp".equalsIgnoreCase(permission)){
			if(allowpvp)
				return true;
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
			if(block2 == block){
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
	
//	public boolean permissionBuild(String player){
//		return build.hasPermission(player, owners);
//	}
//	
//	public boolean permissionEntry(String player){
//		return entry.hasPermission(player, owners);
////		switch(allowentry){//0: only owner, 1: owner + listeded people 2: everyone but listed people 3: everyone
////		case 0:
////			if(player.equals(owner.getName())){
////				return true;
////			}
////			break;
////		case 1:
////			if(player.equals(owner.getName()) || entry.set.contains(player)){
////				return true;
////			}
////			break;
////		case 2:
////			if(player.equals(owner.getName()) || !entry.set.contains(player)){
////				return true;
////			}
////			break;
////		case 3:
////			return true;
////		}
////		return false;
//	}
//	
//	public boolean permissionPvE(String player){
//		return pve.hasPermission(player, owners);
//	}
//	
//	public boolean permissionPvP(){
//		return allowpvp;
//	}
	
//	public Set<Block> lockOutblocklist(Player player){
//		Set<Block> blocklist = new HashSet<Block>();
//		for()
//	}
	
	public void lockOut(Player player){
		if(isInside(player.getLocation())){
			if(srcBlock.getX() + x.getX() <= player.getLocation().getX() && player.getLocation().getX() <= srcBlock.getX() + x.getX() - 1){
				player.teleport(player.getLocation().add(new Vector(1, 0, 0)));
			}
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
	
//	public boolean permissionAccess(String player, Block block){
//		if(blockallow.keySet().contains(block)){
//			switch(blockallow.get(block)){
//			case 0:
//				if(player.equals(owner.getName())){
//					return true;
//				}
//				return false;
//			case 1:
//				if(player.equals(owner.getName()) || blocklist.get(block).contains(player)){
//					return true;
//				}
//				return false;
//			case 2:
//				if(player.equals(owner.getName()) || !blocklist.get(block).contains(player)){
//					return true;
//				}
//				return false;
//			case 3:
//				return true;
//			}
//		}
//		switch(allowaccess){//0: only owner, 1: owner + listeded people 2: everyone but listed people 3: everyone
//		case 0:
//			if(player.equals(owner.getName())){
//				return true;
//			}
//			break;
//		case 1:
//			if(player.equals(owner.getName()) || access.set.contains(player)){
//				return true;
//			}
//			break;
//		case 2:
//			if(player.equals(owner.getName()) || !access.set.contains(player)){
//				return true;
//			}
//			break;
//		case 3:
//			return true;
//		}
//		return false;
//	}
	
	public ItemStack getLocationItem(){
		return ItemHandler.createItem(Material.GOLD_BLOCK, "§6Location", 1, "§f" + getBlock().getX() + ", " + getBlock().getY() + ", " 
				+ getBlock().getZ());
	}
	
	public Set<InventoryMenu> getMenus(){
		return menus;
	}

}
