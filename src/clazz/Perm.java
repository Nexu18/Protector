package clazz;

public enum Perm {
	OWNER_ONLY,
	OWNER_AND_LISTED,
	EVERYONE_BUT_LISTED,
	EVERYONE;
	
	public Perm toggle(){
		if(this.equals(OWNER_ONLY)){
			return OWNER_AND_LISTED;
		}else if(this.equals(OWNER_AND_LISTED)){
			return EVERYONE_BUT_LISTED;
		}else if(this.equals(EVERYONE_BUT_LISTED)){
			return EVERYONE;
		}else if(this.equals(EVERYONE)){
			return OWNER_ONLY;
		}
		return OWNER_ONLY;
	}
	public String toString(){
		switch(this){
		case OWNER_ONLY:
			return "owner only";
		case OWNER_AND_LISTED:
			return "owner and listed people";
		case EVERYONE_BUT_LISTED:
			return "everyone but listed people";
		case EVERYONE:
			return "everyone";
		}
		return "invalid permission byte";
	}

}
