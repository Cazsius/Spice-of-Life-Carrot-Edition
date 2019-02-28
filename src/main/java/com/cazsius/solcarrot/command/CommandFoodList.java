package com.cazsius.solcarrot.command;

import com.cazsius.solcarrot.capability.FoodCapability;
import com.cazsius.solcarrot.lib.Localization;
import net.minecraft.command.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.*;
import net.minecraftforge.server.command.CommandTreeBase;

import java.util.Arrays;
import java.util.Optional;

import static com.cazsius.solcarrot.lib.Localization.keyString;
import static com.cazsius.solcarrot.lib.Localization.localized;

public class CommandFoodList extends CommandTreeBase {
	
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
		return localized("command", getName() + ".usage");
	}
	
	@Override
	public int getRequiredPermissionLevel() {
		return 0;
	}
	
	public static abstract class SubCommand extends CommandBase {
		
		@Override
		public String getUsage(ICommandSender sender) {
			return keyString("command", localizationPath("usage"));
		}
		
		@Override
		public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
			execute(CommandFoodList.getCommandSenderAsPlayer(sender), args);
		}
		
		void execute(EntityPlayer player, String[] args) {
			FoodCapability foodCapability = player.getCapability(FoodCapability.FOOD_CAPABILITY, null);
			assert foodCapability != null;
			execute(player, foodCapability, args);
		}
		
		void execute(EntityPlayer player, FoodCapability foodCapability, String[] args) {}
		
		ITextComponent localizedComponent(String path, Object... args) {
			return Localization.localizedComponent("command", localizationPath(path), args);
		}
		
		ITextComponent localizedQuantityComponent(String path, int number) {
			return Localization.localizedQuantityComponent("command", localizationPath(path), number);
		}
		
		static void showMessage(EntityPlayer player, ITextComponent... message) {
			Optional<ITextComponent> combinedMessage = Arrays.stream(message)
					.reduce((acc, next) -> acc.appendText("\n").appendSibling(next));
			assert combinedMessage.isPresent(); // at least one message to send
			
			ITextComponent formatting = new TextComponentString(TextFormatting.DARK_AQUA.toString());
			player.sendStatusMessage(
					formatting.appendSibling(combinedMessage.get()),
					false
			);
		}
		
		private String localizationPath(String path) {
			return CommandFoodList.name + "." + getName() + "." + path;
		}
	}
}
