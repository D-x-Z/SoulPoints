package net.danh.diemsinhmenh.commands;

import net.danh.diemsinhmenh.Main;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;


public class commands implements CommandExecutor  {


    private Main main;


    public commands(Main main) {
        this.main = main;
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (label.equalsIgnoreCase("souls")) {
            if (args.length == 0) {
                if (!sender.hasPermission("souls.admin")) {
                    sender.sendMessage(main.convert(main.getlang().getString("lang." + main.getConfig().getString("language") + "." + "Perm")));
                    sender.sendMessage("");
                    for (String helpplayer : main.getlang().getStringList("Help-player"))
                    sender.sendMessage(main.convert(helpplayer));
                }
                if (sender.hasPermission("souls.admin")) {
                    for (String helpadmin : main.getlang().getStringList("Help-admin")) {
                        sender.sendMessage(main.convert(helpadmin));

                    }
                }
            }

            if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
                if (!sender.hasPermission("souls.admin")) {
                    sender.sendMessage(main.convert(main.getlang().getString("lang." + main.getConfig().getString("language") + "." + "Perm")));
                }

                if (sender.hasPermission("souls.admin")) {
                    main.reloadConfigs();
                    sender.sendMessage(main.convert(main.getlang().getString("lang." + main.getConfig().getString("language") + "." + "Reload")));
                }
            }


            if (args.length == 2 && args[0].equalsIgnoreCase("check")) {
                if (Bukkit.getPlayer(args[1]) == null) {
                    sender.sendMessage(main.convert(main.getlang().getString("lang." + main.getConfig().getString("language") + "." + "Not-online")));
                    return true;
                }

                sender.sendMessage(main.convert(main.getlang().getString("lang." + main.getConfig().getString("language") + "." + "Check-message")).replaceAll("%souls%", String.valueOf(main.getLives(Bukkit.getPlayer(args[1])))).replaceAll("%player%", Bukkit.getPlayer(args[1]).getName()));
            }

            if (args.length == 3) {
                if (args[0].equalsIgnoreCase("add")) {

                    if (!sender.hasPermission("souls.admin")) {
                        sender.sendMessage(main.convert(main.getlang().getString("lang." + main.getConfig().getString("language") + "." + "Perm")));
                    }

                    if (sender.hasPermission("souls.admin")) {
                        main.addLives(Bukkit.getPlayer(args[1]), Integer.parseInt(args[2]));
                        sender.sendMessage(main.convert(main.getlang().getString("lang." + main.getConfig().getString("language") + "." + "Add-message")).replaceAll("%souls%", String.valueOf(Integer.parseInt(args[2]))).replaceAll("%player%", Bukkit.getPlayer(args[1]).getName()));
                    }
                }

                if (args[0].equalsIgnoreCase("remove")) {
                    if (!sender.hasPermission("souls.admin")) {
                        sender.sendMessage(main.convert(main.getlang().getString("lang." + main.getConfig().getString("language") + "." + "Perm")));
                    }
                    if (sender.hasPermission("souls.admin")) {
                        if (Bukkit.getPlayer(args[1]) == null) {
                            sender.sendMessage(main.convert(main.getlang().getString("lang." + main.getConfig().getString("language") + "." + "Not-online")));
                            return true;
                        }
                        if (main.getLives(Bukkit.getPlayer(args[1])) < Integer.parseInt(args[2])) {
                            sender.sendMessage(main.convert(main.getlang().getString("lang." + main.getConfig().getString("language") + "." + "Enough")));
                            return true;
                        }

                        main.removeLives(Bukkit.getPlayer(args[1]), Integer.parseInt(args[2]));
                        sender.sendMessage(main.convert(main.getlang().getString("lang." + main.getConfig().getString("language") + "." + "Take-message")).replaceAll("%souls%", String.valueOf(Integer.parseInt(args[2]))).replaceAll("%player%", Bukkit.getPlayer(args[1]).getName()));
                    }
                }
                if (args[0].equalsIgnoreCase("set")) {
                    if (!sender.hasPermission("souls.admin")) {
                        sender.sendMessage(main.convert(main.getlang().getString("lang." + main.getConfig().getString("language") + "." + "Perm")));
                    }

                    if (sender.hasPermission("souls.admin")) {
                        main.setLives(Bukkit.getPlayer(args[1]), Integer.parseInt(args[2]));
                        sender.sendMessage(main.convert(main.getlang().getString("lang." + main.getConfig().getString("language") + "." + "Set-message")).replaceAll("%souls%", String.valueOf(Integer.parseInt(args[2]))).replaceAll("%player%", Bukkit.getPlayer(args[1]).getName()));
                    }
                }
                if (args[0].equalsIgnoreCase("reset")) {
                    if (!sender.hasPermission("souls.admin")) {
                        sender.sendMessage(main.convert(main.getlang().getString("lang." + main.getConfig().getString("language") + "." + "Perm")));
                    }

                    if (sender.hasPermission("souls.admin")) {
                        main.resetLives(Bukkit.getPlayer(args[1]));
                        sender.sendMessage(main.convert(main.getlang().getString("lang." + main.getConfig().getString("language") + "." + "Reset-message")).replaceAll("%souls%", String.valueOf(main.getConfig().getInt("General.Normal"))).replaceAll("%player%", Bukkit.getPlayer(args[1]).getName()));
                    }
                }
            }
        }
        return true;
    }
}
