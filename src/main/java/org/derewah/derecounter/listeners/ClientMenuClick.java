package org.derewah.derecounter.listeners;

import de.tr7zw.changeme.nbtapi.NBT;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.derewah.derecounter.DereCounter;
import org.derewah.derecounter.inventories.AnvilSetPrice;
import org.derewah.derecounter.inventories.ClientMenu;
import org.derewah.derecounter.utils.Lang;

import static org.derewah.derecounter.utils.Permissions.ADMIN_PERMISSION;
import static org.derewah.derecounter.utils.Permissions.getSellPermission;

public class ClientMenuClick implements Listener {


    @EventHandler
    public void onClick(InventoryClickEvent event){
        InventoryView view = event.getView();
        int slot = event.getSlot();
        Player player = Bukkit.getPlayer(event.getWhoClicked().getUniqueId());
        ItemStack item = event.getCurrentItem();
        if(item != null && item.getAmount() != 0 && item.getType() != Material.AIR){
            Integer menuType = NBT.get(item, nbt -> (Integer) nbt.getOrDefault("derecounter.menu", -1));
            if(menuType == 1) {
                event.setCancelled(true);
                if(event.getSlot()>= 0 &&
                        !(item.getType() == Material.AIR ||
                                item.getType() == Material.GRAY_STAINED_GLASS_PANE)) {

                    String borsaName = NBT.get(item, nbt -> (String) nbt.getOrNull("derecounter.name", String.class));
                    if(item.getType() == Material.SUNFLOWER){
                        ClientMenu clientMenu = new ClientMenu(borsaName);
                        clientMenu.openMenu(player);
                    }else{
                        OfflinePlayer targetOffPlayer = ((SkullMeta) item.getItemMeta()).getOwningPlayer();
                        if(targetOffPlayer != null && targetOffPlayer.isOnline()){

                            if(player.hasPermission(getSellPermission(borsaName)) || player.hasPermission(ADMIN_PERMISSION)){
                                new AnvilSetPrice(borsaName, player, targetOffPlayer);
                            }else{
                                player.sendMessage(Lang.PREFIX + Lang.NO_PERMS.toString().replace("%permission%",
                                        getSellPermission(borsaName)));
                            }


                        }else{
                            ClientMenu clientMenu = new ClientMenu(borsaName);
                            clientMenu.openMenu(player);
                        }
                    }
                }
            }

        }
    }


}
