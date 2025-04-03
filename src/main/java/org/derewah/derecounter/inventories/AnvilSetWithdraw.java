package org.derewah.derecounter.inventories;

import net.milkbowl.vault.economy.Economy;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.derewah.derecounter.DereCounter;
import org.derewah.derecounter.utils.Lang;

import java.util.Arrays;
import java.util.Collections;

import static org.derewah.derecounter.utils.Helpers.*;
import static org.derewah.derecounter.utils.Permissions.ADMIN_PERMISSION;
import static org.derewah.derecounter.utils.Permissions.getWithdrawPermission;

public class AnvilSetWithdraw {

    Economy econ = DereCounter.getEcon();

    public AnvilSetWithdraw(String borsaName, Player seller) {
        AnvilGUI.Builder anvil = new AnvilGUI.Builder();
        anvil.text(Lang.ANVIL_WITHDRAW_DEFAULT_TEXT.toString());
        anvil.title(Lang.ANVIL_WITHDRAW_TITLE.toString());

        ItemStack paper = new ItemStack(Material.PAPER);
        ItemMeta paperMeta = paper.getItemMeta();
        paperMeta.setLore(Collections.singletonList(Lang.ANVIL_CONFIRM_LORE.toString()));
        paper.setItemMeta(paperMeta);
        setCustomModelData(paper, (Integer) DereCounter.getInstance().getConfig().get("custom-model-data.anvil-item-model-data"));

        anvil.itemLeft(paper);

        anvil.plugin(DereCounter.getInstance());

        anvil.onClick((slot, stateSnapshot) -> {
            Player player = stateSnapshot.getPlayer();
            if(!player.hasPermission(getWithdrawPermission(borsaName)) && !player.hasPermission(ADMIN_PERMISSION)){
                return Arrays.asList(AnvilGUI.ResponseAction.close(), AnvilGUI.ResponseAction.run(() -> {
                    player.sendMessage(Lang.PREFIX+
                            Lang.NO_PERMS.toString().replace("%permission%", getWithdrawPermission(borsaName)));
                }));
            }

            if (slot == 2) {
                String text = stateSnapshot.getText();
                if (isNumeric(text)) {
                    double amount = Double.parseDouble(text);
                    if (amount > 0 && getCompanyBook(borsaName).getBalance() >= amount) {
                        return Arrays.asList(
                                AnvilGUI.ResponseAction.close(),
                                AnvilGUI.ResponseAction.run(() -> new AnvilSetReason(borsaName, seller, amount))
                        );
                    } else {
                        return Arrays.asList(AnvilGUI.ResponseAction.replaceInputText(Lang.ANVIL_WITHDRAW_INSUFFICIENT_FUNDS.toString()));
                    }
                } else {
                    return Arrays.asList(AnvilGUI.ResponseAction.replaceInputText(Lang.ANVIL_WITHDRAW_INVALID_VALUE.toString()));
                }
            }
            return Collections.emptyList();
        });
        anvil.open(seller);
    }
}
