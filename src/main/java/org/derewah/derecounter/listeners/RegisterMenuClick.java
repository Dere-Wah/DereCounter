package org.derewah.derecounter.listeners;

import de.tr7zw.changeme.nbtapi.NBT;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.derewah.derecounter.Data;
import org.derewah.derecounter.DereCounter;
import org.derewah.derecounter.inventories.RegisterMenu;

public class RegisterMenuClick implements Listener {



    Data data = DereCounter.getInstance().getData();
    @EventHandler
    public void onClick(InventoryClickEvent event){
        InventoryView view = event.getView();
        int slot = event.getSlot();
        Player player = Bukkit.getPlayer(event.getWhoClicked().getUniqueId());
        ItemStack item = event.getCurrentItem();
        if (item != null && item.getAmount() != 0 && item.getType() != Material.AIR){
            int menuType = NBT.get(item, nbt -> (Integer) nbt.getOrDefault("derecounter.menu", -1));
            if(menuType == 2) {
                event.setCancelled(true);
                if(slot >= 0 &&
                        (item.getType() == Material.STONE_BUTTON)) {
                    String borsaName = NBT.get(item, nbt -> (String) nbt.getOrNull("derecounter.name", String.class));
                    Integer page = NBT.get(item, nbt -> (Integer) nbt.getOrNull("derecounter.page", Integer.class));
                    RegisterMenu registerMenu = new RegisterMenu(borsaName);
                    registerMenu.openMenu(player, page);
                }
            }
        }

    }
}
