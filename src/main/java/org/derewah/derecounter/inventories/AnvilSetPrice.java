package org.derewah.derecounter.inventories;

import net.milkbowl.vault.economy.Economy;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.derewah.derecounter.DereCounter;
import org.derewah.derecounter.utils.Lang;

import java.util.Arrays;
import java.util.Collections;

import static org.derewah.derecounter.utils.Helpers.isNumeric;
import static org.derewah.derecounter.utils.Helpers.setCustomModelData;
import static org.derewah.derecounter.utils.Permissions.ADMIN_PERMISSION;
import static org.derewah.derecounter.utils.Permissions.getUsePermission;

public class AnvilSetPrice {





    Economy econ = DereCounter.getEcon();

    public AnvilSetPrice(String borsaName, Player seller, OfflinePlayer buyer) {
        AnvilGUI.Builder anvil = new AnvilGUI.Builder();
        anvil.text(Lang.ANVIL_PRICE_DEFAULT_TEXT.toString());
        anvil.title(Lang.ANVIL_PRICE_TITLE.toString());

        ItemStack paper = new ItemStack(Material.PAPER);
        ItemMeta paperMeta = paper.getItemMeta();
        paperMeta.setLore(Collections.singletonList(Lang.ANVIL_CONFIRM_LORE.toString()));
        paper.setItemMeta(paperMeta);
        setCustomModelData(paper, (Integer) DereCounter.getInstance().getConfig().get("custom-model-data.anvil-item-model-data"));

        anvil.itemLeft(paper);

        anvil.plugin(DereCounter.getInstance());
        anvil.onClick((slot, stateSnapshot) -> {
            Player player = stateSnapshot.getPlayer();
            if(!player.hasPermission(getUsePermission(borsaName)) && !player.hasPermission(ADMIN_PERMISSION)){
                return Arrays.asList(AnvilGUI.ResponseAction.close(), AnvilGUI.ResponseAction.run(() -> {
                    player.sendMessage(Lang.PREFIX+
                            Lang.NO_PERMS.toString().replace("%permission%", getUsePermission(borsaName)));
                }));
            }

            if (slot == 2) {
                String text = stateSnapshot.getText();
                if (isNumeric(text)) {
                    double amount = Double.parseDouble(text);
                    if (amount > 0 && econ.getBalance(player) >= amount) {
                        return Arrays.asList(
                                AnvilGUI.ResponseAction.close(),
                                AnvilGUI.ResponseAction.run(() -> new AnvilSetMerch(borsaName, seller, buyer, amount))
                        );
                    } else {
                        return Arrays.asList(AnvilGUI.ResponseAction.replaceInputText(Lang.ANVIL_PRICE_INSUFFICIENT_FUNDS.toString()));
                    }
                } else {
                    return Arrays.asList(AnvilGUI.ResponseAction.replaceInputText(Lang.ANVIL_PRICE_INVALID_VALUE.toString()));
                }
            }
            return Collections.emptyList();
        });
        anvil.open(seller);
    }
}
