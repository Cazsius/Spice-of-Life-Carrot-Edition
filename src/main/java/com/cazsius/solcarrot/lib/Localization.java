package com.cazsius.solcarrot.lib;

import com.cazsius.solcarrot.SOLCarrot;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistryEntry;

public final class Localization {
	public static String keyString(String domain, IForgeRegistryEntry entry, String path) {
		final ResourceLocation location = entry.getRegistryName();
		assert location != null;
		return keyString(domain, location.getPath() + "." + path);
	}
	
	/** e.g. keyString("tooltip", "eaten_status.not_eaten_1") -> "tooltip.solcarrot.eatenStatus.not_eaten_1") */
	public static String keyString(String domain, String path) {
		return domain + "." + SOLCarrot.MOD_ID + "." + path;
	}
	
	@SideOnly(Side.CLIENT)
	public static String localized(String domain, IForgeRegistryEntry entry, String path, Object... args) {
		return I18n.format(keyString(domain, entry, path), args);
	}
	
	public static ITextComponent localizedComponent(String domain, IForgeRegistryEntry entry, String path, Object... args) {
		return new TextComponentTranslation(keyString(domain, entry, path), args);
	}
	
	@SideOnly(Side.CLIENT)
	public static String localized(String domain, String path, Object... args) {
		return I18n.format(keyString(domain, path), args);
	}
	
	public static ITextComponent localizedComponent(String domain, String path, Object... args) {
		return new TextComponentTranslation(keyString(domain, path), args);
	}
	
	@SideOnly(Side.CLIENT)
	public static String localizedQuantity(String domain, String path, int number) {
		return number == 1
			? I18n.format(keyString(domain, path + ".singular"))
			: I18n.format(keyString(domain, path + ".plural"), number);
	}
	
	public static ITextComponent localizedQuantityComponent(String domain, String path, int number) {
		return number == 1
			? new TextComponentTranslation(keyString(domain, path + ".singular"))
			: new TextComponentTranslation(keyString(domain, path + ".plural"), number);
	}
	
	public static String formatBigNumber(int number) {
		if (number < 1000) {
			return "" + number;
		} else if (number < 10_000) {
			return Math.round(number / 1000F) + "." + Math.round((number % 1000) / 100F) + "k";
		} else {
			return Math.round(number / 1000F) + "k";
		}
	}
	
	private Localization() {}
}
