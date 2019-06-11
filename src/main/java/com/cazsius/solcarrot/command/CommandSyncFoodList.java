package com.cazsius.solcarrot.command;

import com.cazsius.solcarrot.tracking.CapabilityHandler;
import net.minecraft.entity.player.EntityPlayer;

final class CommandSyncFoodList extends CommandFoodList.SubCommand {
	@Override
	public String getName() {
		return "sync";
	}
	
	@Override
	void execute(EntityPlayer player, String[] args) {
		CapabilityHandler.syncFoodList(player);
		
		showMessage(player, localizedComponent("success"));
	}
	
	@Override
	public int getRequiredPermissionLevel() {
		return 0;
	}
}
