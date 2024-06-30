package org.derewah.derecounter.utils;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;

/**
 * An enum for requesting strings from the language file.
 * @author gomeow
 */
public enum Lang {

	PREFIX("plugin-prefix", "&7[&aDere&cBorsa&7]&r"),
	RECEIVED_SETCOUNTER("received-setcounter", "&aYou received an item to create a counter!"),
	SETCOUNTER_ITEM_NAME("setcounter-item-name", "&7[&6COUNTER &e%company%&7]"),
	SETCOUNTER_ITEM_LORE("setcounter-item-lore", "&f&oR-Click&a on an OBSERVER to place%nl%&aa counter for the company &e%company%&a!"),
	PLACED_COUNTER("placed-setcounter", "&aThe counter for the company &2%company%&a has been placed!"),
	REMOVED_COUNTER("removed-setcounter", "&cYou have removed the counter of the company &4%company%&c."),
	ALREADY_COUNTER("already-counter", "&cThis block is already a counter. Break it to remove it."),
	NO_PERMS("no-permissions", "&cYou don't have the permission &4%permission%&c."),

	PLUGIN_RELOADED("plugin-reloaded", "&aThe plugin lang file has been reloaded."),

	//sale actions
	ACTION_SALE_NAME("action-sale-name", "&eSale"),
	ACTION_SALE_SELLER("action-sale-seller", "&7Seller: &6%field%"),
	ACTION_SALE_BUYER("action-sale-buyer", "&7Buyer: &6%field%"),
	ACTION_SALE_DATE("action-sale-date", "&7Date: &6%field%"),
	ACTION_SALE_PRICE("action-sale-price", "&7Sale Price: &6$%field%"),
	ACTION_SALE_DESCRIPTION("action-sale-description", "&7Description: &6%field%"),

	//withdraw actions
	ACTION_WITHDRAW_NAME("action-withdraw-name", "&4Withdraw"),
	ACTION_WITHDRAW_SELLER("action-withdraw-seller", "&7Withdrawing: &c%field%"),
	ACTION_WITHDRAW_DATE("action-withdraw-date", "&7Date: &c%field%"),
	ACTION_WITHDRAW_AMOUNT("action-withdraw-amount", "&7Amount: &c$%field%"),
	ACTION_WITHDRAW_DESCRIPTION("action-withdraw-description", "&7Reason: &c%field%"),

	//deposit actions
	ACTION_DEPOSIT_NAME("action-deposit-name", "&aDeposit"),
	ACTION_DEPOSIT_SELLER("action-deposit-seller", "&7Depositing: &2%field%"),
	ACTION_DEPOSIT_DATE("action-deposit-date", "&7Date: &2%field%"),
	ACTION_DEPOSIT_AMOUNT("action-deposit-amount", "&7Amount: &2$%field%"),
	ACTION_DEPOSIT_DESCRIPTION("action-deposit-description", "&7Reason: &2%field%"),

	//anvil deposit text
	ANVIL_DEPOSIT_DEFAULT_TEXT("anvil-deposit-default-text", "Total"),
	ANVIL_DEPOSIT_TITLE("anvil-deposit-title", "&0Enter Deposit"),
	ANVIL_DEPOSIT_SUCCESS_MESSAGE("anvil-deposit-success-message", "&aYou have deposited &2$&a%amount%."),
	ANVIL_DEPOSIT_INSUFFICIENT_FUNDS("anvil-deposit-insufficient-funds", "$$ Insufficient funds"),
	ANVIL_DEPOSIT_INVALID_VALUE("anvil-deposit-invalid-value", "Invalid value."),

	ANVIL_CONFIRM_LORE("anvil-confirm-lore", "&aCONFIRM"),

	//anvil set merch text
	ANVIL_MERCH_TITLE("anvil-merch-title", "&0Enter sold items"),
	ANVIL_MERCH_DEFAULT_TEXT("anvil-merch-text", "Items"),
	ANVIL_MERCH_SUCCESS_MESSAGE("anvil-merch-success-message", "&aYou have registered the sale."),

