package org.derewah.derecounter.objects;

import de.tr7zw.changeme.nbtapi.NBT;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.derewah.derecounter.DereCounter;
import org.derewah.derecounter.utils.Lang;

import java.util.Arrays;

import static org.derewah.derecounter.utils.Helpers.setCustomModelData;

public class CounterSet {


    private String itemName = null;

    private ItemStack item;

    private String counterName;
    public CounterSet(String companyName){
        ItemStack item = new ItemStack(Material.STICK);
        itemName = Lang.SETCOUNTER_ITEM_NAME.toString().replace("%company%", companyName);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(itemName);
        Material counterMaterial = Material.valueOf(DereCounter.getInstance().getConfig().getString("counter-block"));
        itemMeta.setLore(Arrays.asList(Lang.SETCOUNTER_ITEM_LORE.toString().replace("%company%", companyName).replace("%block%", counterMaterial.toString()).split("%nl%")));
        item.setItemMeta(itemMeta);
        counterName = companyName;
        NBT.modify(item, nbt -> {
            nbt.setBoolean("derecounter.setcounter_item", true);
            nbt.setString("derecounter.name", companyName);
        });
        setCustomModelData(item, (Integer) DereCounter.getInstance().getConfig().get("counter-set-item-model-data"));

        this.item = item;
    }

    public String getName(){
        return itemName;
    }

    public ItemStack getItem(){
        return this.item;
    }








}
