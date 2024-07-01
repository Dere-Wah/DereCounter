package org.derewah.derecounter.utils;

import de.tr7zw.changeme.nbtapi.NBT;
import org.bukkit.inventory.ItemStack;

public class Helpers {

	public static boolean isNumeric(String strNum) {
		if (strNum == null) {
			return false;
		}
		try {
			double d = Double.parseDouble(strNum);
		} catch (NumberFormatException nfe) {
			return false;
		}
		return true;

	}

	public static void setCustomModelData(ItemStack item, int modelData){
		NBT.modify(item, nbt -> {nbt.setInteger("CustomModelData", modelData);});
	}

}
