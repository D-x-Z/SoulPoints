package net.danh.soulpoints.Manager;

import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.api.bukkit.BukkitAPIHelper;
import net.danh.soulpoints.SoulPoints;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Animals;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.PluginManager;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DeathEvent implements Listener {

    PluginManager manager = Bukkit.getServer().getPluginManager();

    public int getRandomNumber(int min, int max) {
        Random r = new Random();
        return r.nextInt(max - min) + min;
    }

    @EventHandler
    public void onKill(EntityDeathEvent e) {
        if (Files.getConfig().getBoolean("Mobs.Enable")) {
            LivingEntity mob = e.getEntity();
            Player player = mob.getKiller();
            String vanillamobs = e.getEntityType().toString();
            if (player == null) {
                return;
            }
            if (manager.isPluginEnabled("MythicMobs")) {
                BukkitAPIHelper mythicMobsAPI = MythicMobs.inst().getAPIHelper();
                if (mythicMobsAPI.isMythicMob(mob)) {
                    return;
                }
            }
            if (mob instanceof Animals) {
                if (!Files.getConfig().getBoolean("Mobs.Animals")) {
                    return;
                }
            }
            if (mob instanceof Monster) {
                if (!Files.getConfig().getBoolean("Mobs.Monster")) {
                    return;
                }
            }
            Random randomInt = new Random();
            int max = Files.getmob().getInt("Vanilla.Default.max");
            int min = Files.getmob().getInt("Vanilla.Default.min");
            int chance = Files.getmob().getInt("Vanilla.Default.chance");
            if (max == 0) {
                if (min == 0) {
                    if (chance == 0) {
                        return;
                    }
                }
            }
            for (String getEntityType : Files.getmob().getConfigurationSection("Vanilla.").getKeys(false)) {
                if (vanillamobs.equalsIgnoreCase(getEntityType)) {
                    max = Files.getmob().getInt("Vanilla." + vanillamobs + ".max");
                    min = Files.getmob().getInt("Vanilla." + vanillamobs + ".min");
                    chance = Files.getmob().getInt("Vanilla." + vanillamobs + ".chance");
                    break;
                }
            }
            max = max - min;
            int random = min + randomInt.nextInt(max);
            double chancee = Math.random() * 100.0D;
            if (chancee <= chance) {
                Files.addLives(player, random);
                player.sendMessage(Files.convert(Files.getConfig().getString("prefix") + Files.getlang().getString("lang." + Files.getConfig().getString("language") + "." + "Kill-mobs-message")).replace("%souls%", Integer.toString(random)).replace("%mob%", mob.getName()));
            }
        }
    }


    @EventHandler
    public void death(PlayerDeathEvent e) {
        Player p = e.getEntity();
        Player k = p.getKiller();
        List<String> w = Files.getConfig().getStringList("available-worlds");
        if (w.contains(p.getWorld().getName())) {
            if (!Files.getConfig().getBoolean("PVP.Enable")) {
                return;
            }
            if (Files.getConfig().getBoolean("PVP.Enable")) {
                if (k != null) {
                    double chance = Math.random() * 100.0D;
                    if (chance <= Files.getConfig().getInt("PvP.Chance")) {
                        Files.addLives(k, Files.getConfig().getInt("PVP.Kill"));
                        k.sendMessage(Files.convert(Files.getConfig().getString("prefix") + Files.getlang().getString("lang." + Files.getConfig().getString("language") + "." + "Kill-message")).replace("%souls%", Files.getConfig().getString("PVP.Kill")).replace("%player%", p.getDisplayName()));
                        p.sendMessage(Files.convert(Files.getConfig().getString("prefix") + Files.getlang().getString("lang." + Files.getConfig().getString("language") + "." + "PvP-death-message")).replace("%player%", k.getDisplayName()));
                    }
                }
            }
            if (Files.getConfig().getInt("General.Protect-Inventory") > Files.getConfig().getInt("General.Minimum")) {
                if (Files.getLives(p) >= Files.getConfig().getInt("General.Protect-Inventory")) {
                    e.setKeepInventory(true);
                    (new BukkitRunnable() {
                        public void run() {
                            if (p.isOnline()) {
                                p.sendMessage(Files.convert(Files.getConfig().getString("prefix") + Files.getlang().getString("lang." + Files.getConfig().getString("language") + "." + "DeathEvent-message")).replaceAll("%souls%", String.valueOf(Files.getLives(p))).replaceAll("%lost%", String.valueOf(Files.getConfig().getInt("General.DeathEvent"))));
                            }

                        }
                    }).runTaskLater(SoulPoints.getInstance(), 20L * Files.getConfig().getInt("General.Time"));
                }
                if (Files.getLives(p) < Files.getConfig().getInt("General.Protect-Inventory") && Files.getLives(p) > Files.getConfig().getInt("General.Minimum")) {
                    List<Integer> fullSlots = new ArrayList<>();
                    PlayerInventory playerInventory = p.getInventory();
                    for (int i = 0; i <= playerInventory.getSize(); i++) {
                        if (playerInventory.getItem(i) != null)
                            fullSlots.add(i);
                    }
                    if (fullSlots.size() == 0)
                        return;
                    int theSlot = getRandomNumber(0, fullSlots.size());
                    playerInventory.setItem(fullSlots.get(theSlot), null);

                    (new BukkitRunnable() {
                        public void run() {
                            if (p.isOnline()) {
                                p.sendMessage(Files.convert(Files.getConfig().getString("prefix") + Files.getlang().getString("lang." + Files.getConfig().getString("language") + "." + "DeathEvent-message")).replaceAll("%souls%", String.valueOf(Files.getLives(p))).replaceAll("%lost%", String.valueOf(Files.getConfig().getInt("General.DeathEvent"))));
                                p.sendMessage(Files.convert(Files.getConfig().getString("prefix") + Files.getlang().getString("lang." + Files.getConfig().getString("language") + "." + "Random-drop-inventory")));
                            }

                        }
                    }).runTaskLater(SoulPoints.getInstance(), 20L * Files.getConfig().getInt("General.Time"));
                }


                if (Files.getLives(p) < Files.getConfig().getInt("General.Protect-Inventory") && Files.getLives(p) <= Files.getConfig().getInt("General.Minimum")) {
                    Files.addLives(p, Files.getConfig().getInt("General.Respawn"));
                    p.getInventory().clear();
                    (new BukkitRunnable() {
                        public void run() {
                            if (p.isOnline()) {
                                p.sendMessage(Files.convert(Files.getConfig().getString("prefix") + Files.getlang().getString("lang." + Files.getConfig().getString("language") + "." + "DeathEvent-message")).replaceAll("%souls%", String.valueOf(Files.getLives(p))).replaceAll("%lost%", String.valueOf(Files.getConfig().getInt("General.DeathEvent"))));
                                p.sendMessage(Files.convert(Files.getConfig().getString("prefix") + Files.getlang().getString("lang." + Files.getConfig().getString("language") + "." + "Clear-inventory")));
                            }

                        }
                    }).runTaskLater(SoulPoints.getInstance(), 20L * Files.getConfig().getInt("General.Time"));
                }
            }
            if (Files.getConfig().getInt("General.Protect-Inventory") <= Files.getConfig().getInt("General.Minimum")) {
                if (Files.getLives(p) > Files.getConfig().getInt("General.Minimum")) {
                    e.setKeepInventory(true);
                    (new BukkitRunnable() {
                        public void run() {
                            if (p.isOnline()) {
                                p.sendMessage(Files.convert(Files.getConfig().getString("prefix") + Files.getlang().getString("lang." + Files.getConfig().getString("language") + "." + "DeathEvent-message")).replaceAll("%souls%", String.valueOf(Files.getLives(p))).replaceAll("%lost%", String.valueOf(Files.getConfig().getInt("General.DeathEvent"))));
                            }

                        }
                    }).runTaskLater(SoulPoints.getInstance(), 20L * Files.getConfig().getInt("General.Time"));
                }
                if (Files.getLives(p) <= Files.getConfig().getInt("General.Minimum")) {
                    List<Integer> fullSlots = new ArrayList<>();
                    PlayerInventory playerInventory = p.getInventory();
                    for (int i = 0; i <= playerInventory.getSize(); i++) {
                        if (playerInventory.getItem(i) != null)
                            fullSlots.add(i);
                    }
                    if (fullSlots.size() == 0)
                        return;
                    int theSlot = getRandomNumber(0, fullSlots.size());
                    playerInventory.setItem(fullSlots.get(theSlot), null);

                    (new BukkitRunnable() {
                        public void run() {
                            if (p.isOnline()) {
                                p.sendMessage(Files.convert(Files.getConfig().getString("prefix") + Files.getlang().getString("lang." + Files.getConfig().getString("language") + "." + "DeathEvent-message")).replaceAll("%souls%", String.valueOf(Files.getLives(p))).replaceAll("%lost%", String.valueOf(Files.getConfig().getInt("General.DeathEvent"))));
                                p.sendMessage(Files.convert(Files.getConfig().getString("prefix") + Files.getlang().getString("lang." + Files.getConfig().getString("language") + "." + "Random-drop-inventory")));
                            }

                        }
                    }).runTaskLater(SoulPoints.getInstance(), 20L * Files.getConfig().getInt("General.Time"));
                }
            }

            Files.removeLives(p, Files.getConfig().getInt("General.DeathEvent"));
        }

    }

    @EventHandler
    public void onjoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        if (p.isOp() || p.hasPermission("souls.admin")) {
            new UpdateChecker(SoulPoints.getInstance(), 96396).getVersion(version -> {
                if (!SoulPoints.getInstance().getDescription().getVersion().equals(version)) {
                    p.sendMessage(ChatColor.GREEN + "There's a new update! The new version is " + ChatColor.YELLOW + version + ChatColor.GREEN + ", you are using " + ChatColor.RED + SoulPoints.getInstance().getDescription().getVersion());
                    p.sendMessage(ChatColor.BLUE + "Download: " + ChatColor.WHITE + "https://www.spigotmc.org/resources/96396/");
                }

            });
        }
    }
}

