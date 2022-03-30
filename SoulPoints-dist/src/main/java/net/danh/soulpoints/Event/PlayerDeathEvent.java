package net.danh.soulpoints.Event;

import net.danh.soulpoints.Manager.Data;
import net.danh.soulpoints.Manager.Files;
import net.danh.soulpoints.SoulPoints;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PlayerDeathEvent implements Listener {

    @EventHandler
    public void death(org.bukkit.event.entity.@NotNull PlayerDeathEvent e) {
        Player p = e.getEntity();
        Player k = p.getKiller();
        List<String> w = Files.getConfig().getStringList("disable-worlds");
        if (!w.contains(p.getWorld().getName())) {
            if (!Files.getConfig().getBoolean("PVP.Enable")) {
                return;
            }
            if (Files.getConfig().getBoolean("PVP.Enable")) {
                if (k != null) {
                    double chance = Math.random() * 100.0D;
                    if (chance <= Files.getConfig().getInt("PvP.Chance")) {
                        Data.addLives(k, Files.getConfig().getInt("PVP.Kill"));
                        k.sendMessage(Files.convert(Files.getConfig().getString("prefix") + Files.getlang().getString("lang." + Files.getConfig().getString("language") + "." + "Kill-message")).replace("%souls%", Objects.requireNonNull(Files.getConfig().getString("PVP.Kill"))).replace("%player%", p.getDisplayName()));
                        p.sendMessage(Files.convert(Files.getConfig().getString("prefix") + Files.getlang().getString("lang." + Files.getConfig().getString("language") + "." + "PvP-death-message")).replace("%player%", k.getDisplayName()));
                    }
                }
            }
            if (Files.getConfig().getInt("General.Protect-Inventory") > Files.getConfig().getInt("General.Minimum")) {
                if (Data.getLives(p) >= Files.getConfig().getInt("General.Protect-Inventory")) {
                    e.setKeepInventory(true);
                    (new BukkitRunnable() {
                        public void run() {
                            if (p.isOnline()) {
                                p.sendMessage(Files.convert(Files.getConfig().getString("prefix") + Files.getlang().getString("lang." + Files.getConfig().getString("language") + "." + "Death-message")).replaceAll("%souls%", String.valueOf(Data.getLives(p))).replaceAll("%lost%", String.valueOf(Files.getConfig().getInt("General.Death"))));
                            }

                        }
                    }).runTaskLater(SoulPoints.getInstance(), 20L * Files.getConfig().getInt("General.Time"));
                }
                if (Data.getLives(p) < Files.getConfig().getInt("General.Protect-Inventory") && Data.getLives(p) > Files.getConfig().getInt("General.Minimum")) {
                    List<Integer> fullSlots = new ArrayList<>();
                    PlayerInventory playerInventory = p.getInventory();
                    for (int i = 0; i <= playerInventory.getSize(); i++) {
                        if (playerInventory.getItem(i) != null)
                            fullSlots.add(i);
                    }
                    if (fullSlots.size() == 0)
                        return;
                    int theSlot = Data.getRandomNumber(0, fullSlots.size());
                    playerInventory.setItem(fullSlots.get(theSlot), null);

                    (new BukkitRunnable() {
                        public void run() {
                            if (p.isOnline()) {
                                p.sendMessage(Files.convert(Files.getConfig().getString("prefix") + Files.getlang().getString("lang." + Files.getConfig().getString("language") + "." + "Death-message")).replaceAll("%souls%", String.valueOf(Data.getLives(p))).replaceAll("%lost%", String.valueOf(Files.getConfig().getInt("General.Death"))));
                                p.sendMessage(Files.convert(Files.getConfig().getString("prefix") + Files.getlang().getString("lang." + Files.getConfig().getString("language") + "." + "Random-drop-inventory")));
                            }

                        }
                    }).runTaskLater(SoulPoints.getInstance(), 20L * Files.getConfig().getInt("General.Time"));
                }


                if (Data.getLives(p) < Files.getConfig().getInt("General.Protect-Inventory") && Data.getLives(p) <= Files.getConfig().getInt("General.Minimum")) {
                    Data.addLives(p, Files.getConfig().getInt("General.Respawn"));
                    p.getInventory().clear();
                    (new BukkitRunnable() {
                        public void run() {
                            if (p.isOnline()) {
                                p.sendMessage(Files.convert(Files.getConfig().getString("prefix") + Files.getlang().getString("lang." + Files.getConfig().getString("language") + "." + "Death-message")).replaceAll("%souls%", String.valueOf(Data.getLives(p))).replaceAll("%lost%", String.valueOf(Files.getConfig().getInt("General.Death"))));
                                p.sendMessage(Files.convert(Files.getConfig().getString("prefix") + Files.getlang().getString("lang." + Files.getConfig().getString("language") + "." + "Clear-inventory")));
                            }

                        }
                    }).runTaskLater(SoulPoints.getInstance(), 20L * Files.getConfig().getInt("General.Time"));
                }
            }
            if (Files.getConfig().getInt("General.Protect-Inventory") <= Files.getConfig().getInt("General.Minimum")) {
                if (Data.getLives(p) > Files.getConfig().getInt("General.Minimum")) {
                    e.setKeepInventory(true);
                    (new BukkitRunnable() {
                        public void run() {
                            if (p.isOnline()) {
                                p.sendMessage(Files.convert(Files.getConfig().getString("prefix") + Files.getlang().getString("lang." + Files.getConfig().getString("language") + "." + "Death-message")).replaceAll("%souls%", String.valueOf(Data.getLives(p))).replaceAll("%lost%", String.valueOf(Files.getConfig().getInt("General.Death"))));
                            }

                        }
                    }).runTaskLater(SoulPoints.getInstance(), 20L * Files.getConfig().getInt("General.Time"));
                }
                if (Data.getLives(p) <= Files.getConfig().getInt("General.Minimum")) {
                    List<Integer> fullSlots = new ArrayList<>();
                    PlayerInventory playerInventory = p.getInventory();
                    for (int i = 0; i <= playerInventory.getSize(); i++) {
                        if (playerInventory.getItem(i) != null)
                            fullSlots.add(i);
                    }
                    if (fullSlots.size() == 0)
                        return;
                    int theSlot = Data.getRandomNumber(0, fullSlots.size());
                    playerInventory.setItem(fullSlots.get(theSlot), null);

                    (new BukkitRunnable() {
                        public void run() {
                            if (p.isOnline()) {
                                p.sendMessage(Files.convert(Files.getConfig().getString("prefix") + Files.getlang().getString("lang." + Files.getConfig().getString("language") + "." + "Death-message")).replaceAll("%souls%", String.valueOf(Data.getLives(p))).replaceAll("%lost%", String.valueOf(Files.getConfig().getInt("General.Death"))));
                                p.sendMessage(Files.convert(Files.getConfig().getString("prefix") + Files.getlang().getString("lang." + Files.getConfig().getString("language") + "." + "Random-drop-inventory")));
                            }

                        }
                    }).runTaskLater(SoulPoints.getInstance(), 20L * Files.getConfig().getInt("General.Time"));
                }
            }

            Data.removeLives(p, Files.getConfig().getInt("General.Death"));
        }

    }

}
