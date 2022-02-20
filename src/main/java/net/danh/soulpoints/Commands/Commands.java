package net.danh.soulpoints.Commands;

import net.danh.soulpoints.Manager.Files;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;


public class Commands implements CommandExecutor {


    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (label.equalsIgnoreCase("souls")) {
            if (args.length == 0) {
                if (!sender.hasPermission("souls.admin")) {
                    sender.sendMessage(Files.convert(Files.getConfig().getString("prefix") + Files.getlang().getString("lang." + Files.getConfig().getString("language") + "." + "Perm")));
                    sender.sendMessage("");
                    for (String helpplayer : Files.getlang().getStringList("Help-player"))
                        sender.sendMessage(Files.convert(helpplayer));
                }
                if (sender.hasPermission("souls.admin")) {
                    for (String helpadmin : Files.getlang().getStringList("Help-admin")) {
                        sender.sendMessage(Files.convert(helpadmin));

                    }
                }
            }

            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("reload")) {
                    if (!sender.hasPermission("souls.admin")) {
                        sender.sendMessage(Files.convert(Files.getConfig().getString("prefix") + Files.getlang().getString("lang." + Files.getConfig().getString("language") + "." + "Perm")));
                    }

                    if (sender.hasPermission("souls.admin")) {
                        Files.reloadConfigs();
                        sender.sendMessage(Files.convert(Files.getConfig().getString("prefix") + Files.getlang().getString("lang." + Files.getConfig().getString("language") + "." + "Reload")));
                    }
                }

                if (args[0].equalsIgnoreCase("updatefile")) {

                    if (!sender.hasPermission("souls.admin")) {
                        return true;
                    }
                    if (sender.hasPermission("souls.admin")) {
                        if (Files.getConfig().getDouble("config-version") == 11 && Files.getlang().getDouble("lang-version") == 4) {
                            Files.getConfig().set("config-version", 1.1);
                            Files.getlang().set("lang-version", 0.4);
                            Files.saveconfigs();
                            Files.reloadConfigs();
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
                        sender.sendMessage(Files.convert(Files.getConfig().getString("prefix") + Files.getlang().getString("lang." + Files.getConfig().getString("language") + "." + "Not-online")));
                        return true;
                    }

                    sender.sendMessage(Files.convert(Files.getConfig().getString("prefix") + Files.getlang().getString("lang." + Files.getConfig().getString("language") + "." + "Check-message")).replaceAll("%souls%", String.valueOf(Files.getLives(Bukkit.getPlayer(args[1])))).replaceAll("%player%", Bukkit.getPlayer(args[1]).getName()));
                }
            }

