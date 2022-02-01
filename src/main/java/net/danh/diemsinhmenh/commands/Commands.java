package net.danh.diemsinhmenh.commands;

import net.danh.diemsinhmenh.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;


public class Commands implements CommandExecutor {


    private Main main;


    public Commands(Main main) {
        this.main = main;
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (label.equalsIgnoreCase("souls")) {
            if (args.length == 0) {
                if (!sender.hasPermission("souls.admin")) {
                    sender.sendMessage(main.convert(main.getConfig().getString("prefix") + main.getlang().getString("lang." + main.getConfig().getString("language") + "." + "Perm")));
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

            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("reload")) {
                    if (!sender.hasPermission("souls.admin")) {
                        sender.sendMessage(main.convert(main.getConfig().getString("prefix") + main.getlang().getString("lang." + main.getConfig().getString("language") + "." + "Perm")));
                    }

                    if (sender.hasPermission("souls.admin")) {
                        main.reloadConfigs();
                        sender.sendMessage(main.convert(main.getConfig().getString("prefix") + main.getlang().getString("lang." + main.getConfig().getString("language") + "." + "Reload")));
                    }
                }

                if (args[0].equalsIgnoreCase("updatefile")) {

                    if (!sender.hasPermission("souls.admin")) {
                        return true;
                    }
                    if (sender.hasPermission("souls.admin")) {
                        if (main.getConfig().getDouble("config-version") == 11 && main.getlang().getDouble("lang-version") == 4) {
                            main.getConfig().set("config-version", 1.1);
                            main.getlang().set("lang-version", 0.4);
                            main.saveconfigs();
                            main.reloadConfigs();
                            sender.sendMessage(ChatColor.GREEN + "Updated!");
                        } else {

                            sender.sendMessage(ChatColor.YELLOW + "Your file is too old or has been updated and upgraded");
                        }
                    }
                }
            }


            if (args.length == 2) {
                if (args[0].equalsIgnoreCase("check")) {
                    if (Bukkit.getPlayer(args[1]) == null) {
                        sender.sendMessage(main.convert(main.getConfig().getString("prefix") + main.getlang().getString("lang." + main.getConfig().getString("language") + "." + "Not-online")));
                        return true;
                    }

                    sender.sendMessage(main.convert(main.getConfig().getString("prefix") + main.getlang().getString("lang." + main.getConfig().getString("language") + "." + "Check-message")).replaceAll("%souls%", String.valueOf(main.getLives(Bukkit.getPlayer(args[1])))).replaceAll("%player%", Bukkit.getPlayer(args[1]).getName()));
                }
            }

            if (args.length == 3) {
                if (args[0].equalsIgnoreCase("add")) {

                    if (!sender.hasPermission("souls.admin")) {
                        sender.sendMessage(main.convert(main.getConfig().getString("prefix") + main.getlang().getString("lang." + main.getConfig().getString("language") + "." + "Perm")));
                    }
                    if (sender.hasPermission("souls.admin")) {
                        if (Bukkit.getPlayer(args[1]) == null) {
                            sender.sendMessage(main.convert(main.getConfig().getString("prefix") + main.getlang().getString("lang." + main.getConfig().getString("language") + "." + "Not-online")));
                            return true;
                        }
                        if (Bukkit.getPlayer(args[1]) != null) {
                            main.addLives(Bukkit.getPlayer(args[1]), Integer.parseInt(args[2]));
                            sender.sendMessage(main.convert(main.getConfig().getString("prefix") + main.getlang().getString("lang." + main.getConfig().getString("language") + "." + "Add-message")).replaceAll("%souls%", String.valueOf(Integer.parseInt(args[2]))).replaceAll("%player%", Bukkit.getPlayer(args[1]).getName()));
                        }
                    }
                }

                if (args[0].equalsIgnoreCase("remove")) {
                    if (!sender.hasPermission("souls.admin")) {
                        sender.sendMessage(main.convert(main.getConfig().getString("prefix") + main.getlang().getString("lang." + main.getConfig().getString("language") + "." + "Perm")));
                    }
                    if (sender.hasPermission("souls.admin")) {
                        if (Bukkit.getPlayer(args[1]) == null) {
                            sender.sendMessage(main.convert(main.getConfig().getString("prefix") + main.getlang().getString("lang." + main.getConfig().getString("language") + "." + "Not-online")));
                            return true;
                        }
                        if (Bukkit.getPlayer(args[1]) != null) {
                            if (main.getLives(Bukkit.getPlayer(args[1])) < Integer.parseInt(args[2])) {
                                sender.sendMessage(main.convert(main.getConfig().getString("prefix") + main.getlang().getString("lang." + main.getConfig().getString("language") + "." + "Enough")));
                                return true;
                            }
                            if (main.getLives(Bukkit.getPlayer(args[1])) >= Integer.parseInt(args[2])) {
                                main.removeLives(Bukkit.getPlayer(args[1]), Integer.parseInt(args[2]));
                                sender.sendMessage(main.convert(main.getConfig().getString("prefix") + main.getlang().getString("lang." + main.getConfig().getString("language") + "." + "Take-message")).replaceAll("%souls%", String.valueOf(Integer.parseInt(args[2]))).replaceAll("%player%", Bukkit.getPlayer(args[1]).getName()));
                            }
                        }
                    }
                }
                if (args[0].equalsIgnoreCase("set")) {
                    if (!sender.hasPermission("souls.admin")) {
                        sender.sendMessage(main.convert(main.getConfig().getString("prefix") + main.getlang().getString("lang." + main.getConfig().getString("language") + "." + "Perm")));
                    }
                    if (sender.hasPermission("souls.admin")) {
                        if (Bukkit.getPlayer(args[1]) == null) {
                            sender.sendMessage(main.convert(main.getConfig().getString("prefix") + main.getlang().getString("lang." + main.getConfig().getString("language") + "." + "Not-online")));
                            return true;
                        }
                        if (Bukkit.getPlayer(args[1]) != null) {
                            main.setLives(Bukkit.getPlayer(args[1]), Integer.parseInt(args[2]));
                            sender.sendMessage(main.convert(main.getConfig().getString("prefix") + main.getlang().getString("lang." + main.getConfig().getString("language") + "." + "Set-message")).replaceAll("%souls%", String.valueOf(Integer.parseInt(args[2]))).replaceAll("%player%", Bukkit.getPlayer(args[1]).getName()));
                        }
                    }
                }
                if (args[0].equalsIgnoreCase("reset")) {
                    if (!sender.hasPermission("souls.admin")) {
                        sender.sendMessage(main.convert(main.getConfig().getString("prefix") + main.getlang().getString("lang." + main.getConfig().getString("language") + "." + "Perm")));
                    }
                    if (sender.hasPermission("souls.admin")) {
                        if (Bukkit.getPlayer(args[1]) == null) {
                            sender.sendMessage(main.convert(main.getConfig().getString("prefix") + main.getlang().getString("lang." + main.getConfig().getString("language") + "." + "Not-online")));
                            return true;
                        }

                        if (Bukkit.getPlayer(args[1]) != null) {
                            main.resetLives(Bukkit.getPlayer(args[1]));
                            sender.sendMessage(main.convert(main.getConfig().getString("prefix") + main.getlang().getString("lang." + main.getConfig().getString("language") + "." + "Reset-message")).replaceAll("%souls%", String.valueOf(main.getConfig().getInt("General.Normal"))).replaceAll("%player%", Bukkit.getPlayer(args[1]).getName()));
                        }
                    }
                }
            }
        }
        return true;
    }
}
