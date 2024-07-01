package org.derewah.derecounter.inventories;

import de.tr7zw.changeme.nbtapi.NBT;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.derewah.derecounter.DereCounter;
import org.derewah.derecounter.utils.Lang;

import static org.derewah.derecounter.utils.Helpers.setCustomModelData;

public class MainMenu {


    private Inventory menu;
    private String companyName;

    public MainMenu(String companyName) {
        this.companyName = companyName;
        Inventory inventory = Bukkit.createInventory(null, 27,
                Lang.MAIN_MENU_TITLE.toString().replace("%company%", companyName));

        ItemStack frame = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta frameMeta = frame.getItemMeta();
        frameMeta.setDisplayName(Lang.MAIN_MENU_FRAME_NAME.toString());
        frame.setItemMeta(frameMeta);

        ItemStack sell = new ItemStack(Material.PAPER);
        ItemMeta sellMeta = sell.getItemMeta();
        sellMeta.setDisplayName(Lang.MAIN_MENU_SELL_NAME.toString());
        sell.setItemMeta(sellMeta);
        setCustomModelData(sell, (Integer) DereCounter.getInstance().getConfig().get("custom-model-data.main-menu-sell-model-data"));

        ItemStack register = new ItemStack(Material.PAPER);
        ItemMeta registerMeta = register.getItemMeta();
        registerMeta.setDisplayName(Lang.MAIN_MENU_REGISTER_NAME.toString());
        register.setItemMeta(registerMeta);
        setCustomModelData(register, (Integer) DereCounter.getInstance().getConfig().get("custom-model-data.main-menu-register-model-data"));

        ItemStack deposit = new ItemStack(Material.PAPER);
        ItemMeta depositMeta = deposit.getItemMeta();
        depositMeta.setDisplayName(Lang.MAIN_MENU_DEPOSIT_NAME.toString());
        deposit.setItemMeta(depositMeta);
        setCustomModelData(deposit, (Integer) DereCounter.getInstance().getConfig().get("custom-model-data.main-menu-deposit-model-data"));

        ItemStack withdraw = new ItemStack(Material.PAPER);
        ItemMeta withdrawMeta = withdraw.getItemMeta();
        withdrawMeta.setDisplayName(Lang.MAIN_MENU_WITHDRAW_NAME.toString());
        withdraw.setItemMeta(withdrawMeta);
        setCustomModelData(withdraw, (Integer) DereCounter.getInstance().getConfig().get("custom-model-data.main-menu-withdraw-model-data"));

        for (int i = 0; i < inventory.getSize(); i++) {
            if (i <= 9 || i >= 17) {
                inventory.setItem(i, frame);
            }
        }

        inventory.setItem(10, sell);
        inventory.setItem(12, register);
        inventory.setItem(14, deposit);
        inventory.setItem(16, withdraw);
        menu = inventory;
    }

    public void openMenu(Player player){

        if(!player.hasPermission("derecounter.use."+this.companyName) || !player.hasPermission("derecounter.admin")){
            player.sendMessage(Lang.PREFIX+
                    Lang.NO_PERMS.toString().replace("%permission%", "derecounter.use."+this.companyName));
            return;
        }


        for (int i = 0; i<menu.getSize(); i++){
            ItemStack item = menu.getItem(i);
            if (item != null && item.getType() != Material.AIR){
                NBT.modify(item, nbt -> {
                    nbt.setString("derecounter.name", companyName);
                    nbt.setInteger("derecounter.menu", 0);
                });
            }
        }

        player.openInventory(menu);
    }

}
