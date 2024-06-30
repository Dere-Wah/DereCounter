package org.derewah.derecounter;

import lombok.Getter;
import net.milkbowl.vault.economy.Economy;
import org.bstats.bukkit.Metrics;
import org.bstats.charts.SimplePie;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.derewah.derecounter.commands.CmdCounter;
import org.derewah.derecounter.listeners.CounterClick;
import org.derewah.derecounter.listeners.ClientMenuClick;
import org.derewah.derecounter.listeners.MainMenuClick;
import org.derewah.derecounter.listeners.RegisterMenuClick;
import org.derewah.derecounter.utils.Lang;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Logger;

public class DereCounter extends JavaPlugin {

    static DereCounter instance;
    public static FileConfiguration config;

    public static YamlConfiguration LANG;
    public static File LANG_FILE;

    @Getter
    private Data data;

    @Getter
    private static Economy econ = null;

    @Override
    public void onDisable() {
        data.saveData("./plugins/DereCounter/CompanyBooks.json");
    }

    public void onEnable(){
        instance = this;

        if (!setupEconomy() ) {
            Logger.getLogger("Minecraft").severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        data = new Data();
        try {
            Files.createDirectories(Paths.get("./plugins/DereCounter"));
            if(!Files.exists(Paths.get("./plugins/DereCounter/CompanyBooks.json"))) {
                Files.createFile(Paths.get("./plugins/DereCounter/CompanyBooks.json"));
            }
            data.saveData("./plugins/DereCounter/CompanyBooks.json");
            Bukkit.getLogger().info("Folders created");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Register Metrics
        Metrics metrics = new Metrics(this, 22467);

        metrics.addCustomChart(new SimplePie("plugin_version", () ->
                this.getDescription().getVersion()));
        Bukkit.getLogger().info("[DereCounter] has been enabled!");
        loadLang();

        this.saveDefaultConfig();
        this.config =getConfig();

        saveCommands();
        saveListeners();
    }

    public void saveCommands(){
        this.getCommand("derecounter").setExecutor(new CmdCounter());
    }

    public void saveListeners(){
        getServer().getPluginManager().registerEvents(new CounterClick(), this);
        getServer().getPluginManager().registerEvents(new MainMenuClick(), this);
        getServer().getPluginManager().registerEvents(new ClientMenuClick(), this);
        getServer().getPluginManager().registerEvents(new RegisterMenuClick(), this);
    }

    public static DereCounter getInstance(){
        return instance;
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }


    public YamlConfiguration getLang() {
        return LANG;
    }

    /**
     * Get the lang.yml file.
     * @return The lang.yml file.
     */
    public File getLangFile() {
        return LANG_FILE;
    }

    public void loadLang() {
        File lang = new File(getDataFolder(), "lang.yml");
        if (!lang.exists()) {
            try {
                getDataFolder().mkdir();
                lang.createNewFile();
                InputStream defConfigStream = this.getResource("lang.yml");
                if (defConfigStream != null) {
                    InputStreamReader reader = new InputStreamReader(defConfigStream);
                    YamlConfiguration defConfig = new YamlConfiguration();
                    try{
                        defConfig.load(reader);
                        defConfig.save(lang);
                        Lang.setFile(defConfig);
                    } catch (InvalidConfigurationException e) {
                        throw new RuntimeException(e);
                    }


                    return;
                }
            } catch(IOException e) {
                e.printStackTrace(); // So they notice
                Bukkit.getLogger().severe("[DereCounter] Couldn't create language file.");
                Bukkit.getLogger().severe("[DereCounter] This is a fatal error. Now disabling");
                this.setEnabled(false); // Without it loaded, we can't send them messages
            }
        }
        YamlConfiguration conf = YamlConfiguration.loadConfiguration(lang);
        for(Lang item:Lang.values()) {
            if (conf.getString(item.getPath()) == null) {
                conf.set(item.getPath(), item.getDefault());
            }
        }
        Lang.setFile(conf);
        DereCounter.LANG = conf;
        DereCounter.LANG_FILE = lang;
        try {
            conf.save(getLangFile());
        } catch(IOException e) {
            Bukkit.getLogger().warning("derecounter: Failed to save lang.yml.");
            Bukkit.getLogger().warning("derecounter: Report this stack trace to DereWah.");
            e.printStackTrace();
        }
    }


}
