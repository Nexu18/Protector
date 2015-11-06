package clazz;

import java.util.HashSet;
import java.util.Set;

public class Permission {
	public Perm perm;
	public Set<String> set;
	String identifier;
	
	public Permission(Perm perm){
		this.perm = perm;
		set = new HashSet<String>();
		this.identifier = "unknown identifier";
	}
	
	public Permission(Perm perm, String identifier){
		this.perm = perm;
		set = new HashSet<String>();
		this.identifier = identifier;
	}
	
	public Permission(Perm perm, HashSet<String> set){
		this.perm = perm;
		this.set = set;
	}
	
	public boolean hasPermission(String player, Set<String> owners){
		for(String owner : owners){
			if(owner.equals(player)){
				return true;
			}
		}
		switch(perm){//0: only owner, 1: owner + seteded people 2: everyone but seted people 3: everyone
		case OWNER_ONLY:
			return false;//owner checked already, unreachable
		case OWNER_AND_LISTED:
			for(String str : set){
				if(str.equals(player)){
					return true;
				}
			}
			break;
		case EVERYONE_BUT_LISTED:
			for(String str : set){
				if(str.equals(player)){
					return false;
				}
			}
			return true;
		case EVERYONE:
			return true;
		}
		return false;
	}
	
	public void toggle(){
		perm = perm.toggle();
	}
	
	public String toString(){
		return perm.toString();
	}
	
	public String getIdentifier(){
		return identifier;
	}
	
	
	

}
