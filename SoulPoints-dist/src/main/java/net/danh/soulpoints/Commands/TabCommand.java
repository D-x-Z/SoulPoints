package net.danh.soulpoints.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class TabCommand implements TabCompleter {


    List<String> arg = new ArrayList<>();

    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (arg.isEmpty()) {
            arg.add("check");
            if (sender.hasPermission("souls.admin")) {
                arg.add("set");
                arg.add("add");
                arg.add("remove");
                arg.add("reset");
                arg.add("reload");
                arg.add("update");
            }
        }
        List<String> result = new ArrayList<>();
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