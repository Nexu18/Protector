package clazz;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class BlockAccessMenu extends InventoryMenu{
	Area area;
	int page;
	final static int SIZE = 9*4;

	public BlockAccessMenu(Area area) {
		super("Protector - BlockAccess 1", SIZE+9, getPage(0, area));
		this.area = area;
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void runAction(int pos, String name){
		ItemStack itemStack = getInv().getItem(pos);
		Player player = Bukkit.getPlayer(name);
		if(itemStack == null || itemStack.getItemMeta() == null){
			return;
		}
		System.out.println("menu option " + name + " " + itemStack.getItemMeta().getDisplayName());
		
		if(itemStack.getItemMeta() != null && itemStack.getItemMeta().getDisplayName().contains("Previous") && page-1 >= 0) {
			--page;
			super.update(getPage(page, area));
			super.updateTitle("Protector - BlockAccess " + (page+1) );
		}else if(itemStack.getItemMeta() != null && itemStack.getItemMeta().getDisplayName().contains("Next") && getPage(page+1, area) != null) {
			++page;
			super.update(getPage(page, area));
			super.updateTitle("Protector - BlockAccess " + (page+1) );
		}else if(itemStack.getItemMeta() != null && itemStack.getItemMeta().getDisplayName().contains("Location")) {
			
		}
		
		
		else if(itemStack.getItemMeta() != null){
			String[] args = itemStack.getItemMeta().getDisplayName().split("§\\d");
			System.out.println("args: ");
			for(String str : args){
				System.out.println(str);
			}
			System.out.println("end of args");
			
			
			if(args.length == 9 && args[1].contains("List of")){
				
			}else if(/*args.length == 8 && */args[2].contains(" at (")){
				System.out.println("block");
				try{
					Block block = player.getWorld().getBlockAt(new Location(player.getWorld(), Integer.parseInt(args[3]), Integer.parseInt(args[5]), Integer.parseInt(args[7])));
					System.out.println("block: " + block.getX() + ", " + block.getY() + ", " + block.getZ());
					area.togglePermission(block);
					
				}catch(NumberFormatException e){
					System.out.println("Invalid Item name " + itemStack.getItemMeta().getDisplayName() + " selected in an BlockAccessMenu!");
				}
			}
		}
		
		update(getPage(page, getArea()));
		for(String thisplayer : getPlayers()){
			Bukkit.getPlayer(thisplayer).updateInventory();
		}
	}
	
	public static HashMap<Integer, ItemStack> getPage(int page, Area area){
		HashMap<Integer, ItemStack> items = new HashMap<Integer, ItemStack>();
		Block block;
		for(int i = 0; i < (SIZE/2); i++){
			try{
				block = area.getBlocks().get(i+(page*(SIZE/2)));
			}catch(IndexOutOfBoundsException e){
				block = null;
			}
			if(block != null){
				//if((i/9)%2 == 0){
					//ItemHandler.createItem(Material.PAPER, "§6Edit PvE List", 1, "§5Edit listed people for PvE.")
					items.put(i+((i/9)*9), ItemHandler.createItem(block.getType(), "§3" + block.getType() + "§8 at (§3" + block.getX() 
						+ "§8, §3" + block.getY() + "§8, §3" + block.getZ() + "§8)", 1, block.getData(), "§f" + area.getPermissionForBlock(block)));
					items.put(i+9+((i/9)*9), ItemHandler.createItem(Material.PAPER, "§8List of §3" + block.getType() + "§8 at (§3" + block.getX() 
						+ "§8, §3" + block.getY() + "§8, §3" + block.getZ() + "§8)", 1));
				//}
			}else{
				items.put(i+((i/9)*9), new ItemStack(Material.AIR));
				items.put(i+9+((i/9)*9), new ItemStack(Material.AIR));
			}
			
		}
//		int i = 0;
//		for(Block block : area.getBlocks()){
//			items.put(i, ItemHandler.createItem(block.getType(), "§3" + block.getType() + "§8 at (§3" + block.getX() 
//						+ "§8, §3" + block.getY() + "§8, §3" + block.getZ() + "§8)", 1));
//			i++;
//		}
		if(items.isEmpty()){
			return null;
		}
		boolean empty = true;
		for(ItemStack item : items.values()){
			if(item != null && item.getType() != null && item.getType() != Material.AIR){
				empty = false;
			}
		}
		if(empty){
			return null;
		}
		
		if(page-1 >= 0){
			items.put(SIZE, ItemHandler.createItem(Material.GLOWSTONE_DUST, "§8Previous", 1));
		}else{
			items.put(SIZE, new ItemStack(Material.AIR));
		}
		if(getPage(page+1, area) != null){
			items.put(SIZE+8, ItemHandler.createItem(Material.GLOWSTONE_DUST, "§8Next", 1));
		}else{
			items.put(SIZE+8, new ItemStack(Material.AIR));
		}
		items.put(SIZE+1, area.getLocationItem());
		return items;
	}

}
