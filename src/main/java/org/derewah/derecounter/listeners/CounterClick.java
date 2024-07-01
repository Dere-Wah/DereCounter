package org.derewah.derecounter.listeners;

import de.tr7zw.changeme.nbtapi.NBT;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.derewah.derecounter.DereCounter;
import org.derewah.derecounter.inventories.MainMenu;
import org.derewah.derecounter.utils.Lang;

import static org.derewah.derecounter.utils.Helpers.*;

public class CounterClick implements Listener {

    @EventHandler
    public void onClick(PlayerInteractEvent event){
        Block block = event.getClickedBlock();
        Player player = event.getPlayer();
        ItemStack handItem = event.getItem();
        Action action = event.getAction();
        if (block != null) {
            Material counterMaterial = Material.valueOf(DereCounter.getInstance().getConfig().getString("items.counter-block"));
            if (block.getBlockData().getMaterial() == counterMaterial) {
                if (action == Action.RIGHT_CLICK_BLOCK) {
                    if (handItem != null && (NBT.get(handItem, nbt -> (Boolean) nbt.getBoolean("derecounter.setcounter_item")))) {
                        if (player.hasPermission("derecounter.admin")) {
                            if (!isCounter(block.getLocation())) {
                                assert handItem.getItemMeta().getLore().size() >= 1;
                                String borsaName = NBT.get(handItem, nbt -> (String) nbt.getString("derecounter.name"));

                                createCassa(block.getLocation(), borsaName);
                                player.sendMessage(Lang.PREFIX + Lang.PLACED_COUNTER.toString().replace("%company%", borsaName));
                            } else {
                                player.sendMessage(Lang.PREFIX + Lang.ALREADY_COUNTER.toString());
                            }
                        } else {
                            player.sendMessage(Lang.PREFIX + Lang.NO_PERMS.toString().replace("%permission%", "derecounter.admin"));
                        }
                    } else {
                        if (isCounter(block.getLocation())) {
                            String borsaName = getBookFromCounter(block.getLocation());
                            if (player.hasPermission("derecounter.use." + borsaName)) {
                                new MainMenu(borsaName).openMenu(player);
                            } else {
                                player.sendMessage(Lang.PREFIX + Lang.NO_PERMS.toString().replace("%permission%", "derecounter.use." + borsaName));
                            }
                        }
                    }
                } else if (action == Action.LEFT_CLICK_BLOCK) {
                    if (isCounter(block.getLocation())) {
                        if (player.hasPermission("derecounter.admin")) {
                            String borsaName = getBookFromCounter(block.getLocation());
                            player.sendMessage(Lang.PREFIX + Lang.REMOVED_COUNTER.toString().replace("%company%", borsaName));
                        } else {
                            player.sendMessage(Lang.PREFIX + Lang.NO_PERMS.toString().replace("%permission%", "derecounter.admin"));
                            event.setCancelled(true);
                        }
                    }
                }
            }
        }
    }


}