            if (args.length == 3) {
                if (args[0].equalsIgnoreCase("add")) {

                    if (!sender.hasPermission("souls.admin")) {
                        sender.sendMessage(Files.convert(Files.getConfig().getString("prefix") + Files.getlang().getString("lang." + Files.getConfig().getString("language") + "." + "Perm")));
                    }
                    if (sender.hasPermission("souls.admin")) {
                        if (Bukkit.getPlayer(args[1]) == null) {
                            sender.sendMessage(Files.convert(Files.getConfig().getString("prefix") + Files.getlang().getString("lang." + Files.getConfig().getString("language") + "." + "Not-online")));
                            return true;
                        }
                        if (Bukkit.getPlayer(args[1]) != null) {
                            Files.addLives(Bukkit.getPlayer(args[1]), Integer.parseInt(args[2]));
                            sender.sendMessage(Files.convert(Files.getConfig().getString("prefix") + Files.getlang().getString("lang." + Files.getConfig().getString("language") + "." + "Add-message")).replaceAll("%souls%", String.valueOf(Integer.parseInt(args[2]))).replaceAll("%player%", Bukkit.getPlayer(args[1]).getName()));
                        }
                    }
                }

                if (args[0].equalsIgnoreCase("remove")) {
                    if (!sender.hasPermission("souls.admin")) {
                        sender.sendMessage(Files.convert(Files.getConfig().getString("prefix") + Files.getlang().getString("lang." + Files.getConfig().getString("language") + "." + "Perm")));
                    }
                    if (sender.hasPermission("souls.admin")) {
                        if (Bukkit.getPlayer(args[1]) == null) {
                            sender.sendMessage(Files.convert(Files.getConfig().getString("prefix") + Files.getlang().getString("lang." + Files.getConfig().getString("language") + "." + "Not-online")));
                            return true;
                        }
                        if (Bukkit.getPlayer(args[1]) != null) {
                            if (Files.getLives(Bukkit.getPlayer(args[1])) < Integer.parseInt(args[2])) {
                                sender.sendMessage(Files.convert(Files.getConfig().getString("prefix") + Files.getlang().getString("lang." + Files.getConfig().getString("language") + "." + "Enough")));
                                return true;
                            }
                            if (Files.getLives(Bukkit.getPlayer(args[1])) >= Integer.parseInt(args[2])) {
                                Files.removeLives(Bukkit.getPlayer(args[1]), Integer.parseInt(args[2]));
                                sender.sendMessage(Files.convert(Files.getConfig().getString("prefix") + Files.getlang().getString("lang." + Files.getConfig().getString("language") + "." + "Take-message")).replaceAll("%souls%", String.valueOf(Integer.parseInt(args[2]))).replaceAll("%player%", Bukkit.getPlayer(args[1]).getName()));
                            }
                        }
                    }
                }
                if (args[0].equalsIgnoreCase("set")) {
                    if (!sender.hasPermission("souls.admin")) {
                        sender.sendMessage(Files.convert(Files.getConfig().getString("prefix") + Files.getlang().getString("lang." + Files.getConfig().getString("language") + "." + "Perm")));
                    }
                    if (sender.hasPermission("souls.admin")) {
                        if (Bukkit.getPlayer(args[1]) == null) {
                            sender.sendMessage(Files.convert(Files.getConfig().getString("prefix") + Files.getlang().getString("lang." + Files.getConfig().getString("language") + "." + "Not-online")));
                            return true;
                        }
                        if (Bukkit.getPlayer(args[1]) != null) {
                            Files.setLives(Bukkit.getPlayer(args[1]), Integer.parseInt(args[2]));
                            sender.sendMessage(Files.convert(Files.getConfig().getString("prefix") + Files.getlang().getString("lang." + Files.getConfig().getString("language") + "." + "Set-message")).replaceAll("%souls%", String.valueOf(Integer.parseInt(args[2]))).replaceAll("%player%", Bukkit.getPlayer(args[1]).getName()));
                        }
                    }
                }
                if (args[0].equalsIgnoreCase("reset")) {
                    if (!sender.hasPermission("souls.admin")) {
                        sender.sendMessage(Files.convert(Files.getConfig().getString("prefix") + Files.getlang().getString("lang." + Files.getConfig().getString("language") + "." + "Perm")));
                    }
                    if (sender.hasPermission("souls.admin")) {
                        if (Bukkit.getPlayer(args[1]) == null) {
                            sender.sendMessage(Files.convert(Files.getConfig().getString("prefix") + Files.getlang().getString("lang." + Files.getConfig().getString("language") + "." + "Not-online")));
                            return true;
                        }

                        if (Bukkit.getPlayer(args[1]) != null) {
                            Files.resetLives(Bukkit.getPlayer(args[1]));
                            sender.sendMessage(Files.convert(Files.getConfig().getString("prefix") + Files.getlang().getString("lang." + Files.getConfig().getString("language") + "." + "Reset-message")).replaceAll("%souls%", String.valueOf(Files.getConfig().getInt("General.Normal"))).replaceAll("%player%", Bukkit.getPlayer(args[1]).getName()));
                        }
                    }
                }
            }
        }
        return true;
    }
}
