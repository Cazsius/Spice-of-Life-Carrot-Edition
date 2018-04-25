package com.cazsius.solcarrot.command;

import java.util.Collections;
import java.util.List;

import com.cazsius.solcarrot.capability.FoodCapability;
import com.cazsius.solcarrot.handler.MaxHealthHandler;
import com.cazsius.solcarrot.lib.Constants;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;

public class CommandClearFoodArray extends CommandBase {

	@Override
	public String getName() {
		return "clearfoodlist";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return Constants.CommandMessages.CLEAR_FOOD_ARRAY;
	}
	
	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if (args.length < 1) {
			throw new WrongUsageException(Constants.CommandMessages.CLEAR_FOOD_ARRAY);
		} else {
			EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity();
			TextComponentTranslation clear;
			TextComponentString clear_send;
			FoodCapability food = sender.getCommandSenderEntity().getCapability(FoodCapability.FOOD_CAPABILITY, null);
			food.foodList.clear();
			MaxHealthHandler.updateFoodHPModifier((EntityPlayer) sender);
			clear = new TextComponentTranslation("solcarrot.command.clearfoodarray.desc");
			clear_send = new TextComponentString(TextFormatting.DARK_AQUA + clear.getUnformattedText());
			player.sendStatusMessage(clear_send, true);
		}
	}

	@Override
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args,
			BlockPos targetPos) {
		return args.length == 1 ? getListOfStringsMatchingLastWord(args, server.getOnlinePlayerNames())
				: Collections.<String>emptyList();
	}

}
