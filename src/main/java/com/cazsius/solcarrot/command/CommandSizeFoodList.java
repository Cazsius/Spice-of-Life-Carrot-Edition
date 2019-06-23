package com.cazsius.solcarrot.command;

import com.cazsius.solcarrot.tracking.FoodList;
import com.cazsius.solcarrot.tracking.ProgressInfo;
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
	void execute(ICommandSender sender, EntityPlayer player, FoodList foodList) {
		ProgressInfo progressInfo = foodList.getProgressInfo();
		
		ITextComponent progressDesc = localizedQuantityComponent("desc.foods_eaten", progressInfo.foodsEaten);
		showMessage(sender, progressDesc);
		
		ITextComponent milestoneDesc = progressInfo.hasReachedMax()
			? localizedComponent("desc.milestone.max")
			: localizedComponent("desc.milestone.more", progressInfo.foodsUntilNextMilestone());
		showMessage(sender, milestoneDesc);
	}
	
	@Override
	public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
		return true;
	}
}
