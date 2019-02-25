package com.cazsius.solcarrot.command;

import com.cazsius.solcarrot.capability.FoodCapability;
import com.cazsius.solcarrot.handler.HandlerConfiguration;
import net.minecraft.command.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

import static com.cazsius.solcarrot.lib.Localization.localized;

public abstract class Command extends CommandBase {
	
	@Override
	public String getUsage(ICommandSender sender) {
		return localized("command", getName() + ".usage");
	}
	
	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if (!(sender.getCommandSenderEntity() instanceof EntityPlayer))
			throw new WrongUsageException(localized("command", "error.players_only"));
		EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity();
		execute(player, args);
	}
	
	void execute(EntityPlayer player, String[] args) {
		FoodCapability foodCapability = player.getCapability(FoodCapability.FOOD_CAPABILITY, null);
		assert foodCapability != null;
		execute(player, foodCapability, args);
	}
	
	void execute(EntityPlayer player, FoodCapability foodCapability, String[] args) {}
	
	public static void showMessage(EntityPlayer player, String message) {
		boolean showAboveHotbar = HandlerConfiguration.shouldShowProgressAboveHotbar();
		if (showAboveHotbar) {
			message = message.replace("\n", " ");
		}
		
		player.sendStatusMessage(
				new TextComponentString(TextFormatting.DARK_AQUA + message),
				showAboveHotbar
		);
	}
}
