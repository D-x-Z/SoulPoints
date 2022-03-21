package net.danh.soulpoints.Commands;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.danh.soulpoints.Manager.Data;
import net.danh.soulpoints.Manager.Files;
import net.danh.soulpoints.SoulPoints;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Objects;


public class Commands implements CommandExecutor {


    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
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
                        Files.reloadfiles();
                        sender.sendMessage(Files.convert(Files.getConfig().getString("prefix") + Files.getlang().getString("lang." + Files.getConfig().getString("language") + "." + "Reload")));
                    }
                }

                if (args[0].equalsIgnoreCase("update")) {

                    if (!sender.hasPermission("souls.admin")) {
                        return true;
                    }
                    if (sender.hasPermission("souls.admin")) {
                        try {
                            String tagname;
                            String version;
                            String body;
                            String name;
                            URL api = new URL("https://api.github.com/repos/VoChiDanh/SoulPoints/releases/latest");
                            URLConnection con = api.openConnection();
                            con.setConnectTimeout(15000);
                            con.setReadTimeout(15000);

                            JsonObject json = new JsonParser().parse(new InputStreamReader(con.getInputStream())).getAsJsonObject();
                            tagname = json.get("tag_name").getAsString();
                            version = SoulPoints.getInstance().getDescription().getVersion();
                            body = json.get("body").getAsString();
                            name = json.get("name").getAsString();

                            String parsename = name.replaceAll("v", "");
                            String parsebody = body.replaceAll("## Commits", "");
                            if (!(parsename.equalsIgnoreCase(version))) {
                                URL download = new URL("https://github.com/VoChiDanh/SoulPoints/releases/download/" + tagname + "/SoulPoints.jar");
                                sender.sendMessage(Files.convert("&eDownloading " + name + "! &6Your version is &6v" + version));
                                sender.sendMessage(Files.convert("&aUpdate log: " + parsebody));
                                new BukkitRunnable() {

                                    @Override
                                    public void run() {
                                        try {
                                            InputStream in = download.openStream();
                                            File temp = new File("plugins/SoulPoints/update/");
                                            if (!temp.exists()) {
                                                temp.mkdir();
                                            }
                                            Path path = new File("plugins/SoulPoints/update/" + File.separator + "SoulPoints.jar").toPath();
                                            java.nio.file.Files.copy(in, path, StandardCopyOption.REPLACE_EXISTING);
                                            sender.sendMessage(Files.convert("&6Download finished, stop the server and check new jar in SoulPoints folder to get the new update"));
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }.runTaskLaterAsynchronously(SoulPoints.getInstance(), 1);
                            } else {
                                sender.sendMessage(Files.convert("&aYou are on latest build"));
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
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

                    sender.sendMessage(Files.convert(Files.getConfig().getString("prefix") + Files.getlang().getString("lang." + Files.getConfig().getString("language") + "." + "Check-message")).replaceAll("%souls%", String.valueOf(Data.getLives(Objects.requireNonNull(Bukkit.getPlayer(args[1]))))).replaceAll("%player%", Objects.requireNonNull(Bukkit.getPlayer(args[1])).getName()));
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
                            Data.addLives(Objects.requireNonNull(Bukkit.getPlayer(args[1])), Integer.parseInt(args[2]));
                            sender.sendMessage(Files.convert(Files.getConfig().getString("prefix") + Files.getlang().getString("lang." + Files.getConfig().getString("language") + "." + "Add-message")).replaceAll("%souls%", String.valueOf(Integer.parseInt(args[2]))).replaceAll("%player%", Objects.requireNonNull(Bukkit.getPlayer(args[1])).getName()));
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
                            if (Data.getLives(Objects.requireNonNull(Bukkit.getPlayer(args[1]))) < Integer.parseInt(args[2])) {
                                sender.sendMessage(Files.convert(Files.getConfig().getString("prefix") + Files.getlang().getString("lang." + Files.getConfig().getString("language") + "." + "Enough")));
                                return true;
                            }
                            if (Data.getLives(Objects.requireNonNull(Bukkit.getPlayer(args[1]))) >= Integer.parseInt(args[2])) {
                                Data.removeLives(Objects.requireNonNull(Bukkit.getPlayer(args[1])), Integer.parseInt(args[2]));
                                sender.sendMessage(Files.convert(Files.getConfig().getString("prefix") + Files.getlang().getString("lang." + Files.getConfig().getString("language") + "." + "Take-message")).replaceAll("%souls%", String.valueOf(Integer.parseInt(args[2]))).replaceAll("%player%", Objects.requireNonNull(Bukkit.getPlayer(args[1])).getName()));
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
                            Data.setLives(Objects.requireNonNull(Bukkit.getPlayer(args[1])), Integer.parseInt(args[2]));
                            sender.sendMessage(Files.convert(Files.getConfig().getString("prefix") + Files.getlang().getString("lang." + Files.getConfig().getString("language") + "." + "Set-message")).replaceAll("%souls%", String.valueOf(Integer.parseInt(args[2]))).replaceAll("%player%", Objects.requireNonNull(Bukkit.getPlayer(args[1])).getName()));
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
                            Data.resetLives(Objects.requireNonNull(Bukkit.getPlayer(args[1])));
                            sender.sendMessage(Files.convert(Files.getConfig().getString("prefix") + Files.getlang().getString("lang." + Files.getConfig().getString("language") + "." + "Reset-message")).replaceAll("%souls%", String.valueOf(Files.getConfig().getInt("General.Normal"))).replaceAll("%player%", Objects.requireNonNull(Bukkit.getPlayer(args[1])).getName()));
                        }
                    }
                }
            }
        }
        return true;
    }
}
