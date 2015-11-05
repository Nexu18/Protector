package clazz;

import commands.CommandProtect;
import main.Protector;

public class CommandHandler {
	
	public static void handle() {
		Protector.getInstance().getCommand("protect").setExecutor(new CommandProtect());
	}

}
