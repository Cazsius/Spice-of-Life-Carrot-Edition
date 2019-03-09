package com.cazsius.solcarrot.command;

import com.cazsius.solcarrot.capability.FoodCapability;
import com.cazsius.solcarrot.capability.ProgressInfo;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.ITextComponent;

final class CommandSizeFoodList extends CommandFoodList.SubCommand {
	
	@Override
	public String getName() {
		return "size";
	}
	
	@Override
	void execute(EntityPlayer player, String[] args) {
		ProgressInfo progressInfo = FoodCapability.get(player).getProgressInfo();
		
		ITextComponent progressDesc = localizedQuantityComponent("desc.foods_eaten", progressInfo.foodsEaten);
		showMessage(player, progressDesc);
		
		ITextComponent milestoneDesc = progressInfo.hasReachedMax()
			? localizedComponent("desc.milestone.max")
			: localizedComponent("desc.milestone.more", progressInfo.foodsUntilNextMilestone());
		showMessage(player, milestoneDesc);
	}
	
	@Override
	public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
		return true;
	}
}
