package org.derewah.derecounter.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.derewah.derecounter.DereCounter;
import org.derewah.derecounter.objects.CounterSet;
import org.derewah.derecounter.utils.Lang;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

import static org.derewah.derecounter.utils.Permissions.ADMIN_PERMISSION;

public class CmdCounter implements CommandExecutor, TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(commandSender.hasPermission(ADMIN_PERMISSION)) {
            if(args.length > 0){
                if (args[0].equals("reload")) {
                    DereCounter.getInstance().loadLang();
                    DereCounter.getInstance().reloadConfig();
                    commandSender.sendMessage(Lang.PREFIX + Lang.PLUGIN_RELOADED.toString());
                    return true;
                } else if (args[0].equals("set")) {
                    if (args.length == 2 && commandSender instanceof Player) {
                        Player p = (Player) commandSender;
                        CounterSet cassaSet = new CounterSet(args[1]);
                        p.getInventory().addItem(cassaSet.getItem());
                        p.sendMessage(Lang.PREFIX.toString() + Lang.RECEIVED_SETCOUNTER.toString());
                        return true;
                    } else {
                        commandSender.sendMessage(Lang.PREFIX.toString() + Lang.INVALID_COMMAND_ARG.toString());
                        return true;
                    }
                } else {
                    commandSender.sendMessage(Lang.PREFIX + Lang.CORRECT_USAGE.toString());
                    return true;
                }
            } else {
                commandSender.sendMessage(Lang.PREFIX + Lang.CORRECT_USAGE.toString());
                return true;
            }
        }else{
            commandSender.sendMessage(Lang.NO_PERMS.toString().replace("%permission%", ADMIN_PERMISSION));
            return true;
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] args) {
        if(commandSender instanceof Player){
            if(args.length == 1 && commandSender.hasPermission(ADMIN_PERMISSION)){
                return Arrays.asList("reload", "set");
            }
        }
        return Arrays.asList();
    }
}
