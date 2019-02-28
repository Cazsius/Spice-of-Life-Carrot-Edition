package com.cazsius.solcarrot.command;

import com.cazsius.solcarrot.capability.FoodCapability;
import com.cazsius.solcarrot.handler.CapabilityHandler;
import com.cazsius.solcarrot.handler.MaxHealthHandler;
import net.minecraft.entity.player.EntityPlayer;

final class CommandClearFoodList extends CommandFoodList.SubCommand {
	
	@Override
	public String getName() {
		return "clear";
	}
	
	@Override
	void execute(EntityPlayer player, FoodCapability foodCapability, String[] args) {
		foodCapability.clearFood();
		CapabilityHandler.syncFoodList(player);
		MaxHealthHandler.updateFoodHPModifier(player);
		
		showMessage(player, localizedComponent("success"));
	}
	
	@Override
	public int getRequiredPermissionLevel() {
		return 2;
	}
}
