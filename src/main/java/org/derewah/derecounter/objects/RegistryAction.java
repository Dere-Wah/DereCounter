package org.derewah.derecounter.objects;

import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.derewah.derecounter.DereCounter;
import org.derewah.derecounter.utils.Lang;

import java.io.Serializable;
import java.time.Instant;
import java.util.Arrays;
import java.util.Date;

import static org.derewah.derecounter.utils.Helpers.setCustomModelData;

public class RegistryAction implements Serializable {

    @Getter
    private ActionType type;

    @Getter
    private OfflinePlayer seller;

    @Getter
    private OfflinePlayer buyer;

    @Getter
    private double amount;

    @Getter
    private String description;

    @Getter
    private Date time;


    public RegistryAction(ActionType type, OfflinePlayer seller, OfflinePlayer buyer, double amount, String merch){
        this.type = type;
        this.seller = seller;
        this.buyer = buyer;
        this.amount = amount;
        this.description = merch;
        this.time = Date.from(Instant.now());
    }

    public RegistryAction(ActionType type, OfflinePlayer seller, double amount, String reason){
        this.type = type;
        this.seller = seller;
        this.amount = amount;
        this.description = reason;
        this.time = Date.from(Instant.now());
    }


    public ItemStack getDescriptorItem(){
        ItemStack item = null;
        switch (type) {
            case SALE:
                item = new ItemStack(Material.BOOK);
                ItemMeta bookMeta = item.getItemMeta();
                bookMeta.setDisplayName(Lang.ACTION_SALE_NAME.toString());
                bookMeta.setLore(Arrays.asList(
                        Lang.ACTION_SALE_SELLER.toString().replace("%field%", this.getSeller().getName()),
                        Lang.ACTION_SALE_BUYER.toString().replace("%field%", this.getBuyer().getName()),
                        Lang.ACTION_SALE_DATE.toString().replace("%field%", this.getTime().toString()),
                        Lang.ACTION_SALE_PRICE.toString().replace("%field%", ""+this.getAmount()),
                        Lang.ACTION_SALE_DESCRIPTION.toString().replace("%field%", this.getDescription())
                ));
                item.setItemMeta(bookMeta);
                setCustomModelData(item, (Integer) DereCounter.getInstance().getConfig().get("register-menu-sale-model-data"));
                break;
            case WITHDRAW: {
                item = new ItemStack(Material.REDSTONE);
                ItemMeta itemMeta = item.getItemMeta();
                itemMeta.setDisplayName(Lang.ACTION_WITHDRAW_NAME.toString());

                itemMeta.setLore(Arrays.asList(
                        Lang.ACTION_WITHDRAW_SELLER.toString().replace("%field%", this.getSeller().getName()),
                        Lang.ACTION_WITHDRAW_DATE.toString().replace("%field%", this.getTime().toString()),
                        Lang.ACTION_WITHDRAW_AMOUNT.toString().replace("%field%", "" + this.getAmount()),
                        Lang.ACTION_WITHDRAW_DESCRIPTION.toString().replace("%field%", this.getDescription())
                ));
                item.setItemMeta(itemMeta);
                setCustomModelData(item, (Integer) DereCounter.getInstance().getConfig().get("register-menu-withdraw-model-data"));
                break;
            }
            case DEPOSIT: {
                item = new ItemStack(Material.EMERALD);
                ItemMeta itemMeta = item.getItemMeta();
                itemMeta.setDisplayName(Lang.ACTION_DEPOSIT_NAME.toString());
                itemMeta.setLore(Arrays.asList(
                        Lang.ACTION_DEPOSIT_SELLER.toString().replace("%field%", this.getSeller().getName()),
                        Lang.ACTION_DEPOSIT_DATE.toString().replace("%field%", this.getTime().toString()),
                        Lang.ACTION_DEPOSIT_AMOUNT.toString().replace("%field%", "" + this.getAmount()),
                        Lang.ACTION_DEPOSIT_DESCRIPTION.toString().replace("%field%", this.getDescription())
                ));
                item.setItemMeta(itemMeta);
                setCustomModelData(item, (Integer) DereCounter.getInstance().getConfig().get("register-menu-deposit-model-data"));
                break;
            }
        }
        return item;
    }

    public ItemStack      getReceipt(String company){
        if (type == ActionType.SALE){
            ItemStack item = new ItemStack(Material.PAPER);
            ItemMeta bookMeta = item.getItemMeta();
            bookMeta.setDisplayName(Lang.RECEIPT_ITEM_NAME.toString().replace("%company%", company));
            bookMeta.setLore(Arrays.asList(
                    Lang.ACTION_SALE_SELLER.toString().replace("%field%", this.getSeller().getName()),
                    Lang.ACTION_SALE_BUYER.toString().replace("%field%", this.getBuyer().getName()),
                    Lang.ACTION_SALE_DATE.toString().replace("%field%", this.getTime().toString()),
                    Lang.ACTION_SALE_PRICE.toString().replace("%field%", ""+this.getAmount()),
                    Lang.ACTION_SALE_DESCRIPTION.toString().replace("%field%", this.getDescription())
            ));
            item.setItemMeta(bookMeta);
            setCustomModelData(item, (Integer) DereCounter.getInstance().getConfig().get("receipt-model-data"));
            return item;
        }
        return null;
    }



}
