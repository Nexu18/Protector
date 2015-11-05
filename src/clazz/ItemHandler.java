package clazz;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemHandler {
	
	public static ItemStack createItem(Material mat, String name, int amount) {
		ItemStack itemStack = new ItemStack(mat, amount);
		ItemMeta im = itemStack.getItemMeta();
		im.setDisplayName(name);
		itemStack.setItemMeta(im);
		return itemStack;
	}
	
	public static ItemStack createItem(Material mat, String name, int amount, int data,  String... description) {
		ItemStack itemStack = new ItemStack(mat, amount, (byte) data);
		ItemMeta im = itemStack.getItemMeta();
		im.setDisplayName(name);
		List<String> desc = new ArrayList<String>();
		for (String str : description) desc.add(str);
		im.setLore(desc);
		itemStack.setItemMeta(im);
		return itemStack;
	}
	
	public static ItemStack createItem(Material mat, String name, int amount, String... description) {
		ItemStack itemStack = new ItemStack(mat, amount);
		ItemMeta im = itemStack.getItemMeta();
		im.setDisplayName(name);
		List<String> desc = new ArrayList<String>();
		for (String str : description) desc.add(str);
		im.setLore(desc);
		itemStack.setItemMeta(im);
		return itemStack;
	}
	
	public static ItemStack editDescription(ItemStack itemin, String desc, int place){
		if(itemin != null){
			ItemStack item = itemin.clone();
			ItemMeta im = item.getItemMeta();
			List<String> lore = im.getLore();
			lore.set(place, desc);
			im.setLore(lore);
			item.setItemMeta(im);
			return item;
		}
		System.out.println("Item description edit, item null");
		return null;
	}

}

