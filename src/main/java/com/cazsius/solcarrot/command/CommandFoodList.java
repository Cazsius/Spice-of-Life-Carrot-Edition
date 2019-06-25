package com.cazsius.solcarrot.command;

import com.cazsius.solcarrot.lib.Localization;
import com.cazsius.solcarrot.tracking.FoodList;
import net.minecraft.command.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.*;
import net.minecraftforge.server.command.CommandTreeBase;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

import static com.cazsius.solcarrot.lib.Localization.keyString;

public final class CommandFoodList extends CommandTreeBase {
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
	
	private static EntityPlayer getPlayerEntity(ICommandSender sender) throws SyntaxErrorException {
		Entity senderEntity = sender.getCommandSenderEntity();
		if (!(senderEntity instanceof EntityPlayer)) throw new SyntaxErrorException("commands.generic.player.unspecified");
		return (EntityPlayer) senderEntity;
	}
	
	public abstract static class SubCommand extends CommandBase {
		@Override
		public String getUsage(ICommandSender sender) {
			return keyString("command", localizationPath("usage"));
		}
		
		@Override
		public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
			if (args.length < 2) {
				return getListOfStringsMatchingLastWord(args, server.getOnlinePlayerNames());
			} else {
				return Collections.emptyList();
			}
		}
		
		@Override
		public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
			if (args.length > 1) throw new WrongUsageException(getUsage(sender));
			
			EntityPlayer player;
			if (args.length == 1) {
				player = getPlayer(server, sender, args[0]);
				
				if (!player.equals(sender.getCommandSenderEntity()) && !sender.canUseCommand(2, getName())) {
					showMessage(sender, Localization.localizedComponent("command", "foodlist.no_permissions"), TextFormatting.RED);
					return;
				}
			} else { // no args
				player = getPlayerEntity(sender);
			}
			
			execute(sender, player, FoodList.get(player));
		}
		
		abstract void execute(ICommandSender sender, EntityPlayer player, FoodList foodList);
		
		final ITextComponent localizedComponent(String path, Object... args) {
			return Localization.localizedComponent("command", localizationPath(path), args);
		}
		
		final ITextComponent localizedQuantityComponent(String path, int number) {
			return Localization.localizedQuantityComponent("command", localizationPath(path), number);
		}
		
		static void showMessage(ICommandSender sender, ITextComponent message, TextFormatting color) {
			Style style = new Style().setColor(color);
			sender.sendMessage(message.setStyle(style));
		}
		
		static void showMessage(ICommandSender sender, ITextComponent message) {
			showMessage(sender, message, TextFormatting.DARK_AQUA);
		}
		
		private String localizationPath(String path) {
			return CommandFoodList.name + "." + getName() + "." + path;
		}
	}
}
