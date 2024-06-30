package org.derewah.derecounter.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.derewah.derecounter.DereCounter;
import org.derewah.derecounter.objects.CounterSet;
import org.derewah.derecounter.utils.Lang;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public class CmdCounter implements CommandExecutor, TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Player p = (Player) commandSender;
        if(p.hasPermission("derecounter.admin")) {
            if (args[0].equals("reload")) {
                DereCounter.getInstance().loadLang();
                p.sendMessage(Lang.PREFIX + Lang.PLUGIN_RELOADED.toString());
                return true;
            } else if (args[0].equals("set")) {
                if (args.length == 2) {
                    CounterSet cassaSet = new CounterSet(args[1]);
                    p.getInventory().addItem(cassaSet.getItem());
                    p.sendMessage(Lang.PREFIX.toString() + Lang.RECEIVED_SETCOUNTER.toString());
                    return true;
                } else {
                    p.sendMessage(Lang.PREFIX.toString() + Lang.INVALID_COMMAND_ARG.toString());
                    return true;
                }
            } else {
                p.sendMessage(Lang.PREFIX + Lang.CORRECT_USAGE.toString());
                return true;
            }
        }else{
            p.sendMessage(Lang.NO_PERMS.toString().replace("%permission%", "derecounter.admin"));
            return true;
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] args) {
        Player p = (Player) commandSender;
        if(args.length == 1 && p.hasPermission("derecounter.admin")){
            return Arrays.asList("reload", "set");
        }
        return Arrays.asList();
    }
}
