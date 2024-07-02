package org.derewah.derecounter.inventories;

import net.milkbowl.vault.economy.Economy;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.Material;
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
import static org.derewah.derecounter.utils.Permissions.getWithdrawPermission;

public class AnvilSetReason {

    Economy econ = DereCounter.getEcon();

    public AnvilSetReason(String borsaName, Player seller, double amount) {
        AnvilGUI.Builder anvil = new AnvilGUI.Builder();
        anvil.text(Lang.ANVIL_REASON_DEFAULT_TEXT.toString());
        anvil.title(Lang.ANVIL_REASON_TITLE.toString());

        ItemStack paper = new ItemStack(Material.PAPER);
        ItemMeta paperMeta = paper.getItemMeta();
        paperMeta.setLore(Collections.singletonList(Lang.ANVIL_CONFIRM_LORE.toString()));
        paper.setItemMeta(paperMeta);
        setCustomModelData(paper, (Integer) DereCounter.getInstance().getConfig().get("custom-model-data.anvil-item-model-data"));

        anvil.itemLeft(paper);

        anvil.plugin(DereCounter.getInstance());

        anvil.onClick((slot, stateSnapshot) -> {
            Player player = stateSnapshot.getPlayer();
			if(!player.hasPermission(getWithdrawPermission(borsaName)) || !player.hasPermission(ADMIN_PERMISSION)){
				return Arrays.asList(AnvilGUI.ResponseAction.close(), AnvilGUI.ResponseAction.run(() -> {
					player.sendMessage(Lang.PREFIX+
							Lang.NO_PERMS.toString().replace("%permission%", getWithdrawPermission(borsaName)));
				}));
			}

            if (slot == 2) {
                String text = stateSnapshot.getText();
                Database db = DereCounter.getInstance().getDatabase();
                CompanyBook companyBook = null;
				try {
                    companyBook = db.getCompanyBook(borsaName);
				} catch (SQLException e) {
					throw new RuntimeException(e);
				}

				if (companyBook != null && companyBook.getBalance() >= amount) {
                    return Arrays.asList(
                            AnvilGUI.ResponseAction.close(),
                            AnvilGUI.ResponseAction.run(() -> {

                                RegistryAction action = new RegistryAction(ActionType.WITHDRAW, seller, amount, text);
								try {
									CompanyBook companyBook2 = db.getCompanyBook(borsaName);
									companyBook2.addAction(action);
								} catch (SQLException e) {
									throw new RuntimeException(e);
								}

								econ.depositPlayer(seller, amount);
                                seller.sendMessage(Lang.PREFIX + Lang.ANVIL_REASON_SUCCESS_MESSAGE.toString().replace("%amount%", String.valueOf(amount)));
                            })
                    );
                } else {
                    return Arrays.asList(AnvilGUI.ResponseAction.replaceInputText(Lang.ANVIL_REASON_INSUFFICIENT_FUNDS.toString()));
                }
            }
            return Collections.emptyList();
        });
        anvil.open(seller);
    }
}
