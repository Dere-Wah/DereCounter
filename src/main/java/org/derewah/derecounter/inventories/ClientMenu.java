package org.derewah.derecounter.inventories;

import de.tr7zw.changeme.nbtapi.NBT;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.derewah.derecounter.DereCounter;
import org.derewah.derecounter.utils.Lang;

import java.util.Arrays;

import static org.derewah.derecounter.utils.Helpers.setCustomModelData;
import static org.derewah.derecounter.utils.Permissions.ADMIN_PERMISSION;
import static org.derewah.derecounter.utils.Permissions.getSellPermission;

public class ClientMenu {


    private Inventory menu;

    private String borsaName;

    public ClientMenu(String borsaName) {
        Inventory inventory = Bukkit.createInventory(null, 36, Lang.CLIENT_MENU_TITLE.toString());
        this.borsaName = borsaName;

        ItemStack frame = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta frameMeta = frame.getItemMeta();
        frameMeta.setDisplayName(Lang.CLIENT_MENU_FRAME_NAME.toString());
        frame.setItemMeta(frameMeta);

        ItemStack refresh = new ItemStack(Material.SUNFLOWER);
        ItemMeta refreshMeta = refresh.getItemMeta();
        refreshMeta.setDisplayName(Lang.CLIENT_MENU_REFRESH_NAME.toString());
        refresh.setItemMeta(refreshMeta);
        setCustomModelData(refresh, (Integer) DereCounter.getInstance().getConfig().get("custom-model-data.client-menu-refresh-model-data"));

        for (int i = 0; i < inventory.getSize(); i++) {
            if (!((i >= 10 && i <= 16) || (i >= 19 && i <= 25))) {
                inventory.setItem(i, frame);
            }
        }

        inventory.setItem(4, refresh);
        menu = inventory;
    }

    public void openMenu(Player player){

        if(!player.hasPermission(getSellPermission(borsaName)) || !player.hasPermission(ADMIN_PERMISSION)){
            player.sendMessage(Lang.PREFIX+
                    Lang.NO_PERMS.toString().replace("%permission%", getSellPermission(borsaName)));
            return;
        }


        int i = 9;
        for (Entity ps : player.getNearbyEntities(10, 10, 10)){
            if(ps instanceof Player){
                Player target = (Player) ps;
                    i++;
                    if(i == 17){
                        i = 19;
                    }if(i < 26){
                        ItemStack head = new ItemStack(Material.PLAYER_HEAD);
                        SkullMeta headMeta = (SkullMeta) head.getItemMeta();
                        headMeta.setOwningPlayer(target);
                        headMeta.setDisplayName(ChatColor.DARK_AQUA+target.getName());
                        head.setItemMeta(headMeta);
                        menu.setItem(i, head);
                    }
            }
        }
        ItemStack head = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta headMeta = (SkullMeta) head.getItemMeta();
        headMeta.setOwningPlayer(player);
        headMeta.setDisplayName(ChatColor.DARK_AQUA+player.getName());
        head.setItemMeta(headMeta);
        menu.setItem(10, head);


        for (int j = 0; j<menu.getSize(); j++){
            ItemStack item = menu.getItem(j);
            if (item != null && item.getType() != Material.AIR){
                NBT.modify(item, nbt -> {
                    nbt.setString("derecounter.name", borsaName);
                    nbt.setInteger("derecounter.menu", 1);
                });
            }
        }

        player.openInventory(menu);
    }

}