	//anvil set price
	ANVIL_PRICE_DEFAULT_TEXT("anvil-price-default-text", "Enter price"),
	ANVIL_PRICE_TITLE("anvil-price-title", "&0Enter Price"),
	ANVIL_PRICE_INSUFFICIENT_FUNDS("anvil-price-insufficient-funds", "$$ Insufficient funds"),
	ANVIL_PRICE_INVALID_VALUE("anvil-price-invalid-value", "Invalid value."),

	//anvil set reason
	ANVIL_REASON_DEFAULT_TEXT("anvil-reason-default-text", "Reason"),
	ANVIL_REASON_TITLE("anvil-reason-title", "&0Enter Reason"),
	ANVIL_REASON_INSUFFICIENT_FUNDS("anvil-reason-insufficient-funds", "$$ Insufficient funds"),
	ANVIL_REASON_SUCCESS_MESSAGE("anvil-reason-success-message", "&aYou have withdrawn &2$&a%amount%."),

	//anvil set withdraw
	ANVIL_WITHDRAW_DEFAULT_TEXT("anvil-withdraw-default-text", "Total"),
	ANVIL_WITHDRAW_TITLE("anvil-withdraw-title", "&0Enter Withdrawal"),
	ANVIL_WITHDRAW_INSUFFICIENT_FUNDS("anvil-withdraw-insufficient-funds", "$$ Insufficient funds"),
	ANVIL_WITHDRAW_INVALID_VALUE("anvil-withdraw-invalid-value", "Invalid value."),

	//client menu
	CLIENT_MENU_TITLE("client-menu-title", "&3Select Client"),
	CLIENT_MENU_FRAME_NAME("client-menu-frame-name", " "),
	CLIENT_MENU_REFRESH_NAME("client-menu-refresh-name", "&6Refresh"),


	//main menu
	MAIN_MENU_TITLE("main-menu-title", "&0Account Book &3%company%"),
	MAIN_MENU_FRAME_NAME("main-menu-frame-name", " "),
	MAIN_MENU_SELL_NAME("main-menu-sell-name", "&6Sell"),
	MAIN_MENU_REGISTER_NAME("main-menu-register-name", "&9Sales Register"),
	MAIN_MENU_DEPOSIT_NAME("main-menu-deposit-name", "&2Deposit"),
	MAIN_MENU_WITHDRAW_NAME("main-menu-withdraw-name", "&4Withdraw"),
	MENU_PREVIOUS_NAME("menu-previous-name", "&cPrevious"),
	MENU_NEXT_NAME("menu-next-name", "&cNext"),

	//register menu
	REGISTER_MENU_TITLE("register-menu-title", "&0Register &3%company%"),
	REGISTER_MENU_BALANCE_FORMAT("register-menu-balance-format", "&7 [&2$%balance%&7]");





	private String path;
	private String def;
	private static YamlConfiguration LANG;

	/**
	 * Lang enum constructor.
	 * @param path The string path.
	 * @param start The default string.
	 */
	Lang(String path, String start) {
		this.path = path;
		this.def = start;
	}

	/**
	 * Set the {@code YamlConfiguration} to use.
	 * @param config The config to set.
	 */
	public static void setFile(YamlConfiguration config) {
		LANG = config;
	}

	@Override
	public String toString() {
		if (this == PREFIX)
			return ChatColor.translateAlternateColorCodes('&', LANG.getString(this.path, def)) + " ";
		return ChatColor.translateAlternateColorCodes('&', LANG.getString(this.path, def));
	}

	/**
	 * Get the default value of the path.
	 * @return The default value of the path.
	 */
	public String getDefault() {
		return this.def;
	}

	/**
	 * Get the path to the string.
	 * @return The path to the string.
	 */
	public String getPath() {
		return this.path;
	}
}