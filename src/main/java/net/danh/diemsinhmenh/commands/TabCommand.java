package net.danh.diemsinhmenh.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

public class TabCommand implements TabCompleter {


    List<String> arg = new ArrayList<String>();

    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (arg.isEmpty()) {
            arg.add("check");
            if (sender.hasPermission("souls.admin")) {
                arg.add("set");
                arg.add("add");
                arg.add("remove");
                arg.add("reset");
                arg.add("reload");
                arg.add("updatefile");
            }
        }
        List<String> result = new ArrayList<String>();
        if (args.length == 1) {
            for (String a : arg) {
                if (a.toLowerCase().startsWith(args[0].toLowerCase()))
                    result.add(a);
            }
            return result;
        }
        return null;
    }
}