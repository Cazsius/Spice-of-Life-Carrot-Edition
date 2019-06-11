package com.cazsius.solcarrot.communication;

import com.cazsius.solcarrot.client.gui.GuiFoodBook;
import com.cazsius.solcarrot.item.ItemFoodBook;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

public class GuiHandler implements IGuiHandler {
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
