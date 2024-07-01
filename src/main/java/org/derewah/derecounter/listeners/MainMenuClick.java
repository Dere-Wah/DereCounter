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
import org.derewah.derecounter.DereCounter;
import org.derewah.derecounter.inventories.AnvilSetDeposit;
import org.derewah.derecounter.inventories.AnvilSetWithdraw;
import org.derewah.derecounter.inventories.ClientMenu;
import org.derewah.derecounter.inventories.RegisterMenu;

public class MainMenuClick implements Listener {



    @EventHandler
    public void onClick(InventoryClickEvent event){
        InventoryView view = event.getView();
        int slot = event.getSlot();
        Player player = Bukkit.getPlayer(event.getWhoClicked().getUniqueId());
        ItemStack item = event.getCurrentItem();
        if(item != null && item.getAmount() != 0 && item.getType() != Material.AIR){
            Integer menuType = NBT.get(item, nbt -> (Integer) nbt.getOrDefault("derecounter.menu", -1));
            if(menuType == 0) {
                event.setCancelled(true);
                if(event.getSlot()>= 0 &&
                    !(item.getType() == Material.AIR ||
                    item.getType() == Material.GRAY_STAINED_GLASS_PANE)) {
                    String borsaName = NBT.get(item, nbt -> (String) nbt.getOrNull("derecounter.name", String.class));
                    switch (slot) {
                        case 10:
                            ClientMenu clientMenu = new ClientMenu(borsaName);
                            clientMenu.openMenu(player);
                            break;
                        case 12:
                            RegisterMenu registerMenu = new RegisterMenu(borsaName);
                            registerMenu.openMenu(player, 0);
                            break;
                        case 14:
                            new AnvilSetDeposit(borsaName, player);
                            break;
                        case 16:
                            new AnvilSetWithdraw(borsaName, player);
                            break;
                    }

                }
            }
        }
    }


}
