package net.danh.diemsinhmenh.event;

import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.api.bukkit.BukkitAPIHelper;
import net.danh.diemsinhmenh.Main;
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
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.PluginManager;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Death implements Listener {

    PluginManager manager = Bukkit.getServer().getPluginManager();
    private Main main;


    public Death(Main main) {
        this.main = main;
    }

    public int getRandomNumber(int min, int max) {
        Random r = new Random();
        int randomNumber = r.nextInt(max - min) + min;
        return randomNumber;
    }

    @EventHandler
    public void onKill(EntityDeathEvent e) {
        if (main.getConfig().getBoolean("Mobs.Enable")) {
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
                if (!main.getConfig().getBoolean("Mobs.Animals")) {
                    return;
                }
            }
            if (mob instanceof Monster) {
                if (!main.getConfig().getBoolean("Mobs.Monster")) {
                    return;
                }
            }
            Random randomInt = new Random();
            int max = main.getmob().getInt("Vanilla.Default.max");
            int min = main.getmob().getInt("Vanilla.Default.min");
            int chance = main.getmob().getInt("Vanilla.Default.chance");
            if (max == 0) {
                if (min == 0) {
                    if (chance == 0) {
                        return;
                    }
                }
            }
            for (String getEntityType : main.getmob().getConfigurationSection("Vanilla.").getKeys(false)) {
                if (vanillamobs.equalsIgnoreCase(getEntityType)) {
                    max = main.getmob().getInt("Vanilla." + vanillamobs + ".max");
                    min = main.getmob().getInt("Vanilla." + vanillamobs + ".min");
                    chance = main.getmob().getInt("Vanilla." + vanillamobs + ".chance");
                    break;
                }
            }
            max = max - min;
            int random = min + randomInt.nextInt(max);
            double chancee = Math.random() * 100.0D;
            if (chancee <= chance) {
                main.addLives(player, random);
                player.sendMessage(main.convert(main.getConfig().getString("prefix") + main.getlang().getString("lang." + main.getConfig().getString("language") + "." + "Kill-mobs-message")).replace("%souls%", Integer.toString(random)).replace("%mob%", mob.getName()));
            }
        }
    }


    @EventHandler
    public void death(PlayerDeathEvent e) {
        Player p = e.getEntity();
        Player k = p.getKiller();
        List<String> w = main.getConfig().getStringList("available-worlds");
        if (w.contains(p.getWorld().getName())) {
            if (!main.getConfig().getBoolean("PVP.Enable")) {
                return;
            }
            if (main.getConfig().getBoolean("PVP.Enable")) {
                if (k instanceof Player) {
                    double chance = Math.random() * 100.0D;
                    if (chance <= main.getConfig().getInt("PvP.Chance")) {
                        main.addLives(k, main.getConfig().getInt("PVP.Kill"));
                        k.sendMessage(main.convert(main.getConfig().getString("prefix") + main.getlang().getString("lang." + main.getConfig().getString("language") + "." + "Kill-message")).replace("%souls%", main.getConfig().getString("PVP.Kill")).replace("%player%", p.getDisplayName()));
                        p.sendMessage(main.convert(main.getConfig().getString("prefix") + main.getlang().getString("lang." + main.getConfig().getString("language") + "." + "PvP-death-message")).replace("%player%", k.getDisplayName()));
                    }
                }
            }
            if (main.getConfig().getInt("General.Protect-Inventory") > main.getConfig().getInt("General.Minimum")) {
                if (main.getLives(p) >= main.getConfig().getInt("General.Protect-Inventory")) {
                    e.setKeepInventory(true);
                    (new BukkitRunnable() {
                        public void run() {
                            if (p.isOnline() && p != null) {
                                p.sendMessage(main.convert(main.getConfig().getString("prefix") + main.getlang().getString("lang." + main.getConfig().getString("language") + "." + "Death-message")).replaceAll("%souls%", String.valueOf(main.getLives(p))).replaceAll("%lost%", String.valueOf(String.valueOf(main.getConfig().getInt("General.Death")))));
                            }

                        }
                    }).runTaskLater(main, (long) (20 * main.getConfig().getInt("General.Time")));
                }
                if (main.getLives(p) < main.getConfig().getInt("General.Protect-Inventory") && main.getLives(p) > main.getConfig().getInt("General.Minimum")) {
                    List<Integer> fullSlots = new ArrayList<Integer>();
                    PlayerInventory playerInventory = p.getInventory();
                    for (int i = 0; i <= playerInventory.getSize(); i++) {
                        if (playerInventory.getItem(i) != null)
                            fullSlots.add(Integer.valueOf(i));
                    }
                    if (fullSlots.size() == 0)
                        return;
                    int theSlot = getRandomNumber(0, fullSlots.size());
                    ItemStack itemStack = new ItemStack(playerInventory.getItem(((Integer) fullSlots.get(theSlot)).intValue()));
                    playerInventory.setItem(((Integer) fullSlots.get(theSlot)).intValue(), null);

                    (new BukkitRunnable() {
                        public void run() {
                            if (p.isOnline() && p != null) {
                                p.sendMessage(main.convert(main.getConfig().getString("prefix") + main.getlang().getString("lang." + main.getConfig().getString("language") + "." + "Death-message")).replaceAll("%souls%", String.valueOf(main.getLives(p))).replaceAll("%lost%", String.valueOf(main.getConfig().getInt("General.Death"))));
                                p.sendMessage(main.convert(main.getConfig().getString("prefix") + main.getlang().getString("lang." + main.getConfig().getString("language") + "." + "Random-drop-inventory")));
                            }

                        }
                    }).runTaskLater(main, (long) (20 * main.getConfig().getInt("General.Time")));
                }


                if (main.getLives(p) < main.getConfig().getInt("General.Protect-Inventory") && main.getLives(p) <= main.getConfig().getInt("General.Minimum")) {
                    main.addLives(p, main.getConfig().getInt("General.Respawn"));
                    p.getInventory().clear();
                    (new BukkitRunnable() {
                        public void run() {
                            if (p.isOnline() && p != null) {
                                p.sendMessage(main.convert(main.getConfig().getString("prefix") + main.getlang().getString("lang." + main.getConfig().getString("language") + "." + "Death-message")).replaceAll("%souls%", String.valueOf(main.getLives(p))).replaceAll("%lost%", String.valueOf(main.getConfig().getInt("General.Death"))));
                                p.sendMessage(main.convert(main.getConfig().getString("prefix") + main.getlang().getString("lang." + main.getConfig().getString("language") + "." + "Clear-inventory")));
                            }

                        }
                    }).runTaskLater(main, (long) (20 * main.getConfig().getInt("General.Time")));
                }
            }
            if (main.getConfig().getInt("General.Protect-Inventory") <= main.getConfig().getInt("General.Minimum")) {
                if (main.getLives(p) > main.getConfig().getInt("General.Minimum")) {
                    e.setKeepInventory(true);
                    (new BukkitRunnable() {
                        public void run() {
                            if (p.isOnline() && p != null) {
                                p.sendMessage(main.convert(main.getConfig().getString("prefix") + main.getlang().getString("lang." + main.getConfig().getString("language") + "." + "Death-message")).replaceAll("%souls%", String.valueOf(main.getLives(p))).replaceAll("%lost%", String.valueOf(main.getConfig().getInt("General.Death"))));
                            }

                        }
                    }).runTaskLater(main, (long) (20 * main.getConfig().getInt("General.Time")));
                }
                if (main.getLives(p) <= main.getConfig().getInt("General.Minimum")) {
                    List<Integer> fullSlots = new ArrayList<Integer>();
                    PlayerInventory playerInventory = p.getInventory();
                    for (int i = 0; i <= playerInventory.getSize(); i++) {
                        if (playerInventory.getItem(i) != null)
                            fullSlots.add(Integer.valueOf(i));
                    }
                    if (fullSlots.size() == 0)
                        return;
                    int theSlot = getRandomNumber(0, fullSlots.size());
                    ItemStack itemStack = new ItemStack(playerInventory.getItem(((Integer) fullSlots.get(theSlot)).intValue()));
                    playerInventory.setItem(((Integer) fullSlots.get(theSlot)).intValue(), null);

                    (new BukkitRunnable() {
                        public void run() {
                            if (p.isOnline() && p != null) {
                                p.sendMessage(main.convert(main.getConfig().getString("prefix") + main.getlang().getString("lang." + main.getConfig().getString("language") + "." + "Death-message")).replaceAll("%souls%", String.valueOf(main.getLives(p))).replaceAll("%lost%", String.valueOf(main.getConfig().getInt("General.Death"))));
                                p.sendMessage(main.convert(main.getConfig().getString("prefix") + main.getlang().getString("lang." + main.getConfig().getString("language") + "." + "Random-drop-inventory")));
                            }

                        }
                    }).runTaskLater(main, (long) (20 * main.getConfig().getInt("General.Time")));
                }
            }

            main.removeLives(p, main.getConfig().getInt("General.Death"));
        }

    }

    @EventHandler
    public void onjoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        if (p.isOp() || p.hasPermission("souls.admin")) {
            new UpdateChecker(main, 96396).getVersion(version -> {
                if (main.getDescription().getVersion().equals(version)) {
                } else {
                    p.sendMessage(ChatColor.GREEN + "There's a new update! The new version is " + ChatColor.YELLOW + version + ChatColor.GREEN + ", you are using " + ChatColor.RED + main.getDescription().getVersion());
                    p.sendMessage(ChatColor.BLUE + "Download: " + ChatColor.WHITE + "https://www.spigotmc.org/resources/96396/");
                }

            });
        }
    }
}
