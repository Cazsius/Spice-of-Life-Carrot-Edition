package com.cazsius.solcarrot.lib;

import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistryEntry;

public class Localization {
	/** e.g. keyString("tooltip", "eaten_status.not_eaten_1") -> "tooltip.solcarrot.eatenStatus.not_eaten_1") */
	private static String keyString(String domain, String path) {
		return domain + "." + Constants.MOD_ID + "." + path;
	}
	
	public static String localized(String domain, IForgeRegistryEntry entry, String path, Object... params) {
		final ResourceLocation location = entry.getRegistryName();
		assert location != null;
		return localized(domain, location.getPath() + "." + path, params);
	}
	
	public static String localized(String domain, String path, Object... params) {
		return I18n.format(keyString(domain, path), params);
	}
	
	public static String localizedQuantity(String domain, String path, int number) {
		return number == 1
				? I18n.format(keyString(domain, path + ".singular"))
				: I18n.format(keyString(domain, path + ".plural"), number);
	}
}
