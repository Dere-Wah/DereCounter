package org.derewah.derecounter.utils;

import de.tr7zw.changeme.nbtapi.NBT;
import de.tr7zw.changeme.nbtapi.NBTBlock;
import de.tr7zw.changeme.nbtapi.iface.ReadWriteNBT;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.derewah.derecounter.DereCounter;
import org.derewah.derecounter.database.Database;
import org.derewah.derecounter.objects.CompanyBook;

import java.sql.SQLException;

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


	public static void createCassa(Location location, String name){
		name = name.toLowerCase();
		Database db = DereCounter.getInstance().getDatabase();
		CompanyBook companyBook = null;
		try {
			companyBook = db.getCompanyBook(name);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

		if (companyBook == null){
			try {
				companyBook = db.addCompanyBook(name);
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
		}
		ReadWriteNBT nbt = new NBTBlock(location.getBlock()).getData();
		nbt.setString("derecounter", name);
	}


	public static String getBookFromCounter(Location location){
		Block counterBlock = location.getBlock();
		ReadWriteNBT nbt = new NBTBlock(counterBlock).getData();
		return nbt.getString("derecounter");
	}

	public static CompanyBook getCompanyBook(String companyBookName){
		companyBookName = companyBookName.toLowerCase();

		Database db = DereCounter.getInstance().getDatabase();

		CompanyBook companyBook = null;

		try {
			companyBook = db.getCompanyBook(companyBookName);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

		return companyBook;
	}

	public static boolean isCounter(Location location){
		Block counterBlock = location.getBlock();
		ReadWriteNBT nbt = new NBTBlock(counterBlock).getData();
		String companyName = nbt.getString("derecounter");
		return getCompanyBook(companyName) != null;
	}
}
