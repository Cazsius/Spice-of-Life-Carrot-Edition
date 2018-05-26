package com.cazsius.solcarrot.client;

import com.cazsius.solcarrot.client.gui.GuiFoodBook;
import com.cazsius.solcarrot.command.CommandSizeFoodArray;
import com.cazsius.solcarrot.common.CommonProxy;
import com.cazsius.solcarrot.handler.ContentHandler;
import com.cazsius.solcarrot.handler.HandlerTooltip;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy implements IGuiHandler {

	@Override
	public void init(FMLInitializationEvent event) {
		super.init(event);
		MinecraftForge.EVENT_BUS.register(new HandlerTooltip());
		ClientCommandHandler.instance.registerCommand(new CommandSizeFoodArray());
		ContentHandler.onClientPreInit();
	}
	
    @Override
    public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
		return z;
    	
    }

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		return new GuiFoodBook();
	}
}