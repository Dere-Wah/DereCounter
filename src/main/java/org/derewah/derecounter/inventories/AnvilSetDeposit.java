package org.derewah.derecounter.inventories;

import net.milkbowl.vault.economy.Economy;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.derewah.derecounter.DereCounter;
import org.derewah.derecounter.objects.ActionType;
import org.derewah.derecounter.objects.RegistryAction;
import org.derewah.derecounter.utils.Lang;

import java.util.Arrays;
import java.util.Collections;

import static org.derewah.derecounter.utils.Helpers.isNumeric;
import static org.derewah.derecounter.utils.Helpers.setCustomModelData;

public class AnvilSetDeposit {

    Economy econ = DereCounter.getEcon();

    public AnvilSetDeposit(String borsaName, Player seller) {
        AnvilGUI.Builder anvil = new AnvilGUI.Builder();
        anvil.text(Lang.ANVIL_DEPOSIT_DEFAULT_TEXT.toString());
        anvil.title(Lang.ANVIL_DEPOSIT_TITLE.toString());

        ItemStack paper = new ItemStack(Material.PAPER);
        ItemMeta paperMeta = paper.getItemMeta();
        paperMeta.setLore(Collections.singletonList(Lang.ANVIL_CONFIRM_LORE.toString()));
        paper.setItemMeta(paperMeta);
        setCustomModelData(paper, (Integer) DereCounter.getInstance().getConfig().get("anvil-item-model-data"));
        anvil.itemLeft(paper);

        anvil.plugin(DereCounter.getInstance());
        anvil.onClick((slot, stateSnapshot) -> {
            if (slot == 2) {
                Player player = stateSnapshot.getPlayer();
                String text = stateSnapshot.getText();
                if (isNumeric(text)) {
                    double amount = Double.parseDouble(text);
                    if (econ.getBalance(player) >= amount) {
                        econ.withdrawPlayer(player, amount);
                        return Arrays.asList(
                                AnvilGUI.ResponseAction.close(),
                                AnvilGUI.ResponseAction.run(() -> {
                                    DereCounter.getInstance().getData().getCompanyBook(borsaName).addAction(new RegistryAction(ActionType.DEPOSIT, seller, amount, "Deposit"));
                                    seller.sendMessage(Lang.PREFIX + Lang.ANVIL_DEPOSIT_SUCCESS_MESSAGE.toString().replace("%amount%", String.valueOf(amount)));
                                })
                        );
                    } else {
                        return Arrays.asList(AnvilGUI.ResponseAction.replaceInputText(Lang.ANVIL_DEPOSIT_INSUFFICIENT_FUNDS.toString()));
                    }
                } else {
                    return Arrays.asList(AnvilGUI.ResponseAction.replaceInputText(Lang.ANVIL_DEPOSIT_INVALID_VALUE.toString()));
                }
            }
            return Collections.emptyList();
        });
        anvil.open(seller);
    }
}
