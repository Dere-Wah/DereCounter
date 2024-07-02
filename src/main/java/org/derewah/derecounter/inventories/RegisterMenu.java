package org.derewah.derecounter.inventories;

import de.tr7zw.changeme.nbtapi.NBT;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.derewah.derecounter.DereCounter;
import org.derewah.derecounter.objects.CompanyBook;
import org.derewah.derecounter.objects.RegistryAction;
import org.derewah.derecounter.utils.Lang;

import static org.derewah.derecounter.utils.Helpers.getCompanyBook;
import static org.derewah.derecounter.utils.Helpers.setCustomModelData;
import static org.derewah.derecounter.utils.Permissions.ADMIN_PERMISSION;
import static org.derewah.derecounter.utils.Permissions.getUsePermission;

public class RegisterMenu {


    private Inventory menu;

    CompanyBook companyBook;

    public RegisterMenu(String borsaName) {
        this.companyBook = getCompanyBook(borsaName);
        if (companyBook == null) {
            return;
        }

        double balance = companyBook.getBalance();

        int size = 54;
        Inventory inventory = Bukkit.createInventory(null, size,
                Lang.REGISTER_MENU_TITLE.toString().replace("%company%", borsaName)
                        + Lang.REGISTER_MENU_BALANCE_FORMAT.toString().replace("%balance%", String.format("%.2f", balance)));

        ItemStack frame = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta frameMeta = frame.getItemMeta();
        frameMeta.setDisplayName(Lang.MAIN_MENU_FRAME_NAME.toString());
        frame.setItemMeta(frameMeta);

        for (int i = 0; i < size; i++) {
            if ((i <= 9 || i >= size - 9) || (i % 9 == 0 || (i + 1) % 9 == 0)) {
                inventory.setItem(i, frame);
            }
        }

        menu = inventory;
    }

    public void openMenu(Player player, int page) {

    if(!player.hasPermission(getUsePermission(companyBook.getName())) || !player.hasPermission(ADMIN_PERMISSION)){
            player.sendMessage(Lang.PREFIX+
                    Lang.NO_PERMS.toString().replace("%permission%", getUsePermission(companyBook.getName())));
            return;
        }



        int div = (companyBook.getRegister().size() + 27) / 28;
        if (page < 1) {
            page = div;
        } else if (page > div) {
            page = 1;
        }

        ItemStack prev = new ItemStack(Material.STONE_BUTTON);
        ItemMeta prevMeta = prev.getItemMeta();
        prevMeta.setDisplayName(Lang.MENU_PREVIOUS_NAME.toString());
        prev.setItemMeta(prevMeta);
        setCustomModelData(prev, (Integer) DereCounter.getInstance().getConfig().get("custom-model-data.register-menu-previous-model-data"));
        int finalPage = page;
        NBT.modify(prev, nbt -> {
            nbt.setInteger("derecounter.page", finalPage - 1);
        });

        ItemStack succ = new ItemStack(Material.STONE_BUTTON);
        ItemMeta succMeta = succ.getItemMeta();
        succMeta.setDisplayName(Lang.MENU_NEXT_NAME.toString());
        succ.setItemMeta(succMeta);
        setCustomModelData(prev, (Integer) DereCounter.getInstance().getConfig().get("custom-model-data.register-menu-next-model-data"));
        NBT.modify(succ, nbt -> {
            nbt.setInteger("derecounter.page", finalPage + 1);
        });

        menu.setItem(48, prev);
        menu.setItem(50, succ);

        int max = page * 28;
        int min = max - 28;
        int slot = 10;
        int x = -1;
        for (RegistryAction action : companyBook.getRegister()) {
            x++;
            if (x >= max) {
                break;
            } else if (x >= min) {
                if (slot == 17 || slot == 26 || slot == 35) {
                    slot += 2;
                }
                menu.setItem(slot, action.getDescriptorItem());
                slot++;
            }
        }

        for (int i = 0; i < menu.getSize(); i++) {
            ItemStack item = menu.getItem(i);
            if (item != null && item.getType() != Material.AIR) {
                NBT.modify(item, nbt -> {
                    nbt.setString("derecounter.name", companyBook.getName());
                    nbt.setInteger("derecounter.menu", 2);
                });
            }
        }

        player.openInventory(menu);
    }

}
