package com.cazsius.solcarrot.communication;

import com.cazsius.solcarrot.SOLCarrot;
import com.cazsius.solcarrot.client.gui.GuiFoodBook;
import com.cazsius.solcarrot.item.ItemFoodBook;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

@Mod.EventBusSubscriber(modid = SOLCarrot.MOD_ID)
public final class GuiHandler implements IGuiHandler {
	@SubscribeEvent
	public static void preInit(SOLCarrot.PreInitializationEvent e) {
		NetworkRegistry.INSTANCE.registerGuiHandler(SOLCarrot.instance, new GuiHandler());
	}
	
	@Nullable
	@Override
	public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
		switch (id) {
			default:
				return null;
		}
	}
	
	@Nullable
	@Override
	@SideOnly(Side.CLIENT)
	public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
		switch (id) {
			case ItemFoodBook.GUI_ID:
				return new GuiFoodBook(player);
			default:
				return null;
		}
	}
}
