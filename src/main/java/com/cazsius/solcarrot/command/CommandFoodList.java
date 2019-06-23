package com.cazsius.solcarrot.command;

import com.cazsius.solcarrot.lib.Localization;
import com.cazsius.solcarrot.tracking.FoodList;
import net.minecraft.command.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.*;
import net.minecraftforge.server.command.CommandTreeBase;

import static com.cazsius.solcarrot.lib.Localization.keyString;

public class CommandFoodList extends CommandTreeBase {
	private static final String name = "foodlist";
	
	public CommandFoodList() {
		addSubcommand(new CommandSizeFoodList());
		addSubcommand(new CommandClearFoodList());
		addSubcommand(new CommandSyncFoodList());
	}
	
	@Override
	public String getName() {
		return name;
	}
	
	@Override
	public String getUsage(ICommandSender sender) {
		return Localization.keyString("command", getName() + ".usage");
	}
	
	@Override
	public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
		return true;
	}
	
	public static abstract class SubCommand extends CommandBase {
		
		@Override
		public String getUsage(ICommandSender sender) {
			return keyString("command", localizationPath("usage"));
		}
		
		@Override
		public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
			if (args.length > 1) throw new WrongUsageException(getUsage(sender));
			
			EntityPlayer player;
			if (args.length == 1) {
				player = getPlayer(server, sender, args[0]);
			} else { // no args
				Entity senderEntity = sender.getCommandSenderEntity();
				if (!(senderEntity instanceof EntityPlayer))
					throw new SyntaxErrorException("commands.generic.player.unspecified");
				player = (EntityPlayer) senderEntity;
			}
			
			execute(player, FoodList.get(player));
		}
		
		abstract void execute(EntityPlayer player, FoodList foodList);
		
		ITextComponent localizedComponent(String path, Object... args) {
			return Localization.localizedComponent("command", localizationPath(path), args);
		}
		
		ITextComponent localizedQuantityComponent(String path, int number) {
			return Localization.localizedQuantityComponent("command", localizationPath(path), number);
		}
		
		static void showMessage(EntityPlayer player, ITextComponent message) {
			Style style = new Style().setColor(TextFormatting.DARK_AQUA);
			player.sendStatusMessage(message.setStyle(style), false);
		}
		
		private String localizationPath(String path) {
			return CommandFoodList.name + "." + getName() + "." + path;
		}
	}
}
