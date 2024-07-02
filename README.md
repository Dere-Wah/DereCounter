# DereCounter ![Downloads](https://img.shields.io/github/downloads/Dere-Wah/DereCounter/total)

**DereCounter** is the ultimate plugin for Minecraft roleplay servers, bringing a new level of immersion and realism! Transform your in-game shops with Company Counters that enable vendors to effortlessly track their transactions. Whether you're a server admin setting up a bustling marketplace or a vendor selling to eager players, DereCounter streamlines your sales and keeps your business thriving.

![Stats](https://bstats.org/signatures/bukkit/DereCounter.svg)

## Features

- **Company Counters**: Admins can set up customizable counters for vendors, enhancing the roleplay experience.
- **Transaction Tracking**: Vendors can track every transaction made, ensuring transparency and accountability.
- **Proximity-Based Selling**: Vendors can easily sell items to nearby players through an intuitive GUI.
- **Detailed Sales Process**: Vendors can set prices, descriptions, and amounts, and issue receipts to buyers.
- **Comprehensive Transaction History**: View the full register of all transactions for each company counter.
- **Secure Financial Management**: Vendors can deposit and withdraw money from their company counters with appropriate permissions.
- **Customizable Messages**: Modify all plugin messages and language through the Lang.yml file.
- **GUI Customization**: Change the appearance of the plugin GUI by configuring blocks and custom model data in the Config.yml file.
- **Permission-Based Access**: Granular permission settings ensure that only authorized vendors and admins can access and manage counters.
- **Seamless Integration**: Compatible with Spigot/Paper 1.13+ and integrates with Vault for economy management.

## Installation

To install DereCounter, follow these steps:

1. Go to the [DereCounter release page](https://github.com/Dere-Wah/DereCounter/releases) and download the latest version.
2. Ensure you have the following requirements:
    - **Vault**: Used for economy integration to track money and perform transactions.
    - **Permission Plugin**: Required to set up the necessary permissions.
    - **Spigot/Paper 1.13+**: The plugin is compatible with Spigot/Paper versions 1.13 and above.
3. Place the downloaded `.jar` file into your server's `plugins` directory.
4. Restart your server to generate the configuration files.

## Usage

To set up and use a Company Counter, follow these steps:

1. **Vendor Setup**:
    - Vendors must contact an admin to set up a counter for their shop.

2. **Admin Setup**:
    - An admin with the permission `derecounter.admin` executes the command:
      ```
      /derecounter set <companyname>
      ```
      The `companyname` must be alphabetic lowercase characters.
    - The admin receives a stick which they must use on a Counter block to set it up. The Counter block material is specified in the config file and the stick's lore.
    - Right-click on the Counter block with the stick to make it usable.

3. **Permissions for Vendors**:
    - Admins should grant vendors the permission `derecounter.use.%companyname%` to allow them access to the counter.

4. **Counter Options**:
    - **Sell**:
        - Opens a GUI with the heads of nearby players. Select the player to trade with.
        - An anvil GUI appears to input the price of the item.
        - Input description, amount, etc. The vendor receives a receipt to give to the buyer.
        - Requires permission: `derecounter.use.%companyname%.sell`.
        - Money is taken from the seller's account and deposited into the counter if the balance is sufficient.

    - **View Register**:
        - Opens a history of all transactions for the account `%companyname%`.
        - Shows previous transactions and current counter balance.
        - Requires permission: `derecounter.use.%companyname%`.

    - **Withdraw**:
        - Opens an anvil GUI to withdraw money from the counter.
        - Requires permission: `derecounter.use.%companyname%.withdraw`.

    - **Deposit**:
        - Opens an anvil GUI to deposit money into the counter.
        - Requires permission: `derecounter.use.%companyname%.deposit`.

## Commands and Permissions

- **Commands**:
    - `/derecounter set <companyname>`: Sets up a new company counter (admin only).

- **Permissions**:
    - `derecounter.admin`: Allows setting up company counters.
    - `derecounter.use.%companyname%`: Allows access to a specific company's counter.
    - `derecounter.use.%companyname%.sell`: Allows selling items via the counter.
    - `derecounter.use.%companyname%.withdraw`: Allows withdrawing money from the counter.
    - `derecounter.use.%companyname%.deposit`: Allows depositing money into the counter.

## Configuration

DereCounter provides two main configuration files to customize its behavior and appearance:

- **Lang.yml**: Modify this file to change all the plugin's message files and language.
- **Config.yml**: Customize the blocks and custom model data to change the appearance of the plugin GUI.

## Dependencies

- [Vault](https://www.spigotmc.org/resources/vault.34315/)
- A permission plugin (e.g., LuckPerms, PermissionsEx)
- Spigot/Paper 1.13+

