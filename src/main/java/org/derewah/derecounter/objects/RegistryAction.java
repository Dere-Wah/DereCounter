package org.derewah.derecounter.objects;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.derewah.derecounter.DereCounter;
import org.derewah.derecounter.utils.Lang;

import java.io.Serializable;
import java.time.Instant;
import java.util.Arrays;
import java.util.Date;
import java.util.UUID;

import static org.bukkit.Bukkit.getPlayer;
import static org.bukkit.Bukkit.getServer;
import static org.derewah.derecounter.utils.Helpers.setCustomModelData;

@DatabaseTable(tableName = "registry_actions")
public class RegistryAction{

    @Getter
    @DatabaseField(generatedId = true)
    private Integer id;

    @Getter
    @DatabaseField(canBeNull = false, foreign = true)
    CompanyBook companyBook;

    @Getter @Setter
    @DatabaseField(canBeNull = false)
    private ActionType type;

    @Getter @Setter
    @DatabaseField(canBeNull = false)
    private UUID seller;

    @Getter @Setter
    @DatabaseField(canBeNull = true)
    private UUID buyer;

    @Getter @Setter
    @DatabaseField(canBeNull = false)
    private double amount;

    @Getter @Setter
    @DatabaseField(canBeNull = false)
    private String description;

    @Getter @Setter
    @DatabaseField(canBeNull = false)
    private Date time;

    public RegistryAction(ActionType type, OfflinePlayer seller, OfflinePlayer buyer, double amount, String merch){
        this.type = type;
        this.seller = seller.getUniqueId();
        this.buyer = buyer.getUniqueId();
        this.amount = amount;
        this.description = merch;
        this.time = Date.from(Instant.now());
    }

    public RegistryAction(ActionType type, OfflinePlayer seller, double amount, String reason){
        this.type = type;
        this.seller = seller.getUniqueId();
        this.amount = amount;
        this.description = reason;
        this.time = Date.from(Instant.now());
    }

    public RegistryAction(){

    }


    public ItemStack getDescriptorItem(){
        ItemStack item = null;

        Player playerSeller = getServer().getPlayer(getSeller());

        switch (type) {
            case SALE:
                Player playerBuyer = getServer().getPlayer(getBuyer());
                item = new ItemStack(Material.BOOK);
                ItemMeta bookMeta = item.getItemMeta();
                bookMeta.setDisplayName(Lang.ACTION_SALE_NAME.toString());
                bookMeta.setLore(Arrays.asList(
                        Lang.ACTION_SALE_SELLER.toString().replace("%field%", playerSeller.getName()),
                        Lang.ACTION_SALE_BUYER.toString().replace("%field%", playerBuyer.getName()),
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
                        Lang.ACTION_WITHDRAW_SELLER.toString().replace("%field%", playerSeller.getName()),
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
                        Lang.ACTION_DEPOSIT_SELLER.toString().replace("%field%", playerSeller.getName()),
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

    public ItemStack getReceipt(String company){
        Player seller = getServer().getPlayer(getSeller());
        Player buyer = getServer().getPlayer(getBuyer());
        if (type == ActionType.SALE){
            ItemStack item = new ItemStack(Material.PAPER);
            ItemMeta bookMeta = item.getItemMeta();
            bookMeta.setDisplayName(Lang.RECEIPT_ITEM_NAME.toString().replace("%company%", company));
            bookMeta.setLore(Arrays.asList(
                    Lang.ACTION_SALE_SELLER.toString().replace("%field%", seller.getName()),
                    Lang.ACTION_SALE_BUYER.toString().replace("%field%", buyer.getName()),
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
