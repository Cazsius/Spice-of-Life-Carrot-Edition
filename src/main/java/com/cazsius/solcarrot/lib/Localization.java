package com.cazsius.solcarrot.lib;

import com.cazsius.solcarrot.SOLCarrot;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public final class Localization {
	/** e.g. keyString("tooltip", "eaten_status.not_eaten_1") -> "tooltip.solcarrot.eatenStatus.not_eaten_1") */
	public static String keyString(String domain, String path) {
		return domain + "." + SOLCarrot.MOD_ID + "." + path;
	}
	
	@OnlyIn(Dist.CLIENT)
	public static String localized(String domain, String path, Object... args) {
		return I18n.get(keyString(domain, path), args);
	}
	
	public static MutableComponent localizedComponent(String domain, String path, Object... args) {
		return Component.translatable(keyString(domain, path), args);
	}
	
	@OnlyIn(Dist.CLIENT)
	public static String localizedQuantity(String domain, String path, int number) {
		return number == 1
			? I18n.get(keyString(domain, path + ".singular"))
			: I18n.get(keyString(domain, path + ".plural"), number);
	}
	
	public static MutableComponent localizedQuantityComponent(String domain, String path, int number) {
		return number == 1
			? Component.translatable(keyString(domain, path + ".singular"))
			: Component.translatable(keyString(domain, path + ".plural"), number);
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
