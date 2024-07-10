package org.derewah.derecounter.inventories;

import net.milkbowl.vault.economy.Economy;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.derewah.derecounter.DereCounter;
import org.derewah.derecounter.database.Database;
import org.derewah.derecounter.objects.ActionType;
import org.derewah.derecounter.objects.CompanyBook;
import org.derewah.derecounter.objects.RegistryAction;
import org.derewah.derecounter.utils.Lang;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;

import static org.derewah.derecounter.utils.Helpers.setCustomModelData;
import static org.derewah.derecounter.utils.Permissions.ADMIN_PERMISSION;
import static org.derewah.derecounter.utils.Permissions.getSellPermission;

public class AnvilSetMerch {


    Economy econ = DereCounter.getEcon();

    public AnvilSetMerch(String borsaName, Player seller, OfflinePlayer buyer, double amount) {
        AnvilGUI.Builder anvil = new AnvilGUI.Builder();
        anvil.title(Lang.ANVIL_MERCH_TITLE.toString());
        anvil.text(Lang.ANVIL_MERCH_DEFAULT_TEXT.toString());

        ItemStack paper = new ItemStack(Material.PAPER);
        ItemMeta paperMeta = paper.getItemMeta();
        paperMeta.setLore(Collections.singletonList(Lang.ANVIL_CONFIRM_LORE.toString()));
        paper.setItemMeta(paperMeta);
        setCustomModelData(paper, (Integer) DereCounter.getInstance().getConfig().get("custom-model-data.anvil-item-model-data"));

        anvil.itemLeft(paper);

        anvil.plugin(DereCounter.getInstance());
        anvil.onClick((slot, stateSnapshot) -> {
            Player player = stateSnapshot.getPlayer();
            if(!player.hasPermission(getSellPermission(borsaName)) && !player.hasPermission(ADMIN_PERMISSION)){
                return Arrays.asList(AnvilGUI.ResponseAction.close(), AnvilGUI.ResponseAction.run(() -> {
                    player.sendMessage(Lang.PREFIX+
                            Lang.NO_PERMS.toString().replace("%permission%", getSellPermission(borsaName)));
                }));
            }
            if (slot == 2) {
                String text = stateSnapshot.getText();
                if (econ.getBalance(player) >= amount) {
                    econ.withdrawPlayer(player, amount);
                    return Arrays.asList(
                            AnvilGUI.ResponseAction.close(),
                            AnvilGUI.ResponseAction.run(() -> {

                                Database db = DereCounter.getInstance().getDatabase();

                                RegistryAction action = new RegistryAction(ActionType.SALE, seller, buyer, amount, text);
								try {
                                    CompanyBook companyBook = db.getCompanyBook(borsaName);
									companyBook.addAction(action);
								} catch (SQLException e) {
									throw new RuntimeException(e);
								}

								seller.getInventory().addItem(action.getReceipt(borsaName));
                                seller.sendMessage(Lang.PREFIX + Lang.ANVIL_MERCH_SUCCESS_MESSAGE.toString());
                            })
                    );
                } else {
                    return Arrays.asList(
                            AnvilGUI.ResponseAction.replaceInputText(Lang.ANVIL_PRICE_INSUFFICIENT_FUNDS.toString()));
                }
            }
            return Collections.emptyList();
        });
        anvil.open(seller);
    }
}
