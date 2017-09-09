package com.cazsius.solcarrot.command;

import java.util.Random;

import com.cazsius.solcarrot.capability.FoodCapability;
import com.cazsius.solcarrot.handler.HandlerConfiguration;
import com.cazsius.solcarrot.lib.Constants;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentTranslation;

public class CommandSizeFoodArray extends CommandBase {

	@Override
	public String getName() {
		return "sizefoodarray";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return Constants.CommandMessages.SIZE_FOOD_ARRAY;
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		FoodCapability food = sender.getCommandSenderEntity().getCapability(FoodCapability.FOOD_CAPABILITY, null);
		//TODO Make it display how much food you need total for next milestone
		TextComponentTranslation size = new TextComponentTranslation("You've eaten " + food.foodList.size()
				+ " unqiue food(s)! You need  " + new Random()/*TODO Insert logic for milestone number here */ + " more unique foods to reach you're next milestone",
				new Object[0]);
		sender.sendMessage(size);

	}

}
