package com.cazsius.solcarrot.command;

import com.cazsius.solcarrot.tracking.CapabilityHandler;
import com.cazsius.solcarrot.tracking.FoodList;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;

final class CommandSyncFoodList extends CommandFoodList.SubCommand {
	@Override
	public String getName() {
		return "sync";
	}
	
	@Override
	void execute(ICommandSender sender, EntityPlayer player, FoodList foodList) {
		CapabilityHandler.syncFoodList(player);
		
		showMessage(sender, localizedComponent("success"));
	}
	
	@Override
	public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
		return true;
	}
}
