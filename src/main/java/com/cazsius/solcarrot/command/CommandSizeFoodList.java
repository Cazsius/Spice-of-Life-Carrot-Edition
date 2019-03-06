package com.cazsius.solcarrot.command;

import com.cazsius.solcarrot.lib.ProgressInfo;
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
		ProgressInfo progressInfo = ProgressInfo.getProgressInfo(player);
		
		ITextComponent progressDesc = localizedQuantityComponent("desc.foods_eaten", progressInfo.foodsEaten);
		
		ITextComponent milestoneDesc = progressInfo.hasReachedMax()
				? localizedComponent("desc.milestone.max")
				: localizedComponent("desc.milestone.more", progressInfo.foodsUntilNextMilestone());
		
		showMessage(player, progressDesc, milestoneDesc);
	}
	
	@Override
	public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
		return true;
	}
}
