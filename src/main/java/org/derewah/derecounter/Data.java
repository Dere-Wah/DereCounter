package org.derewah.derecounter;

import de.tr7zw.changeme.nbtapi.NBTBlock;
import de.tr7zw.changeme.nbtapi.iface.ReadWriteNBT;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.derewah.derecounter.objects.CompanyBook;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class Data implements Serializable {

    private HashMap<String, CompanyBook> companyBooks;
    private HashMap<String, Location> counters;

    private static transient final long serialVersionID = -1681012206529286330L;

    public void createCassa(Location location, String name){
        name = name.toLowerCase();
        if(!(companyBooks.containsKey(name))){
            CompanyBook companyBook = new CompanyBook(name);
            companyBooks.put(name, companyBook);
        }
        ReadWriteNBT nbt = new NBTBlock(location.getBlock()).getData();
        nbt.setString("derecounter", name);
    }

    Data(){
        companyBooks = new HashMap<>();

        if(Files.exists(Paths.get("./plugins/DereCounter/CompanyBooks.json"))){
            loadData("./plugins/DereCounter/CompanyBooks.json");
        }

    }

    public String getBookFromCounter(Location location){
        Block counterBlock = location.getBlock();
        ReadWriteNBT nbt = new NBTBlock(counterBlock).getData();
        return nbt.getString("derecounter");
    }

    public CompanyBook getCompanyBook(String companyBookName){
        companyBookName = companyBookName.toLowerCase();
        if(companyBooks.containsKey(companyBookName)){
            return companyBooks.get(companyBookName);
        }else{
            CompanyBook companyBook = new CompanyBook(companyBookName);
            companyBooks.put(companyBookName, companyBook);
            return companyBook;
        }
    }

    public boolean isCounter(Location location){
        Block counterBlock = location.getBlock();
        ReadWriteNBT nbt = new NBTBlock(counterBlock).getData();
        String companyName = nbt.getString("derecounter");
        return companyBooks.containsKey(companyName);
    }

    public boolean saveData(String storagePath){
        try{
            BukkitObjectOutputStream storageOut = new BukkitObjectOutputStream(new GZIPOutputStream(new FileOutputStream(storagePath)));
            storageOut.writeObject(this.companyBooks);
            storageOut.close();
            return true;
        }catch (IOException exception){
            exception.printStackTrace();
            return false;
        }
    }

    public boolean loadData(String storagePath) {
        try {
            BukkitObjectInputStream in = new BukkitObjectInputStream(new GZIPInputStream(new FileInputStream(storagePath)));
            companyBooks = (HashMap<String, CompanyBook>) in.readObject();
            in.close();
            return true;
        } catch (ClassNotFoundException | IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
    }
}
