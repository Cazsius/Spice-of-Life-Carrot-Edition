package com.cazsius.solcarrot.lib;

import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.registries.IForgeRegistryEntry;

public class Localization {
	public static String keyString(String domain, IForgeRegistryEntry entry, String path) {
		final ResourceLocation location = entry.getRegistryName();
		assert location != null;
		return keyString(domain, location.getPath() + "." + path);
	}
	
	/** e.g. keyString("tooltip", "eaten_status.not_eaten_1") -> "tooltip.solcarrot.eatenStatus.not_eaten_1") */
	public static String keyString(String domain, String path) {
		return domain + "." + Constants.MOD_ID + "." + path;
	}
	
	public static String localized(String domain, IForgeRegistryEntry entry, String path, Object... args) {
		return I18n.format(keyString(domain, entry, path), args);
	}
	
	public static ITextComponent localizedComponent(String domain, IForgeRegistryEntry entry, String path, Object... args) {
		return new TextComponentTranslation(keyString(domain, entry, path), args);
	}
	
	public static String localized(String domain, String path, Object... args) {
		return I18n.format(keyString(domain, path), args);
	}
	
	public static ITextComponent localizedComponent(String domain, String path, Object... args) {
		return new TextComponentTranslation(keyString(domain, path), args);
	}
	
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
}
